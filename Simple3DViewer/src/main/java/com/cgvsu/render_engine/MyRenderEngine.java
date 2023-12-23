package com.cgvsu.render_engine;

import com.cgvsu.Math.Matrix.FourDimensionalMatrix;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.model.Model;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

import static com.cgvsu.render_engine.GraphicConveyor.rotateScaleTranslate;

public class MyRenderEngine {
    private Camera camera;
    private List<Model> listMesh = new ArrayList<>();
    private int width;
    private int height;
    private FourDimensionalMatrix modelMatrix;
    private FourDimensionalMatrix viewMatrix;
    private FourDimensionalMatrix projectionMatrix;
    private NDimensionalMatrix modelViewProjectionMatrix;

    public List<Model> getListMesh() {
        return listMesh;
    }

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

//        Path fileName = Path.of("C:/Users/770vd//Desktop/WrapHead.obj");
//
//        try {
//            String fileContent = Files.readString(fileName);
//            Model mesh = ObjReader.read(fileContent);
//            mesh.m = (NDimensionalMatrix) new AffineTransformation().translate(-20, 1, 1);
//            listMesh.add(mesh);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

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

    public void addMesh(Model mesh) {
        listMesh.add(mesh);
    }

    public void drawAllMeshes(GraphicsContext g) {
        for (Model model : listMesh) {
            model.draw(g, modelViewProjectionMatrix, width, height);
        }
    }


}
