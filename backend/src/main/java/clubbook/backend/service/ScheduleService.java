package clubbook.backend.service;

import clubbook.backend.model.Schedule;
import clubbook.backend.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing schedules within the application.
 * This class provides methods to save, find, and delete schedules.
 */
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * Saves a new schedule to the database.
     *
     * @param schedule the Schedule entity to be saved
     * @return the saved Schedule entity
     */
    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    /**
     * Finds a schedule by its ID.
     *
     * @param id the ID of the schedule to be retrieved
     * @return the Schedule entity if found, or null if no schedule with the specified ID exists
     */
    public Schedule findById(int id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    /**
     * Deletes the specified schedule from the database.
     *
     * @param schedule the Schedule entity to be deleted
     */
    public void delete(Schedule schedule) {
        scheduleRepository.delete(schedule);
    }
}
