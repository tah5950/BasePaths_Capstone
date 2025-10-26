package psu.basepaths.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import psu.basepaths.model.TripDTO;
import psu.basepaths.model.User;
import psu.basepaths.service.TripService;

@RestController
@RequestMapping("/api/trip")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService){
        this.tripService = tripService;
    }

    @PostMapping("/create")
    public ResponseEntity<TripDTO> createTrip(@RequestBody TripDTO tripDTO, Authentication auth) {
        User user = (User) auth.getPrincipal();
        TripDTO tripWithUser = new TripDTO(
                tripDTO.tripId(), 
                tripDTO.name(),
                tripDTO.startDate(),
                tripDTO.endDate(),
                tripDTO.startLatitude(),
                tripDTO.startLongitude(),
                tripDTO.endLatitude(),
                tripDTO.endLongitude(),
                tripDTO.isGenerated(),
                tripDTO.maxHoursPerDay(),
                user.getId(),
                tripDTO.tripStops()
        );
        TripDTO created = tripService.createTrip(tripWithUser);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/getCurrentUserTrips")
    public ResponseEntity<List<TripDTO>> getTripsByUser(Authentication auth) {
        User user = (User) auth.getPrincipal();
        List<TripDTO> trips = tripService.getUserTrips(user.getId());
        return ResponseEntity.ok(trips);
    }

    @PutMapping("/updateTrip")
    public ResponseEntity<TripDTO> updateTrip(@RequestBody TripDTO tripDTO, Authentication auth) {
        TripDTO created = tripService.createTrip(tripDTO);
        return ResponseEntity.ok(created);
    }
}
