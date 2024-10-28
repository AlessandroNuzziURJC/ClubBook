package clubbook.backend.dtos;

import clubbook.backend.model.Notebook;
import clubbook.backend.model.StringListConverter;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

public class NotebookEntryDto {

    private List<String> warmUpExercises;

    private List<String> specificExercises;

    private List<String> finalExercises;

    private LocalDate date;

    public NotebookEntryDto(List<String> warmUpExercises, List<String> specificExercises, List<String> finalExercises, LocalDate date) {
        this.warmUpExercises = warmUpExercises;
        this.specificExercises = specificExercises;
        this.finalExercises = finalExercises;
        this.date = date;
    }

    public NotebookEntryDto() {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
