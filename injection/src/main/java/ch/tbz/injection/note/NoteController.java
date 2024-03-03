package ch.tbz.injection.note;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/api/note")
public class NoteController {
    private final NoteService service;
    private final ModelMapper modelMapper;

    @Autowired
    public NoteController(NoteService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ResponseEntity<NoteDTO> getAll() {
        return ResponseEntity.ok(modelMapper.map(service.readAllNotees(), new TypeToken<List<NoteDTO>>(){
        }.getType()));
    }

    @PostMapping("/")
    public ResponseEntity<NoteDTO> postAddress(@Validated @RequestBody Note newNote) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(service.createNote(newNote), NoteDTO.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> findAddressById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(modelMapper.map(service.readNoteById(id), NoteDTO.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> putAddress(
            @PathVariable("addressId") Integer id,
            @Validated @RequestBody Note updatedNote) {
        return ResponseEntity.ok().body(
                modelMapper.map(service.updateNoteById(id, updatedNote), NoteDTO.class)
        );
    }

    public @ResponseBody
    ResponseEntity<String> deleteAddressById(@PathVariable("addressId") Integer id) {
        service.deleteNoteById(id);
        return ResponseEntity.ok().body("Note has been deleted");
    }
}
