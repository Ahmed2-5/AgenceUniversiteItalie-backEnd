package Agence.AgenceUniversiteItalie_backEnd.controller;


import Agence.AgenceUniversiteItalie_backEnd.entity.Utilisateur;
import Agence.AgenceUniversiteItalie_backEnd.repository.StatusCompteRepository;
import Agence.AgenceUniversiteItalie_backEnd.security.JwtUtil;
import Agence.AgenceUniversiteItalie_backEnd.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {


    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StatusCompteRepository statusCompteRepository;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Utilisateur utilisateur) {
        if (utilisateurService.utilisateurExiste(utilisateur.getAdresseMail())){
            return ResponseEntity.badRequest().body("Utilisateur already exist");
        }
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        Utilisateur savedUser = utilisateurService.createUser(utilisateur);
        return ResponseEntity.ok(savedUser);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(id);
        return utilisateur.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        return ResponseEntity.ok(utilisateurService.getAllUtilisateurs());
    }


    //Deleting user

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUtilisateur(@PathVariable Long id) {
        boolean deleted = utilisateurService.deleteUser(id);
        if (deleted){
            return ResponseEntity.ok("Utilisateur deleted");
        }else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/create-Admin")
    public ResponseEntity<?> createAdmin(@RequestBody Utilisateur admin , @RequestParam String superAdminEmail  ) {

        Utilisateur newAdmin = utilisateurService.createAdmin(admin, superAdminEmail);
        return ResponseEntity.ok(newAdmin);
    }

    @PostMapping("/createUserWithRole")
    public ResponseEntity<?> createUserWithRole(@RequestBody Map<String , String> requestBody , @RequestParam String superAdminEmail ) {
        String nom = requestBody.get("nom");
        String prenom = requestBody.get("prenom");
        String email = requestBody.get("email");
        String motDePasse = requestBody.get("motDePasse");
        String roleName = requestBody.get("role");

        try {
            Utilisateur newUser = utilisateurService.createUserWithRole(nom , prenom, email , motDePasse, roleName, superAdminEmail);
            return ResponseEntity.ok(newUser);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }































}
