package com.example.bowling.entity;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

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
@TypeDefs({
        @TypeDef(
                name = "int-array",
                typeClass = IntArrayType.class
        )
})
public class FrameEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    private PlayerEntity player;

    @Type(type = "int-array")
    @Column(
            name = "rolls",
            columnDefinition = "integer[]"
    )
    private int[] rolls;

    @Builder.Default
    private Integer frameScore = null;

    private int frameNumber;

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
