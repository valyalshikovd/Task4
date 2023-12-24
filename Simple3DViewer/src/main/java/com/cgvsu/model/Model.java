package com.cgvsu.model;
import com.cgvsu.Math.AffineTransormation.AffineTransformation;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import javafx.scene.canvas.GraphicsContext;


import java.util.*;

public class Model {
    public ArrayList<ThreeDimensionalVector> vertices;
    public ArrayList<TwoDimensionalVector> textureVertices;
    public ArrayList<ThreeDimensionalVector> normals;
    public ArrayList<Polygon> polygons;
    public NDimensionalMatrix affineMatrix;

    public NDimensionalMatrix translationMatrix = (NDimensionalMatrix) new AffineTransformation().translate(1,1,1);
    public NDimensionalMatrix rotateMatrix = (NDimensionalMatrix) new AffineTransformation().translate(1,1,1);



    public Model(ArrayList<ThreeDimensionalVector> vertices,
                 ArrayList<TwoDimensionalVector> textureVertices,
                 ArrayList<ThreeDimensionalVector> normals,
                 ArrayList<Polygon> polygons) {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
    }


    public void draw(GraphicsContext g, NDimensionalMatrix modelViewProjectionMatrix, int width, int height){
        triangulate();
        for (Polygon p : polygons){
            p.drawPolygon(g, modelViewProjectionMatrix, this, (NDimensionalMatrix) rotateMatrix.multiplyMatrix(translationMatrix),width,height);
        }
    }
    public boolean isEmpty() {
        return vertices.isEmpty();
    }

    public void triangulate(){
        polygons = (ArrayList<Polygon>) Triangulation.triangulatePolygons(polygons);
    }



}
