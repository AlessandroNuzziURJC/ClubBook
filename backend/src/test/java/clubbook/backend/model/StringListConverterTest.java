package clubbook.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringListConverterTest {

    private StringListConverter converter;

    @BeforeEach
    void setUp() {
        converter = new StringListConverter();
    }

    @Test
    void convertToDatabaseColumn() {
        List<String> stringList = Arrays.asList("item1", "item2", "item3");

        String json = converter.convertToDatabaseColumn(stringList);

        assertNotNull(json, "The JSON string should not be null");
        assertTrue(json.contains("item1") && json.contains("item2") && json.contains("item3"),
                "The JSON string should contain all list elements");
    }

    @Test
    void convertToDatabaseColumn_withNull() {
        String json = converter.convertToDatabaseColumn(null);

        assertEquals("null", json, "The JSON string should be 'null' when input list is null");
    }

    @Test
    void convertToEntityAttribute() {
        String json = "[\"item1\",\"item2\",\"item3\"]";

        List<String> stringList = converter.convertToEntityAttribute(json);

        assertNotNull(stringList, "The list should not be null");
        assertEquals(3, stringList.size(), "The list should contain 3 items");
        assertEquals("item1", stringList.get(0), "First item should be 'item1'");
        assertEquals("item2", stringList.get(1), "Second item should be 'item2'");
        assertEquals("item3", stringList.get(2), "Third item should be 'item3'");
    }

    @Test
    void convertToEntityAttribute_withInvalidJson() {
        String invalidJson = "[item1, item2, item3";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            converter.convertToEntityAttribute(invalidJson);
        });

        assertTrue(exception.getMessage().contains("Failed to convert JSON to List<String>"),
                "Exception message should indicate JSON conversion failure");
    }
}
