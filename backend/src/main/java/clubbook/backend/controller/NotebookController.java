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

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<Notebook>> getNotebookById(@PathVariable("id") Integer id) {
        if (!seasonService.seasonStarted()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, notebookService.getNotebook(id)));
    }

    @GetMapping("/entry/today/{id}")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<NotebookEntry>> getNotebookEntryTodayById(@PathVariable("id") Integer id) {
        if (!seasonService.seasonStarted()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, notebookService.getNotebookEntryToday(id)));
    }

    @PutMapping("/config")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<Boolean>> updateNotebookConfig(@Valid @RequestBody NotebookUpdateConfigDto notebookUpdateConfigDto) {
        if (!seasonService.seasonStarted()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        boolean output = this.notebookService.updateNotebookConfiguration(notebookUpdateConfigDto);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, output));
    }

    @PostMapping("/entry/{id}")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<NotebookEntry>> addNotebookEntry(@Valid @RequestBody NotebookEntryDto notebookEntryDto, @PathVariable("id") Integer id) {
        if (!seasonService.seasonStarted()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        NotebookEntry notebookEntryOutput = this.notebookService.addNotebookEntry(notebookEntryDto, id);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, notebookEntryOutput));
    }

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

    @DeleteMapping("/entry/{id}")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<Boolean>> deleteNotebookEntry(@PathVariable("id") Integer id) {
        return  ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.notebookService.deleteEntry(id)));
    }

    @PutMapping("/entry")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<NotebookEntry>> editNotebookEntry(@Valid @RequestBody NotebookEntry notebookEntry) {
        if (!seasonService.seasonStarted()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        NotebookEntry notebookEntryOutput = this.notebookService.editNotebookEntry(notebookEntry);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, notebookEntryOutput));
    }

    @GetMapping("/invalidDates/{id}")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<Set<LocalDate>> getInvalidDates(@PathVariable("id") int id) {
        if (!seasonService.seasonStarted()) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(this.notebookService.getAllDates(id));
    }

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
