package clubbook.backend.service;

import clubbook.backend.model.Role;
import clubbook.backend.model.enumClasses.RoleEnum;
import clubbook.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing roles within the application.
 * This class provides methods to save roles and retrieve them by name.
 */
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Saves a new role to the database.
     *
     * @param role the Role entity to be saved
     * @return the saved Role entity
     */
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    /**
     * Finds a role by its name.
     *
     * @param name the name of the role, represented as a RoleEnum
     * @return the Role entity if found, or null if no role with the specified name exists
     */
    public Role findByName(RoleEnum name) {
        Optional<Role> optionalRole = roleRepository.findByName(name);
        return optionalRole.orElse(null);
    }
}
