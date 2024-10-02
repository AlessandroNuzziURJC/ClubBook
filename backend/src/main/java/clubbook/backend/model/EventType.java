package clubbook.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "T_Event_Type")
public class EventType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer eventTypeId;

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private EventTypeEnum name;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    public EventType(Integer eventTypeId, EventTypeEnum name, Date createdAt) {
        this.eventTypeId = eventTypeId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public EventType() {
    }

    public Integer getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Integer eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public EventTypeEnum getName() {
        return name;
    }

    public void setName(EventTypeEnum name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
