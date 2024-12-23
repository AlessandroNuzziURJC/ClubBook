package clubbook.backend.service;

import clubbook.backend.dtos.RegisterClassGroupDto;
import clubbook.backend.dtos.ScheduleDto;
import clubbook.backend.model.ClassGroup;
import clubbook.backend.model.Schedule;
import clubbook.backend.model.User;
import clubbook.backend.model.enumClasses.WeekDayEnum;
import clubbook.backend.repository.ClassGroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class for managing class groups in the application.
 */
@Service
public class ClassGroupService {

    private final ClassGroupRepository classGroupRepository;
    private final UserService userService;
    private final ScheduleService scheduleService;

    /**
     * Constructs an instance of ClassGroupService with the specified dependencies.
     *
     * @param classGroupRepository the repository used for class group data access.
     * @param userService the service responsible for user management.
     * @param scheduleService the service responsible for schedule management.
     */
    @Autowired
    public ClassGroupService(ClassGroupRepository classGroupRepository, UserService userService, ScheduleService scheduleService) {
        this.classGroupRepository = classGroupRepository;
        this.userService = userService;
        this.scheduleService = scheduleService;
    }

    /**
     * Retrieves all class groups from the repository.
     *
     * @return a list of all class groups.
     */
    public List<ClassGroup> getAllClassGroups() {
        return classGroupRepository.findAll();
    }

    /**
     * Creates a new class group based on the provided DTO.
     *
     * @param registerClassGroupDto the data transfer object containing class group information.
     * @return the created class group.
     * @throws IllegalArgumentException if the input data is invalid.
     */
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

        this.classGroupRepository.save(classGroup);

        return classGroup;
    }

    /**
     * Deletes a class group by its ID.
     *
     * @param id the ID of the class group to be deleted.
     */
    public void delete(int id) {
        ClassGroup classGroup = classGroupRepository.getReferenceById(id);
        classGroupRepository.delete(classGroup);
    }

    /**
     * Finds a class group by its ID.
     *
     * @param id the ID of the class group to be found.
     * @return the found class group.
     */
    public ClassGroup findById(int id) {
        return classGroupRepository.findById(id).orElseThrow();
    }

    /**
     * Updates an existing class group with new information.
     *
     * @param classGroup the class group to be updated.
     * @param classGroupDto the data transfer object containing updated class group information.
     * @return the updated class group.
     */
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

    /**
     * Retrieves a class group by its ID.
     *
     * @param id the ID of the class group to be retrieved.
     * @return the retrieved class group.
     */
    public ClassGroup getClassGroup(int id) {
        return this.classGroupRepository.findById(id).orElseThrow();
    }

    /**
     * Adds new students to a class group.
     *
     * @param id the ID of the class group.
     * @param studentsIds the IDs of the students to be added.
     * @return the list of students in the class group after addition.
     */
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

    /**
     * Removes students from a class group.
     *
     * @param id the ID of the class group.
     * @param studentsIds the IDs of the students to be removed.
     * @return the list of students in the class group after removal.
     * @throws NoSuchElementException if one or more specified students are not found.
     */
    public List<User> removeStudentsClassGroup(int id, List<Integer> studentsIds) {

        ClassGroup classGroup = classGroupRepository.findById(id).orElseThrow();
        List<User> students = classGroup.getStudents();

        if (studentsIds.isEmpty()) {
            return students;
        }

        Set<Integer> studentsIdsSet = new HashSet<>(studentsIds);
        Set<User> usersRemove = new HashSet<>();

        for (User user : students) {
            if (studentsIdsSet.contains(user.getId())) {
                studentsIdsSet.remove(user.getId());
                usersRemove.add(user);
            }
        }

        if (!studentsIdsSet.isEmpty()) {
            throw new NoSuchElementException();
        }

        students.removeAll(usersRemove);
        this.classGroupRepository.save(classGroup);
        return students;
    }

}
