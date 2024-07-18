package clubbook.backend.model;

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

    @ManyToMany
    @Column(nullable = false)
    private List<User> teachers;

    @OneToMany
    private List<User> students;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "classGroup_fk", referencedColumnName = "id", nullable = false)
    private List<Schedule> schedule;

    public ClassGroup() {}

    public ClassGroup(String name, String address, List<User> teachers, List<User> students, List<Schedule> schedule) {
        this.name = name;
        this.address = address;
        this.teachers = teachers;
        this.students = students;
        this.schedule = schedule;
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

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }
}
