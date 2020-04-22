package com.example.bowling.apiModel;

import com.example.bowling.entity.FrameEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private UUID id;
    private List<FrameEntity> frames;
    private String name;
    private int currentFrame = 1;
    private int currentScore = 0;
}
