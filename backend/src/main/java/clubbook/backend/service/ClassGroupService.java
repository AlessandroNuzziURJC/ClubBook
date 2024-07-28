package clubbook.backend.service;

import clubbook.backend.dtos.RegisterClassGroupDto;
import clubbook.backend.dtos.ScheduleDto;
import clubbook.backend.model.ClassGroup;
import clubbook.backend.model.Schedule;
import clubbook.backend.model.User;
import clubbook.backend.model.WeekDayEnum;
import clubbook.backend.repository.ClassGroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ClassGroupService {

    private final ClassGroupRepository classGroupRepository;
    private final UserService userService;
    private final ScheduleService scheduleService;

    @Autowired
    public ClassGroupService(ClassGroupRepository classGroupRepository, UserService userService, ScheduleService scheduleService) {
        this.classGroupRepository = classGroupRepository;
        this.userService = userService;
        this.scheduleService = scheduleService;
    }

    public List<ClassGroup> getAllClassGroups() {
        return classGroupRepository.findAll();
    }

    public ClassGroup create(RegisterClassGroupDto registerClassGroupDto) throws IllegalArgumentException {
        ClassGroup classGroup = new ClassGroup();
        classGroup.setName(registerClassGroupDto.getName());
        classGroup.setAddress(registerClassGroupDto.getAddress());

        List<User> teachers = new ArrayList<>(20);
        for (Integer id : registerClassGroupDto.getTeachers()) {
            teachers.add(userService.findById(id));
        }
        classGroup.setTeachers(teachers);

        List<Schedule> schedules = new ArrayList<>(20);
        Schedule schedule;
        for (ScheduleDto scheduleDto : registerClassGroupDto.getSchedules()) {
            schedule = new Schedule();
            schedule.setWeekDay(WeekDayEnum.valueOf(scheduleDto.getWeekDay()));
            schedule.setInit(scheduleDto.getInit());
            schedule.setDuration(scheduleDto.getDuration());
            schedules.add(schedule);
        }
        classGroup.setSchedules(schedules);
        classGroup.setStudents(new ArrayList<>());

        classGroupRepository.save(classGroup);

        return classGroup;
    }

    public void delete(int id) {
        ClassGroup classGroup = classGroupRepository.getReferenceById(id);
        classGroupRepository.delete(classGroup);
    }


    public ClassGroup findById(int id) {
        return classGroupRepository.findById(id).orElseThrow();
    }

    @Transactional
    public ClassGroup update(ClassGroup classGroup, RegisterClassGroupDto classGroupDto) {
        classGroup.setName(classGroupDto.getName());
        classGroup.setAddress(classGroupDto.getAddress());

        List<User> teachers = new ArrayList<>(20);
        for (Integer id : classGroupDto.getTeachers()) {
            teachers.add(userService.findById(id));
        }
        classGroup.setTeachers(teachers);

        Set<Schedule> scheduleSet = new HashSet<>(classGroup.getSchedules());

        List<Schedule> schedules = new ArrayList<>(20);
        Schedule schedule;
        for (ScheduleDto scheduleDto : classGroupDto.getSchedules()) {
            schedule = scheduleService.findById(scheduleDto.getId());
            if (schedule == null) {
                schedule = new Schedule();
            } else {
                scheduleSet.remove(schedule);
            }
            schedule.setInit(scheduleDto.getInit());
            schedule.setDuration(scheduleDto.getDuration());
            schedule.setWeekDay(WeekDayEnum.valueOf(scheduleDto.getWeekDay()));
            schedules.add(schedule);
        }

        for (Schedule scheduleForRemove: scheduleSet) {
            scheduleService.delete(scheduleForRemove);
        }

        classGroup.setSchedules(schedules);

        classGroupRepository.save(classGroup);

        return classGroupRepository.findById(classGroup.getId()).orElseThrow();
    }

    public ClassGroup getClassGroup(int id) {
        return this.classGroupRepository.findById(id).orElseThrow();
    }

    public List<User> addNewStudentsClassGroup(int id, List<Integer> studentsIds) {
        ClassGroup classGroup = classGroupRepository.findById(id).orElseThrow();
        List<User> students = classGroup.getStudents();
        User aux;
        for (Integer studentId : studentsIds) {
            aux = userService.findById(studentId);
            if (aux != null) {
                students.add(aux);
            }
        }
        classGroup.setStudents(students);
        classGroupRepository.save(classGroup);
        return students;
    }
}
