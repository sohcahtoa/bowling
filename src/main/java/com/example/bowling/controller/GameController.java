package com.example.bowling.controller;

import com.example.bowling.apiModel.Game;
import com.example.bowling.service.GameService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bowling")
public class GameController {
    @Autowired
    private GameService gameService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/game")
    public ResponseEntity<Game> createGame(@RequestBody List<String> playerNames) {
        return new ResponseEntity<>(
                modelMapper.map(gameService.createGame(playerNames), Game.class), HttpStatus.CREATED
        );
    }

    @GetMapping("/game/{id}")
    public ResponseEntity<Game> findGame(@PathVariable UUID id) {
        return new ResponseEntity<>(
                modelMapper.map(gameService.findGame(id), Game.class), HttpStatus.OK
        );
    }
}
