package ch.vorburger.minecraft.logo

import java.util.Deque
import java.util.LinkedList
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spongepowered.api.block.BlockSnapshot
import org.spongepowered.api.command.source.LocatedSource

class UndoableTurtle extends TurtleImpl { // NOTE: Must extend, cannot use *Delegate, because the crux here is the override set()
    private static Logger logger = LoggerFactory.getLogger(UndoableTurtle);
    
    val Deque<BlockSnapshot> undoHistory = new LinkedList
    
    new(LocatedSource source) { super(source) }
    
    override set() {
        undoHistory.addLast(location.createSnapshot)
        super.set
    }
    
    def undo() {
        for(var block = undoHistory.pollLast; block != null; block = undoHistory.pollLast) {
            if (!block.restore(true, true))
                logger.warn("Could not undo Block: " + block)
            else
                if (logging)
                    logger.info("Turtle undo block.restore(): " + block)
        }
    } 
    
}