package clubbook.backend.repository;

import clubbook.backend.model.Event;
import clubbook.backend.model.EventAttendance;
import clubbook.backend.model.Role;
import clubbook.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventAttendanceRepository extends JpaRepository<EventAttendance, Integer> {
    @Query("SELECT ea FROM EventAttendance ea WHERE ea.event.id = :event AND ea.user.id = :user")
    Optional<EventAttendance> findByEventAndUser(int event, int user);

    @Query("SELECT ea FROM EventAttendance ea WHERE ea.event.id = :eventId AND ea.user.role = :role")
    List<EventAttendance> findByEventIdAndUserRole(@Param("eventId") int eventId, @Param("role") Role role);

    @Query("SELECT ea FROM EventAttendance ea WHERE ea.event.id = :eventId AND ea.user.role = :role")
    Set<EventAttendance> findByEventId(@Param("eventId") int eventId, @Param("role") Role role);
}
