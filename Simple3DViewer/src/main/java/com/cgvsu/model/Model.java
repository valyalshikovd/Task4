package com.cgvsu.model;
import com.cgvsu.Math.AffineTransormation.AffineTransformation;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


import java.awt.*;
import java.util.*;
import java.util.List;

public class Model {
    public ArrayList<ThreeDimensionalVector> vertices;
    public ArrayList<TwoDimensionalVector> textureVertices;
    public ArrayList<ThreeDimensionalVector> normals;
    public ArrayList<Polygon> polygons;
    public double scaleX = 1;
    public double scaleY = 1;
    public double scaleZ = 1;
    public int translateX = 1;
    public int translateY = 1;
    public int translateZ = 1;
    public double rotateX = 0;
    public double rotateY = 0;
    public double rotateZ = 0;

    public boolean isTriangulate = false;

    public Color isFill = Color.WHITE;


    public Model(ArrayList<ThreeDimensionalVector> vertices,
                 ArrayList<TwoDimensionalVector> textureVertices,
                 ArrayList<ThreeDimensionalVector> normals,
                 ArrayList<Polygon> polygons) {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
    }


    public void draw(GraphicsContext g, NDimensionalMatrix modelViewProjectionMatrix, int width, int height, ThreeDimensionalVector light , double[][]  Zbuffer){
        List<Polygon> currPoligons = polygons;
        if(isTriangulate || isFill != Color.WHITE){
            currPoligons = triangulate();
        }
        for (Polygon p : currPoligons){
            p.drawPolygon(g, modelViewProjectionMatrix, this, (
                    (NDimensionalMatrix)new AffineTransformation().scale(scaleX, scaleY, scaleZ).multiplyMatrix(
                    (NDimensionalMatrix)new AffineTransformation().rotate((float) rotateX, (float) rotateY, (float) rotateZ)).multiplyMatrix(
                            (NDimensionalMatrix) new AffineTransformation().translate(translateX, translateY, translateZ))),width,height,isFill ,light, Zbuffer);
        }
    }
    public boolean isEmpty() {
        return vertices.isEmpty();
    }

    public ArrayList<Polygon> triangulate(){
        return (ArrayList<Polygon>) Triangulation.triangulatePolygons(polygons);
    }



}
