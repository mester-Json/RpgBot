package RpgBot;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "personnages")
public class Personnage {

    @Id
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

    // Constructeurs, getters, setters
    public Personnage() {}

    public Personnage(Long id, String dernierCoffre, int positionX, int positionY, Utilisateur utilisateur) {
        this.id = id;
        this.dernierCoffre = dernierCoffre;
        this.positionX = positionX;
        this.positionY = positionY;
        this.utilisateur = utilisateur;
    }

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
        if ("nord".equalsIgnoreCase(direction)) {
            positionY++;
        } else if ("sud".equalsIgnoreCase(direction)) {
            positionY--;
        } else if ("est".equalsIgnoreCase(direction)) {
            positionX++;
        } else if ("ouest".equalsIgnoreCase(direction)) {
            positionX--;
        }
    }

    public boolean peutOuvrirCoffre() {
        // Exemple simple : un coffre peut être ouvert si le dernier coffre est null ou vide
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
