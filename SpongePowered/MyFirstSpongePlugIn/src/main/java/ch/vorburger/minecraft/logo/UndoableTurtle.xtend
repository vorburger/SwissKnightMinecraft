package ch.vorburger.minecraft.logo

import java.util.LinkedList
import java.util.Queue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spongepowered.api.block.BlockSnapshot
import org.spongepowered.api.util.command.source.LocatedSource

class UndoableTurtle extends TurtleImpl { // NOTE: Must extend, cannot use *Delegate, because the crux here is the override set()
    private static Logger logger = LoggerFactory.getLogger(UndoableTurtle);
    
    val Queue<BlockSnapshot> undoHistory = new LinkedList
    
    new(LocatedSource source) { super(source) }
    
    override set() {
        undoHistory.add(super.location.createSnapshot)
        super.set
    }
    
    def undo() {
        var block = undoHistory.poll
        while (block != null) {
            if (!block.restore(true, true))
                logger.warn("Could not undo Block: " + block /* .location */)
            block = undoHistory.poll
        }
    } 
    
}