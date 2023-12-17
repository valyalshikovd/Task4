package com.cgvsu.model;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.TwoDimensionalVector;


import java.util.*;

public class Model {

    public ArrayList<ThreeDimensionalVector> vertices = new ArrayList<ThreeDimensionalVector>();
    public ArrayList<TwoDimensionalVector> textureVertices = new ArrayList<TwoDimensionalVector>();
    public ArrayList<ThreeDimensionalVector> normals = new ArrayList<ThreeDimensionalVector>();
    public ArrayList<Polygon> polygons = new ArrayList<Polygon>();
}
