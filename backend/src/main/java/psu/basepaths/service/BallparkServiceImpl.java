package psu.basepaths.service;

import java.util.List;

import org.springframework.stereotype.Service;

import psu.basepaths.model.Ballpark;
import psu.basepaths.model.BallparkDTO;
import psu.basepaths.repository.BallparkRepository;

@Service
public class BallparkServiceImpl implements BallparkService{
    private final BallparkRepository ballparkRepository;

    public BallparkServiceImpl(BallparkRepository ballparkRepository){
        this.ballparkRepository = ballparkRepository;
    }

    @Override
    public int loadBallparks(List<BallparkDTO> ballparks){
        int added = 0;
        for(BallparkDTO ballpark: ballparks){
            Ballpark existing = ballparkRepository.findById(ballpark.id()).orElse(null);
            if(existing == null){
                Ballpark ent = convertToEntity(ballpark);
                ballparkRepository.save(ent);
                added++;
            }
        }
        return added;
    }

    private BallparkDTO convertToDTO(Ballpark ballpark){
        return new BallparkDTO(
            ballpark.getId(),
            ballpark.getName(),
            ballpark.getTeamName(),
            ballpark.getCity(),
            ballpark.getState(),
            ballpark.getCountry(),
            ballpark.getLatitude(),
            ballpark.getLongitude()
        );
    }

     // Convert BallparkDTO to Ballpark Entity
    private Ballpark convertToEntity(BallparkDTO ballparkDTO) {
        Ballpark ballpark = new Ballpark();
        ballpark.setId(ballparkDTO.id());
        ballpark.setName(ballparkDTO.name());
        ballpark.setTeamName(ballparkDTO.teamName());
        ballpark.setCity(ballparkDTO.city());
        ballpark.setState(ballparkDTO.state());
        ballpark.setCountry(ballparkDTO.country());
        ballpark.setLatitude(ballparkDTO.latitude());
        ballpark.setLongitude(ballparkDTO.longitude());
        return ballpark;
    }
}
