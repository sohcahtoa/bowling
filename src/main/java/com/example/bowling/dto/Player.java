package com.example.bowling.dto;

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
    private List<Frame> frames;
    private String name;
    private int lastScoredFrame;
}
