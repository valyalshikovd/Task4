package com.cgvsu.Rasterization;
import com.cgvsu.Math.Vectors.NDimensionalVector;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import com.cgvsu.model.Polygon;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Triangle {
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
    private final double x1;
    private final double x2;
    private final double x3;
    private final double y1;
    private final double y2;
    private final double y3;
    private final double area ;

    public Triangle(Polygon polygon, double lightCoefficient) throws Exception {
        if(polygon.getVertexIndices().size() != 3){
            throw new Exception("Попытка приведения нетриангулированного полигона, к треугольнику");
        }
        this.v1 = polygon.getModel().vertices.get(polygon.getVertexIndices().get(0));
        this.v2 = polygon.getModel().vertices.get(polygon.getVertexIndices().get(1));
        this.v3 = polygon.getModel().vertices.get(polygon.getVertexIndices().get(2));
        this.color = polygon.getModel().fillingColor;
        this.lightCoefficient = lightCoefficient;
        verts = new ThreeDimensionalVector[]{v1, v2, v3};
        Arrays.sort(verts, COMPARATOR);
        x1 = verts[0].getA();
        x2 = verts[1].getA();
        x3 = verts[2].getA();
        y1 = verts[0].getB();
        y2 = verts[1].getB();
        y3 = verts[2].getB();
        area = Math.abs(((NDimensionalVector) v1.subtraction(v2)).crossMagnitude((NDimensionalVector) v1.subtraction(v3)));
    }

    private void drawTriangle(PixelWriter pw){

    }
}
