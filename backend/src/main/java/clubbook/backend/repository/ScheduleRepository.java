package clubbook.backend.repository;

import clubbook.backend.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Schedule entities.
 * This interface extends JpaRepository to provide CRUD operations
 * for Schedule entities.
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    // No additional methods are defined here, as the JpaRepository
    // provides the necessary CRUD functionality out of the box.
}
