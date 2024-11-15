package clubbook.backend.configs;


import clubbook.backend.model.User;
import clubbook.backend.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuration class for Spring Security.
 * This class is responsible for defining beans related to security,
 * including user authentication and password encoding.
 */
@Configuration
public class ApplicationConfiguration {
    private final UserService userService;

    /**
     * Constructor for ApplicationConfiguration.
     *
     * @param userService the user service used to fetch user details.
     */
    public ApplicationConfiguration(UserService userService) {
        this.userService = userService;
    }

    /**
     * Bean definition for UserDetailsService.
     * This service is used for retrieving user information during authentication.
     *
     * @return a UserDetailsService that loads user by username (email).
     * @throws UsernameNotFoundException if the user cannot be found.
     */
    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            User user = userService.findByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException(username);
            } else {
                return user;
            }
        };
    }

    /**
     * Bean definition for BCryptPasswordEncoder.
     * This encoder is used for encoding passwords securely.
     *
     * @return a BCryptPasswordEncoder instance.
     */
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean definition for AuthenticationManager.
     * This manager is responsible for handling authentication requests.
     *
     * @param config the AuthenticationConfiguration object to retrieve the authentication manager.
     * @return the configured AuthenticationManager.
     * @throws Exception if there is an issue retrieving the authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean definition for AuthenticationProvider.
     * This provider is responsible for authenticating users using the user details service and password encoder.
     *
     * @return a configured DaoAuthenticationProvider.
     */
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
