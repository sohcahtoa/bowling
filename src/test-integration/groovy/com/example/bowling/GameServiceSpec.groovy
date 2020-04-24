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
        Frame frame = aRandom.frame()
            .rolls([4, 5] as int[])
            .build()

        when:
        Player result = responseToClass(addFrame(playerId, frame), Player.class)

        then:
        assert result.frames.size() == 1
        assert result.frames.get(0).frameScore == 9
        assert result.lastScoredFrame == 1
    }
}
