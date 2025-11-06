package psu.basepaths.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import psu.basepaths.model.Ballpark;
import psu.basepaths.model.Game;
import psu.basepaths.model.dto.GameDTO;
import psu.basepaths.repository.BallparkRepository;
import psu.basepaths.repository.GameRepository;

@Service
public class GameServiceImpl implements GameService{
    private final GameRepository gameRepository;

    private final BallparkRepository ballparkRepository;

    public GameServiceImpl(GameRepository gameRepository, BallparkRepository ballparkRepository){
        this.gameRepository = gameRepository;
        this.ballparkRepository = ballparkRepository;
    }

    @Override
    public int loadGames(List<GameDTO> games){
        int added = 0;
        for(GameDTO game: games){
            Game existing = gameRepository.findById(game.gameId()).orElse(null);
            if(existing == null){
                Game ent = convertToEntity(game);
                gameRepository.save(ent);
                added++;
            }
        }
        return added;
    }

    @Override
    public GameDTO getGameById(String gameId){
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new RuntimeException("Game not found"));

        return convertToDTO(game);
    }

    @Override
    public List<GameDTO> getGameByDateRange(Date start, Date end){
        List<Game> games = gameRepository.findByDateBetween(start, end);

        return games.stream().map(this::convertToDTO).toList();
    }

    private GameDTO convertToDTO(Game game){
        return new GameDTO(
            game.getId(),
            game.getDate(),
            game.getHomeTeam(),
            game.getAwayTeam(),
            game.getBallpark().getId()
        );
    }

     // Convert GameDTO to Game Entity
    private Game convertToEntity(GameDTO gameDTO) {
        Game game = new Game();
        game.setId(gameDTO.gameId());
        game.setDate(gameDTO.date());
        game.setHomeTeam(gameDTO.homeTeam());
        game.setAwayTeam(gameDTO.awayTeam());
        Ballpark ballpark = ballparkRepository.findById(gameDTO.ballparkId())
            .orElseThrow(() -> new RuntimeException("Ballpark not found for ID: " + gameDTO.ballparkId()));
        game.setBallpark(ballpark);
        return game;
    }
}
