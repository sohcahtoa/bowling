package com.example.bowling.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Frame {
    private UUID id;
    private int[] rolls;
    private int frameNumber;
    private Integer frameScore;
}
