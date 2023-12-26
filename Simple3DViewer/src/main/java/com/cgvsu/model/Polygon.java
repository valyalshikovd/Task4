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

    public void drawPolygon(GraphicsContext g, NDimensionalMatrix modelViewProjectionMatrix, Model mesh, NDimensionalMatrix m, int width, int height, Color isFill, ThreeDimensionalVector light, double[][] Zbuffer){


        ArrayList<ThreeDimensionalVector> resultPoints = new ArrayList<>();
        ArrayList<TwoDimensionalVector> textureVertexes = new ArrayList<>();
        int nVerticesInPolygon = vertexIndices.size();


        ArrayList<ThreeDimensionalVector> vectors = new ArrayList<>();
        for(Integer vert : vertexIndices){
            vectors.add(mesh.vertices.get(vert));
        }
        double lightCoeff  = -NormalUtils.normalPolygon(this, mesh.vertices).scalarProduct(mesh.vertices.get(this.vertexIndices.get(0)).subtraction(light));
        if(lightCoeff < 0){
            lightCoeff =0;
        }
        if(lightCoeff > 1){
            lightCoeff = 1;
        }
     //   System.out.println(lightCoeff);
        for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
            ThreeDimensionalVector vertex =  mesh.vertices.get(vertexIndices.get(vertexInPolygonInd));
            vertex = multiplyMatrix4ByVector3(modelViewProjectionMatrix, multiplyMatrix4ByVector3(m, vertex));
            Point2f resultPoint = vertexToPoint(vertex, width, height);
            resultPoints.add(new ThreeDimensionalVector(resultPoint.x, resultPoint.y, vertex.getC()));
            textureVertexes.add(mesh.textureVertices.get(textureVertexIndices.get(vertexInPolygonInd)));
        }

        if( isFill != Color.WHITE) {




            TriangleRasterization.drawTriangle(g.getPixelWriter(),
                    new ThreeDimensionalVector(resultPoints.get(0).getA(), resultPoints.get(0).getB(), resultPoints.get(0).getC()),
                    new ThreeDimensionalVector(resultPoints.get(1).getA(), resultPoints.get(1).getB(), resultPoints.get(1).getC()),
                    new ThreeDimensionalVector(resultPoints.get(2).getA(), resultPoints.get(2).getB(), resultPoints.get(2).getC()),
                    isFill,
                    textureVertexes, lightCoeff, Zbuffer);
        }else {
        for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
            g.strokeLine(
                    resultPoints.get(vertexInPolygonInd - 1).getA(),
                    resultPoints.get(vertexInPolygonInd - 1).getB(),
                    resultPoints.get(vertexInPolygonInd).getA(),
                    resultPoints.get(vertexInPolygonInd).getB());
        }
        if (nVerticesInPolygon> 0)
            g.strokeLine(
                    resultPoints.get(nVerticesInPolygon - 1).getA(),
                    resultPoints.get(nVerticesInPolygon - 1).getB(),
                    resultPoints.get(0).getA(),
                    resultPoints.get(0).getB());
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
