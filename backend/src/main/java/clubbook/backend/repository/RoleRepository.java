package clubbook.backend.repository;

import clubbook.backend.model.Role;
import clubbook.backend.model.enumClasses.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Role entities.
 * This interface extends JpaRepository to provide CRUD operations
 * for Role entities and includes methods for finding roles by their name.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Retrieves a Role entity by its name.
     *
     * @param name the name of the role as a RoleEnum
     * @return an Optional containing the Role entity if found, or empty if not found
     */
    Optional<Role> findByName(RoleEnum name);
}
