package ch.vorburger.minecraft.michaelpapa7

import ch.vorburger.minecraft.command.Command
import ch.vorburger.minecraft.logo.UndoableTurtle
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.command.source.LocatedSource

import static extension ch.vorburger.xtend.IntegerExtensions2.*
import org.spongepowered.api.event.game.state.GameStateEvent
import ch.vorburger.minecraft.command.AbstractPluginWithCommands

@Plugin(id="michaelpapa7", name="michaelpapa7", version="1.0")
class MichaelPapa7FirstPlugin extends AbstractPluginWithCommands {

    // TODO Turtle should be @Inject on outside call of @Command method (which can also be called inside), and no explicit LocatedSource argument; @Inject of helper classes
    // TODO Support general /undo of last command; there should be no need for a def undo() here (and the turtle should be of type Turtle and not UndoableTurtle)

    /* @Inject */ var extension UndoableTurtle turtle

    def line(int length) { length.times[fwd] length.times[back] }
    def plank(int side1, int side2) {
        side1.times[line(side2) nudge]
        lt side1.times[fwd] rt // return to start
    }
    def nudge() { rt fwd lt }

    def wall(int length, int height) { height.times[ line(length) up ] height.times[down] }
    def box(int side1, int side2, int height) { 
        plank(side1, side2)
        wall(side1, height) rt 
        wall(side2, height) line(side2) lt 
        wall(side1, height) line(side1) lt 
        wall(side2, height) line(side2) lt line(side1) rt rt height.times[ up ]
        plank(side1, side2)
    }
    def cube(int size) { box(size, size, size) }

    @Command def house(LocatedSource source) {
        turtle = new UndoableTurtle(source)
        timed("/house", [  house_(10) ])
    }
    
    def house_(int size) {
        wall(3,5)
        // box(size, size/2, 6)
        // TODO wall in the middle
        // TODO door!
        // TODO x2 window()
    }
    
    def window() {
        
    }
    
    // TODO def room(), with interconnecting interior door 
    
    def floor() {
        box(10, 10, 5)
    }

    def skyscraper(int size, int floors) {
        house_(size)
        floors.times[ floor() ]
    }
    
    def city() {
        // TODO random house or skyscraper, scattered around randomly
    }
     
    // TODO def roof()
    
    @Command def undo(LocatedSource source) {
        timed("/undo", [ turtle?.undo turtle = null ])
    }
    
    // LIVE is Life (Opus, e.g. https://www.youtube.com/watch?v=EGikhmjTSZI)
    // TODO The LIVE support should, eventually, be outside of this plugin, as a "mode" re-running the last entered command
    
    override onServerStopping() {
        turtle?.undo
    }

    override onServerStarting() {
        // TODO redraw if undo on shutdown, but need to persist flag, and last start position..
    }
    
    // TODO this should happen automatically.. unless Sponge ALREADY has stats for timing commands, see https://docs.spongepowered.org/en/server/spongineer/commands.html ?
    def timed(String name, Procedure0 p) {
        val t = System.currentTimeMillis
        p.apply
        var d = System.currentTimeMillis - t
        System.out.println('''«name» took: «d»ms''')
    }
}
