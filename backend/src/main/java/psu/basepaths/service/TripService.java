package psu.basepaths.service;

import java.util.List;

import psu.basepaths.model.dto.TripDTO;

public interface TripService {
    public TripDTO createTrip(TripDTO tripDTO);
    public List<TripDTO> getUserTrips(Long userid);
    public TripDTO updateTrip(TripDTO tripDTO);
    public TripDTO getTripById(Long tripid, Long userid);
    public TripDTO generateTrip(TripDTO trip);
}
