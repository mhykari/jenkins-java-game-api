package com.example.game.service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class GameService {

    private int secretNumber;
    private int attempts;

    public GameService() {
        resetGame();
    }

    public String guess(int number) {
        attempts++;

        if (number < secretNumber) {
            return "too low";
        } else if (number > secretNumber) {
            return "too high";
        } else {
            return "correct!";
        }
    }

    public void resetGame() {
        this.secretNumber = new Random().nextInt(100) + 1;
        this.attempts = 0;
    }

    public int getAttempts() {
        return attempts;
    }
}
