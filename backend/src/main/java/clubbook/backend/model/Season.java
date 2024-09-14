package clubbook.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "T_Season")
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate init;

    @Temporal(TemporalType.DATE)
    private LocalDate finish;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne
    private User adminCreator;

    @ManyToOne
    private User adminFinisher;

    public Season(int id, LocalDate init, LocalDate finish, boolean active, User adminCreator, User adminFinisher) {
        this.id = id;
        this.init = init;
        this.finish = finish;
        this.adminCreator = adminCreator;
        this.adminFinisher = adminFinisher;
        this.active = active;
    }

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
