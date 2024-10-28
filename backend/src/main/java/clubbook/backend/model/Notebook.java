package clubbook.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "T_Notebook")
public class Notebook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String sport;

    private String level;

    private String duration;

    @Column(nullable = false)
    private String ageRange;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "class_group_id", referencedColumnName = "id")
    private ClassGroup classgroup;

    @OneToMany(mappedBy = "notebook", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("date DESC")
    @JsonIgnore
    private List<NotebookEntry> entries;

    public Notebook(int id, String sport, String level, String duration, String ageRange, ClassGroup classgroup, List<NotebookEntry> entries) {
        this.id = id;
        this.sport = sport;
        this.level = level;
        this.duration = duration;
        this.ageRange = ageRange;
        this.classgroup = classgroup;
        this.entries = entries;
    }

    public Notebook() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public ClassGroup getClassgroup() {
        return classgroup;
    }

    public void setClassgroup(ClassGroup classgroup) {
        this.classgroup = classgroup;
    }

    public List<NotebookEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<NotebookEntry> entries) {
        this.entries = entries;
    }
}
