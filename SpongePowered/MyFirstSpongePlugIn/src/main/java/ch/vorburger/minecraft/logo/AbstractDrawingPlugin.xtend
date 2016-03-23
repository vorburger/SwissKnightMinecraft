package ch.vorburger.minecraft.logo

import ch.vorburger.minecraft.command.AbstractPluginWithCommands
import ch.vorburger.minecraft.command.Command
import ch.vorburger.minecraft.config.ConfigurationPersister
import javax.inject.Inject
import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0
import org.spongepowered.api.command.source.LocatedSource

/**
 * Plugin with special support for "live" drawings, incl. /undo.
 * @author Michael Vorburger
 */
abstract class AbstractDrawingPlugin extends AbstractPluginWithCommands {
    
    // TODO Turtle should be @Inject on outside call of @Command method (which can also be called inside), and no explicit LocatedSource argument; @Inject of helper classes
    // TODO Support general /undo of last command; there should be no need for a def undo() here (and the turtle should be of type Turtle and not UndoableTurtle)
    // Also an /undo here won't work as soon as there are going to be 2 such Plugins... TBD!
    // TODO Think through multi-user usage properly.. Configuration is not doing multi-user right.

    @Inject protected ConfigurationPersister<Configuration> config
    /* @Inject */ protected var extension UndoableTurtle turtle

    // LIVE is Life (Opus, e.g. https://www.youtube.com/watch?v=EGikhmjTSZI)
    // TODO The LIVE support should, eventually, be outside of this plugin, as a "mode" re-running the last entered command
    
    override onServerStopping() {
        if (isHotReloading()) {
            turtle?.undo
            config.save
        }
    }

    override onServerStarting() {
        if (isHotReloading) {
        turtle = config?.load(Configuration)?.turtleSnapshot?.restore
            if (turtle != null)
                redrawLast
        }
    }
    // TODO cannot hardcode house here, obviously.. so how to do this better instead of this silly redrawLast()?
    def abstract void redrawLast()
    
    // TODO this should happen automatically.. the @Command could add dyn. wrap execution into this - unless Sponge ALREADY has stats for timing commands, see https://docs.spongepowered.org/en/server/spongineer/commands.html ?
    def timed(String name, Procedure0 p) {
        val t = System.currentTimeMillis
        p.apply
        var d = System.currentTimeMillis - t
        System.out.println('''«name» took: «d»ms''')
    }
    
    @Command def undo(LocatedSource source) {
        timed("/undo", [ turtle?.undo turtle = null ])
    }
    
    @ConfigSerializable static public class Configuration {
        @Setting public TurtleSnapshot turtleSnapshot
        @Setting public String tempTest = "hello, world"
    } 
    
}