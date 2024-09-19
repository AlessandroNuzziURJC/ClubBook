package clubbook.backend.repository;

import clubbook.backend.model.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Integer> {

    Season findByActive(boolean active);
}
