package clubbook.backend.controller;

import clubbook.backend.dtos.NotebookEntryDto;
import clubbook.backend.dtos.NotebookPrincipalInfoDto;
import clubbook.backend.dtos.NotebookUpdateConfigDto;
import clubbook.backend.model.ClassGroup;
import clubbook.backend.model.Notebook;
import clubbook.backend.model.NotebookEntry;
import clubbook.backend.responses.ResponseMessages;
import clubbook.backend.responses.ResponseWrapper;
import clubbook.backend.service.NotebookService;
import clubbook.backend.service.SeasonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class NotebookControllerTest {

    @Mock
    private SeasonService seasonService;

    @Mock
    private NotebookService notebookService;

    @InjectMocks
    private NotebookController notebookController;

    private List<Notebook> notebooks;

    @BeforeEach
    void setUp() {
        notebooks = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            Notebook notebook = new Notebook();
            notebook.setId(i);
            notebook.setLevel("Principiante");
            notebook.setDuration("60");
            notebook.setSport("Boxeo");
            notebook.setEntries(null);
            notebook.setAgeRange("2000 al 2010");
            ClassGroup classGroup = new ClassGroup();
            classGroup.setId(i);
            notebook.setClassgroup(classGroup);
            notebooks.add(notebook);
        }
    }

    @Test
    void getAllNotebooks() {
        when(seasonService.seasonStarted()).thenReturn(true);
        when(this.notebookService.getAllNotebooks()).thenReturn(notebooks);

        ResponseEntity<ResponseWrapper<List<NotebookPrincipalInfoDto>>> allClassGroups = this.notebookController.getAllClassGroups();
        assertEquals(HttpStatus.OK, allClassGroups.getStatusCode());
        ResponseWrapper<List<NotebookPrincipalInfoDto>> response = allClassGroups.getBody();
        assertNotNull(response);
        assertEquals(ResponseMessages.OK, response.getMessage());
        assertNotNull(response.getData());
    }

    @Test
    void getNotebookById() {
        when(seasonService.seasonStarted()).thenReturn(true);
        when(this.notebookService.getNotebook(0)).thenReturn(notebooks.get(0));

        ResponseEntity<ResponseWrapper<Notebook>> notebookById = this.notebookController.getNotebookById(0);
        assertEquals(HttpStatus.OK, notebookById.getStatusCode());
        ResponseWrapper<Notebook> response = notebookById.getBody();
        assertNotNull(response);
        assertEquals(ResponseMessages.OK, response.getMessage());
        assertEquals(notebooks.get(0), response.getData());
    }

    @Test
    void getNotebookEntryTodayById() {
        int id = 1;
        NotebookEntry notebookEntry = new NotebookEntry();
        notebookEntry.setId(id);
        notebookEntry.setDate(LocalDate.now());
        when(seasonService.seasonStarted()).thenReturn(true);
        when(this.notebookService.getNotebookEntryToday(id)).thenReturn(notebookEntry);

        ResponseEntity<ResponseWrapper<NotebookEntry>> notebookEntryTodayById = this.notebookController.getNotebookEntryTodayById(id);
        assertEquals(HttpStatus.OK, notebookEntryTodayById.getStatusCode());
        ResponseWrapper<NotebookEntry> response = notebookEntryTodayById.getBody();
        assertNotNull(response);
        assertEquals(ResponseMessages.OK, response.getMessage());
        assertEquals(notebookEntry, response.getData());
    }

    @Test
    void updateNotebookConfig() {
        NotebookUpdateConfigDto notebookUpdateConfigDto = new NotebookUpdateConfigDto();
        notebookUpdateConfigDto.setLevel("Principiante");
        notebookUpdateConfigDto.setSport("Boxeo");
        when(seasonService.seasonStarted()).thenReturn(true);
        when( this.notebookService.updateNotebookConfiguration(notebookUpdateConfigDto)).thenReturn(true);

        ResponseEntity<ResponseWrapper<Boolean>> responseWrapperResponseEntity = this.notebookController.updateNotebookConfig(notebookUpdateConfigDto);
        assertEquals(HttpStatus.OK, responseWrapperResponseEntity.getStatusCode());
        ResponseWrapper<Boolean> response = responseWrapperResponseEntity.getBody();
        assertNotNull(response);
        assertEquals(ResponseMessages.OK, response.getMessage());
        assertEquals(true, response.getData());
    }

    @Test
    void addNotebookEntry() {
        NotebookEntryDto notebookEntryDto = new NotebookEntryDto();
        notebookEntryDto.setDate(LocalDate.now().plusDays(10));
        ArrayList<String> warmUpExercise = new ArrayList<>(Arrays.asList("Warm up exercise", "Wam up exercise 2"));
        notebookEntryDto.setWarmUpExercises(warmUpExercise);
        ArrayList<String> specificExercise = new ArrayList<>(Arrays.asList("Specific exercise", "Specific exercise 2"));
        notebookEntryDto.setSpecificExercises(specificExercise);
        ArrayList<String> finalExercise = new ArrayList<>(Arrays.asList("Final exercise", "Final exercise 2"));
        notebookEntryDto.setFinalExercises(finalExercise);

        NotebookEntry notebookEntry = new NotebookEntry();
        notebookEntry.setId(0);
        notebookEntry.setNotebook(notebooks.get(0));
        notebookEntry.setDate(LocalDate.now().plusDays(10));
        notebookEntry.setWarmUpExercises(warmUpExercise);
        notebookEntry.setSpecificExercises(specificExercise);
        notebookEntry.setFinalExercises(finalExercise);

        when(seasonService.seasonStarted()).thenReturn(true);
        when(this.notebookService.addNotebookEntry(notebookEntryDto, 0)).thenReturn(notebookEntry);

        ResponseEntity<ResponseWrapper<NotebookEntry>> responseWrapperResponseEntity = this.notebookController.addNotebookEntry(notebookEntryDto, 0);
        assertEquals(HttpStatus.OK, responseWrapperResponseEntity.getStatusCode());
        ResponseWrapper<NotebookEntry> response = responseWrapperResponseEntity.getBody();
        assertNotNull(response);
        assertEquals(ResponseMessages.OK, response.getMessage());
        assertEquals(notebookEntry, response.getData());
    }

    @Test
    void getEntriesPaged() {
        int id = 1;
        int pageNumber = 0;
        int pageSize = 10;
        Page<NotebookEntry> entriesPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(pageNumber, pageSize), 0);

        when(seasonService.seasonStarted()).thenReturn(true);
        when(notebookService.getEntries(id, pageNumber, pageSize)).thenReturn(entriesPage);

        ResponseEntity<ResponseWrapper<Page<NotebookEntry>>> response = this.notebookController.getEntriesPaged(id, pageNumber, pageSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseMessages.OK, response.getBody().getMessage());
        assertEquals(entriesPage, response.getBody().getData());
    }

    @Test
    void deleteNotebookEntry() {
        Integer entryId = 1;
        when(notebookService.deleteEntry(entryId)).thenReturn(true);

        ResponseEntity<ResponseWrapper<Boolean>> response = this.notebookController.deleteNotebookEntry(entryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseMessages.OK, response.getBody().getMessage());
        assertTrue(response.getBody().getData());
    }

    @Test
    void editNotebookEntry() {
        NotebookEntry notebookEntry = new NotebookEntry();
        notebookEntry.setId(1);
        notebookEntry.setNotebook(notebooks.get(0));
        notebookEntry.setDate(LocalDate.now().plusDays(10));
        notebookEntry.setWarmUpExercises(new ArrayList<>(Arrays.asList("Warm up exercise", "Wam up exercise2")));
        notebookEntry.setSpecificExercises(new ArrayList<>(Arrays.asList("Specific exercise", "Specific exercise2")));
        notebookEntry.setFinalExercises(new ArrayList<>(Arrays.asList("Final exercise", "Final exercise2")));

        when(seasonService.seasonStarted()).thenReturn(true);
        when(notebookService.editNotebookEntry(notebookEntry)).thenReturn(notebookEntry);

        ResponseEntity<ResponseWrapper<NotebookEntry>> response = this.notebookController.editNotebookEntry(notebookEntry);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseMessages.OK, response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        assertEquals(notebookEntry.getId(), response.getBody().getData().getId());
        assertEquals(notebookEntry, response.getBody().getData());

    }

    @Test
    public void getInvalidDates() {
        int notebookId = 1;
        Set<LocalDate> expectedDates = new HashSet<>();
        expectedDates.add(LocalDate.of(2024, 10, 1));
        expectedDates.add(LocalDate.of(2024, 10, 2));
        when(seasonService.seasonStarted()).thenReturn(true);
        when(notebookService.getAllDates(notebookId)).thenReturn(expectedDates);

        ResponseEntity<Set<LocalDate>> invalidDates = this.notebookController.getInvalidDates(notebookId);

        assertEquals(HttpStatus.OK, invalidDates.getStatusCode());
        assertEquals(invalidDates.getBody(), expectedDates);
    }

    @Test
    void generateEntry() throws Exception {
        Integer notebookId = 1;
        LocalDate date = LocalDate.now();
        NotebookEntry notebookEntry = new NotebookEntry();
        notebookEntry.setId(1);
        notebookEntry.setDate(date);
        notebookEntry.setWarmUpExercises(new ArrayList<>(Arrays.asList("Warm up exercise", "Wam up exercise2")));
        notebookEntry.setSpecificExercises(new ArrayList<>(Arrays.asList("Specific exercise", "Specific exercise2")));
        notebookEntry.setFinalExercises(new ArrayList<>(Arrays.asList("Final exercise", "Final exercise2")));

        when(seasonService.seasonStarted()).thenReturn(true);
        when(notebookService.generateEntry(notebookId, date)).thenReturn(notebookEntry);

        // Act
        ResponseEntity<ResponseWrapper<NotebookEntry>> response = this.notebookController.generateEntry(notebookId, date);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseMessages.OK, response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        assertEquals(notebookEntry.getId(), response.getBody().getData().getId());
        assertEquals(date, response.getBody().getData().getDate());
    }

    @Test
    void testSeasonNotStarted() throws Exception {
        when(this.seasonService.seasonStarted()).thenReturn(false);

        ResponseEntity<ResponseWrapper<List<NotebookPrincipalInfoDto>>> allClassGroups = this.notebookController.getAllClassGroups();
        assertEquals(HttpStatus.BAD_REQUEST, allClassGroups.getStatusCode());
        assertEquals(ResponseMessages.SEASON_NOT_STARTED, allClassGroups.getBody().getMessage());

        ResponseEntity<ResponseWrapper<Notebook>> notebookById = this.notebookController.getNotebookById(0);
        assertEquals(HttpStatus.BAD_REQUEST, notebookById.getStatusCode());
        assertEquals(ResponseMessages.SEASON_NOT_STARTED, notebookById.getBody().getMessage());

        ResponseEntity<ResponseWrapper<NotebookEntry>> notebookEntryTodayById = this.notebookController.getNotebookEntryTodayById(1);
        assertEquals(HttpStatus.BAD_REQUEST, notebookEntryTodayById.getStatusCode());
        assertEquals(ResponseMessages.SEASON_NOT_STARTED, notebookById.getBody().getMessage());

        NotebookUpdateConfigDto notebookUpdateConfigDto = new NotebookUpdateConfigDto();
        notebookUpdateConfigDto.setLevel("Principiante");
        notebookUpdateConfigDto.setSport("Boxeo");
        ResponseEntity<ResponseWrapper<Boolean>> responseWrapperResponseEntity = this.notebookController.updateNotebookConfig(notebookUpdateConfigDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseWrapperResponseEntity.getStatusCode());
        assertEquals(ResponseMessages.SEASON_NOT_STARTED, responseWrapperResponseEntity.getBody().getMessage());

        NotebookEntryDto notebookEntryDto = new NotebookEntryDto();
        notebookEntryDto.setDate(LocalDate.now().plusDays(10));
        ArrayList<String> warmUpExercise = new ArrayList<>(Arrays.asList("Warm up exercise", "Wam up exercise 2"));
        notebookEntryDto.setWarmUpExercises(warmUpExercise);
        ArrayList<String> specificExercise = new ArrayList<>(Arrays.asList("Specific exercise", "Specific exercise 2"));
        notebookEntryDto.setSpecificExercises(specificExercise);
        ArrayList<String> finalExercise = new ArrayList<>(Arrays.asList("Final exercise", "Final exercise 2"));
        notebookEntryDto.setFinalExercises(finalExercise);
        ResponseEntity<ResponseWrapper<NotebookEntry>> responseWrapperResponseEntity1 = this.notebookController.addNotebookEntry(notebookEntryDto, 1);
        assertEquals(HttpStatus.BAD_REQUEST, responseWrapperResponseEntity1.getStatusCode());
        assertEquals(ResponseMessages.SEASON_NOT_STARTED, responseWrapperResponseEntity1.getBody().getMessage());

        ResponseEntity<ResponseWrapper<Page<NotebookEntry>>> entriesPaged = this.notebookController.getEntriesPaged(0, 1, 10);
        assertEquals(HttpStatus.BAD_REQUEST, entriesPaged.getStatusCode());
        assertEquals(ResponseMessages.SEASON_NOT_STARTED, entriesPaged.getBody().getMessage());

        NotebookEntry notebookEntry = new NotebookEntry();
        notebookEntry.setId(1);
        notebookEntry.setNotebook(notebooks.get(0));
        notebookEntry.setDate(LocalDate.now().plusDays(10));
        notebookEntry.setWarmUpExercises(new ArrayList<>(Arrays.asList("Warm up exercise", "Wam up exercise2")));
        notebookEntry.setSpecificExercises(new ArrayList<>(Arrays.asList("Specific exercise", "Specific exercise2")));
        notebookEntry.setFinalExercises(new ArrayList<>(Arrays.asList("Final exercise", "Final exercise2")));
        ResponseEntity<ResponseWrapper<NotebookEntry>> responseWrapperResponseEntity2 = this.notebookController.editNotebookEntry(notebookEntry);
        assertEquals(HttpStatus.BAD_REQUEST, responseWrapperResponseEntity2.getStatusCode());
        assertEquals(ResponseMessages.SEASON_NOT_STARTED, responseWrapperResponseEntity2.getBody().getMessage());

        ResponseEntity<Set<LocalDate>> invalidDates = this.notebookController.getInvalidDates(1);
        assertEquals(HttpStatus.BAD_REQUEST, invalidDates.getStatusCode());
        assertNull(invalidDates.getBody());

        ResponseEntity<ResponseWrapper<NotebookEntry>> responseWrapperResponseEntity3 = this.notebookController.generateEntry(1, LocalDate.now().plusDays(10));
        assertEquals(HttpStatus.BAD_REQUEST, responseWrapperResponseEntity3.getStatusCode());
        assertEquals(ResponseMessages.SEASON_NOT_STARTED, responseWrapperResponseEntity3.getBody().getMessage());
    }
}