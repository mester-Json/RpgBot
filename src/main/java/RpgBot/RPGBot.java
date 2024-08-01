package RpgBot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class RPGBot extends ListenerAdapter {

    private static final String TOKEN = "";
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

        String messageContent = event.getMessage().getContentRaw().trim();
        String prefix = "!";

        if (messageContent.startsWith(prefix)) {
            String[] command = messageContent.substring(prefix.length()).split(" ", 2);
            if (command.length == 0) {
                event.getChannel().sendMessage("Commande invalide.").queue();
                return;
            }

            String commandName = command[0].toLowerCase();
            String discordId = String.valueOf(event.getAuthor().getIdLong());

            switch (commandName) {
                case "start":
                    jeu.commencerJeu(discordId);
                    event.getChannel().sendMessage("Bienvenue dans le jeu, " + event.getAuthor().getName() + "!").queue();
                    break;

                case "move":
                    if (command.length > 1) {
                        Personnage personnage = jeu.getPersonnage(discordId);
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

                case "coffre":
                    Personnage personnage = jeu.getPersonnage(discordId);
                    if (personnage != null) {
                        if (personnage.peutOuvrirCoffre()) {
                            String lootMessage = personnage.ouvrirCoffre();
                            event.getChannel().sendMessage(lootMessage).queue();
                        } else {
                            event.getChannel().sendMessage("Vous devez attendre avant de pouvoir ouvrir un autre coffre.").queue();
                        }
                    } else {
                        event.getChannel().sendMessage("Vous devez d'abord commencer le jeu avec !start").queue();
                    }
                    break;

                default:
                    event.getChannel().sendMessage("Commande invalide.").queue();
                    break;
            }
        }
    }
}
