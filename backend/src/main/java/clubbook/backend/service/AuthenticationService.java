package clubbook.backend.service;


import clubbook.backend.dtos.*;
import clubbook.backend.model.enumClasses.RoleEnum;
import clubbook.backend.model.User;
import clubbook.backend.repository.UserRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    //@Autowired
    private RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            RoleService roleService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    private String randomColour() {
        List<String> colours = new java.util.ArrayList<>(List.of("blue", "darkblue", "green", "orange", "pink", "purple", "red"));
        Collections.shuffle(colours);
        return colours.get(0);
    }

    public User signup(RegisterUserDto input) {
        User user = new User();
        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(roleService.findByName(RoleEnum.valueOf(input.getRole().toUpperCase())));
        user.setBirthday(input.getBirthday());
        user.setPhoneNumber(input.getPhoneNumber());
        user.setAddress(input.getAddress());
        user.setIdCard(input.getIdCard());
        user.setPartner(input.isPartner());
        try {
            String colour = randomColour();
            ClassPathResource imgFile = new ClassPathResource("assets/profilepics/profile_" + colour + ".png");
            byte[] profilePicture = Files.readAllBytes(imgFile.getFile().toPath());
            user.setProfilePicture(profilePicture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow();

        if (!user.isAllowedAccess()) {
            throw new BadCredentialsException("Access denied");
        }

        return user;
    }
}
