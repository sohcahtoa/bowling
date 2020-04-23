package com.example.bowling

import com.example.bowling.entity.FrameEntity
import com.example.bowling.entity.PlayerEntity
import com.example.bowling.service.ScoringService
import spock.lang.Specification
import spock.lang.Unroll

import static com.example.bowling.ARandom.aRandom;

class ScoringServiceSpec extends Specification {
    private ScoringService scoringService

    def setup() {
        scoringService = new ScoringService()
    }

    @Unroll
    def "can calculate score for player for 1st frame no strike or spares"() {
        given:
        int[] rolls = [firstRoll, secondRoll]
        List<FrameEntity> frameEntities = new ArrayList<>()
        frameEntities.add(aRandom.frameEntity()
                .rolls(rolls)
                .build())

        PlayerEntity playerEntity = aRandom.playerEntity()
                .frames(frameEntities)
                .build()

        when:
        PlayerEntity result = scoringService.calculateScore(playerEntity)

        then:
        assert result.getFrames().get(0).frameScore == expectedScore
        assert result.lastScoredFrame == 1

        where:
        firstRoll   | secondRoll    | expectedScore
        0           | 0             | 0
        4           | 3             | 7
    }

    def "can calculate score for player in middle of game no strike or spares"() {
        given: "previous frames of a game"
        FrameEntity frame1 = aRandom.frameEntity()
                .rolls([1, 8] as int[])
                .frameScore(9)
                .build()
        FrameEntity frame2 = aRandom.frameEntity()
                .rolls([2,4] as int[])
                .frameScore(15)
                .build()

        PlayerEntity playerEntity = aRandom.playerEntity()
                .lastScoredFrame(2)
                .build()

        List<FrameEntity> frameEntities = playerEntity.getFrames()
        frameEntities.add(frame1)
        frameEntities.add(frame2)

        and: "a new frame"
        FrameEntity frame3 = aRandom.frameEntity()
                .rolls([1,2] as int[])
                .build()
        frameEntities.add(frame3)

        when:
        PlayerEntity result = scoringService.calculateScore(playerEntity)

        then:
        assert result.getFrames().get(2).frameScore == 18
        assert result.lastScoredFrame == 3
    }

    @Unroll
    def "can calculate #description"() {
        given:
        FrameEntity frame1 = aRandom.frameEntity()
                .rolls(roll1)
                .build()
        FrameEntity frame2 = aRandom.frameEntity()
                .rolls(roll2)
                .build()
        FrameEntity frame3 = aRandom.frameEntity()
                .rolls(roll3)
                .build()

        PlayerEntity playerEntity = aRandom.playerEntity().build()

        List<FrameEntity> frameEntities = playerEntity.getFrames()
        frameEntities.add(frame1)
        frameEntities.add(frame2)
        frameEntities.add(frame3)

        when:
        PlayerEntity result = scoringService.calculateScore(playerEntity)

        then:
        assert result.getFrames().get(0).frameScore == expectedFrame1
        assert result.getFrames().get(1).frameScore == expectedFrame2
        assert result.getFrames().get(2).frameScore == expectedFrame3
        assert result.lastScoredFrame == expectedLastFrame

        where:
        description         | roll1             | roll2             | roll3             | expectedFrame1 | expectedFrame2 | expectedFrame3 | expectedLastFrame
        "a spare"           | [1, 9] as int[]   | [9, 0] as int[]   | [1, 1] as int[]   | 19             | 28             | 30             | 3
        "multiple spares"   | [1, 9] as int[]   | [9, 1] as int[]   | [1, 1] as int[]   | 19             | 30             | 32             | 3
        "last frame spare"  | [1, 0] as int[]   | [5, 1] as int[]   | [9, 1] as int[]   | 1              | 7              | null           | 2
    }
}
