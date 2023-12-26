package com.cgvsu.model;

import com.cgvsu.Math.Vectors.NDimensionalVector;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.Vector;


import java.util.ArrayList;

public class NormalUtils {

    public static ThreeDimensionalVector normalPolygon(Polygon polygon, ArrayList<ThreeDimensionalVector> vertices) {
        ArrayList<Integer> vertexIndices = polygon.getVertexIndices();
        try {
            return ((NDimensionalVector)vertices.get(vertexIndices.get(1))
                            .subtraction(vertices.get(vertexIndices.get(0))))
                    .vectorProduct(
                    (NDimensionalVector) vertices.get(vertexIndices.get(2))
                            .subtraction(vertices.get(vertexIndices.get(0))));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("известно менее 3 вершин(у полигона)");
        }
        return null;
    }

    public static ArrayList<Vector> normalsVertex(ArrayList<ThreeDimensionalVector> vertices, ArrayList<Polygon> polygons) {
        ArrayList<Vector> normalsVertex = new ArrayList<Vector>();
        ArrayList<Vector> normalsPolygon = new ArrayList<Vector>();

        Integer[] count = new Integer[vertices.size()];
        NDimensionalVector[] normalSummaVertex = new NDimensionalVector[vertices.size()];

        for (int indexPoligon = 0; indexPoligon < polygons.size(); indexPoligon++) {
            normalsPolygon.add(indexPoligon, normalPolygon(polygons.get(indexPoligon), vertices));
            ArrayList<Integer> vertexIndices = polygons.get(indexPoligon).getVertexIndices();

            polygons.get(indexPoligon).setNormalIndices(vertexIndices);

            for (Integer vertexIndex : vertexIndices) {
                if (normalSummaVertex[vertexIndex] == null) {
                    normalSummaVertex[vertexIndex] = (NDimensionalVector) normalsPolygon.get(indexPoligon);
                    count[vertexIndex] = 1;
                } else {
                    normalSummaVertex[vertexIndex] = (NDimensionalVector) normalSummaVertex[vertexIndex].addition(normalsPolygon.get(indexPoligon));
                    count[vertexIndex]++;
                }
            }
        }
        for (int i = 0; i < count.length; i++) {
            // по сути нахождение среднего арифметического в данном случае не особо нужно, так как это просто сокращает длинну вектора суммы, что так же осуществляется с помощью нормализации
            normalsVertex.add(i,normalSummaVertex[i].scale( 1.0/count[i]).normalization());
        }
        return normalsVertex;
    }

}
