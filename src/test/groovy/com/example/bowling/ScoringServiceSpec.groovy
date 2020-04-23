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

    def "can calculate a spare"() {
        given:
        FrameEntity frameSpare = aRandom.frameEntity()
                .rolls([1, 9] as int[])
                .build()
        FrameEntity frame2 = aRandom.frameEntity()
                .rolls([9,0] as int[])
                .build()

        PlayerEntity playerEntity = aRandom.playerEntity().build()

        List<FrameEntity> frameEntities = playerEntity.getFrames()
        frameEntities.add(frameSpare)
        frameEntities.add(frame2)

        when:
        PlayerEntity result = scoringService.calculateScore(playerEntity)

        then:
        assert result.getFrames().get(0).frameScore == 19
        assert result.getFrames().get(1).frameScore == 28
        assert result.lastScoredFrame == 2
    }

    def "can calculate a multiple spares in a row"() {
        given:
        FrameEntity frameSpare1 = aRandom.frameEntity()
                .rolls([1, 9] as int[])
                .build()
        FrameEntity frameSpare2 = aRandom.frameEntity()
                .rolls([9,1] as int[])
                .build()
        FrameEntity frame3 = aRandom.frameEntity()
                .rolls([5,2] as int[])
                .build()

        PlayerEntity playerEntity = aRandom.playerEntity().build()

        List<FrameEntity> frameEntities = playerEntity.getFrames()
        frameEntities.add(frameSpare1)
        frameEntities.add(frameSpare2)
        frameEntities.add(frame3)

        when:
        PlayerEntity result = scoringService.calculateScore(playerEntity)

        then:
        assert result.getFrames().get(0).frameScore == 19
        assert result.getFrames().get(1).frameScore == 34
        assert result.getFrames().get(2).frameScore == 41
        assert result.lastScoredFrame == 3
    }

    def "will not calculate spare if there is not another frame after it"() {
        given:
        FrameEntity frame1 = aRandom.frameEntity()
                .rolls([1, 1] as int[])
                .build()
        FrameEntity frameSpare = aRandom.frameEntity()
                .rolls([9,1] as int[])
                .build()

        PlayerEntity playerEntity = aRandom.playerEntity().build()

        List<FrameEntity> frameEntities = playerEntity.getFrames()
        frameEntities.add(frame1)
        frameEntities.add(frameSpare)

        when:
        PlayerEntity result = scoringService.calculateScore(playerEntity)

        then:
        assert result.getFrames().get(0).frameScore == 2
        assert result.getFrames().get(1).frameScore == null
        assert result.lastScoredFrame == 1
    }
}
