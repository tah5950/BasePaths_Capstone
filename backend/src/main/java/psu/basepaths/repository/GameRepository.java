package psu.basepaths.repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import psu.basepaths.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
    public Optional<Game> findById(String id);
    public List<Game> findByDateBetween(Date start, Date end);
}
