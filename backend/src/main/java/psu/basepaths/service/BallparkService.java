package psu.basepaths.service;

import java.util.List;

import psu.basepaths.model.BallparkDTO;

public interface BallparkService {
    public int loadBallparks(List<BallparkDTO> ballparks);
}
