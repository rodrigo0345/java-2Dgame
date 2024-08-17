package org.example;

import org.example.engine.*;
import org.example.engine.buffers.BufferElement;
import org.example.engine.buffers.BufferLayout;
import org.example.engine.buffers.IndexBuffer;
import org.example.engine.buffers.VertexBuffer;
import org.example.engine.cameras.Camera;
import org.example.engine.cameras.Orthographic;
import org.example.game.GameEngine;
import org.example.game.characters.Zombie;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import java.util.ArrayList;
import java.util.Arrays;
import static org.lwjgl.glfw.GLFW.*;

public class HelloWorld {

    // The window handle (not used in the current code)
    private static Window window;
    private static final Vector3f cameraPosition = new Vector3f();
    private static Vector3f oldCameraPosition = new Vector3f();
    private static Camera camera;
    private static final Vector2f spriteCoords = new Vector2f();
    private static final Vector2f oldSpriteCoords = new Vector2f();
    private static boolean isAttacking = false;

    // Method to run the application
    public static void run() {
        // Initialize the window
        window = Window.InitWindow(1024, 720, "Zombie Survival");
        window.Use();
        // loop();
        loopNew();
    }

    private static void loopNew(){
        Renderer.Init();

        camera = new Orthographic(window, -window.GetAspectRatio(), window.GetAspectRatio(), 1.0f, -1.0f);
        GameEngine game = new GameEngine();

        ArrayList<Zombie> zombies = new ArrayList<>();
        Zombie zombie = new Zombie(new Vector3f(0.0f, 0.0f, 0.0f));
        Zombie zombie2 = new Zombie(new Vector3f(0.3f, 1.0f, 0.0f));
        Zombie zombie3 = new Zombie(new Vector3f(-.3f, -1.0f, 0.0f));

        zombies.add(zombie);
        zombies.add(zombie2);
        zombies.add(zombie3);

        game.AddEntity(zombie);
        game.AddEntity(zombie2);
        game.AddEntity(zombie3);

        while (!window.shouldClose()){
            Timestamp.UpdateDeltaTime();
            Renderer.Clear();
            Renderer.SetClearColor(.13f, .12f, .14f, .0f);

            ProcessInput();

            for(Zombie z: zombies){
                z.resetAnimation();
            }

            try{
                if(oldCameraPosition.equals(cameraPosition)) {}
                else {
                    oldCameraPosition = (Vector3f) cameraPosition.clone();
                    zombie.setPosition(oldCameraPosition);
                }
            } catch (Exception e){
            }

            if (isAttacking) {
                zombie.Attack();
            }


            Renderer.BeginScene(camera);
            game.DrawEntities();
            Renderer.EndScene();

            window.OnUpdate(camera);
        }
        window.Close();
    }

    public static void ProcessInput(){
        isAttacking = false;
        if (Input.IsKeyPressed(window, GLFW_KEY_W, false)) {
            cameraPosition.y += 3f * Timestamp.GetDeltaTime();
        }
        if (Input.IsKeyPressed(window, GLFW_KEY_S, false)) {
            cameraPosition.y -= (float)3 * Timestamp.GetDeltaTime();
        }
        if (Input.IsKeyPressed(window, GLFW_KEY_A, false)) {
            cameraPosition.x -= (float)3 * Timestamp.GetDeltaTime();
        }
        if (Input.IsKeyPressed(window, GLFW_KEY_D, false)) {
            cameraPosition.x += (float)3 * Timestamp.GetDeltaTime();
        }
        if (Input.IsKeyPressed(window, GLFW_MOUSE_BUTTON_LEFT, true)) {
            isAttacking = true;
        }

        camera.SetPosition(cameraPosition);
    }

    // Main method to start the application
    public static void main(String[] args) {
        HelloWorld.run();
    }
}
