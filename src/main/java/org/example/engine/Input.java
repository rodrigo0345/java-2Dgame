package org.example.engine;

import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.GLFW.*;

public class Input {
    public static boolean IsKeyPressed(Window window, int keyCode, boolean mouse) {
        long win = window.GetInternalWindow();
        int status = mouse ? glfwGetMouseButton(win, keyCode) : glfwGetKey(win, keyCode);
        return status == GLFW_PRESS || status == GLFW_REPEAT;
    }
}
