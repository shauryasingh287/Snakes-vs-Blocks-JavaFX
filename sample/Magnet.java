package sample;


import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

/**
 * Model class to hold Magnet information
 */
public class Magnet extends Token{
    private final int width = Constants.width;
    private final int height = Constants.height;
    private final int rows = Constants.rows;

    /**
     * Constructor for Magnet class
     *
     * @param x <code> Double </code>
     * @param y <code> Double </code>
     */
    public Magnet(double x, double y) {
        super(x, y, 2);
        Image img  = new Image("sample/magnet.png");
        this.setFill(new ImagePattern(img));
    }
}
