package clubbook.backend.service;

import clubbook.backend.model.Schedule;
import clubbook.backend.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule findById(int id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    public void delete(Schedule schedule) {
        scheduleRepository.delete(schedule);
    }
}
