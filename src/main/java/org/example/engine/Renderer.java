package org.example.engine;
import org.example.engine.cameras.Camera;
import org.example.game.collisions.CollidingBox;
import org.example.game.entities.EntityInterface;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL45.*;
import org.lwjgl.opengl.GL15.*;
import org.lwjgl.opengl.GL33.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.Callbacks.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;




public class Renderer {
    public static boolean debug = true;
    private static class SceneData{
        Matrix4f ProjectionViewMatrix = new Matrix4f();
    }

    private static final SceneData sceneData = new SceneData();

    public static void Init(){
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

    public static void BeginScene(Camera camera){
        sceneData.ProjectionViewMatrix = camera.GetProjectionViewMatrix();
    }

    public static void EndScene(){}

    public static void Submit(EntityInterface entity){

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

}
