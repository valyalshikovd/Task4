package com.cgvsu.render_engine;
import com.cgvsu.Math.Matrix.FourDimensionalMatrix;
import com.cgvsu.Math.Vectors.*;



public class Camera {

    public Camera(
            final ThreeDimensionalVector position,
            final ThreeDimensionalVector target,
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
        this.currentDistantionToTarget = position.length();
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

    public FourDimensionalMatrix getViewMatrix() {
        return GraphicConveyor.lookAt(position,target);
    }

    public FourDimensionalMatrix getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }

    public double getCurrentDistantionToTarget() {
        return currentDistantionToTarget;
    }

    public void setCurrentDistantionToTarget(double currentDistantionToTarget) {
        this.currentDistantionToTarget = currentDistantionToTarget;
    }

    private Vector position;
    private Vector target;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;
    private double currentDistantionToTarget;

}