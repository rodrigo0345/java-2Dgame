package org.example.engine.cameras;

import org.example.engine.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Orthographic implements Camera {
    private final Window window;
    private Matrix4f projectionMatrix = new Matrix4f();
    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f cache_ProjectionViewMatrix = new Matrix4f();
    private Vector3f m_Position = new Vector3f();
    private float zoom = 5.0f;
    private float m_Rotation = 0.0f;

    public Orthographic(Window win, float left, float right, float top, float bottom) {
        projectionMatrix = new Matrix4f().ortho(left, right, bottom, top, -1.0f, 1.0f);
        cache_ProjectionViewMatrix = projectionMatrix.mul(viewMatrix);
        this.window = win;
    }

    public void SetProjectionMatrix(float left, float right, float top, float bottom) {
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
        float aspectRatio = this.window.GetAspectRatio();
        float left = -zoom * aspectRatio;
        float right = zoom * aspectRatio;
        float top = zoom;
        float bottom = -zoom;

        Matrix4f transform = new Matrix4f().translate(m_Position)
                .rotateY((float) Math.toRadians(m_Rotation));
        viewMatrix = transform.invert();

        // Recalculate the projection matrix based on zoom and aspect ratio
        projectionMatrix = new Matrix4f().ortho(left, right, bottom, top, -1.0f, 1.0f);

        // Calculate the projection-view matrix
        cache_ProjectionViewMatrix = projectionMatrix.mul(viewMatrix);
    }

    public void SetZoom(float zoom) {
        this.zoom = zoom;
        recalculateViewMatrix();
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
