package com.cgvsu.Rasterization;


import com.cgvsu.Math.Barycentric.BarycentricCoordinates;
import com.cgvsu.Math.Vectors.NDimensionalVector;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import com.cgvsu.render_engine.Zbuffer;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TriangleRasterization {


    //static File file = new File("C:\\Users\\770vd\\Desktop\\Task4\\Simple3DViewer\\src\\main\\resources\\texture\\NeutralWrapped.jpg");
    static Image image = new Image("C:\\Users\\wda11\\IdeaProjects\\Task4\\Simple3DViewer\\src\\main\\resources\\texture\\NeutralWrapped.jpg");
    static PixelReader pixelReader = image.getPixelReader();

    private static final Comparator<ThreeDimensionalVector> COMPARATOR = (a, b) -> {
        int cmp = Double.compare(a.getB(), b.getB());
        if (cmp != 0) {
            return cmp;
        } else return Double.compare(a.getA(), b.getA());
    };

    private static TwoDimensionalVector p = new TwoDimensionalVector(0,0);

    public static void drawTriangle(
            final PixelWriter pw,
            final ThreeDimensionalVector v1,
            final ThreeDimensionalVector v2,
            final ThreeDimensionalVector v3,
            final Color color,
            List<TwoDimensionalVector> texture,
            double lightCoefficient,
            Zbuffer zbuffer
    ) {
        final var verts = new ThreeDimensionalVector[]{v1, v2, v3};
        Arrays.sort(verts, COMPARATOR);
        final int x1 = (int) verts[0].getA();
        final int x2 = (int) verts[1].getA();
        final int x3 = (int) verts[2].getA();
        final int y1 = (int) verts[0].getB();
        final int y2 = (int) verts[1].getB();
        final int y3 = (int) verts[2].getB();

        // Double the area of the triangle. Used to calculate the barycentric coordinates later.
        final double area = Math.abs(((NDimensionalVector) v1.subtraction(v2)).crossMagnitude((NDimensionalVector)v1.subtraction(v3)));
        drawTopTriangle(pw, x1, y1, x2, y2, x3, y3, v1, color, v2, v3,  area,texture , lightCoefficient, zbuffer);
        drawBottomTriangle(pw, x1, y1, x2, y2, x3, y3, v1, color, v2, v3, area, texture, lightCoefficient, zbuffer);
    }
    private static void drawTopTriangle(
            final PixelWriter pw,
            final int x1, final int y1,
            final int x2, final int y2,
            final int x3, final int y3,
            final ThreeDimensionalVector v1, Color color,
            final ThreeDimensionalVector v2,
            final ThreeDimensionalVector v3,
            final double area, List<TwoDimensionalVector> texture, double light, Zbuffer zbuffer
    ) {

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


            for (int x = l; x <= r; x++) {
                BarycentricCoordinates barycentricCoordinates = new BarycentricCoordinates(
                        new TwoDimensionalVector(v1.getA(), v1.getB()),
                        new TwoDimensionalVector(v2.getA(), v2.getB()),
                        new TwoDimensionalVector(v3.getA(), v3.getB()),
                        new TwoDimensionalVector(x, y));



                double PTcoordX =  Math.abs(texture.get(0).getA() * barycentricCoordinates.getU() + texture.get(1).getA() * barycentricCoordinates.getV()  +texture.get(2).getA() * barycentricCoordinates.getW()) ;
                double PTcoordY =  Math.abs(texture.get(0).getB() * barycentricCoordinates.getU() + texture.get(1).getB() * barycentricCoordinates.getV() + texture.get(2).getB() * barycentricCoordinates.getW())  ;
//


                //   try {

                System.out.println(image.getHeight() + " " + image.getWidth());

                color = pixelReader.getColor(2000, 2450);

                try {
                    if(zbuffer.bufferCheck(x,y,v1.getC())){
                        double k = 0.5;
                        pw.setColor(x,y, new Color(color.getRed()  * (1 - k  ) + color.getRed()  * light * k, color.getGreen()  * (1 - k  ) + color.getGreen()  * light * k, color.getBlue()  * (1 - k ) + color.getBlue()  * light * k, 1));
                    }
                }catch (Exception e){
                    //System.out.println(light);
                }
//                final int colorBits = interpolateColor(x, y, v1, c1, v2, c2, v3, c3, area, texture);
//
//
//                p = new TwoDimensionalVector(x, y);
//                final double w1 = Math.abs(((NDimensionalVector)v2.subtraction(p)).crossMagnitude((NDimensionalVector)v2.subtraction(v3))) / area;
//
//                final double w2 = Math.abs(((NDimensionalVector)v1.subtraction(p)).crossMagnitude((NDimensionalVector)v1.subtraction(v3))) / area;
//
//                final double w3 = Math.abs(((NDimensionalVector)v1.subtraction(p)).crossMagnitude((NDimensionalVector)v1.subtraction(v2))) / area;
//
//
//                int PTcoordX = (int) (texture.get(0).getA()*w1 + texture.get(1).getA()*w2 +   texture.get(2).getA()*w3);
//                int PTcoordY = (int) (texture.get(0).getB()*w1 + texture.get(1).getB()*w2 +   texture.get(2).getB()*w3);
//
//
//                try {
//                    int pixelColor = image.getRGB(PTcoordX, PTcoordY);
//
//
//                    // Извлечение компонентов цвета
//                    int alpha = (pixelColor >> 24) & 0xFF;
//                    int red = (pixelColor >> 16) & 0xFF;
//                    int green = (pixelColor >> 8) & 0xFF;
//                    int blue = pixelColor & 0xFF;
//                    // System.out.println(red + " " + green + " " + blue);
//                    //  System.out.println(x + " " + y);
//                    // pw.setArgb(x, y, red | green | blue);
//
//                    pw.setColor(x, y, new Color(alpha / 255.0, red / 255.0, green / 255.0, blue / 255.0));
//                }catch (Exception e){
//                    System.out.println(PTcoordX + " " + PTcoordY);
//                }
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
            List<TwoDimensionalVector> texture, double light, Zbuffer zbuffer
    ) {
        final int x3x2 = x3 - x2;
        final int x3x1 = x3 - x1;
        final int y3y2 = y3 - y2;
        final int y3y1 = y3 - y1;

        // Draw the separating line and bottom triangle.
        if (y3y2 == 0 || y3y1 == 0) return; // Stop now if the bottom triangle is degenerate (avoids div by zero).
        for (int y = y2; y <= y3; y++) {
            int l = x3x2 * (y - y2) / y3y2 + x2; // Edge 2-3.
            int r = x3x1 * (y - y1) / y3y1 + x1; // Edge 1-3.
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }



            for (int x = l; x <= r; x++) {


                BarycentricCoordinates barycentricCoordinates = new BarycentricCoordinates(
                        new TwoDimensionalVector(v1.getA(), v1.getB()),
                        new TwoDimensionalVector(v2.getA(), v2.getB()),
                        new TwoDimensionalVector(v3.getA(), v3.getB()),
                        new TwoDimensionalVector(x, y));

//                   // final int colorBits = interpolateColor(x, y, v1, c1, v2, c2, v3, c3, area, texture);
//
//
//                    p = new TwoDimensionalVector(x, y);



//                System.out.println(Arrays.toString(texture.get(0).getArrValues()));
//                System.out.println(Arrays.toString(texture.get(1).getArrValues()));
//                System.out.println(Arrays.toString(texture.get(2).getArrValues()));

                //    double PTcoordX =  Math.abs(texture.get(0).getA() * barycentricCoordinates.getU() + texture.get(1).getA() * barycentricCoordinates.getV()  +texture.get(2).getA() * barycentricCoordinates.getW()) / area;
                //    double PTcoordY =  Math.abs(texture.get(0).getB() * barycentricCoordinates.getU() + texture.get(1).getB() * barycentricCoordinates.getV() + +texture.get(2).getB() * barycentricCoordinates.getW()) / area;
//

             //   System.out.println(Arrays.toString(texture.get(0).getArrValues()));
                color = pixelReader.getColor(2000, 2300);
//
                //System.out.println(texture.get(0).getA() + " " +  texture.get(0).getB());

                try {
                    if(zbuffer.bufferCheck(x, y,v1.getC())){
                        double k = 0.5;
                        pw.setColor(x,y, new Color(color.getRed()  * (1 - k  ) + color.getRed()  * light * k, color.getGreen()  * (1 - k  ) + color.getGreen()  * light * k, color.getBlue()  * (1 - k  ) + color.getBlue()  * light * k, 1));
                    }
                }catch (Exception e){
                    //System.out.println(light);
                }
                  //  pw.setArgb(x, y, red | green | blue);
//                }catch (Exception e){
//                    System.out.println(PTcoordX + " " + PTcoordY);
//                }
            }
        }
    }

    /**
     * Interpolates the color for the given coordinate.
     * @return The interpolated color bits in the ARGB format.
     */
//    private static int interpolateColor(
//            final int x, final int y,
//            final TwoDimensionalVector v1, final Color c1,
//            final TwoDimensionalVector v2, final Color c2,
//            final TwoDimensionalVector v3, final Color c3,
//            final double area,
//            List<TwoDimensionalVector> texture
//    ) {
//        p = new TwoDimensionalVector(x, y);
//        final double w1 = Math.abs(((NDimensionalVector)v2.subtraction(p)).crossMagnitude((NDimensionalVector)v2.subtraction(v3))) / area;
//
//        final double w2 = Math.abs(((NDimensionalVector)v1.subtraction(p)).crossMagnitude((NDimensionalVector)v1.subtraction(v3))) / area;
//
//        final double w3 = Math.abs(((NDimensionalVector)v1.subtraction(p)).crossMagnitude((NDimensionalVector)v1.subtraction(v2))) / area;
//
////        final float w2 = Math.abs(v1.to(p).crossMagnitude(v1.to(v3))) / area;
////        final float w3 = Math.abs(v1.to(p).crossMagnitude(v1.to(v2))) / area;
//
//
//        double PTcoordX = texture.get(0).getA()*w1 + texture.get(1).getA()*w2 +  + texture.get(2).getA()*w3;
//        double PTcoordY = texture.get(0).getB()*w1 + texture.get(1).getB()*w2 +  + texture.get(2).getB()*w3;
//
//        final float red = clamp((float) (w1 * c1.getRed() + w2 * c2.getRed() + w3 * c3.getRed()));
//        final float green = clamp((float) (w1 * c1.getGreen() + w2 * c2.getGreen() + w3 * c3.getGreen()));
//        final float blue = clamp((float) (w1 * c1.getBlue() + w2 * c2.getBlue() + w3 * c3.getBlue()));
//
//        for(TwoDimensionalVector t : texture){
//
//        }
//
//        color.set(red, green, blue);
//        return color.toArgb();
//    }

    /**
     * Clamps the given float value between 0 and 1.
     * @param v The value to clamp.
     * @return The clamped value.
     */
    private static float clamp(float v) {
        if (v < (float) 0.0) return (float) 0.0;
        if (v > (float) 1.0) return (float) 1.0;
        return v;
    }
    private static Color interpolateColor(double x, double y, List<TwoDimensionalVector> texture){
        BarycentricCoordinates barycentricCoordinates = new BarycentricCoordinates(
                new TwoDimensionalVector(texture.get(0).getA(), texture.get(0).getB()),
                new TwoDimensionalVector(texture.get(1).getA(), texture.get(1).getB()),
                new TwoDimensionalVector(texture.get(2).getA(), texture.get(2).getB()),
                new TwoDimensionalVector(x, y));

        double PTcoordX =  texture.get(0).getA() * barycentricCoordinates.getU() + texture.get(1).getA() * barycentricCoordinates.getV()  +texture.get(2).getA() * barycentricCoordinates.getW();
        double PTcoordY =  texture.get(0).getB() * barycentricCoordinates.getU() + texture.get(1).getB() * barycentricCoordinates.getV() + +texture.get(2).getB() * barycentricCoordinates.getW();
//
        return pixelReader.getColor((int)(PTcoordX ), (int)(PTcoordY ));
    }
}
