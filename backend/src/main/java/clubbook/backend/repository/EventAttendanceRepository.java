package clubbook.backend.repository;

import clubbook.backend.model.EventAttendance;
import clubbook.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for managing EventAttendance entities.
 * This interface extends JpaRepository to provide CRUD operations
 * and custom query methods for event attendance-related data.
 */
@Repository
public interface EventAttendanceRepository extends JpaRepository<EventAttendance, Integer> {
    /**
     * Finds an EventAttendance record based on the specified event ID and user ID.
     *
     * @param event the ID of the event for which to find attendance
     * @param user  the ID of the user for whom to find attendance
     * @return an Optional containing the EventAttendance record if found, otherwise empty
     */
    @Query("SELECT ea FROM EventAttendance ea WHERE ea.event.id = :event AND ea.user.id = :user")
    Optional<EventAttendance> findByEventAndUser(int event, int user);

    /**
     * Retrieves a list of EventAttendance records for a specific event ID
     * and user role.
     *
     * @param eventId the ID of the event to filter attendance records
     * @param role    the role of the users whose attendance records are to be retrieved
     * @return a list of EventAttendance records matching the specified event ID and user role
     */
    @Query("SELECT ea FROM EventAttendance ea WHERE ea.event.id = :eventId AND ea.user.role = :role")
    List<EventAttendance> findByEventIdAndUserRole(@Param("eventId") int eventId, @Param("role") Role role);

    /**
     * Retrieves a set of EventAttendance records for a specific event ID
     * and user role.
     *
     * @param eventId the ID of the event to filter attendance records
     * @param role    the role of the users whose attendance records are to be retrieved
     * @return a set of EventAttendance records matching the specified event ID and user role
     */
    @Query("SELECT ea FROM EventAttendance ea WHERE ea.event.id = :eventId AND ea.user.role = :role")
    Set<EventAttendance> findByEventId(@Param("eventId") int eventId, @Param("role") Role role);
}
