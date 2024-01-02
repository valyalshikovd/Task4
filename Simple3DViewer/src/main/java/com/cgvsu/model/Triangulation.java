package com.cgvsu.model;

import com.cgvsu.Math.Vectors.ThreeDimensionalVector;


import java.util.*;



public class Triangulation {
    public static List<Polygon> triangulation(Polygon polygon) {
        List<Polygon> triangularPolygons = new ArrayList<>();

        List<Integer> vertexIndices = polygon.getVertexIndices();
        int quantityVertexes = vertexIndices.size();

        List<Integer> textureVertexIndices = polygon.getTextureVertexIndices();
        checkForCorrectListSize(textureVertexIndices, quantityVertexes, "текстурных координат");

        List<Integer> normalIndices = polygon.getNormalIndices();
        checkForCorrectListSize(normalIndices, quantityVertexes, "нормалей");


        for (int index = 1; index < vertexIndices.size() - 1; index++) {
            List<Integer> threeVertexIndices = getIndicesListForCurrentPolygon(vertexIndices, index);
            List<Integer> threeTextureVertexIndices = getIndicesListForCurrentPolygon(textureVertexIndices, index);
            List<Integer> threeNormalIndices = getIndicesListForCurrentPolygon(normalIndices, index);

            Polygon triangularPolygon = new Polygon(polygon.getModel());
            triangularPolygon.setVertexIndices((ArrayList<Integer>) threeVertexIndices);
            triangularPolygon.setTextureVertexIndices((ArrayList<Integer>) threeTextureVertexIndices);
            triangularPolygon.setNormalIndices((ArrayList<Integer>) threeNormalIndices);

            triangularPolygons.add(triangularPolygon);
        }

        return triangularPolygons;
    }

    private static void checkForCorrectListSize(List<Integer> list, int expectedSize, String listName) {
        if (list.size() != 0 && list.size() != expectedSize) {
            throw new IllegalArgumentException("Некорректное количество " + listName + " в полигоне");
        }
    }

    private static List<Integer> getIndicesListForCurrentPolygon(List<Integer> list, int indexSecondVertex) {
        List<Integer> indices = new ArrayList<>();

        if (list.size() != 0) {
            indices.add(list.get(0));
            indices.add(list.get(indexSecondVertex));
            indices.add(list.get(indexSecondVertex + 1));
        }

        return indices;
    }

    public static void recalculateNormals(Model model) {
        model.normals.clear();

        for (int i = 0; i < model.vertices.size(); i++) {
            model.normals.add(calculateNormalForVertexInModel(model, i));
        }
    }

    protected static ThreeDimensionalVector calculateNormalForPolygon(final Polygon polygon, final Model model){

        List<Integer> vertexIndices = polygon.getVertexIndices();
        int verticesCount = vertexIndices.size();


        ThreeDimensionalVector vector1 = (ThreeDimensionalVector) model.vertices.get(vertexIndices.get(1)).subtraction(model.vertices.get(vertexIndices.get(0)));
        ThreeDimensionalVector vector2 = (ThreeDimensionalVector) model.vertices.get(vertexIndices.get(0)).subtraction(model.vertices.get(vertexIndices.get(verticesCount - 1)));

        return vector1.vectorProduct(vector2);
    }

    protected static ThreeDimensionalVector calculateNormalForVertexInModel(final Model model, final int vertexIndex) {
        List<ThreeDimensionalVector> saved = new ArrayList<>();

        for (Polygon polygon : model.polygons) {
            if (polygon.getVertexIndices().contains(vertexIndex)) {
                ThreeDimensionalVector polygonNormal = calculateNormalForPolygon(polygon, model);
                if (polygonNormal.length() > 0) {
                    saved.add(polygonNormal);
                }
            }
        }

        if (saved.isEmpty()) {
            return new ThreeDimensionalVector(0,0,0);
        }
        ThreeDimensionalVector curr = new ThreeDimensionalVector(0,0,0);
        for(ThreeDimensionalVector v : saved){
            curr = (ThreeDimensionalVector) curr.addition(v);
        }

        return  (ThreeDimensionalVector) curr.scale(1.0/saved.size());
    }

    public static List<Polygon> triangulatePolygons(List<Polygon> initialPolygons) {
        List<Polygon> triangulationPolygons = new ArrayList<>();

        for (Polygon polygon : initialPolygons) {
            triangulationPolygons.addAll(Triangulation.triangulation(polygon));
        }

        return triangulationPolygons;
    }

}