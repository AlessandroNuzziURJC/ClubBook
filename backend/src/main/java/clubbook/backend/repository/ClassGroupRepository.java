package clubbook.backend.repository;

import clubbook.backend.model.ClassGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassGroupRepository extends JpaRepository<ClassGroup, Integer> {

    @Query("SELECT cg FROM ClassGroup cg JOIN cg.students s WHERE s.id = :studentId")
    List<ClassGroup> findByStudentId(int studentId);

    @Query("SELECT cg FROM ClassGroup cg JOIN cg.teachers s WHERE s.id = :teacherId")
    List<ClassGroup> findByTeacherId(int teacherId);
}
