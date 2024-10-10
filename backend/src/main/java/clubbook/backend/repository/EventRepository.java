package clubbook.backend.repository;

import clubbook.backend.dtos.EventDto;
import clubbook.backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByDateGreaterThanEqualOrderByDate(LocalDate now);

    Event findFirstByDateGreaterThanEqualOrderByDateAsc(LocalDate now);

    List<Event> findAllByDateBeforeOrderByDateAsc(LocalDate now);

    @Query("SELECT e FROM Event e WHERE MONTH(e.date) = :month AND YEAR(e.date) = :year")
    List<Event> findAllByCurrentMonth(int month, int year);

    @Query("SELECT e FROM Event e WHERE e.birthYearStart <= :birthday AND e.birthYearEnd >= :birthday")
    List<Event> findAllEventsThatAdmit(@Param("birthday") LocalDate birthday);

    @Query(value = "SELECT * FROM T_Event e WHERE e.birth_year_start <= :birthday AND e.birth_year_end >= :birthday ORDER BY e.date ASC LIMIT 1", nativeQuery = true)
    Event findNextEventThatAdmit(@Param("birthday") LocalDate birthday);

    @Query("SELECT e FROM Event e WHERE e.birthYearStart <= :birthday AND e.birthYearEnd >= :birthday AND MONTH(e.date) = :monthValue AND YEAR(e.date) = :year")
    List<Event> findAllByCurrentMonthForStudent(@Param("monthValue") int monthValue, @Param("year") int year, @Param("birthday") LocalDate birthday);

    @Query("SELECT e FROM Event e WHERE e.date BETWEEN :today AND :dayAfterTomorrow")
    List<Event> findAllEventsInNextTwoDays(@Param("today") LocalDate today, @Param("dayAfterTomorrow") LocalDate dayAfterTomorrow);

}
