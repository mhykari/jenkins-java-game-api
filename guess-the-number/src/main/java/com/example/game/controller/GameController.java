package com.example.game.controller;

import com.example.game.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService game;

    public GameController(GameService game) {
        this.game = game;
    }

    @GetMapping("/guess")
    public String guess(@RequestParam int number) {
        return game.guess(number);
    }

    @PostMapping("/reset")
    public String reset() {
        game.resetGame();
        return "game reset!";
    }

    @GetMapping("/attempts")
    public int attempts() {
        return game.getAttempts();
    }
}
