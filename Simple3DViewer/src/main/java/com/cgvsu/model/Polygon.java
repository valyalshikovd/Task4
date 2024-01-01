package com.cgvsu.model;

import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import com.cgvsu.Rasterization.TriangleRasterization;
import com.cgvsu.render_engine.Zbuffer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;


import static com.cgvsu.model.Model.multiplyMatrix4ByVector3;
import static com.cgvsu.render_engine.GraphicConveyor.vertexToSurface;


public class Polygon {

    private ArrayList<Integer> vertexIndices;
    private ArrayList<Integer> textureVertexIndices;
    private ArrayList<Integer> normalIndices;
    private boolean isTexturing;
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

    public void drawPolygon(GraphicsContext g,
                            NDimensionalMatrix modelViewProjectionMatrix,
                            Model mesh,
                            int width, int height,
                            Color fillingColor,
                            boolean isFill,
                            HashMap< String, ThreeDimensionalVector> light,
                            Zbuffer zbuffer, boolean isTexturing, Image image){


        ArrayList<ThreeDimensionalVector> resultPoints = new ArrayList<>();
        ArrayList<TwoDimensionalVector> textureVertexes = new ArrayList<>();
        int nVerticesInPolygon = vertexIndices.size();
        for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
            ThreeDimensionalVector vertex =  mesh.sceneVertices.get(vertexIndices.get(vertexInPolygonInd));
            vertex = vertexToSurface(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex), width, height);

            resultPoints.add(new ThreeDimensionalVector(vertex.getA(), vertex.getB(), vertex.getC()));

            if(isTexturing && mesh.image != null){
                textureVertexes.add(mesh.textureVertices.get(textureVertexIndices.get(vertexInPolygonInd)));
            }
        }


        if( isFill ) {
            double lightCoefficient = calculateLightCoefficient(mesh, light);
            TriangleRasterization.drawTriangle(g.getPixelWriter(),
                    new ThreeDimensionalVector(resultPoints.get(0).getA(), resultPoints.get(0).getB(), resultPoints.get(0).getC()),
                    new ThreeDimensionalVector(resultPoints.get(1).getA(), resultPoints.get(1).getB(), resultPoints.get(1).getC()),
                    new ThreeDimensionalVector(resultPoints.get(2).getA(), resultPoints.get(2).getB(), resultPoints.get(2).getC()),
                    fillingColor,
                    textureVertexes,
                    lightCoefficient,
                    zbuffer, image);
            textureVertexes.clear();
        }else {
            drawingPolygonNet(g, resultPoints);
        }
    }

    private double calculateLightCoefficient(Model mesh, HashMap< String, ThreeDimensionalVector> lightSourceVector ){
        double lightCoeff = 0;
        for(String lightName : lightSourceVector.keySet()){
            lightCoeff += -NormalUtils.normalPolygon(this, mesh.sceneVertices).
                    scalarProduct(mesh.sceneVertices.get(this.vertexIndices.get(0)).
                            subtraction(lightSourceVector.get(lightName)));
        }
        lightCoeff = lightCoeff * 0.5;
        if(lightCoeff < 0){
            lightCoeff = 0;
        }
        if(lightCoeff > 1){
            lightCoeff = 1;
        }
        return lightCoeff;
    }

    private void drawingPolygonNet(GraphicsContext g, ArrayList<ThreeDimensionalVector> resultPoints){
        for (int vertexInPolygonInd = 1; vertexInPolygonInd < vertexIndices.size(); ++vertexInPolygonInd) {
            g.strokeLine(
                    resultPoints.get(vertexInPolygonInd - 1).getA(),
                    resultPoints.get(vertexInPolygonInd - 1).getB(),
                    resultPoints.get(vertexInPolygonInd).getA(),
                    resultPoints.get(vertexInPolygonInd).getB());
        }
        if (vertexIndices.size()> 0)
            g.strokeLine(
                    resultPoints.get(vertexIndices.size() - 1).getA(),
                    resultPoints.get(vertexIndices.size() - 1).getB(),
                    resultPoints.get(0).getA(),
                    resultPoints.get(0).getB());
    }
}
