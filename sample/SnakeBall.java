package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.Serializable;

/**
 * Model class to hold Snakeball information
 */
public class SnakeBall extends Circle implements Serializable {
    private static final long serialVersionUID = 42L;
    private final int width = Constants.width;
    private final int height = Constants.height;
    private final int rows = Constants.rows;
    double xc, yc;

    /**
     * Constructor for SnakeBall class
     *
     * @param xc <code> Double </code>
     * @param yc <code> Double </code>
     */
    public SnakeBall(double xc, double yc) {
        super(0, 0, 5, Color.ORANGE);
        this.xc = xc;
        this.yc = yc;
        this.setTranslateX(xc);
        this.setTranslateY(yc);
    }

    /**
     * Sets fill color for Snakeballs
     *
     * @param color <code> Color</code>
     */
    public void setFill(Color color) {
        super.setFill(color);
    }

    /**
     * Function to move Snakeball right
     * @param dist <code> Double </code>
     */
    public void moveRight(double dist) {
        this.setTranslateX(Math.min(width, this.getTranslateX() + dist));
    }

    /**
     * Function to move Snakeball left
     * @param dist <code> Double </code>
     */
    public void moveLeft(double dist) {
        this.setTranslateX(Math.max(0, this.getTranslateX() - dist));
    }

    /**
     * Function to move Snakeball to certain location
     * @param v <code> Double </code>
     */
    public void moveTo(double v) {
        this.setTranslateX(Math.min(Math.max(0, v), width));
    }
}
