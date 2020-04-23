package com.example.bowling.service;

import com.example.bowling.entity.FrameEntity;
import com.example.bowling.entity.PlayerEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.bowling.common.Constants.MAX_PINS;

@Service
public class ScoringService {
    public PlayerEntity calculateScore(PlayerEntity playerEntity) {
        List<FrameEntity> frameEntities = playerEntity.getFrames();
        int lastScoredFrame = playerEntity.getLastScoredFrame();
        int score = getLastRecordedScore(frameEntities, lastScoredFrame);
        for(int i = lastScoredFrame; i < frameEntities.size(); i++) {
            FrameEntity frameEntity = frameEntities.get(i);
            if(frameEntity.isSpare()) {
                if(i + 1 >= frameEntities.size()) {
                    break;
                }
                score += MAX_PINS + frameEntities.get(i + 1).getRolls()[0];

                frameEntity.setFrameScore(score);
                playerEntity.setLastScoredFrame(playerEntity.getLastScoredFrame() + 1);
            } else {
                score += frameEntity.getRolls()[0] + frameEntity.getRolls()[1];

                frameEntity.setFrameScore(score);
                playerEntity.setLastScoredFrame(playerEntity.getLastScoredFrame() + 1);
            }
        }
        return playerEntity;
    }

    private int getLastRecordedScore(List<FrameEntity> frameEntities, int lastScoredFrame) {
        return lastScoredFrame > 0 ? frameEntities.get(lastScoredFrame - 1).getFrameScore() : 0;
    }
}
