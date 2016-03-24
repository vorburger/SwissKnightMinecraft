package ch.vorburger.minecraft.logo

import org.spongepowered.api.block.BlockType
import org.spongepowered.api.util.Direction
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

/** 
 * Minecraft Turtle API.
 * 
 * Turtles, contrary to Players:<ul>
 *  <li>never move diagonally;
 *  <li>never "hit" obstacles, or "fall" lower due to gravity - they just move, like a.. bulldozer, in thin air, and "into" anything;
 *  <li>hold one kind of Block, like a Player's current one, by default the one of the Player who created the Turtle, and can then change that to any kind; 
 *  <li>place their current Block (like right-click) by default, but have have their "pen up" to move around without drawing;
 * </ul>
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Logo_(programming_language)">Logo (programming language) on Wikipedia</a>
 * @see <a href="http://computercraft.info/wiki/Turtle_(API)">Computer Craft Turtle API</a>
 *
 * @author Michael Vorburger
 */
interface Turtle {

    /** 
     * Move forward; like pressing "W" key.
     * Also places block, like right-clicking and pressing "W", if 'pen is down'.
     */
    def Location<World> fwd()

    /** 
     * Move backward; like pressing "S" key. 
     * Also places block, like right-clicking and pressing "W", if 'pen is down'.
     */
    def Location<World> back()

    /** Moves upward; like pressing "Space" key. Also places block, if 'pen is down'. */
    def Location<World> up()

    /** Moves downward; like pressing "Shift" key. Also places block, if 'pen is down'. */
    def Location<World> down()

    /** Turn sharp right; like moving mouse. Never places block. */
    def void rt()

    /** Turn sharp left; like moving mouse. Never places block. */
    def void lt()

    /** Change the kind of Block which the Turtle currently holds. Use BlockTypes... to find new types. */
    def void setBlockType(BlockType blockType)

    /** Breaks block in current place, like left-clicking. */
    // TODO rename this to "break" ?
    def void remove()

    /** Lifts the Turtle's "pen up", and it won't automatically place blocks anymore. */
    // TODO rename this to "pu" ?
    def void noSetBlockOnMove()

    /** Put the Turtle's "pen down" again. */
    // TODO rename this to "pd" ?
    def void setBlockOnMove()

    /**
     * Places block, if 'pen is up'.
     * Has no effect if pen is already down, because then there normally already is a block where the Turtle is, from its previous movement. 
     */
    def void set()

    def Location<World> getLocation()

    def Direction getDirection()

    def BlockType getBlockType()

    def TurtleSnapshot createSnapshot()

}
