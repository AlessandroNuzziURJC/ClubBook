package clubbook.backend.model;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

/**
 * A converter that converts a List of Strings to a JSON string for storage in the database
 * and vice versa. This is used for persisting lists in a single database column.
 */
@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converts a List of Strings into a JSON string for database storage.
     *
     * @param stringList the List of Strings to convert
     * @return a JSON string representation of the List
     * @throws RuntimeException if the conversion fails
     */
    @Override
    public String convertToDatabaseColumn(List stringList) {
        try {
            return objectMapper.writeValueAsString(stringList);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert List<String> to JSON", e);
        }
    }

    /**
     * Converts a JSON string from the database back into a List of Strings.
     *
     * @param json the JSON string to convert
     * @return the List of Strings represented by the JSON string
     * @throws RuntimeException if the conversion fails
     */
    @Override
    public List<String> convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, List.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to List<String>", e);
        }
    }
}
