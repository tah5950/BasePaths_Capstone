package psu.basepaths.service;

import java.util.List;

import org.springframework.stereotype.Service;

import psu.basepaths.model.Game;
import psu.basepaths.model.GameDTO;
import psu.basepaths.repository.GameRepository;

@Service
public class GameServiceImpl implements GameService{
    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    @Override
    public int loadGames(List<GameDTO> games){
        int added = 0;
        for(GameDTO game: games){
            gameRepository.save(convertToEntity(game));
            added++;
        }
        return added;
    }

    private GameDTO convertToDTO(Game game){
        return new GameDTO(
            game.getId(),
            game.getDate(),
            game.getHomeTeam(),
            game.getAwayTeam(),
            game.getBallpark()
        );
    }

     // Convert GameDTO to Game Entity
    private Game convertToEntity(GameDTO gameDTO) {
        Game game = new Game();
        game.setId(gameDTO.id());
        game.setDate(gameDTO.date());
        game.setHomeTeam(gameDTO.homeTeam());
        game.setAwayTeam(gameDTO.awayTeam());
        game.setBallpark(gameDTO.ballpark());
        return game;
    }
}
