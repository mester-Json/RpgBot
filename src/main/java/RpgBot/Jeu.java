package RpgBot;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.HashSet;

import java.util.HashMap;
import java.util.Map;

public class Jeu {
    private SessionFactory sessionFactory;
    private Map<String, Personnage> personnages = new HashMap<>();

    public Jeu() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public void commencerJeu(String discordId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Personnage personnage = null;

        try {
            transaction = session.beginTransaction();

            // Chercher l'utilisateur
            Utilisateur utilisateur = session.createQuery("SELECT u FROM Utilisateur u WHERE u.discordId = :discordId", Utilisateur.class)
                    .setParameter("discordId", discordId)
                    .uniqueResult();

            if (utilisateur == null) {
                utilisateur = new Utilisateur();
                utilisateur.setDiscordId(discordId);
                utilisateur.setNomUtilisateur("Utilisateur");

                session.save(utilisateur);
            }

            if (utilisateur.getPersonnages() == null) {
                utilisateur.setPersonnages(new HashSet<>());
            }

            personnage = utilisateur.getPersonnages().stream().findFirst().orElse(null);
            if (personnage == null) {
                personnage = new Personnage();
                personnage.setUtilisateur(utilisateur);
                personnage.setPositionX(0);
                personnage.setPositionY(0);

                utilisateur.getPersonnages().add(personnage);
                session.save(personnage);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        personnages.put(discordId, personnage);
    }

    public Personnage getPersonnage(String discordId) {
        return personnages.get(discordId);
    }

    public void close() {
        sessionFactory.close();
    }
}
