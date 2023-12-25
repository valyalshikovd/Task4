package com.cgvsu.Math.Figure;

import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Triangle {
    private static final Color DEFAULT_COLOR_1 = Color.RED;
    private static final Color DEFAULT_COLOR_2 = Color.LIME;
    private static final Color DEFAULT_COLOR_3 = Color.BLUE;

    private static final Random rand = ThreadLocalRandom.current();

    public TwoDimensionalVector v1;
    public TwoDimensionalVector v2;
    public TwoDimensionalVector v3;
    public Color c1 = DEFAULT_COLOR_1;
    public Color c2 = DEFAULT_COLOR_2;
    public Color c3 = DEFAULT_COLOR_3;

    public Triangle(TwoDimensionalVector v1, TwoDimensionalVector v2, TwoDimensionalVector v3, Color c1, Color c2, Color c3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

    public void randomizeVertices(float maxWidth, float maxHeight) {
        float x1 = rand.nextFloat(maxWidth);
        float y1 = rand.nextFloat(maxHeight);
        float x2 = rand.nextFloat(maxWidth);
        float y2 = rand.nextFloat(maxHeight);
        float x3 = rand.nextFloat(maxWidth);
        float y3 = rand.nextFloat(maxHeight);
        v1 = new TwoDimensionalVector(x1, y1);
        v2 = new TwoDimensionalVector(x2, y2);
        v3 = new TwoDimensionalVector(x3, y3);
    }

    public void resetColors() {
        c1 = DEFAULT_COLOR_1;
        c2 = DEFAULT_COLOR_2;
        c3 = DEFAULT_COLOR_3;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "v1=" + v1 +
                ", v2=" + v2 +
                ", v3=" + v3 +
                ", c1=" + c1 +
                ", c2=" + c2 +
                ", c3=" + c3 +
                '}';
    }
}
