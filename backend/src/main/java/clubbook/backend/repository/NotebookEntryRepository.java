package clubbook.backend.repository;

import clubbook.backend.model.NotebookEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Repository interface for managing NotebookEntry entities.
 * This interface extends JpaRepository to provide CRUD operations
 * for NotebookEntry entities, including methods for retrieving,
 * saving, updating, and deleting notebook entries.
 */
public interface NotebookEntryRepository extends JpaRepository<NotebookEntry, Integer> {

    /**
     * Retrieves a paginated list of notebook entries for a given notebook ID,
     * ordered by the entry date in descending order.
     *
     * @param notebookId the ID of the notebook for which to retrieve entries
     * @param pageable   the pagination information (page number, size, etc.)
     * @return a page of NotebookEntry entities
     */
    @Query("SELECT e FROM NotebookEntry e WHERE e.notebook.id = :notebookId ORDER BY e.date DESC")
    Page<NotebookEntry> findEntries(int notebookId, Pageable pageable);

    /**
     * Retrieves the notebook entry for today for a given notebook ID.
     *
     * @param notebookId the ID of the notebook for which to retrieve today's entry
     * @return the NotebookEntry for today, or null if none exists
     */
    @Query("SELECT e FROM NotebookEntry e WHERE e.notebook.id = :notebookId AND e.date = CURRENT_DATE")
    NotebookEntry findTodayEntryByNotebookId(int notebookId);

    /**
     * Retrieves all unique dates of notebook entries for a given notebook ID.
     *
     * @param notebookId the ID of the notebook for which to retrieve entry dates
     * @return a set of LocalDate representing the dates of all entries
     */
    @Query("SELECT e.date FROM NotebookEntry e WHERE e.notebook.id = :notebookId")
    Set<LocalDate> findAllDates(int notebookId);

    /**
     * Retrieves all notebook entries before a specified date, ordered by date in ascending order.
     *
     * @param date the date before which to retrieve entries
     * @return a list of NotebookEntry entities
     */
    @Query("SELECT n FROM NotebookEntry n WHERE n.date < :date ORDER BY n.date ASC")
    List<NotebookEntry> findEntriesBeforeDate(LocalDate date);
}
