package clubbook.backend.service;

import clubbook.backend.dtos.RegisterClassGroupDto;
import clubbook.backend.dtos.ScheduleDto;
import clubbook.backend.model.ClassGroup;
import clubbook.backend.model.Schedule;
import clubbook.backend.model.User;
import clubbook.backend.model.WeekDayEnum;
import clubbook.backend.repository.ClassGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassGroupService {

    private final ClassGroupRepository classGroupRepository;
    private final UserService userService;

    @Autowired
    public ClassGroupService(ClassGroupRepository classGroupRepository, UserService userService) {
        this.classGroupRepository = classGroupRepository;
        this.userService = userService;
    }

    public List<ClassGroup> getAllClassGroups() {
        return classGroupRepository.findAll();
    }

    public ClassGroup create(RegisterClassGroupDto registerClassGroupDto) throws IllegalArgumentException {
        ClassGroup classGroup = new ClassGroup();
        classGroup.setName(registerClassGroupDto.getName());
        classGroup.setAddress(registerClassGroupDto.getAddress());

        List<User> teachers = new ArrayList<>(20);
        for (Integer id: registerClassGroupDto.getIdTeachers()) {
            teachers.add(userService.findById(id));
        }
        classGroup.setTeachers(teachers);

        List<Schedule> schedules = new ArrayList<>(20);
        Schedule schedule;
        for (ScheduleDto scheduleDto: registerClassGroupDto.getSchedulesDto()) {
            schedule = new Schedule();
            schedule.setWeekDay(WeekDayEnum.valueOf(scheduleDto.getWeekDay()));
            schedule.setInit(scheduleDto.getInit());
            schedule.setEnd(scheduleDto.getFinish());
            schedules.add(schedule);
        }
        classGroup.setSchedule(schedules);

        classGroupRepository.save(classGroup);

        return classGroup;
    }
}
