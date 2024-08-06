package clubbook.backend.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "T_ClassGroup")
public class ClassGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @ManyToMany(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private List<User> teachers;

    @OneToMany(fetch = FetchType.EAGER)
    private List<User> students;

    @OneToMany(cascade = CascadeType.ALL,  fetch = FetchType.EAGER)
    @JoinColumn(name = "classGroup_fk", referencedColumnName = "id", nullable = false)
    private List<Schedule> schedules;

    public ClassGroup() {}

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
