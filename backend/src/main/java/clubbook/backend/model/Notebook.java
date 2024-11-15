package clubbook.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

/**
 * Represents a notebook for recording entries related to a specific sport.
 */
@Entity
@Table(name = "T_Notebook")
public class Notebook {

    /**
     * The unique identifier of the notebook.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The sport associated with the notebook.
     */
    private String sport;

    /**
     * The skill level of the sport.
     */
    private String level;

    /**
     * The duration of the activities recorded in the notebook.
     */
    private String duration;

    /**
     * The age range of the participants for whom the notebook is intended. Can't be null.
     */
    @Column(nullable = false)
    private String ageRange;

    /**
     * The associated class group for this notebook.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "class_group_id", referencedColumnName = "id")
    private ClassGroup classgroup;

    /**
     * The list of entries recorded in the notebook.
     */
    @OneToMany(mappedBy = "notebook", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("date DESC")
    @JsonIgnore
    private List<NotebookEntry> entries;

    /**
     * Constructs a new Notebook with specified parameters.
     *
     * @param id        the unique identifier of the notebook
     * @param sport     the sport associated with the notebook
     * @param level     the skill level of the sport
     * @param duration  the duration of the activities recorded in the notebook
     * @param ageRange  the age range of the participants for whom the notebook is intended
     * @param classgroup the associated class group for this notebook
     * @param entries   the list of entries recorded in the notebook
     */
    public Notebook(int id, String sport, String level, String duration, String ageRange, ClassGroup classgroup, List<NotebookEntry> entries) {
        this.id = id;
        this.sport = sport;
        this.level = level;
        this.duration = duration;
        this.ageRange = ageRange;
        this.classgroup = classgroup;
        this.entries = entries;
    }

    /**
     * Default constructor for Notebook.
     */
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
