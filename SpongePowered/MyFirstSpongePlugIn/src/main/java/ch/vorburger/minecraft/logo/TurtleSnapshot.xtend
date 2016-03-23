package ch.vorburger.minecraft.logo

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.EqualsHashCode
import org.eclipse.xtend.lib.annotations.ToString
import org.spongepowered.api.block.BlockType
import org.spongepowered.api.util.Direction
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

// TODO Xtend AA @Bean, auto. compound of @ToString, @Accessors, @EqualsHashCode   
@ToString
@Accessors
@EqualsHashCode
// TODO ConfigurationPersister should not require @ConfigSerializable & @Setting but just serialize everything
@ConfigSerializable
class TurtleSnapshot {
    
    // TODO This probably should instead better be class Position { Vector3d position /* from Location getPosition() */, together with its World Extent
    @Setting Location<World> location

    @Setting Direction direction
   
    @Setting BlockType blockType
    
    @Setting boolean isSettingBlockOnMove
    
    new() { // MANDATORY for configurate @ConfigSerializable
    }
    
    // TODO Xtend AA @BeanConstructor
    new(Location<World> location, Direction direction, BlockType blockType, boolean isSettingBlockOnMove) {
        this.location = location
        this.direction = direction
        this.blockType = blockType
        this.isSettingBlockOnMove = isSettingBlockOnMove;
    }
    
    def UndoableTurtle restore() {
        new UndoableTurtle(location, direction, blockType, isSettingBlockOnMove)
    }
    
}