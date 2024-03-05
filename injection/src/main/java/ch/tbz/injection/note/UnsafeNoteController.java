package ch.tbz.injection.note;

import ch.tbz.injection.note.Note;
import ch.tbz.injection.user.User;
import ch.tbz.injection.user.UserLogin;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/api/note")
public class UnsafeNoteController {
    private final DataSource dataSource;

    public UnsafeNoteController() {
        dataSource = DataSourceBuilder
                .create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/postgres")
                .username("postgres")
                .password("postgres").build();
    }

    @GetMapping("/unsafe/")
    public ResponseEntity<List<Object>> getAll() throws SQLException {
        String query = "SELECT * FROM note;";
        List<Object> result = unsafeResult(query, new ArrayList<>());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/unsafe/id/{id}")
    public ResponseEntity<Object> getById(
            @PathVariable(name = "id") Object id
    ) throws SQLException {
        String query = String.format("""
                SELECT note.id, note.title, note.content
                FROM note
                WHERE note.id = %s;
                """, id);
        List<Object> result = unsafeResult(query, new ArrayList<>());

        return ResponseEntity.ok(result);
    }

//    1. Make PathVariable an Integer
//    2. Change placeholder in String.format to %d (int)
//    3. Id should be unique: only return 1
    @GetMapping("/safer/id/{id}")
    public ResponseEntity<Note> saferGetById(
            @PathVariable(name = "id") Integer id
    ) throws SQLException {
        String query = String.format("""
                SELECT note.id, note.title, note.content
                FROM note
                WHERE note.id = %d;
                """, id);
        Note result = saferResult(query, new ArrayList<>()).get(0);

        return ResponseEntity.ok(result);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //    Only allow full Note to be seen if note is by valid user
    @GetMapping("/unsafe/title/{title}")
    public ResponseEntity<Object> getByTitle(
            @PathVariable(name = "title", required = true) String title,
            @RequestBody UserLogin userLogin
    ) throws SQLException {
        String query = String.format("""
                SELECT note.id, note.title, note.content
                FROM note
                INNER JOIN note_users nu
                    ON note.id = nu.note_id
                INNER JOIN users
                    ON nu.note_id = users.id
                WHERE note.title = '%s' AND users.username = '%s' AND users.password = '%s';
                """, title, userLogin.getPassword(), userLogin.getUsername());
        List<Object> result = unsafeResult(query, new ArrayList<>());

        return ResponseEntity.ok(result);
    }

//    Validate user in separate query
    @GetMapping("/safer/title/{title}")
    public ResponseEntity<Object> saferGetByTitle(
            @PathVariable(name = "title", required = true) String title,
            @RequestBody UserLogin userLogin
    ) throws SQLException {
        if (!isValidUser(userLogin)){
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).build();
        }
        String query = String.format("""
                SELECT note.id, note.title, note.content
                FROM note
                INNER JOIN note_users nu
                    ON note.id = nu.note_id
                INNER JOIN users
                    ON nu.note_id = users.id
                WHERE note.title = '%s' AND users.username = '%s' AND users.password = '%s';
                """, title, userLogin.getPassword(), userLogin.getUsername());
        List<Object> result = unsafeResult(query, new ArrayList<>());

        return ResponseEntity.ok(result);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private List<Object> unsafeResult(String query, List<Object> notes) throws SQLException {
        log.info("QUERY: \n\n" + query);
        Connection connection = dataSource.getConnection();
        ResultSet set = connection.createStatement().executeQuery(query);

        while (set.next()) {
            Note note = new Note();
            note.setId(set.getInt(1));
            note.setTitle(set.getString(2));
            note.setContent(set.getString(3));
            notes.add(note);
        }
        return notes;
    }

    private List<Note> saferResult(String query, List<Note> notes) throws SQLException {
        log.info("QUERY: \n\n" + query);
        Connection connection = dataSource.getConnection();
        ResultSet set = connection.createStatement().executeQuery(query);

        while (set.next()) {
            Note note = new Note();
            note.setId(set.getInt("id"));
            note.setTitle(set.getString("title"));
            note.setContent(set.getString("content"));
            notes.add(note);
        }
        return notes;
    }

    private List<User> saferUser(String query, List<User> users) throws SQLException {
        log.info("QUERY: \n\n" + query);
        Connection connection = dataSource.getConnection();
        ResultSet set = connection.createStatement().executeQuery(query);

        while (set.next()) {
            User user = new User();
            user.setId(set.getInt("id"));
            user.setUsername(set.getString("username"));
            user.setPassword(set.getString("password"));
            users.add(user);
        }
        return users;
    }

    private boolean isValidUser(UserLogin userLogin) throws SQLException {
        String userQuery = String.format("""
                SELECT id, username, password
                FROM users
                WHERE users.username = '%s' AND users.password = '%s';
                """, userLogin.getPassword(), userLogin.getUsername());
        List<User> queryResult = saferUser(userQuery, new ArrayList<>());

        if(queryResult.size() > 1) {
            return false;
        }
        if(!queryResult.get(0).getUsername().equals(userLogin.getUsername())){
            return false;
        }
        if(!queryResult.get(0).getPassword().equals(userLogin.getPassword())){
            return false;
        }
        return true;
    }
}
