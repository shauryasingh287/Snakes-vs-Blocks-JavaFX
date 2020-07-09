package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Model class to hold Token information
 */
public class Token extends Circle implements Serializable {
    private static final long serialVersionUID = 42L;
    private final int width = Constants.width;
    private final int height = Constants.height;
    private final int rows = Constants.rows;
    private int tokenKind;
    private double xc;
    private double yc;
    private static transient ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(Color.AQUAMARINE, Color.DARKGREY, Color.CYAN, Color.CHOCOLATE));

    /**
     * Constructor for Token Class
     *
     * @param x <code> Double </code>
     * @param y <code> Double </code>
     * @param tokenKind <code> Integer </code>
     */
    Token(double x, double y, int tokenKind) {
        super(x, y, 8);
        xc = x;
        yc = y;
        this.setFill(colors.get(tokenKind-1));
        this.tokenKind = tokenKind;
    }

    /**
     * Store function to prepare for serialization
     */
    public void store(){
        xc = getTranslateX();
        yc = getTranslateY();
    }


    /**
     * Function to pull down tokens
     * @param dist <code> Double </code>
     */
    public void pull(double dist) {
        this.setTranslateY(this.getTranslateY() + dist);
    }

    /**
     * Getter for tokenKind
     * @return an <code> Integer </code> tokenKind
     */
    public int getTokenKind() {
        return tokenKind;
    }

    /**
     * Restore function for deserialization
     */
    public void restore() {
        setTranslateX(xc);
        setTranslateY(yc);
        this.setRadius(8);
        colors = new ArrayList<Color>(Arrays.asList(Color.AQUAMARINE, Color.DARKGREY, Color.CYAN, Color.CHOCOLATE));
        this.setFill(colors.get(tokenKind-1));
    }
}
