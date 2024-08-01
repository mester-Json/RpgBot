package RpgBot;

import javax.persistence.*;

@Entity
@Table(name = "personnages")
public class Personnage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name = "dernier_coffre")
    private String dernierCoffre;

    @Column(name = "position_x")
    private int positionX;

    @Column(name = "position_y")
    private int positionY;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDernierCoffre() {
        return dernierCoffre;
    }

    public void setDernierCoffre(String dernierCoffre) {
        this.dernierCoffre = dernierCoffre;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
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
                throw new IllegalArgumentException("Direction inconnue : " + direction);
        }
    }

    public boolean peutOuvrirCoffre() {
        return dernierCoffre == null || dernierCoffre.isEmpty();
    }

    public String ouvrirCoffre() {
        if (peutOuvrirCoffre()) {
            dernierCoffre = "Nouveau coffre ouvert!";
            return "Vous avez ouvert un coffre et trouvé un trésor!";
        } else {
            return "Vous avez déjà ouvert un coffre récemment.";
        }
    }
}
