package com.example.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.example.game.service.GameService;

public class GameServiceTest {

    @Test
    void testGuessDoesNotBreak() {
        GameService game = new GameService();
        String result = game.guess(50);

        assertNotNull(result);
        assertTrue(result.equals("too low") ||
                   result.equals("too high") ||
                   result.equals("correct!"));
    }

    @Test
    void testReset() {
        GameService game = new GameService();
        game.guess(10); // increase attempts
        assertTrue(game.getAttempts() > 0);

        game.resetGame();
        assertEquals(0, game.getAttempts());
    }
}
