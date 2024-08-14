package org.example.engine;

import static org.lwjgl.glfw.GLFW.*;

public class Timestamp {
    public static double LastFrameTime = 0.0f;
    public static double DeltaTime = 0.0f;
    public static double CurrentFPS = 0.0f;

    public static double UpdateDeltaTime(){
        double time = (float) glfwGetTime();
        DeltaTime = time - LastFrameTime;
        LastFrameTime = time;
        CurrentFPS = 1 / (float) LastFrameTime;
        return LastFrameTime;
    }

    public static double GetDeltaTime(){
        return DeltaTime;
    }
}
