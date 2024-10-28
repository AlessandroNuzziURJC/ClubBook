package clubbook.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "T_Entry")
public class NotebookEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Convert(converter = StringListConverter.class)
    private List<String> warmUpExercises;

    @Convert(converter = StringListConverter.class)
    private List<String> specificExercises;

    @Convert(converter = StringListConverter.class)
    private List<String> finalExercises;

    @ManyToOne
    @JoinColumn(name = "notebook_id")
    @JsonIgnore
    private Notebook notebook;

    private LocalDate date;

    public NotebookEntry(int id, List<String> warmUpExercises, List<String> specificExercises, List<String> finalExercises, Notebook notebook, LocalDate date) {
        this.id = id;
        this.warmUpExercises = warmUpExercises;
        this.specificExercises = specificExercises;
        this.finalExercises = finalExercises;
        this.notebook = notebook;
        this.date = date;
    }

    public NotebookEntry() {

    }

    public NotebookEntry(String json, LocalDate date) {
        JSONObject jsonObject = new JSONObject(json);

        this.warmUpExercises = jsonArrayToList(jsonObject.getJSONArray("warmUpExercises"));
        this.specificExercises = jsonArrayToList(jsonObject.getJSONArray("specificExercises"));
        this.finalExercises = jsonArrayToList(jsonObject.getJSONArray("finalExercises"));
        this.date = date;
    }

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

    public String generateExercisesData() {
        StringBuilder sb = new StringBuilder();
        sb.append("- La clase de la fecha ").append(this.getDate().toString()).append(": {");
        sb.append("warmUpExercises: ").append(warmUpExercises).append(",");
        sb.append("specificExercises: ").append(specificExercises).append(",");
        sb.append("finalExercises: ").append(finalExercises).append("}");
        return sb.toString();
    }
}
