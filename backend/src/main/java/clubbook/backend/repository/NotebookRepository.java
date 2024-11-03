package clubbook.backend.repository;

import clubbook.backend.model.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Notebook entities.
 * This interface extends JpaRepository to provide CRUD operations
 * for Notebook entities, including methods for retrieving, saving,
 * updating, and deleting notebooks.
 */
public interface NotebookRepository extends JpaRepository<Notebook, Integer> {
    // No additional methods are defined here, as the JpaRepository
    // provides the necessary CRUD functionality out of the box.
}
