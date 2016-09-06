package ch.vorburger.minecraft.learning;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

import ch.vorburger.learning.LearningService;
import ch.vorburger.learning.LearningServiceException;
import ch.vorburger.learning.ServiceQuestion;
import ch.vorburger.learning.UserID;
import ch.vorburger.learning.server.LearningServiceImpl;

public class QuestionCommand implements CommandExecutor {

	LearningService learning = new LearningServiceImpl();
	
	@Override
	public CommandResult execute(@Nullable CommandSource src, @Nullable CommandContext args) throws CommandException {
		if (src != null && args != null) { // TODO remove when https://bugs.eclipse.org/bugs/show_bug.cgi?id=500885 is fixed
	        Collection<Player> players = args.<Player>getAll("player");
	        if (players.isEmpty()) {
				// If there were no players listed, then the Player sending the
				// command wants to get himself get a question asked, so:
				if (src instanceof Player) {
					Player player = (Player) src;
					players = Collections.singleton(player);
				}
	        }
	        for (Player player : players) {
	        	UserID uid = new UserID("minecraft", player.getUniqueId().toString());
	        	try {
					ServiceQuestion q = learning.newQuestion(uid);
					// TODO support q.getChoices()
					LiteralText qText = Text.of(q.getText());
					// TODO this will still send it from the player who did /question, instead of a Professor..
					//   player.sendMessage(qText);
					// Human professor = LearningPlugin.staticProfessor;
					// This could send it from any source, just need to create a Professor Human in LearningPlugin
					// player.getMessageChannel().send(src, qText);
					MessageChannel.fixed(player).send(src /*professor*/, qText);
					// TODO build a custom MessageChannel and setChannel, so that replied typed go straight there
					// https://docs.spongepowered.org/master/en/plugin/text/messagechannels.html
				} catch (LearningServiceException e) {
					throw new CommandException(Text.of("Failed to obtain new question for Player " + player.getName()), e);
				}
			}
		}
		return CommandResult.empty();
	}

}
