package com.example.bowling

import com.example.bowling.dto.Frame
import com.example.bowling.dto.Game
import com.example.bowling.dto.Player

import static com.example.bowling.ARandom.aRandom
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class GameServiceSpec extends BaseIntegrationSpec {

    def "can create a game with multiple players"() {
        given:
        List<String> players = aRandom.playerNames(2)

        when:
        Game createdGame = responseToClass(createGame(players), Game.class)
        Game foundGame = responseToClass(findGame(createdGame.id), Game.class)

        then:
        assert foundGame.players.size() == 2
        assert foundGame.players.get(0).name == players.get(0)
        assert foundGame.players.get(1).name == players.get(1)
    }

    def "will return a not found if game is not found"() {
        when:
        findGame(aRandom.uuid(), status().isNotFound())

        then:
        noExceptionThrown()
    }

    def "can add a frame for a player"() {
        given:
        List<String> players = aRandom.playerNames(2)
        Game createdGame = responseToClass(createGame(players), Game.class)
        UUID playerId = createdGame.players.get(0).id
        Frame frame = Frame.builder()
            .rolls([4, 5] as int[])
            .frameNumber(1)
            .build()

        when:
        Player result = responseToClass(addFrame(playerId, frame), Player.class)

        then:
        assert result.frames.size() == 1
        assert result.frames.get(0).frameScore == 9
        assert result.lastScoredFrame == 1
    }

    def "will return a not found if player is not found"() {
        when:
        Frame frame = Frame.builder()
                .rolls([4, 5] as int[])
                .frameNumber(1)
                .build()
        addFrame(aRandom.uuid(), frame, status().isNotFound())

        then:
        noExceptionThrown()
    }

    def "will return a bad request if frame number is not passed"() {
        when:
        List<String> players = aRandom.playerNames(2)
        Game createdGame = responseToClass(createGame(players), Game.class)
        UUID playerId = createdGame.players.get(0).id
        Frame frame = Frame.builder()
                .rolls([4, 5] as int[])
                .build()
        addFrame(playerId, frame, status().isBadRequest())

        then:
        noExceptionThrown()
    }

    def "can play a full game"() {
        given:
        List<String> players = aRandom.playerNames(1)
        Game createdGame = responseToClass(createGame(players), Game.class)
        UUID playerId = createdGame.players.get(0).id
        List<Frame> frames = new ArrayList<>()
        frames.add(Frame.builder()
                .rolls([4, 5] as int[])
                .frameNumber(1)
                .build())
        frames.add(Frame.builder()
                .rolls([4,6] as int[])
                .frameNumber(2)
                .build())
        for(int i = 3; i <= 9; i++) {
            frames.add(Frame.builder()
                    .rolls([10] as int[])
                    .frameNumber(i)
                    .build())
        }
        frames.add(Frame.builder()
                .rolls([10,10,10] as int[])
                .frameNumber(10)
                .build())

        when:
        Player result = null
        for(Frame frame : frames) {
            result = responseToClass(addFrame(playerId, frame), Player.class)
        }

        then:
        assert result.frames.size() == 10
        assert result.frames.get(0).frameScore == 9
        assert result.frames.get(1).frameScore == 29
        assert result.frames.get(2).frameScore == 59
        assert result.frames.get(3).frameScore == 89
        assert result.frames.get(4).frameScore == 119
        assert result.frames.get(5).frameScore == 149
        assert result.frames.get(6).frameScore == 179
        assert result.frames.get(7).frameScore == 209
        assert result.frames.get(8).frameScore == 239
        assert result.frames.get(9).frameScore == 269
        assert result.lastScoredFrame == 10
    }
}
