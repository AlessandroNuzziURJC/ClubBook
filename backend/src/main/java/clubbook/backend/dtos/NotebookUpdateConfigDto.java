package clubbook.backend.dtos;

public class NotebookUpdateConfigDto {

    private int id;
    private String sport, level;

    public NotebookUpdateConfigDto(int id, String sport, String level) {
        this.id = id;
        this.sport = sport;
        this.level = level;
    }

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
