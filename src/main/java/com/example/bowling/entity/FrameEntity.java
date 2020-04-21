package com.example.bowling.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class FrameEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    private PlayerEntity player;

    private int frameNumber;

    private int firstRoll;

    private int secondRoll;

    public boolean isStrike() {
        return firstRoll == MAX_PINS;
    }

    public boolean isSpare() {
        return firstRoll + secondRoll == MAX_PINS;
    }
}
