package com.vihanga.malinda.svmf.camera;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Vector2f position;

    public Camera(Vector2f position,
                  Matrix4f projectionMatrix,
                  Matrix4f viewMatrix) {
        this.position = position;
        this.projectionMatrix = projectionMatrix;
        this.viewMatrix = viewMatrix;
        this.adjustProjection();
    }

    public void adjustProjection() {
        this.projectionMatrix.identity();
        this.projectionMatrix.ortho(0.0f,
                                    32.0f * 40.0f,
                                    0.0f,
                                    32.0f * 21.0f,
                                    0.0f,
                                    100.0f);

    }

    public Matrix4f getViewMatrix() {
        // Camera is looking at -1.0f in z direction
        Vector3f cameraFront = new Vector3f(0.0f,
                                            0.0f,
                                            -1.0f);
        Vector3f cameraUP = new Vector3f(0.0f,
                                         1.0f,
                                         0.0f);

        //Where the camera is located at
        Vector3f cameraEye = new Vector3f(position.x,
                                          position.y,
                                          20.0f);
        this.viewMatrix.identity();
        this.viewMatrix = this.viewMatrix.lookAt(cameraEye,
                                                 cameraFront.add(position.x,
                                                                 position.y,
                                                                 0.0f),
                                                 cameraUP);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Vector2f getPosition() {
        return position;
    }
}
