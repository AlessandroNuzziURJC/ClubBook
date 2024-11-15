package clubbook.backend.dtos;

/**
 * Data Transfer Object used to update the configuration of a notebook.
 * It includes the notebook ID, sport type, and skill level.
 */
public class NotebookUpdateConfigDto {

    /**
     * The unique identifier of the notebook.
     */
    private int id;

    /**
     * The type of sport associated with the notebook.
     */
    private String sport;

    /**
     * The skill level for the sport.
     */
    private String level;

    /**
     * Constructs a NotebookUpdateConfigDto with the specified ID, sport, and level.
     *
     * @param id the unique identifier of the notebook
     * @param sport the type of sport associated with the notebook
     * @param level the skill level for the sport
     */
    public NotebookUpdateConfigDto(int id, String sport, String level) {
        this.id = id;
        this.sport = sport;
        this.level = level;
    }

    /**
     * Default constructor for creating an empty NotebookUpdateConfigDto.
     */
    public NotebookUpdateConfigDto() {

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

}
