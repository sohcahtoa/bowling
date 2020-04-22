package com.example.bowling.service;

import com.example.bowling.entity.GameEntity;
import com.example.bowling.entity.PlayerEntity;
import com.example.bowling.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    public GameEntity createGame(List<String> playerNames) {
        GameEntity gameEntity = GameEntity.builder().build();
        List<PlayerEntity> players = buildPlayersForGame(playerNames, gameEntity);
        gameEntity.setPlayers(players);
        return gameRepository.save(gameEntity);
    }

    public GameEntity findGame(UUID gameId) {
        return gameRepository.findById(gameId).orElseThrow(EntityNotFoundException::new);
    }

    private List<PlayerEntity> buildPlayersForGame(List<String> playerNames, GameEntity gameEntity) {
        List<PlayerEntity> players = new ArrayList<>();
        for(String playerName : playerNames) {
            PlayerEntity playerEntity = PlayerEntity.builder()
                    .game(gameEntity)
                    .name(playerName)
                    .build();
            players.add(playerEntity);
        }
        return players;
    }
}
