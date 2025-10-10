package psu.basepaths.service;

import java.util.List;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import psu.basepaths.model.BallparkDTO;
import psu.basepaths.model.UserDTO;

public interface BallparkService {
    public int loadBallparks(List<BallparkDTO> ballparks);
}
