package ch.vorburger.minecraft.michaelpapa7

import ch.vorburger.minecraft.command.Command
import ch.vorburger.minecraft.logo.AbstractDrawingPlugin
import ch.vorburger.minecraft.logo.UndoableTurtle
import org.spongepowered.api.command.source.LocatedSource
import org.spongepowered.api.plugin.Plugin

import static extension ch.vorburger.xtend.IntegerExtensions2.*

@Plugin(id="michaelpapa7", name="michaelpapa7", version="1.0")
class MichaelPapa7FirstPlugin extends AbstractDrawingPlugin {

    def line(int length) { 
        (length-1).times[fwd] (length-1).times[back]
    }

    def plank(int side1, int side2) {
        side1.times([line(side2)], [nudge])
        lt() (side1-1).times[fwd] rt // return to start
    }

    def nudge() { 
        rt fwd lt
    }

    def wall(int length, int height) { 
        height.times([ line(length)], [ up ]) 
        height.times[down]
    }
   
    def box(int side1, int side2, int height) { 
        plank(side1, side2)
        wall(side2, height) rt up
        wall(side1, height)
         
        line(side1) lt 
        wall(side1, height) line(side1) lt 
//        wall(side2, height) line(side2) lt line(side1) rt rt height.times[ up ]
//        plank(side1, side2)
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
        house_(10)
    }
    
    def house_(int size) {
        box(3, 4, 5)
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
    
}
