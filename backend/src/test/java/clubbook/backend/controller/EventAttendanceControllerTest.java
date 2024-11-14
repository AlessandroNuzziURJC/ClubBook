package clubbook.backend.controller;

import clubbook.backend.dtos.UpdateEventAttendanceDto;
import clubbook.backend.model.EventAttendance;
import clubbook.backend.responses.ResponseMessages;
import clubbook.backend.responses.ResponseWrapper;
import clubbook.backend.service.EventAttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EventAttendanceControllerTest {

    @InjectMocks
    private EventAttendanceController eventAttendanceController;

    @Mock
    private EventAttendanceService eventAttendanceService;

    @BeforeEach
    void setUp() {
    }

    // Test getStudentAttendance
    @Test
    void getStudentAttendance() {
        int eventId = 1;
        List<EventAttendance> mockAttendanceList = Arrays.asList(new EventAttendance(), new EventAttendance());
        when(eventAttendanceService.getEventAttendanceStudents(eventId)).thenReturn(mockAttendanceList);

        // When
        ResponseEntity<ResponseWrapper<List<EventAttendance>>> response = eventAttendanceController.getStudentAttendance(eventId);

        // Then
        assertEquals(ResponseMessages.OK, response.getBody().getMessage());
        assertEquals(mockAttendanceList, response.getBody().getData());
        verify(eventAttendanceService, times(1)).getEventAttendanceStudents(eventId);
    }

    // Test getTeacherAttendance
    @Test
    void getTeacherAttendance() {
        // Given
        int eventId = 1;
        List<EventAttendance> mockAttendanceList = Arrays.asList(new EventAttendance(), new EventAttendance());
        when(eventAttendanceService.getEventAttendanceTeachers(eventId)).thenReturn(mockAttendanceList);

        // When
        ResponseEntity<ResponseWrapper<List<EventAttendance>>> response = eventAttendanceController.getTeacherAttendance(eventId);

        // Then
        assertEquals(ResponseMessages.OK, response.getBody().getMessage());
        assertEquals(mockAttendanceList, response.getBody().getData());
        verify(eventAttendanceService, times(1)).getEventAttendanceTeachers(eventId);
    }

    // Test getUserAttendance
    @Test
    void getUserAttendance() {
        // Given
        int eventId = 1;
        int userId = 1;
        EventAttendance mockAttendance = new EventAttendance();
        when(eventAttendanceService.getEventAttendanceByUser(eventId, userId)).thenReturn(mockAttendance);

        // When
        ResponseEntity<ResponseWrapper<EventAttendance>> response = eventAttendanceController.getUserAttendance(eventId, userId);

        // Then
        assertEquals(ResponseMessages.OK, response.getBody().getMessage());
        assertEquals(mockAttendance, response.getBody().getData());
        verify(eventAttendanceService, times(1)).getEventAttendanceByUser(eventId, userId);
    }

    // Test updateAttendance
    @Test
    void updateAttendance() {
        // Given
        UpdateEventAttendanceDto attendanceDto = new UpdateEventAttendanceDto();
        attendanceDto.setEventId(1);
        attendanceDto.setUserId(1);
        attendanceDto.setStatus(true);

        // When
        ResponseEntity<ResponseWrapper<Boolean>> response = eventAttendanceController.updateAttendance(attendanceDto);

        // Then
        assertEquals(ResponseMessages.ATTENDANCE_UPDATED, response.getBody().getMessage());
        assertEquals(true, response.getBody().getData());
        verify(eventAttendanceService, times(1)).saveEventAttendance(attendanceDto);
    }
}