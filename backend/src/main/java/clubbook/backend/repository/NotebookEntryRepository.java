package clubbook.backend.repository;

import clubbook.backend.model.NotebookEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface NotebookEntryRepository extends JpaRepository<NotebookEntry, Integer> {

    @Query("SELECT e FROM NotebookEntry e WHERE e.notebook.id = :notebookId ORDER BY e.date DESC")
    Page<NotebookEntry> findEntries(int notebookId, Pageable pageable);

    @Query("SELECT e FROM NotebookEntry e WHERE e.notebook.id = :notebookId AND e.date = CURRENT_DATE")
    NotebookEntry findTodayEntryByNotebookId(int notebookId);

    @Query("SELECT e.date FROM NotebookEntry e WHERE e.notebook.id = :notebookId")
    Set<LocalDate> findAllDates(int notebookId);

    @Query("SELECT n FROM NotebookEntry n WHERE n.date < :date ORDER BY n.date ASC")
    List<NotebookEntry> findEntriesBeforeDate(LocalDate date);
}
