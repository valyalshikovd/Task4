package com.cgvsu.render_engine;

import java.util.ArrayList;
import java.util.Arrays;

import com.cgvsu.Math.Matrix.FourDimensionalMatrix;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.FourDimensionalVector;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.Vector;
import javafx.scene.canvas.GraphicsContext;
import javax.vecmath.*;
import com.cgvsu.model.Model;
import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height)
    {
        FourDimensionalMatrix modelMatrix = rotateScaleTranslate();
        FourDimensionalMatrix viewMatrix =  camera.getViewMatrix();
        FourDimensionalMatrix projectionMatrix = camera.getProjectionMatrix();

        NDimensionalMatrix modelViewProjectionMatrix = modelMatrix;
        modelViewProjectionMatrix = (NDimensionalMatrix)  modelViewProjectionMatrix.multiplyMatrix(viewMatrix);
        modelViewProjectionMatrix = (NDimensionalMatrix)  modelViewProjectionMatrix.multiplyMatrix(projectionMatrix);

        final int nPolygons = mesh.polygons.size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.polygons.get(polygonInd).getVertexIndices().size();

            ArrayList<Point2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {


                ThreeDimensionalVector vertex = mesh.vertices.get(mesh.polygons.get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                ThreeDimensionalVector vertexVecmath = new ThreeDimensionalVector(vertex.getA(), vertex.getB(), vertex.getC());
                Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex), width, height);

                resultPoints.add(resultPoint);
            }

            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).x,
                        resultPoints.get(vertexInPolygonInd - 1).y,
                        resultPoints.get(vertexInPolygonInd).x,
                        resultPoints.get(vertexInPolygonInd).y);
            }

            if (nVerticesInPolygon > 0)
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).x,
                        resultPoints.get(nVerticesInPolygon - 1).y,
                        resultPoints.get(0).x,
                        resultPoints.get(0).y);
        }
    }

    public static ThreeDimensionalVector multiplyMatrix4ByVector3(final NDimensionalMatrix matrix, final ThreeDimensionalVector vertex) {
        final double x = (vertex.getA() * matrix.getMatrixInVectors()[0].getArrValues()[0]) + (vertex.getB() * matrix.getMatrixInVectors()[1].getArrValues()[0]) + (vertex.getC() * matrix.getMatrixInVectors()[2].getArrValues()[0]) + matrix.getMatrixInVectors()[3].getArrValues()[0];
        final double y = (vertex.getA() * matrix.getMatrixInVectors()[0].getArrValues()[1]) + (vertex.getB() * matrix.getMatrixInVectors()[1].getArrValues()[1]) + (vertex.getC() * matrix.getMatrixInVectors()[2].getArrValues()[1]) + matrix.getMatrixInVectors()[3].getArrValues()[1];
        final double z = (vertex.getA() * matrix.getMatrixInVectors()[0].getArrValues()[2]) + (vertex.getB() * matrix.getMatrixInVectors()[1].getArrValues()[2]) + (vertex.getC() * matrix.getMatrixInVectors()[2].getArrValues()[2]) + matrix.getMatrixInVectors()[3].getArrValues()[2];
        final double w = (vertex.getA() * matrix.getMatrixInVectors()[0].getArrValues()[3]) + (vertex.getB() * matrix.getMatrixInVectors()[1].getArrValues()[3]) + (vertex.getC() * matrix.getMatrixInVectors()[2].getArrValues()[3]) + matrix.getMatrixInVectors()[3].getArrValues()[3];
        Vector tmpVector = matrix.multiplyVector(new FourDimensionalVector(vertex.getA(), vertex.getB(), vertex.getC(), 1));
        //      System.out.println(Arrays.toString(tmpVector.getArrValues()));
        //      System.out.println(x + " " +  y + " " +  z + " " + w);
        //      System.out.println(x / w + " " +  y / w + " " +  z / w);
        //      System.out.println(Arrays.toString(new ThreeDimensionalVector(tmpVector.getArrValues()[0] / tmpVector.getArrValues()[3], tmpVector.getArrValues()[1] / tmpVector.getArrValues()[3], tmpVector.getArrValues()[2] / tmpVector.getArrValues()[3]).getArrValues()));
        return new ThreeDimensionalVector(tmpVector.getArrValues()[0] / w, tmpVector.getArrValues()[1] / w, tmpVector.getArrValues()[2] / w);
    }
}