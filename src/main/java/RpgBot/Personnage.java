package RpgBot;

import lombok.Data;
import javax.persistence.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public String ouvrirCoffre() {
        if (peutOuvrirCoffre()) {
            dernierCoffre = LocalDateTime.now();
            return genererLoot();
        } else {
            return "Vous ne pouvez pas ouvrir de coffre maintenant.";
        }
    }

    private String genererLoot() {
        List<String> communLoot = new ArrayList<>();
        List<String> rareLoot = new ArrayList<>();
        List<String> legendaireLoot = new ArrayList<>();
        Random rand = new Random();

        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/RpgBot/loot.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String categorie = parts[2].trim();
                    switch (categorie) {
                        case "commun":
                            communLoot.add(parts[1].trim());
                            break;
                        case "rare":
                            rareLoot.add(parts[1].trim());
                            break;
                        case "légendaire":
                            legendaireLoot.add(parts[1].trim());
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int lootTier = rand.nextInt(100);
        String loot;

        if (lootTier < 5 && !legendaireLoot.isEmpty()) {
            loot = legendaireLoot.get(rand.nextInt(legendaireLoot.size()));
        } else if (lootTier < 20 && !rareLoot.isEmpty()) {
            loot = rareLoot.get(rand.nextInt(rareLoot.size()));
        } else {
            loot = communLoot.get(rand.nextInt(communLoot.size()));
        }

        return "Vous avez trouvé un(e) " + loot + "!";
    }
}
