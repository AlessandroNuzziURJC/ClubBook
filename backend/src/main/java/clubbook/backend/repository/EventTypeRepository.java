package clubbook.backend.repository;

import clubbook.backend.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing EventType entities.
 * This interface extends JpaRepository to provide CRUD operations
 * for EventType entities, including methods for retrieving, saving,
 * updating, and deleting event types.
 */
@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Integer> {
    // No additional methods are defined here, as the JpaRepository
    // provides the necessary CRUD functionality out of the box.
}
