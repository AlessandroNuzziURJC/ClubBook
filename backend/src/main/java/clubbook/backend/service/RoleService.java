package clubbook.backend.service;

import clubbook.backend.model.Role;
import clubbook.backend.model.RoleEnum;
import clubbook.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public Role findByName(RoleEnum name) {
        Optional<Role> optionalRole = roleRepository.findByName(name);
        return optionalRole.orElse(null);
    }
}
