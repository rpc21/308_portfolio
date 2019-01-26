package example;

import javafx.scene.image.Image;

/**
 * I included this code as a concrete example of a subclass of my GenericBrick abstract superclass.  The
 * TwoHitBrick is an interesting use case of the getReplacementBrick() method in which a new OneHitBrick is returned
 * in the TwoHitBrick's place when the bouncer hits the TwoHitBrick.
 */
/**
 * Brick that takes two hits to destroy and is worth 10 points for every collision
 */
public class TwoHitBrick extends GenericBrick {

    private static final String TWO_HIT_BRICK_IMAGE = "brick2.gif";
    private static final int TWO_HIT_BRICK_POINT_VALUE= 10;

    private int myPointValue;

    /**
     * TwoHitBrick constructor
     * @param xPos x position of the top left corner of the image
     * @param yPos y position of the top left corner of the image
     * @param brickLength length of the brick to be generated
     */
    public TwoHitBrick(double xPos, double yPos, double brickLength){
        super(xPos, yPos, brickLength, new Image(TWO_HIT_BRICK_IMAGE));
        myPointValue = TWO_HIT_BRICK_POINT_VALUE;
    }

    /**
     * TwoHitBrick constructor that is useful for handling a collision with a ThreeHitBrick
     * @param brick
     */
    public TwoHitBrick(GenericBrick brick){
        super(brick.getX(),brick.getY(),brick.getFitWidth(),new Image(TWO_HIT_BRICK_IMAGE));
        myPointValue = TWO_HIT_BRICK_POINT_VALUE;
    }

    /**
     * Getter for the point value of the brick
     * @return myPointValue
     */
    @Override
    public int getMyPointValue() {
        return myPointValue;
    }

    /**
     * When TwoHitBricks are hit they turn into OneHitBricks
     * @return new OneHitBrick to be added to the scene in place of the TwoHitBrick
     */
    @Override
    public GenericBrick getReplacementBrick() {
        return new OneHitBrick(this);
    }

}
