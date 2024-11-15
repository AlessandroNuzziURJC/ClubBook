package clubbook.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Represents a season within the application, which can be created and finished by administrators.
 */
@Entity
@Table(name = "T_Season")
public class Season {

    /**
     * The unique identifier of the season.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The start date of the season.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate init;

    /**
     * The end date of the season.
     */
    @Temporal(TemporalType.DATE)
    private LocalDate finish;

    /**
     * Whether the season is currently active.
     */
    @Column(nullable = false)
    private boolean active;

    /**
     * The user who created the season.
     */
    @ManyToOne
    private User adminCreator;

    /**
     * The user who finished the season.
     */
    @ManyToOne
    private User adminFinisher;

    /**
     * Constructs a new Season with the specified parameters.
     *
     * @param id           the unique identifier of the season
     * @param init         the start date of the season
     * @param finish       the end date of the season
     * @param active       whether the season is currently active
     * @param adminCreator the user who created the season
     * @param adminFinisher the user who finished the season
     */
    public Season(int id, LocalDate init, LocalDate finish, boolean active, User adminCreator, User adminFinisher) {
        this.id = id;
        this.init = init;
        this.finish = finish;
        this.adminCreator = adminCreator;
        this.adminFinisher = adminFinisher;
        this.active = active;
    }

    /**
     * Default constructor for Season.
     */
    public Season() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getInit() {
        return init;
    }

    public void setInit(LocalDate init) {
        this.init = init;
    }

    public LocalDate getFinish() {
        return finish;
    }

    public void setFinish(LocalDate finish) {
        this.finish = finish;
    }

    public User getAdminCreator() {
        return adminCreator;
    }

    public void setAdminCreator(User adminCreator) {
        this.adminCreator = adminCreator;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User getAdminFinisher() {
        return adminFinisher;
    }

    public void setAdminFinisher(User adminFinisher) {
        this.adminFinisher = adminFinisher;
    }
}
