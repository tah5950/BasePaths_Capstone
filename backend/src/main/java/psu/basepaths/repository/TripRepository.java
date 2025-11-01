package psu.basepaths.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import psu.basepaths.model.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {
    public Optional<List<Trip>> findByUserId(Long userid);
    public Optional<Trip> findByIdAndUserId(Long tripid, Long userid);
}
