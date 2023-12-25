package com.cgvsu.model;

import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import com.cgvsu.Rasterization.TriangleRasterization;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javax.vecmath.Point2f;
import java.util.ArrayList;

import static com.cgvsu.render_engine.GraphicConveyor.vertexToPoint;


public class Polygon {

    private ArrayList<Integer> vertexIndices;
    private ArrayList<Integer> textureVertexIndices;
    private ArrayList<Integer> normalIndices;


    public Polygon() {
        vertexIndices = new ArrayList<Integer>();
        textureVertexIndices = new ArrayList<Integer>();
        normalIndices = new ArrayList<Integer>();
    }

    public void setVertexIndices(ArrayList<Integer> vertexIndices) {
        assert vertexIndices.size() >= 3;
        this.vertexIndices = vertexIndices;
    }

    public void setTextureVertexIndices(ArrayList<Integer> textureVertexIndices) {
        assert textureVertexIndices.size() >= 3;
        this.textureVertexIndices = textureVertexIndices;
    }

    public void setNormalIndices(ArrayList<Integer> normalIndices) {
        assert normalIndices.size() >= 3;
        this.normalIndices = normalIndices;
    }



    public ArrayList<Integer> getVertexIndices() {
        return vertexIndices;
    }

    public ArrayList<Integer> getTextureVertexIndices() {
        return textureVertexIndices;
    }

    public ArrayList<Integer> getNormalIndices() {
        return normalIndices;
    }

    public void drawPolygon(GraphicsContext g, NDimensionalMatrix modelViewProjectionMatrix, Model mesh, NDimensionalMatrix m, int width, int height, boolean isFill){


        ArrayList<Point2f> resultPoints = new ArrayList<>();
        int nVerticesInPolygon = vertexIndices.size();
        for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
            ThreeDimensionalVector vertex =  mesh.vertices.get(vertexIndices.get(vertexInPolygonInd));
            Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, multiplyMatrix4ByVector3(m, vertex)), width, height);
            resultPoints.add(resultPoint);
        }

        if(isFill) {

            TriangleRasterization.drawTriangle(g.getPixelWriter(), new TwoDimensionalVector(resultPoints.get(0).x, resultPoints.get(0).y),
                    new TwoDimensionalVector(resultPoints.get(1).x, resultPoints.get(1).y),
                    new TwoDimensionalVector(resultPoints.get(2).x, resultPoints.get(2).y),
                    Color.BLACK, Color.BLACK, Color.BLACK);
        }else {
        for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
            g.strokeLine(
                    resultPoints.get(vertexInPolygonInd - 1).x,
                    resultPoints.get(vertexInPolygonInd - 1).y,
                    resultPoints.get(vertexInPolygonInd).x,
                    resultPoints.get(vertexInPolygonInd).y);
        }
        if (nVerticesInPolygon> 0)
            g.strokeLine(
                    resultPoints.get(nVerticesInPolygon - 1).x,
                    resultPoints.get(nVerticesInPolygon - 1).y,
                    resultPoints.get(0).x,
                    resultPoints.get(0).y);
        }
    }
    public static ThreeDimensionalVector multiplyMatrix4ByVector3(final NDimensionalMatrix matrix, final ThreeDimensionalVector vertex) {
        final double x  = matrix.getMatrixInVectors()[0].getArrValues()[0] * vertex.getA() +  matrix.getMatrixInVectors()[1].getArrValues()[0] * vertex.getB() + matrix.getMatrixInVectors()[2].getArrValues()[0] * vertex.getC() + matrix.getMatrixInVectors()[3].getArrValues()[0];
        final double y  = matrix.getMatrixInVectors()[0].getArrValues()[1] * vertex.getA() +  matrix.getMatrixInVectors()[1].getArrValues()[1] * vertex.getB() + matrix.getMatrixInVectors()[2].getArrValues()[1] * vertex.getC() + matrix.getMatrixInVectors()[3].getArrValues()[1];
        final double z = matrix.getMatrixInVectors()[0].getArrValues()[2] * vertex.getA() +  matrix.getMatrixInVectors()[1].getArrValues()[2] * vertex.getB() + matrix.getMatrixInVectors()[2].getArrValues()[2] * vertex.getC() + matrix.getMatrixInVectors()[3].getArrValues()[2];
        final double w  = matrix.getMatrixInVectors()[0].getArrValues()[3] * vertex.getA() +  matrix.getMatrixInVectors()[1].getArrValues()[3] * vertex.getB() + matrix.getMatrixInVectors()[2].getArrValues()[3] * vertex.getC() + matrix.getMatrixInVectors()[3].getArrValues()[3];

        return new ThreeDimensionalVector(x/w, y/w, z/w);
    }
}
