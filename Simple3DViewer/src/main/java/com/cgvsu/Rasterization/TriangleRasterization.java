package com.cgvsu.Rasterization;

import com.cgvsu.Math.Vectors.NDimensionalVector;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import com.cgvsu.lines.Bresenham;
import com.cgvsu.render_engine.Zbuffer;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TriangleRasterization {

    private static final Comparator<ThreeDimensionalVector> COMPARATOR = (a, b) -> {
        int cmp = Double.compare(a.getB(), b.getB());
        if (cmp != 0) {
            return cmp;
        } else return Double.compare(a.getA(), b.getA());
    };

    private static TwoDimensionalVector p = new TwoDimensionalVector(0, 0);

    public static void drawTriangle(
            final PixelWriter pw,
            final ThreeDimensionalVector v1,
            final ThreeDimensionalVector v2,
            final ThreeDimensionalVector v3,
            final Color color,
            List<TwoDimensionalVector> texture,
            double lightCoefficient,
            Image image
    ) {
        final var verts = new ThreeDimensionalVector[]{v1, v2, v3};
        Arrays.sort(verts, COMPARATOR);
        final int x1 = (int) verts[0].getA();
        final int x2 = (int) verts[1].getA();
        final int x3 = (int) verts[2].getA();
        final int y1 = (int) verts[0].getB();
        final int y2 = (int) verts[1].getB();
        final int y3 = (int) verts[2].getB();
        final double area = Math.abs(((NDimensionalVector) v1.subtraction(v2)).crossMagnitude((NDimensionalVector) v1.subtraction(v3)));
        drawTopTriangle(pw, x1, y1, x2, y2, x3, y3, v1, color, v2, v3, area, texture, lightCoefficient,  image);
       // drawBottomTriangle(pw, x1, y1, x2, y2, x3, y3, v1, color, v2, v3, area, texture, lightCoefficient,  image);
    }

    private static void drawTopTriangle(
            final PixelWriter pw,
            final int x1, final int y1,
            final int x2, final int y2,
            final int x3, final int y3,
            final ThreeDimensionalVector v1, Color color,
            final ThreeDimensionalVector v2,
            final ThreeDimensionalVector v3,
            final double area, List<TwoDimensionalVector> texture, double light, Image image
    ) {

        PixelReader pixelReader = null;
        if(!texture.isEmpty()){
            pixelReader = image.getPixelReader();
        }

        Color color1 = null;
        Color color2 = null;
        Color color3 = null;

        if(!texture.isEmpty() ) {

            color1 = pixelReader.getColor((int) (texture.get(0).getA() * image.getWidth()), (int) (texture.get(0).getB() * image.getHeight()));
            color2 = pixelReader.getColor((int) (texture.get(1).getA() * image.getWidth()), (int) (texture.get(1).getB() * image.getHeight()));
            color3 = pixelReader.getColor((int) (texture.get(2).getA() * image.getWidth()), (int) (texture.get(2).getB() * image.getHeight()));


            double k = 0.8;

            Color color1tmp = new Color(color1.getRed() * (1 - k) + color1.getRed() * light * k, color1.getGreen() * (1 - k) + color1.getGreen() * light * k, color1.getBlue() * (1 - k) + color1.getBlue() * light * k, 1);
            Color color2tmp = new Color(color2.getRed() * (1 - k) + color2.getRed() * light * k, color2.getGreen() * (1 - k) + color2.getGreen() * light * k, color2.getBlue() * (1 - k) + color2.getBlue() * light * k, 1);
            Color color3tmp = new Color(color3.getRed() * (1 - k) + color3.getRed() * light * k, color3.getGreen() * (1 - k) + color3.getGreen() * light * k, color3.getBlue() * (1 - k) + color3.getBlue() * light * k, 1);

            new Bresenham(x1, x2, y1, y2, color1tmp, color2tmp).draw(pw, v1.getC(), 2);
            new Bresenham(x2, x3, y2, y3, color2tmp, color3tmp).draw(pw, v1.getC(), 2);
            new Bresenham(x3, x1, y3, y1, color3tmp, color1tmp).draw(pw, v1.getC(), 2);
        }

        final int x2x1 = x2 - x1;
        final int x3x1 = x3 - x1;
        final int y2y1 = y2 - y1;
        final int y3y1 = y3 - y1;

        for (int y = y1; y < y2; y++) {
            int l = x2x1 * (y - y1) / y2y1 + x1;
            int r = x3x1 * (y - y1) / y3y1 + x1;
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            for (int x = l; x <= r + 1; x++) {
                Color currentColor = null;

                try {
                    if (Zbuffer.bufferCheck(x, y, v1.getC())) {
                        double k = 0.8;
                        pw.setColor(x, y, new Color(color.getRed() * (1 - k) + color.getRed() * light * k, color.getGreen() * (1 - k) + color.getGreen() * light * k, color.getBlue() * (1 - k) + color.getBlue() * light * k, 1));
                    }
                } catch (Exception ignored) {

                }
            }
        }
    }


    private static void drawBottomTriangle(
            final PixelWriter pw,
            final int x1, final int y1,
            final int x2, final int y2,
            final int x3, final int y3,
            final ThreeDimensionalVector v1, Color color,
            final ThreeDimensionalVector v2,
            final ThreeDimensionalVector v3,
            final double area,
            List<TwoDimensionalVector> texture, double light, Image image
    ) {

        Color color1 = null;
        Color color2 = null;
        Color color3 = null;

        PixelReader pixelReader = null;
        if(!texture.isEmpty()){
            pixelReader = image.getPixelReader();
        }

        if(!texture.isEmpty() ) {
            color1 = pixelReader.getColor((int) (texture.get(0).getA() * image.getWidth()), (int) (texture.get(0).getB() * image.getHeight()));
            color2 = pixelReader.getColor((int) (texture.get(1).getA() * image.getWidth()), (int) (texture.get(1).getB() * image.getHeight()));
            color3 = pixelReader.getColor((int) (texture.get(2).getA() * image.getWidth()), (int) (texture.get(2).getB() * image.getHeight()));

            double k = 0.8;

            Color color1tmp = new Color(color1.getRed() * (1 - k) + color1.getRed() * light * k, color1.getGreen() * (1 - k) + color1.getGreen() * light * k, color1.getBlue() * (1 - k) + color1.getBlue() * light * k, 1);
            Color color2tmp = new Color(color2.getRed() * (1 - k) + color2.getRed() * light * k, color2.getGreen() * (1 - k) + color2.getGreen() * light * k, color2.getBlue() * (1 - k) + color2.getBlue() * light * k, 1);
            Color color3tmp = new Color(color3.getRed() * (1 - k) + color3.getRed() * light * k, color3.getGreen() * (1 - k) + color3.getGreen() * light * k, color3.getBlue() * (1 - k) + color3.getBlue() * light * k, 1);

            new Bresenham(x1, x2, y1, y2, color1tmp, color2tmp).draw(pw, v1.getC(), 2);
            new Bresenham(x2, x3, y2, y3, color2tmp, color3tmp).draw(pw, v1.getC(),2);
            new Bresenham(x3, x1, y3, y1, color3tmp, color1tmp).draw(pw, v1.getC(), 2);
        }
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
            Color currentColor = null;
            for (int x = l; x <= r + 1; x++) {
                if (!texture.isEmpty()) {
                    p = new TwoDimensionalVector(x, y);
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


                    if(red == 1 || green == 1 || blue == 1 || red == 0 || green == 0 || blue == 0){
                        color = color1;
                    }else{
                        color = new Color(red, green, blue, 1);
                    }

                }

                try {
                    if (Zbuffer.bufferCheck(x, y, v1.getC())) {
                        double k = 0.8;
                        pw.setColor(x, y, new Color(color.getRed() * (1 - k) + color.getRed() * light * k, color.getGreen() * (1 - k) + color.getGreen() * light * k, color.getBlue() * (1 - k) + color.getBlue() * light * k, 1));
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private static double clamp(double v) {
        if (v < 0.0) return 0;
        if (v > 1.0) return 1.0;
        return v;
    }
}
