package com.example.bowling

import com.example.bowling.repository.GameRepository
import com.example.bowling.service.GameService
import spock.lang.Specification

import javax.persistence.EntityNotFoundException

import static com.example.bowling.ARandom.aRandom

class GameServiceSpec extends Specification {
    GameRepository gameRepository
    GameService gameService

    def setup() {
        gameRepository = Mock(GameRepository)
        gameService = new GameService(gameRepository)
    }

    def "should throw an EntityNotFoundException when game is not found" () {
        given:
        gameRepository.findById(_) >> Optional.empty()

        when:
        gameService.findGame(aRandom.uuid())

        then:
        thrown(EntityNotFoundException)
    }
}
