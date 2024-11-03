package clubbook.backend.repository;

import clubbook.backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing Event entities.
 * This interface extends JpaRepository to provide CRUD operations
 * and custom query methods for event-related data.
 */
public interface EventRepository extends JpaRepository<Event, Integer> {

    /**
     * Finds all Event records with a date greater than or equal to the specified date,
     * ordered by date in ascending order.
     *
     * @param now the date to compare against
     * @return a list of Event entities occurring on or after the specified date
     */
    List<Event> findAllByDateGreaterThanEqualOrderByDate(LocalDate now);

    /**
     * Finds the first Event record with a date greater than or equal to the specified date,
     * ordered by date in ascending order.
     *
     * @param now the date to compare against
     * @return the first Event entity occurring on or after the specified date
     */
    Event findFirstByDateGreaterThanEqualOrderByDateAsc(LocalDate now);

    /**
     * Finds all Event records with a date before the specified date,
     * ordered by date in ascending order.
     *
     * @param now the date to compare against
     * @return a list of Event entities occurring before the specified date
     */
    List<Event> findAllByDateBeforeOrderByDateAsc(LocalDate now);

    /**
     * Finds all Event records that occur in the specified month and year.
     *
     * @param month the month to filter events by
     * @param year  the year to filter events by
     * @return a list of Event entities occurring in the specified month and year
     */
    @Query("SELECT e FROM Event e WHERE MONTH(e.date) = :month AND YEAR(e.date) = :year")
    List<Event> findAllByCurrentMonth(int month, int year);

    /**
     * Finds all Event records that admit users with the specified birthday.
     *
     * @param birthday the birthday to filter events by
     * @return a list of Event entities that admit users with the specified birthday
     */
    @Query("SELECT e FROM Event e WHERE e.birthYearStart <= :birthday AND e.birthYearEnd >= :birthday")
    List<Event> findAllEventsThatAdmit(@Param("birthday") LocalDate birthday);

    /**
     * Finds the next Event record that admits users with the specified birthday.
     *
     * @param birthday the birthday to filter events by
     * @return the next Event entity that admits users with the specified birthday
     */
    @Query(value = "SELECT * FROM T_Event e WHERE e.birth_year_start <= :birthday AND e.birth_year_end >= :birthday ORDER BY e.date ASC LIMIT 1", nativeQuery = true)
    Event findNextEventThatAdmit(@Param("birthday") LocalDate birthday);

    /**
     * Finds all Event records for the current month that admit users with the specified birthday.
     *
     * @param monthValue the month to filter events by
     * @param year      the year to filter events by
     * @param birthday   the birthday to filter events by
     * @return a list of Event entities for the current month that admit users with the specified birthday
     */
    @Query("SELECT e FROM Event e WHERE e.birthYearStart <= :birthday AND e.birthYearEnd >= :birthday AND MONTH(e.date) = :monthValue AND YEAR(e.date) = :year")
    List<Event> findAllByCurrentMonthForStudent(@Param("monthValue") int monthValue, @Param("year") int year, @Param("birthday") LocalDate birthday);

    /**
     * Finds all Event records occurring in the next two days from the specified date.
     *
     * @param today              the starting date
     * @param dayAfterTomorrow   the date two days after today
     * @return a list of Event entities occurring between today and the day after tomorrow
     */
    @Query("SELECT e FROM Event e WHERE e.date BETWEEN :today AND :dayAfterTomorrow")
    List<Event> findAllEventsInNextTwoDays(@Param("today") LocalDate today, @Param("dayAfterTomorrow") LocalDate dayAfterTomorrow);

    /**
     * Finds all Event records with a deadline that matches the specified date.
     *
     * @param today the date to filter events by
     * @return a list of Event entities with a deadline matching today
     */
    @Query("SELECT e FROM Event e WHERE e.deadline = :today")
    List<Event> findAllEventsDeadlineToday(LocalDate today);

    /**
     * Finds all Event records with a deadline that matches the specified date (yesterday).
     *
     * @param yesterday the date to filter events by
     * @return a list of Event entities with a deadline matching yesterday
     */
    @Query("SELECT e FROM Event e WHERE e.deadline = :yesterday")
    List<Event> findAllEventsInscriptionFinished(LocalDate yesterday);
}
