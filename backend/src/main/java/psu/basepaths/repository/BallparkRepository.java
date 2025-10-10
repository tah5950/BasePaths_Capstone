package psu.basepaths.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import psu.basepaths.model.Ballpark;

public interface BallparkRepository extends JpaRepository<Ballpark, Long> {
    public Optional<Ballpark> findById(int id);
}
