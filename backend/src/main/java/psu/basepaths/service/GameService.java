package psu.basepaths.service;

import java.util.Date;
import java.util.List;

import psu.basepaths.model.dto.GameDTO;

public interface GameService {
    public int loadGames(List<GameDTO> games);
    public GameDTO getGameById(String gameId);
    public List<GameDTO> getGameByDateRange(Date start, Date end);
}
