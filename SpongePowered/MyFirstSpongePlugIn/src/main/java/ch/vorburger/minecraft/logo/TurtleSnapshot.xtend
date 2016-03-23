package ch.vorburger.minecraft.logo

import org.eclipse.xtend.lib.annotations.Data
import org.spongepowered.api.block.BlockType
import org.spongepowered.api.util.Direction
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

@Data
class TurtleSnapshot {
    
    Location<World> location

    Direction direction
   
    BlockType blockType
    
    boolean isSettingBlockOnMove
    
    def UndoableTurtle restore() {
        new UndoableTurtle(location, direction, blockType, isSettingBlockOnMove)
    }
    
}