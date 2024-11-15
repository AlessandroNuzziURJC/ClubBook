package clubbook.backend.service;

import clubbook.backend.model.ClassGroup;
import clubbook.backend.model.Role;
import clubbook.backend.model.User;
import clubbook.backend.repository.ClassGroupRepository;
import clubbook.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing users within the application.
 * This class provides methods to create, update, delete, and retrieve user information.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    private final ClassGroupRepository classGroupRepository;

    /**
     * Constructs a new UserService with the specified user and class group repositories.
     *
     * @param userRepository the repository for managing user data
     * @param classGroupRepository the repository for managing class group data
     */
    @Autowired
    public UserService(UserRepository userRepository, ClassGroupRepository classGroupRepository) {
        this.userRepository = userRepository;
        this.classGroupRepository = classGroupRepository;
    }

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Saves a new user or updates an existing user in the system.
     *
     * @param user the user to be saved
     * @return the saved user
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user to find
     * @return the user if found, null otherwise
     */
    public User findByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElse(null);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find
     * @return the user if found, null otherwise
     */
    public User findById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    /**
     * Retrieves a paginated list of students sorted by their name.
     *
     * @param pageNumber the page number to retrieve
     * @param pageSize the number of students per page
     * @return a page of users who are students
     */
    public Page<User> getStudentsPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAllStudentsByOrderByNameAsc(pageable);
    }

    /**
     * Modifies the search string to format it for database querying.
     *
     * @param search the original search string
     * @return the modified search string suitable for a LIKE query
     */
    private String modifySearch(String search) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < search.length(); i++) {
            char currentChar = search.charAt(i);
            if ((currentChar >= 'a' && currentChar <= 'z')|| (currentChar >= 'A' && currentChar <= 'Z')) {
                output.append(currentChar);
            } else {
                output.append('_');
            }
        }
        return '%' + output.toString() + '%';
    }

    /**
     * Retrieves a filtered list of students by their name.
     *
     * @param search the name to filter by
     * @return a list of students matching the search criteria
     */
    public List<User> getStudentsListFilteredByName(String search) {
        String searchMod = modifySearch(search);
        return userRepository.findAllStudentsByOrderByNameAscWithSearch(searchMod);
    }

    /**
     * Retrieves a paginated list of teachers sorted by their name.
     *
     * @param pageNumber the page number to retrieve
     * @param pageSize the number of teachers per page
     * @return a page of users who are teachers
     */
    public Page<User> getTeachersPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAllTeachersByOrderByNameAsc(pageable);
    }

    /**
     * Retrieves a filtered list of teachers by their name.
     *
     * @param search the name to filter by
     * @return a list of teachers matching the search criteria
     */
    public List<User> getTeachersListFilteredByName(String search) {
        String searchMod = modifySearch(search);
        return userRepository.findAllTeachersByOrderByNameAscWithSearch(searchMod);
    }

    /**
     * Retrieves all teachers in the system.
     *
     * @return a list of all teachers
     */
    public List<User> getAllTeachers() {
        return userRepository.findAllTeachers();
    }

    /**
     * Retrieves all students who are not assigned to any class group.
     *
     * @return a list of students without class groups
     */
    public List<User> getAllStudentsWithoutClassGroup() {
        return userRepository.findAllStudentsWithoutClassGroup();
    }

    /**
     * Finds users born between the specified years and with a given role.
     *
     * @param birthYearStart the start of the birth year range
     * @param birthYearEnd the end of the birth year range
     * @param role the role of the users to find
     * @return a list of users matching the criteria
     */
    public List<User> findUserBornBetween(LocalDate birthYearStart, LocalDate birthYearEnd, Role role) {
        return this.userRepository.findAllUsersBornBetweenWithRole(birthYearStart, birthYearEnd, role.getName().name());
    }

    /**
     * Retrieves all administrators in the system.
     *
     * @return a list of all administrators
     */
    public List<User> findAllAdministrators() {
        return this.userRepository.findAllAdministrators();
    }

    /**
     * Retrieves all administrators except for the specified user ID.
     *
     * @param id the ID of the administrator to exclude
     * @return a list of administrators excluding the specified ID
     */
    public List<User> findAllAdministratorsExceptId(int id) {
        return this.userRepository.findAllAdministratorsExceptId(id);
    }

    /**
     * Extracts a user by their ID and checks if they are not assigned to any class groups.
     *
     * @param id the ID of the user to extract
     * @return the user if they are not assigned to any class groups, null otherwise
     */
    private User extractUser(Integer id) {
        User user = this.userRepository.findById(id).orElseThrow();
        List<ClassGroup> classGroups;
        classGroups = this.classGroupRepository.findByStudentId(id);
        if (!classGroups.isEmpty()) {
            return null;
        }
        classGroups = this.classGroupRepository.findByTeacherId(id);
        if (!classGroups.isEmpty()) {
            return null;
        }
        return user;
    }

    /**
     * Changes the access status of a user to not allowed.
     *
     * @param id the ID of the user to change the status for
     * @return true if the status was successfully changed, false otherwise
     */
    public Boolean changeStatusUser(Integer id) {
        User user = this.extractUser(id);
        if (user == null) {
            return false;
        }
        user.setAllowedAccess(false);
        this.userRepository.save(user);
        return true;
    }

    /**
     * Deletes a user from the system.
     *
     * @param id the ID of the user to delete
     * @return true if the user was successfully deleted, false otherwise
     */
    public Boolean deleteUser(Integer id) {
        User user = this.extractUser(id);
        if (user == null) {
            return false;
        }
        this.userRepository.delete(user);
        return true;
    }

    /**
     * Removes all users who do not have access.
     */
    @Transactional
    public void removeUsers() {
        this.userRepository.deleteByAllowedAccessFalse();
    }
}
