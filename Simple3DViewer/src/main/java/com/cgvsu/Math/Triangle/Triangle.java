package com.cgvsu.Math.Triangle;

import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import com.cgvsu.Rasterization.TriangleRasterization;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Triangle {
    private static final Color DEFAULT_COLOR_1 = Color.RED;
    private static final Color DEFAULT_COLOR_2 = Color.BLUE;
    private static final Color DEFAULT_COLOR_3 = Color.LIME;
    private final TwoDimensionalVector v1;
    private final TwoDimensionalVector v2;
    private final TwoDimensionalVector v3;

    private Color c1 = DEFAULT_COLOR_1;
    private Color c2 = DEFAULT_COLOR_2;
    private Color c3 = DEFAULT_COLOR_3;

    public Triangle( TwoDimensionalVector v1, TwoDimensionalVector v2, TwoDimensionalVector v3, Color c1,  Color c2, Color c3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;

        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

    public Triangle(final TwoDimensionalVector v1, final TwoDimensionalVector v2, final TwoDimensionalVector v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public Triangle(final int x1, final int y1, final int x2, final int y2, final int x3, final int y3) {
        this(new TwoDimensionalVector(x1, y1), new TwoDimensionalVector(x2, y2), new TwoDimensionalVector(x3, y3));
    }

    public void drawTriangle(GraphicsContext graphicsContext){
        TriangleRasterization.drawTriangle(graphicsContext, this);
    }



    public void setDefaultColors(){
        c1 = DEFAULT_COLOR_1;
        c2 = DEFAULT_COLOR_2;
        c3 = DEFAULT_COLOR_3;
    }

    public TwoDimensionalVector v1() {
        return v1;
    }

    public TwoDimensionalVector v2() {
        return v2;
    }

    public TwoDimensionalVector v3() {
        return v3;
    }

    public Color c1() {
        return c1;
    }

    public Color c2() {
        return c2;
    }

    public Color c3() {
        return c3;
    }
}
