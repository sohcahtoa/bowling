package com.example.bowling.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

import static com.example.bowling.common.Constants.MAX_PINS;

@Data
@Builder
@Entity
@Table(name = "frame")
@NoArgsConstructor
@AllArgsConstructor
public class FrameEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    private PlayerEntity player;

    private int[] rolls;

    @Builder.Default
    private Integer frameScore = null;

    public boolean isStrike() {
        if(rolls.length == 1) {
            return rolls[0] == MAX_PINS;
        }
        return false;
    }

    public boolean isSpare() {
        if(rolls.length == 2) {
            return rolls[0] + rolls[1] == MAX_PINS;
        }
        return false;
    }
}
