package Agence.AgenceUniversiteItalie_backEnd.controller;


import Agence.AgenceUniversiteItalie_backEnd.entity.StatusCompte;
import Agence.AgenceUniversiteItalie_backEnd.entity.Utilisateur;
import Agence.AgenceUniversiteItalie_backEnd.repository.StatusCompteRepository;
import Agence.AgenceUniversiteItalie_backEnd.security.JwtUtil;
import Agence.AgenceUniversiteItalie_backEnd.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin(origins = "http://localhost:4200")
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


    @GetMapping("/alluser")
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


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Utilisateur utilisateur) {
        Optional<Utilisateur> userOpt = utilisateurService.getUtilisateurByEmail(utilisateur.getAdresseMail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Email ou mot de passe incorrect.");
        }

        Utilisateur user = userOpt.get();

        if (!passwordEncoder.matches(utilisateur.getMotDePasse(), user.getMotDePasse())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Email ou mot de passe incorrect.");
        }

        // ✅ Vérifie si le compte est activé
        if (user.getStatusCompte().getIdStatusCompte() == 2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("❌ Votre compte est désactivé. Veuillez vérifier votre email pour l'activer.");
        }

        // ✅ Mise à jour de la date de dernière connexion
        user.setDateDerniereConnexion(LocalDateTime.now());
        utilisateurService.updateUtilisateur(user);

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }



    @GetMapping("/activer-compte")
    public ResponseEntity<?> activerCompte(@RequestParam String email) {

        Optional<Utilisateur> utilisateurOpt = utilisateurService.getUtilisateurByEmail(email);

        if(utilisateurOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introvable");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        if (utilisateur.getStatusCompte().getIdStatusCompte()==1){
            return ResponseEntity.ok("Votre compte est deja activé!");
        }

        StatusCompte statusCompteActif = statusCompteRepository.findById(1L)
                .orElseThrow(()-> new RuntimeException("StatusCompte ID=1 intovable en base!")  );

        utilisateur.setStatusCompte(statusCompteActif);
        utilisateurService.updateUtilisateur(utilisateur);

        return ResponseEntity.ok("Votre Compte a ete activé avec succés !");
    }

}
