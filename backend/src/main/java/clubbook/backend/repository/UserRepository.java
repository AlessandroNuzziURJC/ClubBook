package clubbook.backend.repository;

import clubbook.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * This interface extends JpaRepository to provide CRUD operations for User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a User by their email.
     *
     * @param email the email of the User
     * @return an Optional containing the User if found, or empty if not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves a paginated list of Students ordered by their first names in ascending order.
     *
     * @param pageable pagination information
     * @return a Page of Users who are Students
     */
    @Query("SELECT u FROM User u WHERE u.role.name ='STUDENT' AND u.allowedAccess ORDER BY unaccent(u.firstName) ASC")
    Page<User> findAllStudentsByOrderByNameAsc(Pageable pageable);

    /**
     * Retrieves a list of Students filtered by a search term, ordered by their first names in ascending order.
     *
     * @param searchMod the search term used for filtering
     * @return a List of Users who are Students matching the search criteria
     */
    @Query(
            value = "SELECT * FROM T_user u JOIN T_role r ON u.role_fk_id = r.role_id WHERE r.name = 'STUDENT' AND (unaccent(u.first_name) ILIKE unaccent(:searchMod) OR unaccent(u.last_name) ILIKE unaccent(:searchMod)) AND u.allowed_access ORDER BY unaccent(u.first_name) ASC;",
            countQuery = "SELECT count(*) FROM T_user u JOIN T_role r ON u.role_fk_id = r.role_id WHERE r.name = 'STUDENT' AND (unaccent(u.first_name) ILIKE unaccent(:searchMod) OR unaccent(u.last_name) ILIKE unaccent(:searchMod)) AND u.allowed_access;",
            nativeQuery = true
    )
    List<User> findAllStudentsByOrderByNameAscWithSearch(String searchMod);

    /**
     * Retrieves a paginated list of Teachers ordered by their first names in ascending order.
     *
     * @param pageable pagination information
     * @return a Page of Users who are Teachers
     */
    @Query("SELECT u FROM User u WHERE u.role.name ='TEACHER' AND u.allowedAccess ORDER BY unaccent(u.firstName) ASC")
    Page<User> findAllTeachersByOrderByNameAsc(Pageable pageable);

    /**
     * Retrieves a list of Teachers filtered by a search term, ordered by their first names in ascending order.
     *
     * @param searchMod the search term used for filtering
     * @return a List of Users who are Teachers matching the search criteria
     */
    @Query(
            value = "SELECT * FROM T_user u JOIN T_role r ON u.role_fk_id = r.role_id WHERE r.name = 'TEACHER' AND (unaccent(u.first_name) ILIKE unaccent(:searchMod) OR unaccent(u.last_name) ILIKE unaccent(:searchMod)) AND u.allowed_access ORDER BY unaccent(u.first_name) ASC;",
            countQuery = "SELECT count(*) FROM T_user u JOIN T_role r ON u.role_fk_id = r.role_id WHERE r.name = 'TEACHER' AND (unaccent(u.first_name) ILIKE unaccent(:searchMod) OR unaccent(u.last_name) ILIKE unaccent(:searchMod)) AND u.allowed_access;",
            nativeQuery = true
    )
    List<User> findAllTeachersByOrderByNameAscWithSearch(String searchMod);

    /**
     * Retrieves a list of all Teachers.
     *
     * @return a List of Users who are Teachers
     */
    @Query("SELECT u FROM User u WHERE u.role.name ='TEACHER' AND u.allowedAccess ORDER BY unaccent(u.firstName) ASC")
    List<User> findAllTeachers();

    /**
     * Retrieves a list of Students who do not belong to any class group.
     *
     * @return a List of Users who are Students without class groups
     */
    @Query(
            value = "SELECT * FROM T_USER LEFT OUTER JOIN T_CLASS_GROUP_STUDENTS ON students_id = id WHERE role_fk_id = 1 AND class_group_id IS NULL AND allowed_access ORDER BY first_name; ",
            nativeQuery = true
    )
    List<User> findAllStudentsWithoutClassGroup();

    /**
     * Retrieves a list of Users born between specified years who have a specific role.
     *
     * @param birthYearStart the start year for the birth date range
     * @param birthYearEnd the end year for the birth date range
     * @param roleName the name of the role
     * @return a List of Users matching the criteria
     */
    @Query(
            value = "SELECT u.* FROM T_user u JOIN T_role r ON u.role_fk_id = r.role_id " +
                    "WHERE r.name = :roleName AND u.birthday BETWEEN :birthYearStart AND :birthYearEnd AND u.allowed_access;",
            nativeQuery = true
    )
    List<User> findAllUsersBornBetweenWithRole(@Param("birthYearStart") LocalDate birthYearStart,
                                               @Param("birthYearEnd") LocalDate birthYearEnd,
                                               @Param("roleName") String roleName);

    /**
     * Retrieves a list of all Administrators.
     *
     * @return a List of Users who are Administrators
     */
    @Query("SELECT u FROM User u WHERE u.role.name ='ADMINISTRATOR' AND u.allowedAccess ORDER BY unaccent(u.firstName) ASC")
    List<User> findAllAdministrators();

    /**
     * Retrieves a list of all Administrators excluding a specific User by ID.
     *
     * @param id the ID of the User to exclude
     * @return a List of Users who are Administrators, excluding the specified User
     */
    @Query("SELECT u FROM User u WHERE u.role.name ='ADMINISTRATOR' AND u.id != 1 AND u.id != :id AND u.allowedAccess ORDER BY unaccent(u.firstName) ASC")
    List<User> findAllAdministratorsExceptId(int id);

    /**
     * Deletes Users who do not have access.
     */
    void deleteByAllowedAccessFalse();
}
