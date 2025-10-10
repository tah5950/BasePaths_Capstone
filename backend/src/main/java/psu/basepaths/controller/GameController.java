package psu.basepaths.controller;

import psu.basepaths.model.GameDTO;
import psu.basepaths.service.GameService;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {
    
    private final GameService gameService;

    @Value("${data.load.secret}")
    private String loadSecret;

    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    @PostMapping("/load")
    public ResponseEntity<?> loadGames(@RequestBody List<GameDTO> games, @RequestHeader("X-LOAD-KEY") String loadKey) {

        if(!loadKey.equals(loadSecret)){
            return ResponseEntity.status(403).body("Unauthorized data load");
        }

        int added = gameService.loadGames(games);
        return ResponseEntity.ok("Game data load complete. " + added + " new entries added.");
    }
}
