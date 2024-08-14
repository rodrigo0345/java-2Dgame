package org.example;

import org.example.engine.Renderer;
import org.example.engine.Shader;
import org.example.engine.VertexArray;
import org.example.engine.Window;
import org.example.engine.buffers.BufferElement;
import org.example.engine.buffers.BufferLayout;
import org.example.engine.buffers.IndexBuffer;
import org.example.engine.buffers.VertexBuffer;
import org.example.engine.cameras.Camera;
import org.example.engine.cameras.Orthographic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class HelloWorld {

    // The window handle (not used in the current code)
    private static Window window;
    private int width, height;

    // Method to run the application
    public static void run() {
        // Initialize the window
        window = Window.InitWindow(1024, 720, "Some name");
        window.Use();
        loop();
    }

    private static void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

        VertexArray vao = new VertexArray();
        vao.Bind();
        float[] quad = {
                -0.5f, -0.5f, 0.0f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.0f,  1.0f, 0.0f,
                0.5f, 0.5f, 0.0f,   1.0f, 1.0f,
                -0.5f, 0.5f, 0.0f,  0.0f, 1.0f
        };
        int[] indices = {
                0, 1, 2, 2, 3, 0
        };
        VertexBuffer vb = new VertexBuffer(quad);
        IndexBuffer ib = new IndexBuffer(indices);
        BufferLayout layout = new BufferLayout(
                new ArrayList<BufferElement>(
                        Arrays.asList(
                                new BufferElement("a_Position", BufferElement.ShaderDataType.Float3, false),
                                new BufferElement("a_TexCoord", BufferElement.ShaderDataType.Float2, false)
                        )
                )
        );
        vb.SetLayout(layout);
        vao.AddVertexBuffer(vb);
        vao.SetIndexBuffer(ib);

        String vertexShader = """
              #version 330 core
              layout(location = 0) in vec3 a_Position;
              layout(location = 1) in vec2 a_TexCoord;
    
              out vec2 v_TexCoord;
              uniform mat4 u_ViewProjection;
    
              void main() {
                v_TexCoord = a_TexCoord;
                gl_Position = u_ViewProjection * vec4(a_Position, 1.0);
              }
        """;
        String fragmentShader = """
          #version 330 core

          layout(location = 0) out vec4 o_Color;
          in vec2 v_TexCoord;

          void main() {
            o_Color = vec4(v_TexCoord, 0.0, 1.0f);
          }
        """;

        Shader shader = new Shader(vertexShader, fragmentShader);
        float aspectRatio = window.GetAspectRatio();
        Camera camera = new Orthographic(-aspectRatio, aspectRatio, -1.0f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window.GetInternalWindow()) ) {
            Renderer.Clear();
            Renderer.SetClearColor(.13f, .12f, .14f, .0f);

            Renderer.BeginScene(camera);
            Renderer.Submit(vao, shader);
            Renderer.EndScene();

            window.OnUpdate(camera);
        }

        window.Close();
    }

    // Main method to start the application
    public static void main(String[] args) {
        HelloWorld.run();
    }
}
