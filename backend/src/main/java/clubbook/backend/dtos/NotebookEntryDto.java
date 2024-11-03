package clubbook.backend.dtos;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object representing an entry in the notebook.
 * This entry includes exercises for warm-up, specific training, and cool down, along with the date of the entry.
 */
public class NotebookEntryDto {

    /**
     * A list of warm-up exercises.
     */
    private List<String> warmUpExercises;

    /**
     * A list of specific exercises for the training session.
     */
    private List<String> specificExercises;

    /**
     * A list of final exercises for cooling down after training.
     */
    private List<String> finalExercises;

    /**
     * The date of the notebook entry.
     */
    private LocalDate date;

    /**
     * Constructs a NotebookEntryDto with specified warm-up, specific, and final exercises, and the date.
     *
     * @param warmUpExercises a list of warm-up exercises
     * @param specificExercises a list of specific training exercises
     * @param finalExercises a list of final exercises
     * @param date the date of the entry
     */
    public NotebookEntryDto(List<String> warmUpExercises, List<String> specificExercises, List<String> finalExercises, LocalDate date) {
        this.warmUpExercises = warmUpExercises;
        this.specificExercises = specificExercises;
        this.finalExercises = finalExercises;
        this.date = date;
    }

    /**
     * Default constructor.
     */
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
