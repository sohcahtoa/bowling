package com.example.bowling.service;

import com.example.bowling.entity.FrameEntity;
import com.example.bowling.entity.PlayerEntity;
import com.example.bowling.exception.InvalidFrameException;
import com.example.bowling.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

import static com.example.bowling.common.Constants.LAST_FRAME;
import static com.example.bowling.common.Constants.MAX_PINS;

@Service
@RequiredArgsConstructor
public class ScoringService {
    private final PlayerRepository playerRepository;

    public PlayerEntity handleAddFrame(UUID playerId, FrameEntity frameEntity) {
        PlayerEntity playerEntity = playerRepository.findById(playerId).orElseThrow(EntityNotFoundException::new);
        validateFrame(frameEntity, playerEntity);
        addFrameToPlayer(playerEntity, frameEntity);
        calculateScore(playerEntity);
        return playerRepository.save(playerEntity);
    }

    private void validateFrame(FrameEntity frameEntity, PlayerEntity playerEntity) {
        assertFrameNumberValid(frameEntity.getFrameNumber(),playerEntity.getFrames().size() + 1);
        assertRollsValid(frameEntity.getRolls(), frameEntity.getFrameNumber());
    }

    private void assertFrameNumberValid(int frameNumber, int currentFrameNumber) {
        if(frameNumber <= 0 || frameNumber != currentFrameNumber) {
            throw new InvalidFrameException("Frame Number was incorrect.");
        }
    }

    private void assertRollsValid(int[] rolls, int frameNumber) {
        if(frameNumber != LAST_FRAME) {
            if(isValidNumberOfRolls(rolls)) {
                throw new InvalidFrameException("Provided incorrect number of rolls.");
            }
            int frameScore = 0;
            for(int roll : rolls) {
                frameScore += roll;
                if(roll < 0 || roll > MAX_PINS) {
                    throw new InvalidFrameException("Provided incorrect roll values.");
                }
            }
            if(frameScore > MAX_PINS) {
                throw new InvalidFrameException("Provided incorrect roll values.");
            }
        } else {
            if(!(rolls.length == 2 || rolls.length == 3)) {
                throw new InvalidFrameException("Provided incorrect number of rolls.");
            }
            if(!(isClosedFrame(rolls) && rolls.length == 3)) {
                throw new InvalidFrameException("Provided incorrect number of rolls.");
            }
            int frameScore = 0;
            for(int roll : rolls) {
                frameScore += roll;
                if(roll < 0 || roll > MAX_PINS) {
                    throw new InvalidFrameException("Provided incorrect roll values.");
                }
            }
            if(isLastFrameScoreValid(frameScore, rolls.length)) {
                throw new InvalidFrameException("Provided incorrect roll values.");
            }
        }
    }

    private boolean isValidNumberOfRolls(int[] rolls) {
        return rolls.length == 0 ||
                rolls.length >= 3 ||
                (rolls.length == 1 && rolls[0] != MAX_PINS) ||
                (rolls[0] == MAX_PINS && rolls.length != 1);
    }

    private boolean isClosedFrame(int[] rolls) {
        return rolls[0] == MAX_PINS || rolls[0] + rolls[1] == MAX_PINS;
    }

    private boolean isLastFrameScoreValid(int frameScore, int numRolls) {
        return frameScore > MAX_PINS && numRolls == 2;
    }

    private void addFrameToPlayer(PlayerEntity playerEntity, FrameEntity frameEntity) {
        frameEntity.setPlayer(playerEntity);
        List<FrameEntity> frameEntities = playerEntity.getFrames();
        frameEntities.add(frameEntity);
        playerEntity.setFrames(frameEntities);
    }

    private void calculateScore(PlayerEntity playerEntity) {
        List<FrameEntity> frameEntities = playerEntity.getFrames();
        int lastScoredFrame = playerEntity.getLastScoredFrame();
        int score = getLastRecordedScore(frameEntities, lastScoredFrame);

        for(int i = lastScoredFrame; i < frameEntities.size(); i++) {
            FrameEntity frameEntity = frameEntities.get(i);
            if(isLastFrame(i)) {
                for(int roll : frameEntity.getRolls()) {
                    score += roll;
                }
                updatePlayerAndFrame(playerEntity, frameEntity, score);
            } else if(frameEntity.isStrike()) {
                if(!canCalculateStrike(frameEntities, i)) {
                    break;
                }
                int nextTwoRollsScore = calculateNextTwoRollsScore(frameEntities, i);
                score += MAX_PINS + nextTwoRollsScore;

                updatePlayerAndFrame(playerEntity, frameEntity, score);
            } else if(frameEntity.isSpare()) {
                if(i + 1 >= frameEntities.size()) {
                    break;
                }
                score += MAX_PINS + frameEntities.get(i + 1).getRolls()[0];

                updatePlayerAndFrame(playerEntity, frameEntity, score);
            } else {
                score += frameEntity.getRolls()[0] + frameEntity.getRolls()[1];

                updatePlayerAndFrame(playerEntity, frameEntity, score);
            }
        }
    }

    private int getLastRecordedScore(List<FrameEntity> frameEntities, int lastScoredFrame) {
        return lastScoredFrame > 0 ? frameEntities.get(lastScoredFrame - 1).getFrameScore() : 0;
    }

    private void updatePlayerAndFrame(PlayerEntity playerEntity, FrameEntity frameEntity, int score) {
        frameEntity.setFrameScore(score);
        playerEntity.setLastScoredFrame(playerEntity.getLastScoredFrame() + 1);
    }

    private boolean canCalculateStrike(List<FrameEntity> frameEntities, int index) {
        return (index + 1 < frameEntities.size() && !frameEntities.get(index + 1).isStrike())
                || (index + 2 < frameEntities.size());
    }

    private int calculateNextTwoRollsScore(List<FrameEntity> frameEntities, int index) {
        if(!frameEntities.get(index + 1).isStrike()) {
            int[] rolls = frameEntities.get(index + 1).getRolls();
            return rolls[0] + rolls[1];
        } else {
            int roll1 = frameEntities.get(index + 1).getRolls()[0];
            int roll2 = frameEntities.get(index + 2).getRolls()[0];
            return roll1 + roll2;
        }
    }

    private boolean isLastFrame(int index) {
        return index == 9;
    }
}
