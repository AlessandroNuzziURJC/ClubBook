package clubbook.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "T_Notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private LocalDate date;

    private String content;

    @ManyToOne
    private User user;

    public Notification() {}

    public Notification(int id, String title, LocalDate date, String content, User user) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.content = content;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
