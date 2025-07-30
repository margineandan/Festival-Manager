package main.Controller;

import Domain.Spectacol;
import Domain.Validators.ValidationException;
import Repository.Spectacol.SpectacolDBHibernateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin
@RestController
@RequestMapping("/festival/spectacole")
public class SpectacolController {

    @Autowired
    private SpectacolDBHibernateRepo spectacolDBRepo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/{id}")
    public ResponseEntity<Spectacol> findOne(@PathVariable Integer id) {
        System.out.println("Fetching spectacol with ID: " + id);
        try {
            Spectacol spectacol = spectacolDBRepo.findOne(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Spectacol with ID " + id + " not found"
                    ));
            return ResponseEntity.ok(spectacol);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Failed to fetch spectacol: " + e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error fetching spectacol",
                    e
            );
        }
    }

    @GetMapping
    public ResponseEntity<List<Spectacol>> getAllSpectacole() {
        System.out.println("Fetching all spectacole");
        try {
            List<Spectacol> spectacole = StreamSupport.stream(spectacolDBRepo.findAll().spliterator(), false)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(spectacole);
        } catch (Exception e) {
            System.err.println("Failed to fetch spectacole: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching spectacole");
        }
    }

    @PostMapping
    public ResponseEntity<Spectacol> addSpectacol(@RequestBody Spectacol spectacol) {
        System.out.println("Adding new spectacol: " + spectacol);
        try {
            spectacol.setId(null);
            spectacolDBRepo.save(spectacol);
            messagingTemplate.convertAndSend("/topic/spectacole", "update");
            return ResponseEntity.status(HttpStatus.CREATED).body(spectacol);
        } catch (ValidationException e) {
            System.err.println("Validation failed: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to add spectacol: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding spectacol");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Spectacol> updateSpectacol(
            @PathVariable Integer id,
            @RequestBody Spectacol spectacol) {
        System.out.println("Updating spectacol ID " + id + " with data: " + spectacol);
        try {
            if (spectacol.getId() != null && !spectacol.getId().equals(id)) {
                throw new ValidationException("ID in path doesn't match ID in body");
            }

            spectacol.setId(id);
            spectacolDBRepo.update(spectacol);
            messagingTemplate.convertAndSend("/topic/spectacole", "update");
            return ResponseEntity.ok(spectacol);
        } catch (ValidationException e) {
            System.err.println("Validation failed: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to update spectacol: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating spectacol");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpectacol(@PathVariable Integer id) {
        System.out.println("Deleting spectacol with ID: " + id);
        try {
            if (spectacolDBRepo.findOne(id).isEmpty()) {
                System.err.println("Spectacol with ID " + id + " not found");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Spectacol not found");
            }

            spectacolDBRepo.delete(id);
            messagingTemplate.convertAndSend("/topic/spectacole", "update");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("Failed to delete spectacol: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting spectacol");
        }
    }
}
