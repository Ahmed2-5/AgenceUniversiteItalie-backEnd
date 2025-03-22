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

    @Column(length = 1000)
    private String description;



    @Enumerated(EnumType.STRING)
    private EnumStatutTache status= EnumStatutTache.PAS_ENCORE;

    private LocalDateTime dateCreation = LocalDateTime.now();

    private LocalDateTime dateModification = LocalDateTime.now();

    private LocalDateTime dateEcheance = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "createdBy", nullable = false)
    private Utilisateur createdBy;

    @ManyToMany
    @JoinTable(
            name = "tache_admins",
            joinColumns = @JoinColumn(name = "tache_id"),
            inverseJoinColumns = @JoinColumn(name = "admin_id")
    )
    private Set<Utilisateur> assignedAdmins = new HashSet<>();

    @OneToMany(mappedBy = "tache" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Utilisateur> commentaires = new HashSet<>();

    @PrePersist
    protected void onCreate(){
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
        if (this.status == null){
            this.status= EnumStatutTache.PAS_ENCORE;
        }
    }

    @PreUpdate
    protected void onUpdate(){
        this.dateModification = LocalDateTime.now();
    }


    public Tache(String titre, String description, Utilisateur createdBy) {
        this.titre = titre;
        this.description = description;
        this.createdBy = createdBy;
    }


}
