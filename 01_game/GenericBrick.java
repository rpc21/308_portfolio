package example;

import javafx.scene.image.Image;
/**
 * This code establishes an abstract GenericBrick from which new kinds of bricks can extend.  This code demonstrates
 * how I am leveraging polymorphism and inheritance in my implementation.  This abstract class outlines the methods
 * that all kinds of bricks (its subclasses) should have an implementation for.  For example, all bricks need to what
 * to do in response to a collision with the bouncer.  This behavior should be implemented in the getReplacementBrick()
 * method.  This abstract makes the bricks more extendable and makes it easy to add new brick types to the game.
 */

/**
 * Abstract class that provides the basic methods for the various brick types. Constructs a brick based on its
 * location as specified by top left corner, and an image to represent brick. Can handle what happens to itself when
 * it collides with a ball in terms of whether it is replaced or removed. Has a point value associated with a
 * collision between the brick and the bouncer
 * Subclasses: OneHitBrick, TwoHitBrick, ThreeHitBrick, DangerBrick, PermanentBrick
 * Class can be extended by specifying a new image for the brick, a point value for the brick
 * and how it reacts to a collision with the bouncer. The frequency for which the brick occurs in various difficulty
 * modes needs to be specified in the GameDifficulty class.  The other bricks' frequency's will also be updated.
 */
public abstract class GenericBrick extends Sprite {

    public static final int GENERIC_BRICK_POINT_VALUE = 10;
    public static final String ONE_HIT_BRICK_IMAGE = "brick1.gif";

    private int myPointValue;

    /**
     * GenericBrick default constructor, constructs a brick with the ONE_HIT_BRICK_IMAGE
     */
    public GenericBrick(){
        super(new Image(ONE_HIT_BRICK_IMAGE));
        myPointValue = GENERIC_BRICK_POINT_VALUE;
    }

    /**
     * GenericBrick constructor
     * @param xPos x position of top left corner
     * @param yPos y position of top left corner
     * @param brickLength length of the brick to generate
     * @param image image that will display for the brick
     */
    public GenericBrick(double xPos, double yPos, double brickLength, Image image){
        super(image);
        setFitWidth(brickLength);
        setX(xPos);
        setY(yPos);
        myPointValue = GENERIC_BRICK_POINT_VALUE;
    }

    /**
     * Getter for brick's point value
     * @return myPointValue
     */
    public int getMyPointValue() {
        return myPointValue;
    }

    /**
     * Generates the brick meant to take the spot of the brick if it is involved in a collision, default is to remove
     * @return null if brick is to be removed from the scene, otherwise return replacement brick
     */
    public GenericBrick getReplacementBrick(){
        return null;
    }

    /**
     * @return whether or not a collision with this brick costs the player a life - default is false
     */
    public boolean costsLife(){
        return false;
    }
}
