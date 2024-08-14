package org.example.engine.cameras;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface Camera {
    Matrix4f GetProjectionMatrix();
    Matrix4f GetViewMatrix();
    Matrix4f GetProjectionViewMatrix();
    Vector3f GetPosition();
    void SetPosition(Vector3f position);
    Vector3f GetRotation();
    void SetRotation(float rotationY);
    void SetProjectionMatrix(float left, float right, float top, float bottom);
}
