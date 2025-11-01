package psu.basepaths.service;

import java.util.List;
import java.util.Optional;

import psu.basepaths.model.TripDTO;

public interface TripService {
    public TripDTO createTrip(TripDTO tripDTO);
    public List<TripDTO> getUserTrips(Long userid);
    public TripDTO updateTrip(TripDTO tripDTO);
    public TripDTO getTripById(Long tripid, Long userid);
}
