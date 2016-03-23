package ch.vorburger.minecraft.logo

import ch.vorburger.minecraft.command.AbstractPluginWithCommands
import ch.vorburger.minecraft.command.Command
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0
import org.spongepowered.api.command.source.LocatedSource

abstract class AbstractDrawingPlugin extends AbstractPluginWithCommands {
    
    // TODO Turtle should be @Inject on outside call of @Command method (which can also be called inside), and no explicit LocatedSource argument; @Inject of helper classes
    // TODO Support general /undo of last command; there should be no need for a def undo() here (and the turtle should be of type Turtle and not UndoableTurtle)
    // Also an /undo here won't work as soon as there are going to be 2 such Plugins... TBD!

    /* @Inject */ protected var extension UndoableTurtle turtle

    // LIVE is Life (Opus, e.g. https://www.youtube.com/watch?v=EGikhmjTSZI)
    // TODO The LIVE support should, eventually, be outside of this plugin, as a "mode" re-running the last entered command
    
    override onServerStopping() {
        turtle?.undo
    }

    override onServerStarting() {
        // TODO redraw if undo on shutdown, but need to persist flag, and last start position..
    }
    
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
    
    
}