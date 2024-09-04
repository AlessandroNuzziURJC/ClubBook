package clubbook.backend.dtos;

public class YearsDto {

    private int value;
    private String label;

    public YearsDto(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public YearsDto () {}

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
