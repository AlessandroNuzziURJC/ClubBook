package clubbook.backend.model;

import clubbook.backend.model.enumClasses.EventTypeEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

/**
 * Represents a type of event in the system.
 */
@Entity
@Table(name = "T_Event_Type")
public class EventType {

    /**
     * The unique identifier of the event type.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer eventTypeId;

    /**
     * The name of the event type. Can't be null.
     */
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private EventTypeEnum name;

    /**
     * The creation timestamp of the event type.
     */
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    /**
     * Constructs a new EventType with specified parameters.
     *
     * @param eventTypeId the unique identifier of the event type
     * @param name        the name of the event type
     * @param createdAt   the creation timestamp of the event type
     */
    public EventType(Integer eventTypeId, EventTypeEnum name, Date createdAt) {
        this.eventTypeId = eventTypeId;
        this.name = name;
        this.createdAt = createdAt;
    }

    /**
     * Default constructor for EventType.
     */
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
