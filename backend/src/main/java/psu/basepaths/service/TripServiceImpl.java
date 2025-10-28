package psu.basepaths.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.lang.Collections;
import psu.basepaths.model.Trip;
import psu.basepaths.model.TripDTO;
import psu.basepaths.model.TripStop;
import psu.basepaths.model.TripStopDTO;
import psu.basepaths.model.User;
import psu.basepaths.repository.TripRepository;

@Service
public class TripServiceImpl implements TripService {
    private final TripRepository tripRepository;

    public TripServiceImpl(TripRepository tripRepository){
        this.tripRepository = tripRepository;
    }

    @Override
    public TripDTO createTrip(TripDTO tripDTO) {
        validateTrip(tripDTO);

        Trip trip = convertToEntity(tripDTO);

        Trip createdTrip = tripRepository.save(trip);

        return convertToDTO(createdTrip);
    }

    @Override
    public List<TripDTO> getUserTrips(Long userid) {
        return tripRepository.findByUserId(userid)
            .orElse(Collections.emptyList())
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public TripDTO updateTrip(TripDTO tripDTO) {
        Trip existingTrip = tripRepository.findById(tripDTO.tripId())
            .orElseThrow(() -> new RuntimeException("Trip not found with id " + tripDTO.tripId()));

        existingTrip.setName(tripDTO.name());
        existingTrip.setStartDate(tripDTO.startDate());
        existingTrip.setEndDate(tripDTO.endDate());
        existingTrip.setStartLatitude(tripDTO.startLatitude());
        existingTrip.setStartLongitude(tripDTO.startLongitude());
        existingTrip.setEndLatitude(tripDTO.endLatitude());
        existingTrip.setEndLongitude(tripDTO.endLongitude());
        existingTrip.setIsGenerated(tripDTO.isGenerated());
        existingTrip.setMaxHoursPerDay(tripDTO.maxHoursPerDay());

        //Update trip stops
        existingTrip.getTripStops().clear();

        if(tripDTO.tripStops() != null) {
            List<TripStop> newStops = tripDTO.tripStops().stream()
                .map(stopDto -> {
                    TripStop stop = new TripStop();
                    stop.setId(stopDto.tripStopId());
                    stop.setDate(stopDto.date());
                    stop.setLocation(stopDto.location());
                    stop.setBallparkId(stopDto.ballparkId());
                    stop.setGameId(stopDto.gameId());
                    stop.setTrip(existingTrip);
                    return stop;
                }).collect(Collectors.toList());
            existingTrip.getTripStops().addAll(newStops);
        }

        Trip updatedTrip = tripRepository.save(existingTrip);
        return convertToDTO(updatedTrip);
    }

    private void validateTrip(TripDTO tripDTO){
        if(tripDTO.name().isEmpty()){
            throw new IllegalArgumentException("Name must not be blank");
        }
        if(tripDTO.startDate() == null) {
            throw new IllegalArgumentException("Start Date must not be empty");
        }
        if(tripDTO.endDate() == null) {
            throw new IllegalArgumentException("End Date must not be empty");
        }
        if(tripDTO.startDate().after(tripDTO.endDate())){
            throw new IllegalArgumentException("Start Date must be before End Date");
        }
    }
    
    private Trip convertToEntity(TripDTO tripDTO) {
        Trip trip = new Trip();
        trip.setId(tripDTO.tripId());
        trip.setName(tripDTO.name());
        trip.setStartDate(tripDTO.startDate());
        trip.setEndDate(tripDTO.endDate());
        trip.setStartLatitude(tripDTO.startLatitude());
        trip.setStartLongitude(tripDTO.startLongitude());
        trip.setEndLatitude(tripDTO.endLatitude());
        trip.setEndLongitude(tripDTO.endLongitude());
        trip.setIsGenerated(tripDTO.isGenerated());
        trip.setMaxHoursPerDay(tripDTO.maxHoursPerDay());
        trip.setUserId(tripDTO.userId());

        if (tripDTO.tripStops() != null) {
            List<TripStop> stops = tripDTO.tripStops()
                .stream()
                .map(stopDto -> {
                    TripStop stop = new TripStop();
                    stop.setId(stopDto.tripStopId());
                    stop.setDate(stopDto.date());
                    stop.setLocation(stopDto.location());
                    stop.setBallparkId(stopDto.ballparkId());
                    stop.setGameId(stopDto.gameId());
                    stop.setTrip(trip);
                    return stop;
                }).collect(Collectors.toList());
            trip.setTripStops(stops);
        }

        return trip;
    }

    private TripDTO convertToDTO(Trip trip){
        List<TripStopDTO> tripStopDTOs = trip.getTripStops()
            .stream()
            .map(stop -> {
                TripStopDTO stopDTO = new TripStopDTO(
                    stop.getId(),
                    stop.getDate(),
                    stop.getLocation(),
                    stop.getBallparkId(),
                    stop.getGameId()
                );
                return stopDTO;
            }).collect(Collectors.toList());
        
        TripDTO tripDTO = new TripDTO(
            trip.getId(),
            trip.getName(),
            trip.getStartDate(),
            trip.getEndDate(),
            trip.getStartLatitude(),
            trip.getStartLongitude(),
            trip.getEndLatitude(),
            trip.getEndLongitude(),
            trip.getIsGenerated(),
            trip.getMaxHoursPerDay(),
            trip.getUserId(),
            tripStopDTOs
        );
        

        return tripDTO;
    }
}
