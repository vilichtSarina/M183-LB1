package ch.tbz.injection.note;

import ch.tbz.injection.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "note")
public class Note {

    @Column(name = "id")
    @Id
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "note_users",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "note_id", referencedColumnName = "id"))
    private User owner;

    public Note() {
    }

    public Note(Integer id, String title, String content, User owner) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.owner = owner;
    }
}
