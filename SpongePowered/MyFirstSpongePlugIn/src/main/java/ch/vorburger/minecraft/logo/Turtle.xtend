package ch.vorburger.minecraft.logo

import org.spongepowered.api.block.BlockType
import org.spongepowered.api.util.Direction
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

interface Turtle {

    def Location<World> fwd()

    def Location<World> back()

    def void rt()

    def void lt()

    def Location<World> up()

    def Location<World> down()

    def void setBlockType(BlockType blockType)

    def void set()

    def void remove()

    def void setBlockOnMove()

    def void noSetBlockOnMove()

    def Location<World> getLocation()

    def Direction getDirection()

    def BlockType getBlockType()

    def TurtleSnapshot createSnapshot()

}
