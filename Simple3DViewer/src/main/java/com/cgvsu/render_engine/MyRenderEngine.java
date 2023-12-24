package com.cgvsu.render_engine;

import com.cgvsu.Math.AffineTransormation.AffineTransformation;
import com.cgvsu.Math.Matrix.FourDimensionalMatrix;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import javafx.scene.canvas.GraphicsContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cgvsu.render_engine.GraphicConveyor.rotateScaleTranslate;

public class MyRenderEngine {
    private Camera camera;

    private int width;
    private int height;
    private FourDimensionalMatrix modelMatrix;
    private FourDimensionalMatrix viewMatrix;
    private FourDimensionalMatrix projectionMatrix;
    private NDimensionalMatrix modelViewProjectionMatrix;
    public String currentModelName = null;

    private Map<String, Model> loadedModels = new HashMap<>();



    public MyRenderEngine(Camera camera, int width, int height) {
        this.camera = camera;
        this.width = width;
        this.height = height;

        this.modelMatrix = rotateScaleTranslate();
        this.viewMatrix = camera.getViewMatrix();
        this.projectionMatrix = camera.getProjectionMatrix();

        this.modelViewProjectionMatrix = modelMatrix;
        modelViewProjectionMatrix = (NDimensionalMatrix) modelViewProjectionMatrix.multiplyMatrix(viewMatrix);
        modelViewProjectionMatrix = (NDimensionalMatrix) modelViewProjectionMatrix.multiplyMatrix(projectionMatrix);

        Path fileName = Path.of("C:/Users/770vd//Desktop/WrapHead.obj");

//        try {
//            String fileContent = Files.readString(fileName);
//            Model mesh = ObjReader.read(fileContent);
//            mesh.affineMatrix = (NDimensionalMatrix) new AffineTransformation().translate(20, 1, 1);
//            listMesh.add(mesh);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
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
        modelViewProjectionMatrix = (NDimensionalMatrix) modelViewProjectionMatrix.multiplyMatrix(projectionMatrix);

    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public void drawAllMeshes(GraphicsContext g) {
        for (String model : loadedModels.keySet()) {
            loadedModels.get(model).draw(g, modelViewProjectionMatrix, width, height);
        }
    }


}
