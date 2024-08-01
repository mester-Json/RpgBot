package RpgBot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class RPGBot extends ListenerAdapter {

    private static final String TOKEN = "Api Bot ";
    private Jeu jeu = new Jeu();

    public static void main(String[] args) throws Exception {
        JDABuilder builder = JDABuilder.createDefault(TOKEN)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS);
        builder.addEventListeners(new RPGBot());
        builder.build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        String[] command = event.getMessage().getContentRaw().split(" ", 2);
        long utilisateurId = Long.parseLong(event.getAuthor().getId());

        if (command.length > 0) {
            switch (command[0].toLowerCase()) {
                case "!start":
                    jeu.ajouterPersonnage(utilisateurId);
                    event.getChannel().sendMessage("Bienvenue dans le jeu, " + event.getAuthor().getName() + "!").queue();
                    break;

                case "!move":
                    if (command.length > 1) {
                        Personnage personnage = jeu.getPersonnage(utilisateurId);
                        if (personnage != null) {
                            personnage.deplacer(command[1]);
                            event.getChannel().sendMessage("Vous êtes maintenant en position (" + personnage.getPositionX() + ", " + personnage.getPositionY() + ")").queue();
                        } else {
                            event.getChannel().sendMessage("Vous devez d'abord commencer le jeu avec !start").queue();
                        }
                    } else {
                        event.getChannel().sendMessage("Veuillez spécifier une direction pour le déplacement.").queue();
                    }
                    break;

                case "!coffre":
                    Personnage personnage = jeu.getPersonnage(utilisateurId);
                    if (personnage != null) {
                        if (personnage.peutOuvrirCoffre()) {
                            String lootMessage = personnage.ouvrirCoffre();
                            event.getChannel().sendMessage(lootMessage).queue();
                        } else {
                            event.getChannel().sendMessage("Vous devez attendre 24 heures pour ouvrir un autre coffre.").queue();
                        }
                    } else {
                        event.getChannel().sendMessage("Vous devez d'abord commencer le jeu avec !start").queue();
                    }
                    break;

                default:
                    event.getChannel().sendMessage("Commande non reconnue. Utilisez !start, !move ou !coffre.").queue();
                    break;
            }
        } else {
            event.getChannel().sendMessage("Aucune commande spécifiée.").queue();
        }
    }
}
