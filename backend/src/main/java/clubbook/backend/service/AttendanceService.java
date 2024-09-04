package clubbook.backend.service;

import clubbook.backend.dtos.AttendanceDto;
import clubbook.backend.dtos.ClassGroupAttendanceDto;
import clubbook.backend.dtos.UserAttendanceDto;
import clubbook.backend.dtos.YearsDto;
import clubbook.backend.model.Attendance;
import clubbook.backend.model.ClassGroup;
import clubbook.backend.model.User;
import clubbook.backend.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserService userService;
    private final ClassGroupService classGroupService;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository, UserService userService, ClassGroupService classGroupService) {
        this.attendanceRepository = attendanceRepository;
        this.userService = userService;
        this.classGroupService = classGroupService;
    }

    public AttendanceDto saveAll(AttendanceDto attendanceDto) {
        User user;
        Attendance attendance;
        List<Attendance> attendanceList = new ArrayList<>(30);
        ClassGroup classGroup = classGroupService.findById(attendanceDto.getClassGroup());
        Set<User> userSet = new HashSet<>(classGroup.getStudents());
        for (int userId: attendanceDto.getUsersIdsAttended()) {
            user = userService.findById(userId);
            userSet.remove(user);
            attendance = new Attendance();
            attendance.setUser(user);
            attendance.setAttendanceDate(attendanceDto.getDate());
            attendance.setAttended(true);
            attendanceList.add(attendance);
        }
        for (int userId: attendanceDto.getUsersIdsNotAttended()) {
            user = userService.findById(userId);
            userSet.remove(user);
            attendance = new Attendance();
            attendance.setUser(user);
            attendance.setAttendanceDate(attendanceDto.getDate());
            attendance.setAttended(false);
            attendanceList.add(attendance);
        }
        if (!userSet.isEmpty()) {
            throw new RuntimeException();
        }
        attendanceRepository.saveAll(attendanceList);
        return attendanceDto;
    }

    public Attendance saveAttendance(Attendance attendance) {
        this.attendanceRepository.save(attendance);
        return attendance;
    }

    private List<UserAttendanceDto> getUserAttendanceDtos(int year, int month, int classGroup) {
        List<Object[]> results = attendanceRepository.findAllUserAttendanceDtoRaw(year, month, classGroup);
        return results.stream().map(result ->
                new UserAttendanceDto(
                        (Integer) result[0],
                        (String) result[1],
                        (String) result[2],
                        Arrays.asList((Boolean[]) result[3])
                )
        ).collect(Collectors.toList());
    }

    private List<LocalDate> getClassDates(int year, int month, int classGroup) {
        List<java.sql.Date> sqlDates = attendanceRepository.getClassDates(year, month, classGroup);
        return sqlDates.stream()
                .map(java.sql.Date::toLocalDate)
                .collect(Collectors.toList());
    }

    private Boolean getUserAttendance(int userId, LocalDate date) {
        Attendance attendance = this.attendanceRepository.findAttendance(userId, date);
        if (attendance == null) {
            return null;
        }
        return attendance.isAttended();
    }

    public ClassGroupAttendanceDto getClassGroupAttendanceWithYearAndMonth(int year, int month, int classGroupId) {
        List<LocalDate> dates = this.getClassDates(year, month, classGroupId);
        ClassGroup classGroup = classGroupService.findById(classGroupId);
        List<User> userList = classGroup.getStudents();
        List<UserAttendanceDto> userAttendanceDtoList = new ArrayList<>(userList.size());
        for (User user : userList){
            userAttendanceDtoList.add(new UserAttendanceDto(user.getId(), user.getFirstName(), user.getLastName(), new ArrayList<>()));
        }
        for (LocalDate date : dates) {
            for (UserAttendanceDto userAttendanceDto : userAttendanceDtoList){
                userAttendanceDto.getAttendanceList().add(this.getUserAttendance(userAttendanceDto.getId(), date));
            }
        }

        return new ClassGroupAttendanceDto(dates, userAttendanceDtoList);
    }

    public List<YearsDto> getYears(int classGroupId) {
        List<String> yearsList = this.attendanceRepository.getYearsUsingClassGroup(classGroupId);
        List<YearsDto> yearsDtoList = new ArrayList<>(20);
        for (String year: yearsList) {
            yearsDtoList.add(new YearsDto(Integer.parseInt(year), year));
        }
        return yearsDtoList;
    }
}
