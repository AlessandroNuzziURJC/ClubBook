package clubbook.backend.controller;

import clubbook.backend.dtos.NewEventDto;
import clubbook.backend.model.Event;
import clubbook.backend.model.EventType;
import clubbook.backend.model.enumClasses.EventTypeEnum;
import clubbook.backend.repository.EventRepository;
import clubbook.backend.repository.EventTypeRepository;
import clubbook.backend.responses.ResponseWrapper;
import clubbook.backend.service.EventAttendanceService;
import clubbook.backend.service.EventService;
import clubbook.backend.service.SeasonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class EventControllerTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventTypeRepository eventTypeRepository;

    @Mock
    private EventAttendanceService eventAttendanceService;

    @Mock
    private SeasonService seasonService;

    @InjectMocks
    private EventService eventService;

    private EventController eventController;

    private Map<EventTypeEnum, EventType> eventTypeMap;

    private List<Event> eventList;

    private NewEventDto eventDto;

    private void createEventType(EventTypeEnum enumValue, int id, Date date) {
        EventType eventType = new EventType();
        eventType.setName(enumValue);
        eventType.setEventTypeId(id);
        eventType.setCreatedAt(date);
        this.eventTypeMap.put(enumValue, eventType);
    }

    @BeforeEach
    void setUp() {
        this.eventTypeMap = new HashMap<>(10);

        this.createEventType(EventTypeEnum.COMPETITION, 1, new Date());
        this.createEventType(EventTypeEnum.EXHIBITION, 2, new Date());
        this.createEventType(EventTypeEnum.TRAINING, 3, new Date());

        this.eventList = new ArrayList<>(10);

        Event event1 = new Event();
        event1.setId(1);
        event1.setTitle("Title 1");
        event1.setAddress("Address 1");
        event1.setDate(LocalDate.of(2024, 11, 3));
        event1.setAdditionalInfo("AdditionalInfo 1");
        event1.setBirthYearStart(LocalDate.of(2012, 1, 1));
        event1.setBirthYearEnd(LocalDate.of(2019, 12, 31));
        event1.setType(this.eventTypeMap.get(EventTypeEnum.COMPETITION));

        this.eventList.add(event1);

        Event event2 = new Event();
        event2.setId(2);
        event2.setTitle("Title 2");
        event2.setAddress("Address 2");
        event2.setDate(LocalDate.of(2024, 10, 13));
        event2.setAdditionalInfo("AdditionalInfo 2");
        event2.setBirthYearStart(LocalDate.of(2010, 1, 1));
        event2.setBirthYearEnd(LocalDate.of(2018, 12, 31));
        event2.setType(this.eventTypeMap.get(EventTypeEnum.EXHIBITION));

        this.eventList.add(event2);

        Event event3 = new Event();
        event3.setId(1);
        event3.setTitle("Title 3");
        event3.setAddress("Address 3");
        event3.setDate(LocalDate.of(2024, 10, 3));
        event3.setAdditionalInfo("AdditionalInfo 3");
        event3.setBirthYearStart(LocalDate.of(2016, 1, 1));
        event3.setBirthYearEnd(LocalDate.of(2022, 12, 31));
        event3.setType(this.eventTypeMap.get(EventTypeEnum.TRAINING));

        this.eventList.add(event3);

        this.eventDto = new NewEventDto("Title Event", "Address Event", 1,
                LocalDate.of(2024,12,3), "Additional Info 1", LocalDate.of(2010, 1, 1), LocalDate.of(2018,12,31));

        this.eventController = new EventController(this.eventService, this.seasonService);
    }

    @Test
    void getEventTypesTest() {
        List<EventType> values = new ArrayList<>(this.eventTypeMap.values());
        when(this.eventTypeRepository.findAll()).thenReturn(values);
        ResponseEntity<List<EventType>> response = this.eventController.getEventTypes();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(values, response.getBody());
    }

    @Test
    void saveNewEvent() {
        when(this.eventRepository.save(any(Event.class))).thenReturn(null);
        when(this.eventTypeRepository.findById(any(Integer.class))).thenReturn(Optional.of(this.eventTypeMap.get(EventTypeEnum.COMPETITION)));
        ResponseEntity<ResponseWrapper<Boolean>> response = this.eventController.saveNewEvent(this.eventDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getData());
    }

}