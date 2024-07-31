package RpgBot;

import java.util.HashMap;
import java.util.Map;

public class Jeu {
    private Map<Long, Personnage> personnages = new HashMap<>();

    public void ajouterPersonnage(long utilisateurId) {
        personnages.putIfAbsent(utilisateurId, new Personnage(utilisateurId));
    }

    public Personnage getPersonnage(long utilisateurId) {
        return personnages.get(utilisateurId);
    }
}

