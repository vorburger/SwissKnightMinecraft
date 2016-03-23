package ch.vorburger.minecraft.logo

import java.util.NoSuchElementException
import java.util.Optional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spongepowered.api.block.BlockType
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.command.source.LocatedSource
import org.spongepowered.api.entity.Entity
import org.spongepowered.api.util.Direction
import org.spongepowered.api.util.Direction.Division
import org.spongepowered.api.util.blockray.BlockRay
import org.spongepowered.api.util.blockray.BlockRayHit
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import com.flowpowered.math.vector.Vector3d
import com.flowpowered.math.vector.Vector3i

/** 
 * Turtle.
 * @see <a href="https://en.wikipedia.org/wiki/Logo_(programming_language)">Logo (programming language) on Wikipedia</a>
 * @see <a href="http://computercraft.info/wiki/Turtle_(API)">Computer Craft Turtle API</a>
 *
 * @author Michael Vorburger
 */
class TurtleImpl implements Turtle {
	static Logger logger = LoggerFactory.getLogger(TurtleImpl)
	
	Location<World> location
	Direction direction
	BlockType blockType
	boolean isSettingBlockOnMove = true
	
	new(Entity player) {
		init(player) 
	}
	
	new(LocatedSource locatedSource) {
		if (locatedSource instanceof Entity) {
			init((locatedSource as Entity)) 
		} else {
			System.out.println("NaE so fixed direction") 
			this.location  = locatedSource.getLocation() 
			this.direction = Direction.SOUTH 
			this.blockType = BlockTypes.STONE 
		}
	}
	
	def private void init(Entity player) {
		this.location  = getStartingLocation(player) 
		this.direction = getDirection(player.getRotation()) 
		// TODO how to obtain Player's current Block? (+ Separate constructors for Entity & Player.) - Player.getItemInHand() => ItemStack.getItem().getBlockType()
		this.blockType = BlockTypes.STONE 
	}
	
	def private Location<World> getStartingLocation(Entity entity) {
		// TODO How to get the Location from where the Player is looking at?
		// https://bukkit.org/threads/tutorial-how-to-calculate-vectors.138849/ ?
		var Optional<BlockRayHit<World>> block = Optional.empty() 
		try {
			block = BlockRay.from(entity).filter(BlockRay.onlyAirFilter(), BlockRay.maxDistanceFilter(entity.getLocation().getPosition(), 100)).end() 
		} catch (NoSuchElementException e) {// Caused by: java.util.NoSuchElementException: Filter limit reached
			
		}
		if (block.isPresent()) {
			return block.get().getLocation() 
		} else {
			logger.warn("getStartingLocation: BlockRay hasn't found anything, return Player location") 
			return entity.getLocation() 
		}
		// TODO Forum post, this doesn't quite work, it's always "off" and too close... https://forums.spongepowered.org/t/how-to-get-the-location-the-player-is-looking-at-from-an-entity-location-and-rotation/8906
		// TODO Try this https://forums.spongepowered.org/t/jumppad-plugin/6244 ??
		// TODO double check impact of "BTW, right now SpongeAPI returns the rotations in a Vector3i with the mapping X -> yaw, Y -> pitch, Z -> roll. I'm changing that tomorrow to X -> pitch, Y -> yaw, Z -> roll to match flow-math (and the standard)." of https://forums.spongepowered.org/t/relative-teleportaion/7671/14
		//		Location location = entity.getLocation();
		//		Vector3d rotation = entity.getRotation();
		//        Vector3d direction = Quaterniond.fromAxesAnglesDeg(rotation.getY(), 360 - rotation.getX(), rotation.getZ()).getDirection();
		//        return new Location(location.getExtent(), location.getPosition().add(direction));
	}
	
	def private Direction getDirection(Vector3d rotation) {
		var Direction initialDirection = Direction.getClosestHorizontal(rotation, Division.CARDINAL) 
		if (initialDirection.equals(Direction.NONE)) {
			logger.warn("getDirection: Failed, cannot be NONE, so assuming NORTH") 
			return Direction.NORTH 
		} else {
			return initialDirection 
		}
	}
	
	// ---
	
	override void setBlockType(BlockType blockType) {
		this.blockType = blockType 
	}
	
	// ---
	
	override void setBlockOnMove() {
		isSettingBlockOnMove = true 
	}
	
	override void noSetBlockOnMove() {
		isSettingBlockOnMove = false 
	}
	
	// ---
	
	override void rt() {
		switch (direction) {
			case NORTH: direction = Direction.EAST
			case EAST:  direction = Direction.SOUTH
			case SOUTH: direction = Direction.WEST
			case WEST: 	direction = Direction.NORTH
			default: throw new IllegalStateException(direction.toString())
		}
		onChangeDirection(direction)
	}
	
	override void lt() {
		switch (direction) {
			case NORTH: direction = Direction.WEST
			case EAST:  direction = Direction.NORTH
			case SOUTH: direction = Direction.EAST
			case WEST:  direction = Direction.SOUTH
			default: throw new IllegalStateException(direction.toString())
		}
		onChangeDirection(direction) 
	}
	
	def protected void onChangeDirection(Direction newDirection) {
	}
	
	/** 
	 * W
	 */
	override Location<World> fwd() {
		return move(direction) 
	}

	/** 
	 * S 
	 */
	override Location<World> back() {
		return move(direction.getOpposite()) 
	}

	/** 
	 * Space 
	 */
	override Location<World> up() {
		return move(Direction.UP) 
	}

	/** 
	 * Shift 
	 */
	override Location<World> down() {
		return move(Direction.DOWN) 
	}

	def private Location<World> move(Direction directionToMove) {
		setBlockIfPenDown() 
		var Vector3i oldBlockPosition = location.getBlockPosition() 
		// TODO For performance, it would be better if Direction class had a toVector3i
		var Vector3i newBlockPosition = oldBlockPosition.add(directionToMove.toVector3d().toInt()) 
		location = new Location<World>(location.getExtent(),newBlockPosition) 
		onMove(location) 
		return location 
	}

	def protected void onMove(Location<World> newLocation) {
	}

	def private void setBlockIfPenDown() {
		if (isSettingBlockOnMove) set() 
	}

	// ---
	override void set() {
		// TODO Perf better? if (!location.getBlockType().equals(blockType))
		// TODO Perf with/without notifyNeighbors ?  Note also doin' this in UndoableTurtle
		// TODO Variant with notifyNeighbors ==> AbstractMethodError
		location.setBlockType(blockType/*, true */) 
	}

	override void remove() {
		location.removeBlock() 
	}

	override Location<World> getLocation() {
		return location 
	}
	
	override Direction getDirection() {
        return direction;
    }
	
	//	void interact() {
	//		// TODO missing Direction arg, req. in latest 2.1-SNAPSHOT ?
	//		location.interactBlock();
	//	}
	//	void dig() {
	//		location.digBlock();
	//	}
	
}