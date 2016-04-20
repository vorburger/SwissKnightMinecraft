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


    @Command def line(LocatedSource source) {
        turtle = new UndoableTurtle(source)
        10.times[ fwd ]
    }

    @Command def square(LocatedSource source) {
        turtle = new UndoableTurtle(source)
        4.times[
            7.times[ fwd ] 
            rt
        ]
    }












































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
    
    def house_() {
        val originalBlockType = blockType
        setBlockOnMove
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
        
        //blockType = BlockTypes.WOODEN_DOOR
        //set
        blockType = originalBlockType
        noSetBlockOnMove
    }
    
    def houses() {
        house_
        10.times[fwd]
        house_
        rt
        8.times[fwd]
        house_                
    }




    def cube(int size) { 
        box(size, size, size)
    }

    @Command def houses(LocatedSource source) {
        turtle = new UndoableTurtle(source)
        config.configuration.turtleSnapshot = turtle.createSnapshot
        timed("/houses", [ redrawLast() ])
    }
    override redrawLast() {
        houses()
    }
    
}
