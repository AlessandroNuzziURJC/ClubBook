package clubbook.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an entry in a notebook containing exercises for a specific date.
 */
@Entity
@Table(name = "T_Entry")
public class NotebookEntry {

    /**
     * The unique identifier of the notebook entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The list of warm-up exercises.
     */
    @Convert(converter = StringListConverter.class)
    private List<String> warmUpExercises;

    /**
     * The list of specific exercises.
     */
    @Convert(converter = StringListConverter.class)
    private List<String> specificExercises;

    /**
     * The list of final exercises.
     */
    @Convert(converter = StringListConverter.class)
    private List<String> finalExercises;

    /**
     * The associated notebook for this entry.
     */
    @ManyToOne
    @JoinColumn(name = "notebook_id")
    @JsonIgnore
    private Notebook notebook;

    /**
     * The date of the notebook entry.
     */
    private LocalDate date;

    /**
     * Constructs a new NotebookEntry with specified parameters.
     *
     * @param id               the unique identifier of the notebook entry
     * @param warmUpExercises  the list of warm-up exercises
     * @param specificExercises the list of specific exercises
     * @param finalExercises   the list of final exercises
     * @param notebook         the associated notebook for this entry
     * @param date             the date of the notebook entry
     */
    public NotebookEntry(int id, List<String> warmUpExercises, List<String> specificExercises, List<String> finalExercises, Notebook notebook, LocalDate date) {
        this.id = id;
        this.warmUpExercises = warmUpExercises;
        this.specificExercises = specificExercises;
        this.finalExercises = finalExercises;
        this.notebook = notebook;
        this.date = date;
    }

    /**
     * Default constructor for NotebookEntry.
     */
    public NotebookEntry() {

    }

    /**
     * Constructs a new NotebookEntry from a JSON string.
     *
     * @param json the JSON string representing the notebook entry
     * @param date the date of the notebook entry
     */
    public NotebookEntry(String json, LocalDate date) {
        JSONObject jsonObject = new JSONObject(json);

        this.warmUpExercises = jsonArrayToList(jsonObject.getJSONArray("warmUpExercises"));
        this.specificExercises = jsonArrayToList(jsonObject.getJSONArray("specificExercises"));
        this.finalExercises = jsonArrayToList(jsonObject.getJSONArray("finalExercises"));
        this.date = date;
    }

    /**
     * Converts a JSONArray to a List of Strings.
     *
     * @param jsonArray the JSONArray to convert
     * @return a List of Strings containing the elements of the JSONArray
     */
    private List<String> jsonArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getWarmUpExercises() {
        return warmUpExercises;
    }

    public void setWarmUpExercises(List<String> warmUpExercises) {
        this.warmUpExercises = warmUpExercises;
    }

    public List<String> getSpecificExercises() {
        return specificExercises;
    }

    public void setSpecificExercises(List<String> specificExercises) {
        this.specificExercises = specificExercises;
    }

    public List<String> getFinalExercises() {
        return finalExercises;
    }

    public void setFinalExercises(List<String> finalExercises) {
        this.finalExercises = finalExercises;
    }

    public Notebook getNotebook() {
        return notebook;
    }

    public void setNotebook(Notebook notebook) {
        this.notebook = notebook;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Generates a string representation of the exercises data for this entry.
     *
     * @return a string representation of the exercises data
     */
    public String generateExercisesData() {
        StringBuilder sb = new StringBuilder();
        sb.append("- La clase de la fecha ").append(this.getDate().toString()).append(": {");
        sb.append("warmUpExercises: ").append(warmUpExercises).append(",");
        sb.append("specificExercises: ").append(specificExercises).append(",");
        sb.append("finalExercises: ").append(finalExercises).append("}");
        return sb.toString();
    }
}
