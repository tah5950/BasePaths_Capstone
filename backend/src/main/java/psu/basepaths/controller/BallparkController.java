package psu.basepaths.controller;

import psu.basepaths.model.BallparkDTO;
import psu.basepaths.service.BallparkService;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ballpark")
public class BallparkController {
    
    private final BallparkService ballparkService;

    @Value("${data.load.secret}")
    private String loadSecret;

    public BallparkController(BallparkService ballparkService){
        this.ballparkService = ballparkService;
    }

    @PostMapping("/load")
    public ResponseEntity<?> loadBallparks(@RequestBody @Valid List<@Valid BallparkDTO> ballparks, @RequestHeader("X-LOAD-KEY") String loadKey) {

        if(!loadKey.equals(loadSecret)){
            return ResponseEntity.status(403).body("Unauthorized data load");
        }

        int added = ballparkService.loadBallparks(ballparks);
        return ResponseEntity.ok("Ballpark data load complete. " + added + " new entries added.");
    }
}
