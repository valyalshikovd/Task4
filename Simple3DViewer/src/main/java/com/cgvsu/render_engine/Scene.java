package com.cgvsu.render_engine;

import com.cgvsu.Math.Matrix.FourDimensionalMatrix;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.model.Model;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

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

    private Map<String, Camera> addedCameras = new HashMap<>();
    private Map<String, Model> loadedModels = new HashMap<>();
    private Map<String, Image> loadedTextures = new HashMap<>();
    private Map<String, ThreeDimensionalVector> lightSourses = new HashMap<>();

    private Zbuffer zbuffer;


    public Map<String, ThreeDimensionalVector> getLightSourses() {
        return lightSourses;
    }

    public Scene(Camera camera, int width, int height) {
        lightSourses.put("default ", new ThreeDimensionalVector(30,10,1));
        this.camera = camera;
        this.width = width;
        this.height = height;

        this.zbuffer = new Zbuffer(width, height);



        this.modelMatrix = rotateScaleTranslate();
        this.viewMatrix = camera.getViewMatrix();
        this.projectionMatrix = camera.getProjectionMatrix();

        this.modelViewProjectionMatrix = modelMatrix;
        modelViewProjectionMatrix = (NDimensionalMatrix) modelViewProjectionMatrix.multiplyMatrix(viewMatrix);
        modelViewProjectionMatrix = (NDimensionalMatrix) modelViewProjectionMatrix.multiplyMatrix(projectionMatrix);

        Path fileName = Path.of("C:/Users/770vd//Desktop/WrapHead.obj");

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
        this.modelMatrix = rotateScaleTranslate();
        this.viewMatrix = camera.getViewMatrix();
        this.projectionMatrix = camera.getProjectionMatrix();

        this.modelViewProjectionMatrix = modelMatrix;
        modelViewProjectionMatrix = (NDimensionalMatrix) modelViewProjectionMatrix.multiplyMatrix(viewMatrix);
        this.modelViewProjectionMatrix = (NDimensionalMatrix) modelViewProjectionMatrix.multiplyMatrix(projectionMatrix);

        zbuffer.clearBuffer();

        for (String model : loadedModels.keySet()) {
            loadedModels.get(model).draw(g, modelViewProjectionMatrix, width, height, (HashMap<String, ThreeDimensionalVector>) lightSourses, zbuffer);
        }
    }

    public void setCurrentCamera(Camera camera) {
        // this.camera = camera;
        setCamera(camera, width,height);
    }

    public Map<String, Image> getLoadedTextures() {
        return loadedTextures;
    }

    public Camera getCamera() {
        return camera;
    }



}
