package com.cgvsu.model;
import com.cgvsu.Math.AffineTransormation.AffineTransformation;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import com.cgvsu.render_engine.Scene;
import com.cgvsu.render_engine.Zbuffer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.List;



public class Model {
    public ArrayList<ThreeDimensionalVector> vertices;
    public ArrayList<ThreeDimensionalVector> sceneVertices = new ArrayList<>();
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
    public Color fillingColor = null;
    public boolean isFill;
    public boolean isTextured = false;
    public Image image = null;
    private Scene scene;


    public Model(ArrayList<ThreeDimensionalVector> vertices,
                 ArrayList<TwoDimensionalVector> textureVertices,
                 ArrayList<ThreeDimensionalVector> normals,
                 ArrayList<Polygon> polygons) {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
    }



    public void changeModel(){
        sceneVertices.clear();
        NDimensionalMatrix m = (NDimensionalMatrix)new AffineTransformation().scale(scaleX, scaleY, scaleZ)
                .multiplyMatrix(new AffineTransformation().rotate((float) rotateX, (float) rotateY, (float) rotateZ))
                .multiplyMatrix(new AffineTransformation().translate(translateX, translateY, translateZ));
        for (int i = 0; i <this.vertices.size(); i++){
            sceneVertices.add(multiplyMatrix4ByVector3(m, vertices.get(i)));
        }
    }

    public void draw(GraphicsContext g){
        changeModel();
        for (Polygon p : checkTriangulate(polygons)){
            p.drawPolygon(g,  this);
        }
    }

    public boolean isEmpty() {
        return vertices.isEmpty();
    }

    public ArrayList<Polygon> triangulate(){
        return (ArrayList<Polygon>) Triangulation.triangulatePolygons(polygons);
    }
    public static ThreeDimensionalVector multiplyMatrix4ByVector3(final NDimensionalMatrix matrix, final ThreeDimensionalVector vertex) {
        final double x  = matrix.getMatrixInVectors()[0].getArrValues()[0] * vertex.getA() +  matrix.getMatrixInVectors()[1].getArrValues()[0] * vertex.getB() + matrix.getMatrixInVectors()[2].getArrValues()[0] * vertex.getC() + matrix.getMatrixInVectors()[3].getArrValues()[0];
        final double y  = matrix.getMatrixInVectors()[0].getArrValues()[1] * vertex.getA() +  matrix.getMatrixInVectors()[1].getArrValues()[1] * vertex.getB() + matrix.getMatrixInVectors()[2].getArrValues()[1] * vertex.getC() + matrix.getMatrixInVectors()[3].getArrValues()[1];
        final double z = matrix.getMatrixInVectors()[0].getArrValues()[2] * vertex.getA() +  matrix.getMatrixInVectors()[1].getArrValues()[2] * vertex.getB() + matrix.getMatrixInVectors()[2].getArrValues()[2] * vertex.getC() + matrix.getMatrixInVectors()[3].getArrValues()[2];
        final double w  = matrix.getMatrixInVectors()[0].getArrValues()[3] * vertex.getA() +  matrix.getMatrixInVectors()[1].getArrValues()[3] * vertex.getB() + matrix.getMatrixInVectors()[2].getArrValues()[3] * vertex.getC() + matrix.getMatrixInVectors()[3].getArrValues()[3];
        return new ThreeDimensionalVector(x/w, y/w, z/w);
    }

    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    public ArrayList<ThreeDimensionalVector> getVertices() {
        return vertices;
    }

    public ArrayList<TwoDimensionalVector> getTextureVertices() {
        return textureVertices;
    }

    public ArrayList<ThreeDimensionalVector> getNormals() {
        return normals;
    }

    private List<Polygon> checkTriangulate(List<Polygon> currPoligons){
        if(isTriangulate || fillingColor == null){
            currPoligons = triangulate();
        }
        return currPoligons;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
