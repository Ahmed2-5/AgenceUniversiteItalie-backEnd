package Agence.AgenceUniversiteItalie_backEnd.service;


import Agence.AgenceUniversiteItalie_backEnd.entity.EnumRole;
import Agence.AgenceUniversiteItalie_backEnd.entity.Tache;
import Agence.AgenceUniversiteItalie_backEnd.entity.Utilisateur;
import Agence.AgenceUniversiteItalie_backEnd.repository.TacheRepository;
import Agence.AgenceUniversiteItalie_backEnd.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TacheServie {

    private final TacheRepository tacheRepository;

    private final UtilisateurRepository utilisateurRepository;


    public TacheServie(TacheRepository tacheRepository, UtilisateurRepository utilisateurRepository) {
        this.tacheRepository = tacheRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public Tache createTache(String titre, String description, String superAdminEmail, Set<Long> adminIds) {

        Utilisateur superAdmin = utilisateurRepository.findByAdresseMail(superAdminEmail)
                .orElseThrow(() -> new RuntimeException("❌ SuperAdmin not found!"));

        if (superAdmin.getRole() == null || !superAdmin.getRole().getLibelleRole().equals(EnumRole.SUPER_ADMIN)) {
            throw new RuntimeException("❌ Only Super Admins can create tasks!");
        }

        Tache tache = new Tache(titre, description, superAdmin);

        Set<Utilisateur> assignedAdmins = utilisateurRepository.findAllById(adminIds)
                .stream()
                .filter(u -> u.getRole() != null && u.getRole().getLibelleRole().equals(EnumRole.ADMIN)) // Prevent NullPointer
                .collect(Collectors.toSet());

        if (assignedAdmins.isEmpty()) {
            throw new RuntimeException("❌ At least one valid Admin is required!");
        }

        tache.setAssignedAdmins(assignedAdmins);

        return tacheRepository.save(tache);
    }


     public List<Tache> getAllTaches() {
        return tacheRepository.findAll();
     }

     public Optional<Tache> getTacheById(long id) {
        return tacheRepository.findById(id);
     }


}
