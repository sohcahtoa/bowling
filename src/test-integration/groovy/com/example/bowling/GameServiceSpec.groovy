package com.example.bowling

import com.example.bowling.apiModel.Game
import com.example.bowling.apiModel.Player
import org.springframework.http.HttpStatus

import static com.example.bowling.ARandom.aRandom

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
        for(Player player : foundGame.players) {
            assert player.currentFrame == 1
            assert player.currentScore == 0
        }
    }
}
