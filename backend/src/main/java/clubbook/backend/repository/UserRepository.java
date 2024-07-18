package clubbook.backend.repository;

import clubbook.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role.name ='STUDENT' ORDER BY unaccent(u.firstName) ASC")
    Page<User> findAllStudentsByOrderByNameAsc(Pageable pageable);

    @Query(
            value = "SELECT * FROM T_user u JOIN T_role r ON u.role_fk_id = r.role_id WHERE r.name = 'STUDENT' AND (unaccent(u.first_name) ILIKE unaccent(:searchMod) OR unaccent(u.last_name) ILIKE unaccent(:searchMod)) ORDER BY unaccent(u.first_name) ASC;",
            countQuery = "SELECT count(*) FROM T_user u JOIN T_role r ON u.role_fk_id = r.role_id WHERE r.name = 'STUDENT' AND (unaccent(u.first_name) ILIKE unaccent(:searchMod) OR unaccent(u.last_name) ILIKE unaccent(:searchMod));",
            nativeQuery = true
    )
    List<User> findAllStudentsByOrderByNameAscWithSearch(String searchMod);

    @Query("SELECT u FROM User u WHERE u.role.name ='TEACHER' ORDER BY unaccent(u.firstName) ASC")
    Page<User> findAllTeachersByOrderByNameAsc(Pageable pageable);

    @Query(
            value = "SELECT * FROM T_user u JOIN T_role r ON u.role_fk_id = r.role_id WHERE r.name = 'TEACHER' AND (unaccent(u.first_name) ILIKE unaccent(:searchMod) OR unaccent(u.last_name) ILIKE unaccent(:searchMod)) ORDER BY unaccent(u.first_name) ASC;",
            countQuery = "SELECT count(*) FROM T_user u JOIN T_role r ON u.role_fk_id = r.role_id WHERE r.name = 'TEACHER' AND (unaccent(u.first_name) ILIKE unaccent(:searchMod) OR unaccent(u.last_name) ILIKE unaccent(:searchMod));",
            nativeQuery = true
    )
    List<User> findAllTeachersByOrderByNameAscWithSearch(String searchMod);
}
