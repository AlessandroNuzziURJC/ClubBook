package clubbook.backend.repository;

import clubbook.backend.model.ClassGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing ClassGroup entities.
 * This interface extends JpaRepository to provide CRUD operations
 * and custom query methods for class group-related data.
 */
@Repository
public interface ClassGroupRepository extends JpaRepository<ClassGroup, Integer> {

    /**
     * Finds all ClassGroup records that include a specific student by their ID.
     *
     * @param studentId the ID of the student whose class groups are to be retrieved
     * @return a list of ClassGroup entities associated with the specified student ID
     */
    @Query("SELECT cg FROM ClassGroup cg JOIN cg.students s WHERE s.id = :studentId")
    List<ClassGroup> findByStudentId(int studentId);

    /**
     * Finds all ClassGroup records that include a specific teacher by their ID.
     *
     * @param teacherId the ID of the teacher whose class groups are to be retrieved
     * @return a list of ClassGroup entities associated with the specified teacher ID
     */
    @Query("SELECT cg FROM ClassGroup cg JOIN cg.teachers s WHERE s.id = :teacherId")
    List<ClassGroup> findByTeacherId(int teacherId);
}
