package com.cgvsu.model;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.TwoDimensionalVector;


import java.util.*;

public class Model {
    public ArrayList<ThreeDimensionalVector> vertices;
    public ArrayList<TwoDimensionalVector> textureVertices;
    public ArrayList<ThreeDimensionalVector> normals;
    public ArrayList<Polygon> polygons;

    public Model(ArrayList<ThreeDimensionalVector> vertices,
                 ArrayList<TwoDimensionalVector> textureVertices,
                 ArrayList<ThreeDimensionalVector> normals,
                 ArrayList<Polygon> polygons) {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
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

    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    public boolean isEmpty() {
        return vertices.isEmpty();
    }
}
