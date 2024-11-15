package clubbook.backend.repository;

import clubbook.backend.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing Attendance entities.
 * This interface extends JpaRepository to provide CRUD operations
 * and custom query methods for attendance-related data.
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    /**
     * Retrieves a list of user attendance records for a specific year and month
     * for a given class group. The result includes user ID, first name, last name,
     * and an aggregated list of attendance records for each user.
     *
     * @param year      the year of attendance records to retrieve
     * @param month     the month of attendance records to retrieve
     * @param classGroup the ID of the class group to filter the attendance records
     * @return a list of objects containing user ID, first name, last name,
     *         and an array of attendance records
     */
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

    /**
     * Retrieves distinct attendance dates for a given class group
     * in a specified month.
     *
     * @param month      the month of attendance records to retrieve
     * @param classGroup the ID of the class group to filter the attendance records
     * @return a list of distinct dates when attendance was recorded
     */
    @Query(
            value = "SELECT DISTINCT CAST(a.attendance_date AS DATE) " +
                    "FROM T_ATTENDANCE a " +
                    "JOIN T_CLASS_GROUP_STUDENTS c ON a.user_id = c.students_id " +
                    "WHERE EXTRACT(MONTH FROM a.attendance_date) = :month " +
                    "AND c.class_group_id = :classGroup ;",
            nativeQuery = true
    )
    List<java.sql.Date> getClassDates(int month, int classGroup);

    /**
     * Retrieves distinct years of attendance records for a specific class group.
     *
     * @param classGroupId the ID of the class group to filter the attendance records
     * @return a list of distinct years in which attendance records exist for the class group
     */
    @Query(
            value = "SELECT DISTINCT EXTRACT(YEAR FROM a.attendance_date) " +
                    "FROM T_ATTENDANCE a " +
                    "JOIN T_CLASS_GROUP_STUDENTS c ON a.user_id = c.students_id " +
                    "WHERE c.class_group_id = :classGroupId ;",
            nativeQuery = true
    )
    List<String> getYearsUsingClassGroup(int classGroupId);

    /**
     * Finds the attendance record for a specific user on a given date.
     *
     * @param userId the ID of the user whose attendance record is to be retrieved
     * @param date   the date of the attendance record to retrieve
     * @return the attendance record for the specified user and date, or null if not found
     */
    @Query(
            value = "SELECT a.id, a.attendance_date, a.attended, a.user_id FROM T_ATTENDANCE a " +
            "WHERE a.attendance_date = :date AND a.user_id = :userId ;", nativeQuery = true
    )
    Attendance findAttendance(int userId, LocalDate date);
}
