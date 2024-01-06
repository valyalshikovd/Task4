package com.cgvsu.Rasterization;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.model.Polygon;
import com.cgvsu.render_engine.Zbuffer;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import java.util.Arrays;
import java.util.Comparator;

public class Triangle implements  InterfaceTriangle {
    private static final Comparator<ThreeDimensionalVector> COMPARATOR = (a, b) -> {
        int cmp = Double.compare(a.getB(), b.getB());
        if (cmp != 0) {
            return cmp;
        } else return Double.compare(a.getA(), b.getA());
    };
    private final ThreeDimensionalVector v1;
    private final Color color;
    private final double lightCoefficient;
    private final int x1;
    private final int x2;
    private final int x3;
    private final int y1;
    private final int y2;
    private final int y3;

    public Triangle(Polygon polygon, double lightCoefficient)  {
        this.v1 = polygon.getResultPoints().get(0);
        ThreeDimensionalVector v2 = polygon.getResultPoints().get(1);
        ThreeDimensionalVector v3 = polygon.getResultPoints().get(2);
        this.color = polygon.getModel().fillingColor;
        this.lightCoefficient = lightCoefficient;
        ThreeDimensionalVector[] verts = new ThreeDimensionalVector[]{v1, v2, v3};
        Arrays.sort(verts, COMPARATOR);
        x1 = (int) verts[0].getA();
        x2 = (int) verts[1].getA();
        x3 = (int) verts[2].getA();
        y1 = (int) verts[0].getB();
        y2 = (int) verts[1].getB();
        y3 = (int) verts[2].getB();
    }

    public void drawTriangle(PixelWriter pw) {
        drawTopTriangle(pw);
        drawBottomTriangle(pw);
    }

    private void drawBottomTriangle(PixelWriter pw) {

        final int x3x2 = x3 - x2;
        final int x3x1 = x3 - x1;
        final int y3y2 = y3 - y2;
        final int y3y1 = y3 - y1;

        if (y3y2 == 0 || y3y1 == 0) return;
        for (int y = y2; y <= y3; y++) {
            int l = x3x2 * (y - y2) / y3y2 + x2;
            int r = x3x1 * (y - y1) / y3y1 + x1;
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            for (int x = l; x <= r + 1; x++) {
                fillingPixels(x, y,pw);
            }
        }
    }

    private void drawTopTriangle(PixelWriter pw) {
        final int x2x1 = (int) (x2 - x1);
        final int x3x1 = (int) (x3 - x1);
        final int y2y1 = (int) (y2 - y1);
        final int y3y1 = (int) (y3 - y1);

        for (double y = y1; y < y2; y++) {
            int l = (int) (x2x1 * (y - y1) / y2y1 + x1);
            int r = (int) (x3x1 * (y - y1) / y3y1 + x1);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            for (int x = l; x <= r + 1; x++) {
                fillingPixels(x, (int) y,pw);
            }
        }
    }

    private void fillingPixels(int x, int y, PixelWriter pw){
        try{
            if (Zbuffer.bufferCheck(x, y, v1.getC())) {
                double k = 0.8;
                pw.setColor(x,  y, new Color(color.getRed() * (1 - k) + color.getRed() * lightCoefficient * k, color.getGreen() * (1 - k) + color.getGreen() * lightCoefficient * k, color.getBlue() * (1 - k) + color.getBlue() * lightCoefficient * k, 1));
            }
        } catch (Exception ignored) {
        }
    }
}
