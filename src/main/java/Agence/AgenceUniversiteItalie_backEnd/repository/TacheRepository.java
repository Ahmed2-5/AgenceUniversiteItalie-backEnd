package Agence.AgenceUniversiteItalie_backEnd.repository;

import Agence.AgenceUniversiteItalie_backEnd.entity.Tache;
import Agence.AgenceUniversiteItalie_backEnd.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TacheRepository extends JpaRepository<Tache,Long>   {

    List<Tache> findByAssignedAdmins(Utilisateur admin);

}
