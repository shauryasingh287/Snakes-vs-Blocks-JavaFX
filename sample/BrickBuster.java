package sample;


import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

/**
 * Model class to hold Brickbuster information
 */
public class BrickBuster extends Token{
    private final int width = Constants.width;
    private final int height = Constants.height;
    private final int rows = Constants.rows;

    /**
     * Constructor for BrickBuster class
     * @param x <code> Double </code>
     * @param y <code> Double </code>
     */
    public BrickBuster(double x, double y) {
        super(x, y, 3);
        Image img = new Image("sample/Brick_block.png");
        this.setFill(new ImagePattern(img));
    }
}
