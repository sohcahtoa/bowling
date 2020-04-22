package com.example.bowling.apiModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Frame {
    private int frameNumber;
    private int firstRoll;
    private int secondRoll;
}
