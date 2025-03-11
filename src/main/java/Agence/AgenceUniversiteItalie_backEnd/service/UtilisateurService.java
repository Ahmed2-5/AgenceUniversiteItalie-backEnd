package Agence.AgenceUniversiteItalie_backEnd.service;

import Agence.AgenceUniversiteItalie_backEnd.entity.EnumRole;
import Agence.AgenceUniversiteItalie_backEnd.entity.Role;
import Agence.AgenceUniversiteItalie_backEnd.entity.StatusCompte;
import Agence.AgenceUniversiteItalie_backEnd.entity.Utilisateur;
import Agence.AgenceUniversiteItalie_backEnd.repository.RoleRepository;
import Agence.AgenceUniversiteItalie_backEnd.repository.StatusCompteRepository;
import Agence.AgenceUniversiteItalie_backEnd.repository.UtilisateurRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UtilisateurService {



    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StatusCompteRepository statusCompteRepository;

    @Autowired
    private EmailService emailService;


    //initialise le compte Super Admin

    @PostConstruct
    public void createSuperAdmin(){
        if(utilisateurRepository.findByAdresseMail("admin@universiteitalie.com").isPresent()){
            System.out.println("Utilisateur already exist");
            return;
        }
        // verification si le super Admin existe ou non
        Role superAdminRole = roleRepository.findByLibelleRole(EnumRole.SUPER_ADMIN)
                .orElseThrow(()-> new RuntimeException("Le Role super n'existe pas "));


        String encodedPassword = passwordEncoder.encode("admin123");

        Utilisateur superAdmin = new Utilisateur("Admin","Super","admin@universiteitalie.com",encodedPassword,superAdminRole);

        utilisateurRepository.save(superAdmin);
        System.out.println("Admin creer avec success");
    }

    // cree un utilisateur

    public Utilisateur createUser(Utilisateur utilisateur){
        if (utilisateur.getMotDePasse() == null || utilisateur.getMotDePasse().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Le mot de pass ne peut pas etre vide");
        }

        if (utilisateurExiste(utilisateur.getAdresseMail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"ce mail est deja utilisé");
        }

        Role role = roleRepository.findByLibelleRole(utilisateur.getRole().getLibelleRole())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"le rôle specifie n'existe pas "));
        utilisateur.setRole(role);

        StatusCompte statusCompte;
        if (role.getLibelleRole() == EnumRole.CLIENT){
            statusCompte = statusCompteRepository.findById(2L)
                    .orElseThrow(()-> new RuntimeException("Status Compte (Desactiver) introuvable"));
        }else {
            statusCompte = statusCompteRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Status Compte Actif introvable"));
        }
        utilisateur.setStatusCompte(statusCompte);

        //Encode MotDePasse
        if (!utilisateur.getMotDePasse().startsWith("$2s$")){
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        }

        //save utilisateur

        Utilisateur savedUser = utilisateurRepository.save(utilisateur);
        System.out.println("Utilisateur creer avec status :"+ savedUser.getStatusCompte().getIdStatusCompte());

        if (role.getLibelleRole() == EnumRole.CLIENT){
            emailService.envoyerEmailActivation(savedUser);
        }
        return savedUser;
    }

    private boolean utilisateurExiste(String email) {
        return utilisateurRepository.findByAdresseMail(email).isPresent();
    }


}
