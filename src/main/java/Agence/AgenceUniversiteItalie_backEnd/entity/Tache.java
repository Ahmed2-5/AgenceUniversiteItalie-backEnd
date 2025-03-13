package Agence.AgenceUniversiteItalie_backEnd.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTache;

    private String titre;
    private String description;



    @Enumerated(EnumType.STRING)
    private TacheStatus status= TacheStatus.PAS_ENCORE;

    private LocalDateTime dateCreation = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "createdBy", nullable = false)
    private Utilisateur createdBy;

    @ManyToMany
    @JoinTable(
            name = "tache_admins",
            joinColumns = @JoinColumn(name = "tache_id"),
            inverseJoinColumns = @JoinColumn(name = "admin_id")
    )
    private Set<Utilisateur> admins = new HashSet<>();


    public Tache(String titre, String description, Utilisateur createdBy) {
        this.titre = titre;
        this.description = description;
        this.createdBy = createdBy;
    }


}
