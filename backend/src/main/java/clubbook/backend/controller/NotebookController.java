package clubbook.backend.controller;

import clubbook.backend.dtos.NotebookEntryDto;
import clubbook.backend.dtos.NotebookPrincipalInfoDto;
import clubbook.backend.dtos.NotebookUpdateConfigDto;
import clubbook.backend.model.Notebook;
import clubbook.backend.model.NotebookEntry;
import clubbook.backend.responses.ResponseMessages;
import clubbook.backend.responses.ResponseWrapper;
import clubbook.backend.service.NotebookService;
import clubbook.backend.service.SeasonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Controller for notebooks.
 * Provides endpoints for creating, editing, retrieving, and deleting notebooks and notebook entries.
 */
@Validated
@RestController
@RequestMapping("/notebook")
public class NotebookController {

    private final SeasonService seasonService;
    private final NotebookService notebookService;

    @Autowired
    public NotebookController(SeasonService seasonService, NotebookService notebookService) {
        this.seasonService = seasonService;
        this.notebookService = notebookService;
    }

    /**
     * Retrieves all class groups (notebooks) for teachers.
     *
     * @return ResponseEntity containing a list of NotebookPrincipalInfoDto objects
     */
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<List<NotebookPrincipalInfoDto>>> getAllClassGroups() {
        if (!seasonService.seasonStarted()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }
        List<Notebook> notebooks = this.notebookService.getAllNotebooks();
        List<NotebookPrincipalInfoDto> notebookPrincipalInfoDtos = new ArrayList<>(notebooks.size());
        for (Notebook notebook : notebooks) {
            notebookPrincipalInfoDtos.add(new NotebookPrincipalInfoDto(notebook));
        }
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, notebookPrincipalInfoDtos));
    }

    /**
     * Retrieves a specific notebook by its ID.
     *
     * @param id the ID of the notebook to retrieve
     * @return ResponseEntity containing the Notebook object
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<Notebook>> getNotebookById(@PathVariable("id") Integer id) {
        if (!seasonService.seasonStarted()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, notebookService.getNotebook(id)));
    }

    /**
     * Retrieves today's notebook entry for a specific notebook by its ID.
     *
     * @param id the ID of the notebook to retrieve the entry for
     * @return ResponseEntity containing the NotebookEntry object for today
     */
    @GetMapping("/entry/today/{id}")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<NotebookEntry>> getNotebookEntryTodayById(@PathVariable("id") Integer id) {
        if (!seasonService.seasonStarted()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, notebookService.getNotebookEntryToday(id)));
    }

    /**
     * Updates the configuration of a notebook.
     *
     * @param notebookUpdateConfigDto the configuration details to update
     * @return ResponseEntity indicating success or failure of the update operation
     */
    @PutMapping("/config")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<Boolean>> updateNotebookConfig(@Valid @RequestBody NotebookUpdateConfigDto notebookUpdateConfigDto) {
        if (!seasonService.seasonStarted()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        boolean output = this.notebookService.updateNotebookConfiguration(notebookUpdateConfigDto);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, output));
    }

    /**
     * Adds a new entry to a notebook.
     *
     * @param notebookEntryDto the details of the notebook entry to add
     * @param id the ID of the notebook to add the entry to
     * @return ResponseEntity containing the added NotebookEntry object
     */
    @PostMapping("/entry/{id}")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<NotebookEntry>> addNotebookEntry(@Valid @RequestBody NotebookEntryDto notebookEntryDto, @PathVariable("id") Integer id) {
        if (!seasonService.seasonStarted()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        NotebookEntry notebookEntryOutput = this.notebookService.addNotebookEntry(notebookEntryDto, id);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, notebookEntryOutput));
    }

    /**
     * Retrieves paginated notebook entries for a specific notebook by its ID.
     *
     * @param id the ID of the notebook to retrieve entries for
     * @param pageNumber the page number to retrieve (default is 0)
     * @param pageSize the number of entries per page (default is 10)
     * @return ResponseEntity containing a page of NotebookEntry objects
     */
    @GetMapping("/entry/{id}")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<Page<NotebookEntry>>> getEntriesPaged(@PathVariable("id") int id,
                                                                                @RequestParam(defaultValue = "0") int pageNumber,
                                                                                @RequestParam(defaultValue = "10") int pageSize) {
        if (!seasonService.seasonStarted()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.notebookService.getEntries(id, pageNumber, pageSize)));
    }

    /**
     * Deletes a notebook entry by its ID.
     *
     * @param id the ID of the entry to delete
     * @return ResponseEntity indicating success or failure of the delete operation
     */
    @DeleteMapping("/entry/{id}")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<Boolean>> deleteNotebookEntry(@PathVariable("id") Integer id) {
        return  ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.notebookService.deleteEntry(id)));
    }

    /**
     * Edits an existing notebook entry.
     *
     * @param notebookEntry the NotebookEntry object containing updated details
     * @return ResponseEntity containing the updated NotebookEntry object
     */
    @PutMapping("/entry")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<NotebookEntry>> editNotebookEntry(@Valid @RequestBody NotebookEntry notebookEntry) {
        if (!seasonService.seasonStarted()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        NotebookEntry notebookEntryOutput = this.notebookService.editNotebookEntry(notebookEntry);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, notebookEntryOutput));
    }

    /**
     * Retrieves all invalid dates for a specific notebook by its ID.
     *
     * @param id the ID of the notebook to retrieve invalid dates for
     * @return ResponseEntity containing a set of invalid LocalDate objects
     */
    @GetMapping("/invalidDates/{id}")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<Set<LocalDate>> getInvalidDates(@PathVariable("id") int id) {
        if (!seasonService.seasonStarted()) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(this.notebookService.getAllDates(id));
    }

    /**
     * Generates a notebook entry for a specific date.
     *
     * @param id the ID of the notebook to generate the entry for
     * @param date the date for which to generate the entry
     * @return ResponseEntity containing the generated NotebookEntry object
     * @throws Exception if there is an error generating the entry
     */
    @GetMapping("/generateEntry/{id}")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<NotebookEntry>> generateEntry(@PathVariable("id") Integer id, @RequestParam LocalDate date) throws Exception {
        if (!seasonService.seasonStarted()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        NotebookEntry entry = this.notebookService.generateEntry(id, date);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, entry));
    }

}
