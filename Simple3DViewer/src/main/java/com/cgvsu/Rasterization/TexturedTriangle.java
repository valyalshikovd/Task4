package com.cgvsu.Rasterization;

import com.cgvsu.Math.Vectors.NDimensionalVector;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import com.cgvsu.lines.Bresenham;
import com.cgvsu.model.Polygon;
import com.cgvsu.render_engine.Zbuffer;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TexturedTriangle implements InterfaceTriangle {
    private static final Comparator<ThreeDimensionalVector> COMPARATOR = (a, b) -> {
        int cmp = Double.compare(a.getB(), b.getB());
        if (cmp != 0) {
            return cmp;
        } else return Double.compare(a.getA(), b.getA());
    };
    private final ThreeDimensionalVector v1;
    private ThreeDimensionalVector v2;
    private final ThreeDimensionalVector v3;
    private Color color;
    private double lightCoefficient;

    private final ThreeDimensionalVector[] verts;
    private final int x1;
    private final int x2;
    private final int x3;
    private final int y1;
    private final int y2;
    private final int y3;
    private final int area;
    private final List<TwoDimensionalVector> texture;
    private Image image;

    public TexturedTriangle(Polygon polygon, double lightCoefficient) {
        this.image = polygon.getModel().image;
        this.texture = polygon.getTextureVertecies();
        this.v1 = polygon.getResultPoints().get(0);
        this.v2 = polygon.getResultPoints().get(1);
        this.v3 = polygon.getResultPoints().get(2);
        this.color = polygon.getModel().fillingColor;
        this.lightCoefficient = lightCoefficient;
        verts = new ThreeDimensionalVector[]{v1, v2, v3};
        Arrays.sort(verts, COMPARATOR);
        x1 = (int) verts[0].getA();
        x2 = (int) verts[1].getA();
        x3 = (int) verts[2].getA();
        y1 = (int) verts[0].getB();
        y2 = (int) verts[1].getB();
        y3 = (int) verts[2].getB();
        area = (int) Math.abs(((NDimensionalVector) v1.subtraction(v2)).crossMagnitude((NDimensionalVector) v1.subtraction(v3)));
    }

    public void drawTriangle(PixelWriter pw) {
        drawTopTriangle(pw);
        drawBottomTriangle(pw);

    }

    private void drawBottomTriangle(PixelWriter pw) {


        PixelReader pixelReader = image.getPixelReader();


        Color color1 = pixelReader.getColor((int) (texture.get(0).getA() * image.getWidth()), (int) (texture.get(0).getB() * image.getHeight()));
        Color color2 = pixelReader.getColor((int) (texture.get(1).getA() * image.getWidth()), (int) (texture.get(1).getB() * image.getHeight()));
        Color color3 = pixelReader.getColor((int) (texture.get(2).getA() * image.getWidth()), (int) (texture.get(2).getB() * image.getHeight()));

        double k = 0.8;

        Color color1tmp = new Color(color1.getRed() * (1 - k) + color1.getRed() * lightCoefficient * k, color1.getGreen() * (1 - k) + color1.getGreen() * lightCoefficient * k, color1.getBlue() * (1 - k) + color1.getBlue() * lightCoefficient * k, 1);
        Color color2tmp = new Color(color2.getRed() * (1 - k) + color2.getRed() * lightCoefficient * k, color2.getGreen() * (1 - k) + color2.getGreen() * lightCoefficient * k, color2.getBlue() * (1 - k) + color2.getBlue() * lightCoefficient * k, 1);
        Color color3tmp = new Color(color3.getRed() * (1 - k) + color3.getRed() * lightCoefficient * k, color3.getGreen() * (1 - k) + color3.getGreen() * lightCoefficient * k, color3.getBlue() * (1 - k) + color3.getBlue() * lightCoefficient * k, 1);

        new Bresenham(x1, x2, y1, y2, color1tmp, color2tmp).draw(pw, v1.getC(), 2);
        new Bresenham(x2, x3, y2, y3, color2tmp, color3tmp).draw(pw, v1.getC(), 2);
        new Bresenham(x3, x1, y3, y1, color3tmp, color1tmp).draw(pw, v1.getC(), 2);


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
                TwoDimensionalVector p = new TwoDimensionalVector(x, y);
                final double w1 = Math.abs(((NDimensionalVector) new TwoDimensionalVector(v2.getA(), v2.getB()).subtraction(p)).crossMagnitude((NDimensionalVector) new TwoDimensionalVector(v2.getA(), v2.getB()).subtraction(new TwoDimensionalVector(v3.getA(), v3.getB())))) / area;
                final double w2 = Math.abs(((NDimensionalVector) new TwoDimensionalVector(v1.getA(), v1.getB()).subtraction(p)).crossMagnitude((NDimensionalVector) new TwoDimensionalVector(v1.getA(), v1.getB()).subtraction(new TwoDimensionalVector(v3.getA(), v3.getB())))) / area;
                final double w3 = Math.abs(((NDimensionalVector) new TwoDimensionalVector(v1.getA(), v1.getB()).subtraction(p)).crossMagnitude((NDimensionalVector) new TwoDimensionalVector(v1.getA(), v1.getB()).subtraction(new TwoDimensionalVector(v2.getA(), v2.getB())))) / area;

                final double red = clamp(((w1 * color1.getRed())
                        + (w2 * color2.getRed())
                        + (w3 * color3.getRed())));
                final double green = clamp(((w1 * color1.getGreen())
                        + (w2 * color2.getGreen())
                        + (w3 * color3.getGreen())));
                final double blue = clamp(((w1 * color1.getBlue())
                        + (w2 * color2.getBlue())
                        + (w3 * color3.getBlue())));


                if (red == 1 || green == 1 || blue == 1 || red == 0 || green == 0 || blue == 0) {
                    color = color1;
                } else {
                    color = new Color(red, green, blue, 1);
                }
                fillingPixels(x, y, pw);
            }
        }
    }

    private void drawTopTriangle(PixelWriter pw) {

        PixelReader pixelReader = image.getPixelReader();


        Color color1 = pixelReader.getColor((int) (texture.get(0).getA() * image.getWidth()), (int) (texture.get(0).getB() * image.getHeight()));
        Color color2 = pixelReader.getColor((int) (texture.get(1).getA() * image.getWidth()), (int) (texture.get(1).getB() * image.getHeight()));
        Color color3 = pixelReader.getColor((int) (texture.get(2).getA() * image.getWidth()), (int) (texture.get(2).getB() * image.getHeight()));

        double k = 0.8;

        Color color1tmp = new Color(color1.getRed() * (1 - k) + color1.getRed() * lightCoefficient * k, color1.getGreen() * (1 - k) + color1.getGreen() * lightCoefficient * k, color1.getBlue() * (1 - k) + color1.getBlue() * lightCoefficient * k, 1);
        Color color2tmp = new Color(color2.getRed() * (1 - k) + color2.getRed() * lightCoefficient * k, color2.getGreen() * (1 - k) + color2.getGreen() * lightCoefficient * k, color2.getBlue() * (1 - k) + color2.getBlue() * lightCoefficient * k, 1);
        Color color3tmp = new Color(color3.getRed() * (1 - k) + color3.getRed() * lightCoefficient * k, color3.getGreen() * (1 - k) + color3.getGreen() * lightCoefficient * k, color3.getBlue() * (1 - k) + color3.getBlue() * lightCoefficient * k, 1);

        new Bresenham(x1, x2, y1, y2, color1tmp, color2tmp).draw(pw, v1.getC(), 2);
        new Bresenham(x2, x3, y2, y3, color2tmp, color3tmp).draw(pw, v1.getC(), 2);
        new Bresenham(x3, x1, y3, y1, color3tmp, color1tmp).draw(pw, v1.getC(), 2);


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
                TwoDimensionalVector p = new TwoDimensionalVector(x, y);
                final double w1 = Math.abs(((NDimensionalVector) new TwoDimensionalVector(v2.getA(), v2.getB()).subtraction(p)).crossMagnitude((NDimensionalVector) new TwoDimensionalVector(v2.getA(), v2.getB()).subtraction(new TwoDimensionalVector(v3.getA(), v3.getB())))) / area;
                final double w2 = Math.abs(((NDimensionalVector) new TwoDimensionalVector(v1.getA(), v1.getB()).subtraction(p)).crossMagnitude((NDimensionalVector) new TwoDimensionalVector(v1.getA(), v1.getB()).subtraction(new TwoDimensionalVector(v3.getA(), v3.getB())))) / area;
                final double w3 = Math.abs(((NDimensionalVector) new TwoDimensionalVector(v1.getA(), v1.getB()).subtraction(p)).crossMagnitude((NDimensionalVector) new TwoDimensionalVector(v1.getA(), v1.getB()).subtraction(new TwoDimensionalVector(v2.getA(), v2.getB())))) / area;

                final double red = clamp(((w1 * color1.getRed())
                        + (w2 * color2.getRed())
                        + (w3 * color3.getRed())));
                final double green = clamp(((w1 * color1.getGreen())
                        + (w2 * color2.getGreen())
                        + (w3 * color3.getGreen())));
                final double blue = clamp(((w1 * color1.getBlue())
                        + (w2 * color2.getBlue())
                        + (w3 * color3.getBlue())));


                if (red == 1 || green == 1 || blue == 1 || red == 0 || green == 0 || blue == 0) {
                    color = color1;
                } else {
                    color = new Color(red, green, blue, 1);
                }
                fillingPixels(x, (int) y, pw);
            }
        }
    }

    private void fillingPixels(int x, int y, PixelWriter pw) {
        try {
            if (Zbuffer.bufferCheck(x, y, v1.getC())) {
                double k = 0.8;
                pw.setColor(x, y, new Color(color.getRed() * (1 - k) + color.getRed() * lightCoefficient * k, color.getGreen() * (1 - k) + color.getGreen() * lightCoefficient * k, color.getBlue() * (1 - k) + color.getBlue() * lightCoefficient * k, 1));
            }
        } catch (Exception ignored) {
        }
    }

    private static double clamp(double v) {
        if (v < 0.0) return 0;
        if (v > 1.0) return 1.0;
        return v;
    }
}