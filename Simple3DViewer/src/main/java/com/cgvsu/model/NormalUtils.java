package com.cgvsu.model;

import com.cgvsu.math.Vector3f;
import com.cgvsu.math.VectorOperations;

import java.util.ArrayList;

public class NormalUtils {

    private static Vector3f normalPolygon(Polygon polygon, ArrayList<Vector3f> vertices) {
        ArrayList<Integer> vertexIndices = polygon.getVertexIndices();
        try {
            return VectorOperations.vectorProduct(VectorOperations.vector(vertices.get(vertexIndices.get(0)), vertices.get(vertexIndices.get(1))),
                    VectorOperations.vector(vertices.get(vertexIndices.get(0)), vertices.get(vertexIndices.get(2))));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("известно менее 3 вершин (у полигона)");
        }
        return null;
    }

    public static ArrayList<Vector3f> normalsVertex(ArrayList<Vector3f> vertices, ArrayList<Polygon> polygons) {
        ArrayList<Vector3f> normalsVertex = new ArrayList<Vector3f>();
        ArrayList<Vector3f> normalsPolygon = new ArrayList<Vector3f>();

        Integer[] count = new Integer[vertices.size()];
        Vector3f[] normalSummaVertex = new Vector3f[vertices.size()];

        for (int indexPoligon = 0; indexPoligon < polygons.size(); indexPoligon++) {
            normalsPolygon.add(indexPoligon, normalPolygon(polygons.get(indexPoligon), vertices));
            ArrayList<Integer> vertexIndices = polygons.get(indexPoligon).getVertexIndices();

            polygons.get(indexPoligon).setNormalIndices(vertexIndices);

            for (Integer vertexIndex : vertexIndices) {
                if (normalSummaVertex[vertexIndex] == null) {
                    normalSummaVertex[vertexIndex] = normalsPolygon.get(indexPoligon);
                    count[vertexIndex] = 1;
                } else {
                    normalSummaVertex[vertexIndex] = VectorOperations.summaVector(normalSummaVertex[vertexIndex], normalsPolygon.get(indexPoligon));
                    count[vertexIndex]++;
                }
            }
        }
        for (int i = 0; i < count.length; i++) {
            // по сути нахождение среднего арифметического в данном случае не особо нужно, так как это просто сокращает длинну вектора суммы, что так же осуществляется с помощью нормализации
            normalsVertex.add(i, VectorOperations.normalize(VectorOperations.quotient(normalSummaVertex[i], count[i])));
        }
        return normalsVertex;
    }

}
