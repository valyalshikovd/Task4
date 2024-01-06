package com.cgvsu.render_engine;

import com.cgvsu.Math.AffineTransormation.AffineTransformation;
import com.cgvsu.Math.Matrix.FourDimensionalMatrix;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.NDimensionalVector;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.model.Model;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.cgvsu.model.Model.multiplyMatrix4ByVector3;
import static com.cgvsu.render_engine.GraphicConveyor.rotateScaleTranslate;

public class Scene {
    private Camera camera;
    private int width;
    private int height;
    private FourDimensionalMatrix modelMatrix;
    private FourDimensionalMatrix viewMatrix;
    private FourDimensionalMatrix projectionMatrix;
    private NDimensionalMatrix modelViewProjectionMatrix;
    public String currentModelName = null;
    private final Map<String, Camera> addedCameras = new HashMap<>();
    private final Map<String, Model> loadedModels = new HashMap<>();
    private final Map<String, Image> loadedTextures = new HashMap<>();
    private final Map<String, ThreeDimensionalVector> lightSources = new HashMap<>();





    public Map<String, ThreeDimensionalVector> getLightSources() {
        return lightSources;
    }

    public Scene(Camera camera, int width, int height) {
        lightSources.put("default ", new ThreeDimensionalVector(30,10,1));
        this.camera = camera;
        this.width = width;
        this.height = height;
        Zbuffer.setNewZbuffer(width, height);
        this.modelMatrix = rotateScaleTranslate();
        this.viewMatrix = camera.getViewMatrix();
        this.projectionMatrix = camera.getProjectionMatrix();
        this.modelViewProjectionMatrix = modelMatrix;
        modelViewProjectionMatrix = (NDimensionalMatrix) modelViewProjectionMatrix.multiplyMatrix(viewMatrix);
        modelViewProjectionMatrix = (NDimensionalMatrix) modelViewProjectionMatrix.multiplyMatrix(projectionMatrix);
    }
    public Map<String, Camera> getAddedCameras() {
        return addedCameras;
    }
    public Map<String, Model> getLoadedModels() {
        return loadedModels;
    }
    public void setCamera(Camera camera, int width, int height) {
        this.camera = camera;
        this.height = height;
        this.width = width;

        this.modelMatrix = rotateScaleTranslate();
        this.viewMatrix = camera.getViewMatrix();
        this.projectionMatrix = camera.getProjectionMatrix();

        this.modelViewProjectionMatrix = modelMatrix;
        modelViewProjectionMatrix = (NDimensionalMatrix) modelViewProjectionMatrix.multiplyMatrix(viewMatrix);
        this.modelViewProjectionMatrix = (NDimensionalMatrix) modelViewProjectionMatrix.multiplyMatrix(projectionMatrix);
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void drawAllMeshes(GraphicsContext g) {
        sceneUpdate();
        for (String model : loadedModels.keySet()) {
            loadedModels.get(model).draw(g);
        }
    }
    public void setCurrentCamera(Camera camera) {
        setCamera(camera, width,height);
    }
    public Map<String, Image> getLoadedTextures() {
        return loadedTextures;
    }
    public Camera getCamera() {
        return camera;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public FourDimensionalMatrix getModelMatrix() {
        return modelMatrix;
    }
    public FourDimensionalMatrix getViewMatrix() {
        return viewMatrix;
    }
    public FourDimensionalMatrix getProjectionMatrix() {
        return projectionMatrix;
    }
    public NDimensionalMatrix getModelViewProjectionMatrix() {
        return modelViewProjectionMatrix;
    }
    public String getCurrentModelName() {
        return currentModelName;
    }
    private void sceneUpdate(){
        System.out.println(Arrays.toString(camera.getPosition().getArrValues()));
        this.modelMatrix = rotateScaleTranslate();
        this.viewMatrix = camera.getViewMatrix();
        this.projectionMatrix = camera.getProjectionMatrix();
        this.modelViewProjectionMatrix = modelMatrix;
        modelViewProjectionMatrix = (NDimensionalMatrix) modelViewProjectionMatrix.multiplyMatrix(viewMatrix);
        this.modelViewProjectionMatrix = (NDimensionalMatrix) modelViewProjectionMatrix.multiplyMatrix(projectionMatrix);
        Zbuffer.clearBuffer();
    }
}
