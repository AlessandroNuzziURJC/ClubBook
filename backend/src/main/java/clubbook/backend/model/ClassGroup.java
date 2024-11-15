package clubbook.backend.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * Class representing a class group, which contains information about the group,
 * its teachers, students, and schedules.
 */
@Entity
@Table(name = "T_ClassGroup")
public class ClassGroup {

    /**
     * Unique identifier for class group.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Name for class group. Can't be null.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Address of class group. Can't be null.
     */
    @Column(nullable = false)
    private String address;

    /**
     * Teachers that teach in class group. Can't be null.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @Column(nullable = false)
    @OrderBy("firstName ASC, lastName ASC")
    private List<User> teachers;

    /**
     * Students that attend to a class group.
     */
    @OneToMany(fetch = FetchType.EAGER)
    @OrderBy("firstName ASC, lastName ASC")
    private List<User> students;

    /**
     * List of schedules of a class group.
     */
    @OneToMany(cascade = CascadeType.ALL,  fetch = FetchType.EAGER)
    @JoinColumn(name = "classGroup_fk", referencedColumnName = "id", nullable = false)
    private List<Schedule> schedules;

    /**
     * Default constructor for ClassGroup.
     */
    public ClassGroup() {}

    /**
     * Constructor for ClassGroup with specified parameters.
     *
     * @param name     the name of the class group
     * @param address  the address of the class group
     * @param teachers the list of teachers in the class group
     * @param students the list of students in the class group
     * @param schedules the list of schedules for the class group
     */
    public ClassGroup(String name, String address, List<User> teachers, List<User> students, List<Schedule> schedules) {
        this.name = name;
        this.address = address;
        this.teachers = teachers;
        this.students = students;
        this.schedules = schedules;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<User> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<User> teachers) {
        this.teachers = teachers;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedule) {
        this.schedules = schedule;
    }
}
