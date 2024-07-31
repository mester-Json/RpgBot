package RpgBot;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "personnages")
@Data
public class Personnage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "utilisateur_id")
    private Long utilisateurId;

    private String nom;

    @Column(name = "position_x")
    private int positionX;

    @Column(name = "position_y")
    private int positionY;

    @Column(name = "dernier_coffre")
    private LocalDateTime dernierCoffre;

    public Personnage() {}

    public Personnage(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public void deplacer(String direction) {
        switch (direction.toLowerCase()) {
            case "nord":
                positionY++;
                break;
            case "sud":
                positionY--;
                break;
            case "est":
                positionX++;
                break;
            case "ouest":
                positionX--;
                break;
            default:
                System.out.println("Direction inconnue.");
        }
    }

    public boolean peutOuvrirCoffre() {
        return dernierCoffre == null || LocalDateTime.now().isAfter(dernierCoffre.plusMinutes(10));
    }

    public void ouvrirCoffre() {
        if (peutOuvrirCoffre()) {
            System.out.println("Coffre ouvert !");
            dernierCoffre = LocalDateTime.now();
        } else {
            System.out.println("Vous ne pouvez pas ouvrir de coffre maintenant.");
        }
    }
}
