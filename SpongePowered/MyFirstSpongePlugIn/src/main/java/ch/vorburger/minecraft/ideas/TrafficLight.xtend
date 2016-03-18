package ch.vorburger.minecraft.ideas

import ch.vorburger.minecraft.logo.Turtle
import javax.inject.Inject
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

import static extension ch.vorburger.xtend.IntegerExtensions2.*

class TrafficLight {
    
    @Inject Turtle t
    
    Location<World> light
    
    def draw() {
        t => [
            blockType = BlockTypes.COAL_BLOCK
            3.times[up]
            blockType = BlockTypes.GLASS 
            light = up
        ]
    }
    
    def onClick() {
        if (light.blockType == BlockTypes.GLASS)
            light.blockType = BlockTypes.GLOWSTONE
        else
            light.blockType = BlockTypes.GLASS
    }
    
}