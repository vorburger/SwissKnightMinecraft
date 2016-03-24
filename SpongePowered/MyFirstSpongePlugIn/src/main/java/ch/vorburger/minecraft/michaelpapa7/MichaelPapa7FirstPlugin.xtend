package ch.vorburger.minecraft.michaelpapa7

import ch.vorburger.minecraft.command.Command
import ch.vorburger.minecraft.logo.AbstractDrawingPlugin
import ch.vorburger.minecraft.logo.UndoableTurtle
import org.spongepowered.api.command.source.LocatedSource
import org.spongepowered.api.plugin.Plugin

import static extension ch.vorburger.xtend.IntegerExtensions2.*
import org.spongepowered.api.block.BlockTypes

@Plugin(id="michaelpapa7", name="michaelpapa7", version="1.0")
class MichaelPapa7FirstPlugin extends AbstractDrawingPlugin {

    def line(int length) { 
        (length-1).times[fwd] (length-1).times[back]
    }

    def plank(int side1, int side2) {
        side1.times([line(side2)], [rt fwd lt])
        lt() (side1-1).times[fwd] rt // return to start
    }

    def wall(int length, int height) { 
        height.times([ line(length)], [ up ]) 
        (length-1).times[fwd]
        (height-1).times[down]
    }
   
    def box(int side1, int side2, int height) { 
        plank(side1, side2)
        wall(side2, height) rt
        wall(side1, height) rt
        wall(side2, height) rt
        wall(side1, height) rt()
        (height-1).times[up]
        plank(side1, side2)
    }
    
    def cube(int size) { 
        box(size, size, size)
    }

    @Command def house(LocatedSource source) {
        turtle = new UndoableTurtle(source)
        config.configuration.turtleSnapshot = turtle.createSnapshot
        timed("/house", [ redrawLast() ])
    }
    override redrawLast() {
        house_()
    }
    
    def house_() {
        box(7, 6, 5)

        noSetBlockOnMove
        fwd
        rt
        fwd
        blockType = BlockTypes.GLASS
        set  

        back
        lt
        2.times[down]
        remove
        down
        remove
        
        blockType = BlockTypes.WOODEN_DOOR
        set
    }
    
    def village() {
        // TODO random house or skyscraper, scattered around randomly
    }
    
}
