package sample;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

/**
 * Model class to hold Wall information
 */
public class Wall extends Rectangle implements Serializable {
    private static final long serialVersionUID = 42L;
    private final int width = Constants.width;
    private final int height = Constants.height;
    private final int rows = Constants.rows;
    private double xc;
    private double yc;
    private double h;

    /**
     * Constructor for Wall class
     *
     * @param xc <code> Double </code>
     * @param yc <code> Double </code>
     * @param h <code> Double </code>
     */
    public Wall(double xc, double yc, double h) {
        super(0, 0, 1, h);
        this.xc = xc;
        this.yc = yc;
        this.setFill(Color.WHITE);
        this.h = h;
        this.setTranslateX(xc);
        this.setTranslateY(yc);
    }

    /**
     * Store function to prepare for serialization
     */
    public void store() {
        xc = getTranslateX();
        yc = getTranslateY();
    }

    /**
     * Restore function for deserialization
     */
    public void restore() {
        setTranslateX(xc);
        setTranslateY(yc);
        setWidth(1);
        setHeight(h);
        setFill(Color.WHITE);
    }
}
