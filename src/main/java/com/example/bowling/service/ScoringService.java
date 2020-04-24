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
        if(frameEntity.getFrameNumber() <= 0 || frameEntity.getFrameNumber() != playerEntity.getFrames().size() + 1) {
            throw new InvalidFrameException("Frame Number was incorrect.");
        }
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
