package Agence.AgenceUniversiteItalie_backEnd.controller;


import Agence.AgenceUniversiteItalie_backEnd.entity.Tache;
import Agence.AgenceUniversiteItalie_backEnd.repository.TacheRepository;
import Agence.AgenceUniversiteItalie_backEnd.service.TacheServie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/Taches")
public class TacheController {

    private final TacheServie tacheService;
    private TacheRepository tacheRepository;

    public TacheController(TacheServie tacheService) {
        this.tacheService = tacheService;
    }

    // ✅ SuperAdmin creates a task
    @PostMapping("/create")
    public ResponseEntity<?> createTache(@RequestParam String titre,
                                         @RequestParam String description,
                                         @RequestParam String superAdminEmail,
                                         @RequestParam Set<Long> adminIds) {
        Tache tache = tacheService.createTache(titre, description, superAdminEmail, adminIds);
        return ResponseEntity.ok(tache);
    }

    @GetMapping("/GetAllTaches")
    public ResponseEntity<List<Tache>> getAllTaches() {
        return ResponseEntity.ok(tacheService.getAllTaches());
    }

    @GetMapping("/GetTacheButId")
    public ResponseEntity<Tache> getTacheButId(@RequestParam long id) {
        Optional<Tache> tache = tacheService.getTacheById(id);
        return tache.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
