package psu.basepaths.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import psu.basepaths.model.TripStop;

public interface TripStopRepository extends JpaRepository<TripStop, Long> {
    public Optional<List<TripStop>> findByTrip_id(Long tripid);
}
