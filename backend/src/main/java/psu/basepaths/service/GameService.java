package psu.basepaths.service;

import java.util.List;

import psu.basepaths.model.GameDTO;

public interface GameService {
    public int loadGames(List<GameDTO> games);
}
