package org.example.engine;

import org.example.engine.cameras.Camera;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.Callbacks.*;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private long m_WindowHandle = 0L;
    private int m_Width;
    private int m_Height;
    private String m_Title;
    private boolean m_VsyncEnabled;

    public static Window InitWindow(int width, int height, String title) {
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

        // Set all errors to be displayed in the System.err
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        Window win = new Window(width, height, title);
        return win;
    }

    public long GetInternalWindow() {
        return m_WindowHandle;
    }

    private Window(int width, int height, String title) {
        assert width > 0 && height > 0 : "Width and Height cannot be negative";
        this.m_Width = width;
        this.m_Height = height;
        this.m_Title = title;

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        m_WindowHandle = glfwCreateWindow(m_Width, m_Height, title, NULL, NULL);
        if (m_WindowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(m_WindowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(m_WindowHandle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    m_WindowHandle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(m_WindowHandle);
        // Enable V-Sync
        glfwSwapInterval(0);
        m_VsyncEnabled = false;

        // Initialize OpenGL capabilities
        GL.createCapabilities();
        setupResizeCallback();
    }

    public void OnUpdate(Camera camera){
        camera.SetProjectionMatrix(-GetAspectRatio(), GetAspectRatio(), 1.0f, -1.0f);
        glfwSwapBuffers(m_WindowHandle); // swap the color buffers
        glfwPollEvents();
    }

    public void Close() {
        // Free callbacks and destroy the window
        glfwDestroyWindow(m_WindowHandle);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void ToggleVSync() {
        glfwMakeContextCurrent(m_WindowHandle);
        m_VsyncEnabled = !m_VsyncEnabled;
        glfwSwapInterval(m_VsyncEnabled ? 1 : 0);
    }

    public void Use() {
        glfwMakeContextCurrent(m_WindowHandle);
        glfwShowWindow(m_WindowHandle);
    }

    public int GetWidth() {
        return m_Width;
    }

    public int GetHeight() {
        return m_Height;
    }

    public float GetAspectRatio() {
        return (float)m_Width / (float)m_Height;
    }

    private void setupResizeCallback() {
        glfwSetFramebufferSizeCallback(m_WindowHandle, (window, width, height) -> {
            m_Width = width;
            m_Height = height;
            glViewport(0, 0, width, height);
            // Update the aspect ratio or any other rendering parameters that depend on window size
        });
    }
}
