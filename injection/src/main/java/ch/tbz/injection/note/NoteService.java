package ch.tbz.injection.note;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class NoteService {
    private final NoteRepository repository;

    @Autowired
    public NoteService(NoteRepository repository) {
        this.repository = repository;
    }

    public Note createNote(Note newNote) {
        if (newNote.getId() != null) {
            log.warn(HttpStatus.BAD_REQUEST + ": Creating note unsuccessful since invalid information was provided.");
            throw new EntityNotFoundException();
        } else {
            repository.save(newNote);
            log.info(HttpStatus.OK + ": Creating note successful.");
        }
        return newNote;
    }

    public Note readNoteById(Integer noteId) {
        if (repository.existsById(noteId)) {
            log.info(HttpStatus.OK + "Getting note taking place");
        } else {
            log.warn(HttpStatus.NOT_FOUND + ": Getting note unsuccessful due to non-existent noteId.");
        }
        return repository.findById(noteId).orElseThrow(EntityNotFoundException::new);
    }

    public Note updateNoteById(Integer noteId, Note updatedNote) {
        if (repository.existsById(noteId)) {
            log.info(HttpStatus.OK + ": Updating note taking place.");
        } else {
            log.info(HttpStatus.CREATED + ": Creating note taking place.");
        }
        updatedNote.setId(noteId);
        return repository.save(updatedNote);
    }

    public void deleteNoteById(Integer noteId) {
        if (!repository.existsById(noteId)) {
            log.warn(HttpStatus.NOT_FOUND + ": Deleting note unsuccessful due to non-existent noteId");
            throw new EntityNotFoundException();
        }
        repository.deleteById(noteId);
        log.info(HttpStatus.OK + ": Deleting note successful");
    }

    public List<Note> readAllNotees() {
        return repository.findAll();
    }
}
