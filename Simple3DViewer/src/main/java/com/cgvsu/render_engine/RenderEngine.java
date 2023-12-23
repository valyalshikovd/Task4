package com.cgvsu.render_engine;

import java.util.ArrayList;

import com.cgvsu.Math.AffineTransormation.AffineTransformation;
import com.cgvsu.Math.Matrix.FourDimensionalMatrix;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.model.Model;
import javafx.scene.canvas.GraphicsContext;
import javax.vecmath.*;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    private static GraphicsContext graphicsContext;
    private static Camera camera;
    private static Model mesh;
    private static int width;
    private static int height;

    public static void render(
            final GraphicsContext graphicsContextInput,
            final Camera cameraInput,
            final Model meshInput,
            final int widthInput,
            final int heightInput)
    {
        graphicsContext = graphicsContext;
        FourDimensionalMatrix modelMatrix = rotateScaleTranslate();
        FourDimensionalMatrix viewMatrix =  camera.getViewMatrix();
        FourDimensionalMatrix projectionMatrix = camera.getProjectionMatrix();

        NDimensionalMatrix modelViewProjectionMatrix = modelMatrix;
        modelViewProjectionMatrix = (NDimensionalMatrix)  modelViewProjectionMatrix.multiplyMatrix(viewMatrix);
        modelViewProjectionMatrix = (NDimensionalMatrix)  modelViewProjectionMatrix.multiplyMatrix(projectionMatrix);

        NDimensionalMatrix m = (NDimensionalMatrix) new AffineTransformation().translate(20,1,1);
        final int nPolygons = mesh.polygons.size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.polygons.get(polygonInd).getVertexIndices().size();

            ArrayList<Point2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {


                ThreeDimensionalVector vertex = mesh.vertices.get(mesh.polygons.get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                ThreeDimensionalVector vertexVecmath = new ThreeDimensionalVector(vertex.getA(), vertex.getB(), vertex.getC());
                Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, multiplyMatrix4ByVector3(m, vertex)), width, height);

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

    public static void drawPolygon(){

    }


    public static ThreeDimensionalVector multiplyMatrix4ByVector3(final NDimensionalMatrix matrix, final ThreeDimensionalVector vertex) {
        final double x  = matrix.getMatrixInVectors()[0].getArrValues()[0] * vertex.getA() +  matrix.getMatrixInVectors()[1].getArrValues()[0] * vertex.getB() + matrix.getMatrixInVectors()[2].getArrValues()[0] * vertex.getC() + matrix.getMatrixInVectors()[3].getArrValues()[0];
        final double y  = matrix.getMatrixInVectors()[0].getArrValues()[1] * vertex.getA() +  matrix.getMatrixInVectors()[1].getArrValues()[1] * vertex.getB() + matrix.getMatrixInVectors()[2].getArrValues()[1] * vertex.getC() + matrix.getMatrixInVectors()[3].getArrValues()[1];
        final double z = matrix.getMatrixInVectors()[0].getArrValues()[2] * vertex.getA() +  matrix.getMatrixInVectors()[1].getArrValues()[2] * vertex.getB() + matrix.getMatrixInVectors()[2].getArrValues()[2] * vertex.getC() + matrix.getMatrixInVectors()[3].getArrValues()[2];
        final double w  = matrix.getMatrixInVectors()[0].getArrValues()[3] * vertex.getA() +  matrix.getMatrixInVectors()[1].getArrValues()[3] * vertex.getB() + matrix.getMatrixInVectors()[2].getArrValues()[3] * vertex.getC() + matrix.getMatrixInVectors()[3].getArrValues()[3];

        return new ThreeDimensionalVector(x/w, y/w, z/w);
    }

}