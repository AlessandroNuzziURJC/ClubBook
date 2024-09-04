package clubbook.backend.repository;

import clubbook.backend.dtos.UserAttendanceDto;
import clubbook.backend.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    @Query(
            value = "SELECT u.id, u.first_name, u.last_name, ARRAY_AGG(a.attended ORDER BY EXTRACT(DAY FROM a.attendance_date)) AS attended_list FROM T_ATTENDANCE a " +
                    "JOIN T_USER u ON a.user_id = u.id JOIN T_CLASS_GROUP_STUDENTS c ON a.user_id = c.students_id " +
                    "WHERE EXTRACT(YEAR FROM a.attendance_date) = :year " +
                    "AND EXTRACT(MONTH FROM a.attendance_date) = :month " +
                    "AND c.class_group_id = :classGroup " +
                    "GROUP BY u.id, u.first_name, u.last_name " +
                    "ORDER BY CONCAT(u.first_name, ' ', u.last_name);",
            nativeQuery = true
    )
    List<Object[]> findAllUserAttendanceDtoRaw(int year, int month, int classGroup);

    @Query(
            value = "SELECT DISTINCT CAST(a.attendance_date AS DATE) " +
                    "FROM T_ATTENDANCE a " +
                    "JOIN T_CLASS_GROUP_STUDENTS c ON a.user_id = c.students_id " +
                    "WHERE EXTRACT(YEAR FROM a.attendance_date) = :year " +
                    "AND EXTRACT(MONTH FROM a.attendance_date) = :month " +
                    "AND c.class_group_id = :classGroup ;",
            nativeQuery = true
    )
    List<java.sql.Date> getClassDates(int year, int month, int classGroup);

    @Query(
            value = "SELECT DISTINCT EXTRACT(YEAR FROM a.attendance_date) " +
                    "FROM T_ATTENDANCE a " +
                    "JOIN T_CLASS_GROUP_STUDENTS c ON a.user_id = c.students_id " +
                    "WHERE c.class_group_id = :classGroupId ;",
            nativeQuery = true
    )
    List<String> getYearsUsingClassGroup(int classGroupId);

    @Query(
            value = "SELECT a.id, a.attendance_date, a.attended, a.user_id FROM T_ATTENDANCE a " +
            "WHERE a.attendance_date = :date AND a.user_id = :userId ;", nativeQuery = true
    )
    Attendance findAttendance(int userId, LocalDate date);
}
