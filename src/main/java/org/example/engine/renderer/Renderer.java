package org.example.engine.renderer;

import org.example.engine.buffers.VertexArray;
import org.example.engine.cameras.Camera;
import org.example.engine.collisions.CollidingBox;
import org.example.engine.entities.EntityInterface;
import org.example.engine.shaders.Shader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Renderer {
    private static final SceneData sceneData = new SceneData();
    public static boolean debug = true;

    public static void Init() {
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
    }

    public static void Clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void SetClearColor(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
    }

    public static void DrawIndexed(VertexArray vertexArray) {
        vertexArray.Bind();
        GL11.glDrawElements(GL11.GL_TRIANGLES, vertexArray.GetIndexBuffer().GetCount(), GL11.GL_UNSIGNED_INT, 0);
    }

    public static void BeginScene(Camera camera) {
        sceneData.ProjectionViewMatrix = camera.GetProjectionViewMatrix();
    }

    public static void EndScene() {
    }

    public static void Submit(EntityInterface entity) {

        Shader shader = entity.getShader();
        VertexArray vao = entity.getVao();
        float[] transformData = entity.getTransform();
        float[] vpData = new float[16]; // A 4x4 matrix has 16 elements

        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Allocate a float buffer with 16 floats on the stack
            FloatBuffer buffer = stack.mallocFloat(16);
            // Put the matrix data into the buffer
            sceneData.ProjectionViewMatrix.get(buffer);
            // Extract the data from the buffer into the array
            buffer.get(vpData);

        }

        if (debug) {
            CollidingBox box = entity.getShape().GetCollidingBox();
            box.draw(vpData, transformData);
        }

        shader.bind();
        shader.uploadUniformMat4("u_ViewProjection", vpData);
        shader.uploadUniformMat4("u_Transform", transformData);
        Renderer.DrawIndexed(vao);
    }

    private static class SceneData {
        Matrix4f ProjectionViewMatrix = new Matrix4f();
    }

}
