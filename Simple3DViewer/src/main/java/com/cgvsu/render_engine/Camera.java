package com.cgvsu.render_engine;
import com.cgvsu.Math.Matrix.FourDimensionalMatrix;
import com.cgvsu.Math.Vectors.*;



public class Camera {

    public Camera(
            final Vector position,
            final Vector target,
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        this.position = position;
        this.target = target;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public void setPosition(final Vector position) {
        this.position = position;
    }

    public void setTarget(final ThreeDimensionalVector target) {
        this.target = target;
    }

    public void setAspectRatio(final float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getTarget() {
        return target;
    }

    public void movePosition(final Vector translation) {
        this.position = this.position.addition(translation);
    }

    public void moveTarget(final ThreeDimensionalVector translation) {
        this.target = target.addition(translation);
    }

    FourDimensionalMatrix getViewMatrix() {
        return GraphicConveyor.lookAt(position,target);
    }

    FourDimensionalMatrix getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }

    private Vector position;
    private Vector target;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;
}