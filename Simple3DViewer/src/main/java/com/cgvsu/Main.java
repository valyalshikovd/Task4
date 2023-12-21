package com.cgvsu;

import com.cgvsu.Math.Matrix.FourDimensionalMatrix;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.FourDimensionalVector;
import com.cgvsu.Math.Vectors.NDimensionalVector;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.GraphicConveyor;
import com.cgvsu.render_engine.RenderEngine;

import javax.vecmath.Point2f;
import java.util.Arrays;

import static com.cgvsu.render_engine.GraphicConveyor.rotateScaleTranslate;
import static com.cgvsu.render_engine.GraphicConveyor.vertexToPoint;

public class Main {
    public static void main(String[] args) {
//
//        FourDimensionalMatrix fourDimensionalMatrix = new FourDimensionalMatrix(
//                new FourDimensionalVector(1, 5, 9 ,13),
//                new FourDimensionalVector(2, 6, 10 ,14),
//                new FourDimensionalVector(3, 7, 11 ,15),
//                new FourDimensionalVector(4, 8, 12 ,16)
//        );
//
//        ThreeDimensionalVector threeDimensionalVector = new ThreeDimensionalVector(100,100,100);
//
//        System.out.println(Arrays.toString(RenderEngine.multiplyMatrix4ByVector3(fourDimensionalMatrix, threeDimensionalVector).getArrValues()));

        Camera camera = new Camera(
                new ThreeDimensionalVector(0, 0, 100),
                new ThreeDimensionalVector(0, 0, 0),
                1.0F, 1, 0.01F, 100);

        FourDimensionalMatrix viewMatrix =  camera.getViewMatrix();
        FourDimensionalMatrix projectionMatrix = camera.getProjectionMatrix();
        FourDimensionalMatrix modelMatrix = rotateScaleTranslate();
        camera.getProjectionMatrix().printMatrix();

        NDimensionalMatrix modelViewProjectionMatrix = modelMatrix;
        modelViewProjectionMatrix = (NDimensionalMatrix)  modelViewProjectionMatrix.multiplyMatrix(viewMatrix);
        modelViewProjectionMatrix = (NDimensionalMatrix)  modelViewProjectionMatrix.multiplyMatrix(projectionMatrix);

        Point2f resultPoint = vertexToPoint(RenderEngine.multiplyMatrix4ByVector3(modelViewProjectionMatrix, new ThreeDimensionalVector(200, 3, 123)), 800, 800);
        System.out.println(Arrays.toString(RenderEngine.multiplyMatrix4ByVector3(modelViewProjectionMatrix, new ThreeDimensionalVector(200, 3, 123)).getArrValues()));
modelViewProjectionMatrix.printMatrix();
        System.out.println(resultPoint);


        System.out.println("-------------------");

        GraphicConveyor.lookAt(new ThreeDimensionalVector(1,2,3), new ThreeDimensionalVector(5,3,8)).printMatrix();
    }
}
