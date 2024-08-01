package RpgBot;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class Jeu {

    public Personnage getPersonnage(String discordId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT p FROM Personnage p JOIN p.utilisateur u WHERE u.discordId = :discordId";
            Query<Personnage> query = session.createQuery(hql, Personnage.class);
            query.setParameter("discordId", discordId);
            Personnage personnage = query.uniqueResult();
            return personnage;
        }
    }

    public void ajouterPersonnage(String discordId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String hqlUser = "FROM Utilisateur WHERE discordId = :discordId";
            Query<Utilisateur> queryUser = session.createQuery(hqlUser, Utilisateur.class);
            queryUser.setParameter("discordId", discordId);
            Utilisateur utilisateur = queryUser.uniqueResult();

            if (utilisateur == null) {
                transaction.rollback();
                return;
            }

            String hqlPersonnage = "FROM Personnage WHERE utilisateur.discordId = :discordId";
            Query<Personnage> queryPersonnage = session.createQuery(hqlPersonnage, Personnage.class);
            queryPersonnage.setParameter("discordId", discordId);
            Personnage personnage = queryPersonnage.uniqueResult();

            if (personnage == null) {
                personnage = new Personnage();
                personnage.setUtilisateur(utilisateur);
                session.save(personnage);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void commencerJeu(String discordId) {
        Personnage personnage = getPersonnage(discordId);
        if (personnage == null) {
            ajouterPersonnage(discordId);
        }
    }

    public void deplacerEtOuvrirCoffre(String discordId, String direction) {
        Personnage personnage = getPersonnage(discordId);
        if (personnage != null) {
            personnage.deplacer(direction);
            String loot = personnage.ouvrirCoffre();
            System.out.println(loot);
        }
    }
}
