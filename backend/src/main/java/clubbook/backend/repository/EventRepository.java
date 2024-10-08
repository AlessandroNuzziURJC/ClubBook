package clubbook.backend.repository;

import clubbook.backend.dtos.EventDto;
import clubbook.backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByDateGreaterThanEqualOrderByDate(LocalDate now);

    Event findFirstByDateGreaterThanEqualOrderByDateAsc(LocalDate now);

    List<Event> findAllByDateBeforeOrderByDateAsc(LocalDate now);

    @Query("SELECT e FROM Event e WHERE MONTH(e.date) = :month AND YEAR(e.date) = :year")
    List<Event> findAllByCurrentMonth(int month, int year);
}
