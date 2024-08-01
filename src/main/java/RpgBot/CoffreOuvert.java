package RpgBot;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "coffres_ouverts")
public class CoffreOuvert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "personnage_id")
    private Personnage personnage;

    @Column(name = "date_ouverture")
    private Timestamp dateOuverture;

    @Column(name = "recompense")
    private String recompense;

    // Getters et setters omis pour la brièveté
}
