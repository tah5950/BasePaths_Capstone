package psu.basepaths.controller;

import psu.basepaths.model.GameDTO;
import psu.basepaths.service.GameService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

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
    public ResponseEntity<?> loadGames(@RequestBody @Valid List<@Valid GameDTO> games, @RequestHeader("X-LOAD-KEY") String loadKey) {

        if(!loadKey.equals(loadSecret)){
            return ResponseEntity.status(403).body("Unauthorized data load");
        }

        int added = 0;
        try{ 
            added = gameService.loadGames(games);
        }
        catch(RuntimeException ex){
             return ResponseEntity.status(403).body("Unauthorized data load. " + ex.getMessage());
        }
        return ResponseEntity.ok("Game data load complete. " + added + " new entries added.");
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<?> getGameById(@PathVariable String gameId, Authentication auth) {
        try{
            GameDTO game = gameService.getGameById(gameId);
            return ResponseEntity.ok(game);
        } 
        catch(RuntimeException e){
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
}
