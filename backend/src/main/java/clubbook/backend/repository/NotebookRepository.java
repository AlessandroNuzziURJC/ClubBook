package clubbook.backend.repository;

import clubbook.backend.model.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotebookRepository extends JpaRepository<Notebook, Integer> {

}
