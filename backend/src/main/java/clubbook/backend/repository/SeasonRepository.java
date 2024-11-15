package clubbook.backend.repository;

import clubbook.backend.model.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Season entities.
 * This interface extends JpaRepository to provide CRUD operations
 * for Season entities.
 */
@Repository
public interface SeasonRepository extends JpaRepository<Season, Integer> {

    /**
     * Finds the active Season entity.
     *
     * @param active a boolean indicating whether to find the active season
     * @return the active Season entity, or null if none is found
     */
    Season findByActive(boolean active);
}
