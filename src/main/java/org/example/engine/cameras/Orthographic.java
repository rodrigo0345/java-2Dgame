package org.example.engine.cameras;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Orthographic implements Camera {
    private Matrix4f projectionMatrix = new Matrix4f();
    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f cache_ProjectionViewMatrix = new Matrix4f();
    private Vector3f m_Position = new Vector3f();
    private float m_Rotation = 0.0f;

    public Orthographic(float left, float right, float top, float bottom){
        projectionMatrix = new Matrix4f().ortho(left, right, bottom, top, -1.0f, 1.0f);
        cache_ProjectionViewMatrix = projectionMatrix.mul(viewMatrix);
    }

    public void SetProjectionMatrix(float left, float right, float top, float bottom){
        projectionMatrix = new Matrix4f().ortho(left, right, bottom, top, -1.0f, 1.0f);
        recalculateViewMatrix();
    }

    @Override
    public Matrix4f GetProjectionMatrix() {
        return projectionMatrix;
    }

    @Override
    public Matrix4f GetViewMatrix() {
        return viewMatrix;
    }

    @Override
    public Matrix4f GetProjectionViewMatrix() {
        return cache_ProjectionViewMatrix;
    }

    @Override
    public Vector3f GetPosition() {
        return m_Position;
    }

    @Override
    public void SetPosition(Vector3f position) {
        m_Position = position;
        recalculateViewMatrix();
    }

    @Override
    public Vector3f GetRotation() {
        return new Vector3f(0.0f, m_Rotation, 0.0f);
    }

    @Override
    public void SetRotation(float rotationY) {
        m_Rotation = rotationY;
        recalculateViewMatrix();
    }

    private void recalculateViewMatrix() {
        Matrix4f transform = new Matrix4f().translate(m_Position)
                .rotateY((float)Math.toRadians(m_Rotation));
        viewMatrix = transform.invert();
        projectionMatrix = new Matrix4f();
        cache_ProjectionViewMatrix = projectionMatrix.mul(viewMatrix);
    }

    @Override
    public void LookAt(Vector3f target) {
        Vector3f direction = new Vector3f(target).sub(m_Position);
        direction.y = 0; // We only care about the horizontal direction for orthographic

        // Calculate angle
        float angle = (float) Math.atan2(direction.z, direction.x);
        angle = (float) Math.toDegrees(angle);

        SetRotation(angle);
    }
}
