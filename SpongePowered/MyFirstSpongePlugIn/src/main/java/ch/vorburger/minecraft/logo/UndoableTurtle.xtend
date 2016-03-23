package ch.vorburger.minecraft.logo

import java.util.Deque
import java.util.LinkedList
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spongepowered.api.block.BlockSnapshot
import org.spongepowered.api.command.source.LocatedSource
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import org.spongepowered.api.util.Direction
import org.spongepowered.api.block.BlockType

class UndoableTurtle extends TurtleImpl { // NOTE: Must extend, cannot use *Delegate, because the crux here is the override set()
    private static Logger logger = LoggerFactory.getLogger(UndoableTurtle);
    
    val Deque<BlockSnapshot> undoHistory = new LinkedList
    
    new(LocatedSource source) { super(source) }
    
    new(Location<World> location, Direction direction, BlockType type, boolean isSettingBlockOnMove) {
        super(location, direction, type, isSettingBlockOnMove)
    }
    
    override set() {
        undoHistory.addLast(location.createSnapshot)
        super.set
    }
    
    def undo() {
        for(var block = undoHistory.pollLast; block != null; block = undoHistory.pollLast) {
            if (!block.restore(true, true))
                logger.warn("Could not undo Block, restore() API returned false, for Block: " + block)
        }
    } 
    
}