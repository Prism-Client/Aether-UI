package net.prismclient.aether;

import net.prismclient.aether.ui.renderer.UIRenderer;
import net.prismclient.aether.ui.renderer.builder.UIRendererDSL;
import net.prismclient.aether.ui.util.UIKey;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * An example runner class with LWJGL 3 & glfw
 */
public class Example {
    private static double mouseX, mouseY;
    public static int framebufferWidth, framebufferHeight;
    public static float contentScaleX, contentScaleY;
    public static int actualFps = 0;
    public static int fps = 0;
    public static long lastSecond = System.currentTimeMillis();
    public static UICore core;

    public static void main(String[] args) {
        GLFWErrorCallback.createPrint().set();
        if (!glfwInit()) {
            throw new RuntimeException("Failed to init GLFW");
        }

        if (Platform.get() == Platform.MACOSX) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        }
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);

        long window = glfwCreateWindow(1000, 600, "Prism UI", NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException();
        }

        glfwSetCharCallback(window, (window1, codepoint) -> {
            core.keyPressed(UIKey.UNKNOWN, (char) codepoint);
        });

        glfwSetKeyCallback(window, (windowHandle, keyCode, scancode, action, mods) -> {
            if (keyCode == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                UICore.setActiveScreen(new ExampleScreen());
                return;
            }

            if (keyCode == GLFW_KEY_LEFT_SHIFT) {
                if (action == GLFW_PRESS) {
                    core.setShiftHeld(true);
                } else if (action == GLFW_RELEASE) {
                    core.setShiftHeld(false);
                }
            }
            if (keyCode == GLFW_KEY_LEFT_CONTROL) {
                if (action == GLFW_PRESS) {
                    core.setCtrlHeld(true);
                } else if (action == GLFW_RELEASE) {
                    core.setCtrlHeld(false);
                }
            }

            if (action == GLFW_PRESS || action == GLFW_REPEAT) {
                String keyName = glfwGetKeyName(keyCode, scancode);
                var key = keyMap.get(keyCode);

                if (key != null && key == UIKey.SPACE) {
                    return;
                }

                core.keyPressed(key == null ? UIKey.DEFAULT : key, '\u0000');
            }
        });

        glfwSetMouseButtonCallback(window, (window1, button, action, mods) -> {
            if (button == 0) { // 1 = Down, 0 = Up // 0 - LMB, 1 - RMB //
                if (action == 1) {
                    core.mousePressed((float) mouseX, (float) mouseY);
                } else if (action == 0) {
                    core.mouseReleased((float) mouseX, (float) mouseY);
                }
            }
        });

        glfwSetCursorPosCallback(window, (handle, xpos, ypos) -> {
            mouseX = xpos;
            mouseY = ypos;
            core.mouseMoved((float) mouseX, (float) mouseY);
        });

        glfwSetScrollCallback(window, (handle, xscroll, yscroll) -> {
            core.mouseScrolled((float) mouseX, (float) mouseY, (float) yscroll);
        });

        glfwSetFramebufferSizeCallback(window, (handle, w, h) -> {
            framebufferWidth = w;
            framebufferHeight = h;
            UICore.Companion.setWidth(w / contentScaleX);
            UICore.Companion.setHeight(h / contentScaleX);
            core.update();
        });
        glfwSetWindowContentScaleCallback(window, (handle, xscale, yscale) -> {
            contentScaleX = xscale;
            contentScaleY = yscale;
            core.update();

            UICore.Companion.setContentScaleX(contentScaleX);
            UICore.Companion.setContentScaleY(contentScaleY);
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer fw = stack.mallocInt(1);
            IntBuffer fh = stack.mallocInt(1);
            FloatBuffer sx = stack.mallocFloat(1);
            FloatBuffer sy = stack.mallocFloat(1);

            glfwGetFramebufferSize(window, fw, fh);
            framebufferWidth = fw.get(0);
            framebufferHeight = fh.get(0);

            glfwGetWindowContentScale(window, sx, sy);
            contentScaleX = sx.get(0);
            contentScaleY = sy.get(0);

            UICore.Companion.setContentScaleX(contentScaleX);
            UICore.Companion.setContentScaleY(contentScaleY);
        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glfwSetTime(0);
        glfwSwapInterval(0);

        // Create the core
        core = new UICore(new DefaultRenderer());

        UICore.Companion.setWidth(framebufferWidth / contentScaleX);
        UICore.Companion.setHeight(framebufferHeight / contentScaleY);

        // Set the active screen (set width, and height first)

        UICore.setActiveScreen(new ExampleScreen());

        while (!glfwWindowShouldClose(window)) {
            double t = glfwGetTime();
            long n = System.nanoTime();
            int width = (int) (framebufferWidth / contentScaleX);
            int height = (int) (framebufferHeight / contentScaleY);

            UICore.Companion.setWidth(width);
            UICore.Companion.setHeight(height);
            UICore.Companion.setMouseX((float) mouseX);
            UICore.Companion.setMouseY((float) mouseY);

            core.renderContent();

            glViewport(0, 0, framebufferWidth, framebufferHeight);
            glClearColor(0f, 0f, 0f, 0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

            core.beginFrame(width, height, Math.max(contentScaleX, contentScaleY));
            core.render(width, height);
            core.endFrame();

            glfwSwapBuffers(window);
            glfwPollEvents();

            if (lastSecond + 1000L <= System.currentTimeMillis()) {
                lastSecond = System.currentTimeMillis();
                fps = actualFps;
                actualFps = 0;
            } else {
                actualFps++;
            }
        }
        //core.delete();

        GL.setCapabilities(null);

        glfwFreeCallbacks(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static float getWidth() {
        return framebufferWidth / contentScaleX;
    }

    public static float getHeight() {
        return framebufferHeight / contentScaleY;
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }


    private static final Map<Integer, UIKey> keyMap = new HashMap<>();

    static {
        keyMap.put(GLFW.GLFW_KEY_ESCAPE, UIKey.ESCAPE);
        keyMap.put(GLFW.GLFW_KEY_BACKSPACE, UIKey.BACKSPACE);
        keyMap.put(GLFW.GLFW_KEY_Q, UIKey.Q);
        keyMap.put(GLFW.GLFW_KEY_W, UIKey.W);
        keyMap.put(GLFW.GLFW_KEY_E, UIKey.E);
        keyMap.put(GLFW.GLFW_KEY_R, UIKey.R);
        keyMap.put(GLFW.GLFW_KEY_T, UIKey.T);
        keyMap.put(GLFW.GLFW_KEY_Y, UIKey.Y);
        keyMap.put(GLFW.GLFW_KEY_U, UIKey.U);
        keyMap.put(GLFW.GLFW_KEY_I, UIKey.I);
        keyMap.put(GLFW.GLFW_KEY_O, UIKey.O);
        keyMap.put(GLFW.GLFW_KEY_P, UIKey.P);
        keyMap.put(GLFW.GLFW_KEY_ENTER, UIKey.ENTER);
        keyMap.put(GLFW.GLFW_KEY_LEFT_CONTROL, UIKey.CONTROL_LEFT);
        keyMap.put(GLFW.GLFW_KEY_A, UIKey.A);
        keyMap.put(GLFW.GLFW_KEY_S, UIKey.S);
        keyMap.put(GLFW.GLFW_KEY_D, UIKey.D);
        keyMap.put(GLFW.GLFW_KEY_F, UIKey.F);
        keyMap.put(GLFW.GLFW_KEY_G, UIKey.G);
        keyMap.put(GLFW.GLFW_KEY_H, UIKey.H);
        keyMap.put(GLFW.GLFW_KEY_J, UIKey.J);
        keyMap.put(GLFW.GLFW_KEY_K, UIKey.K);
        keyMap.put(GLFW.GLFW_KEY_L, UIKey.L);
        keyMap.put(GLFW.GLFW_KEY_LEFT_SHIFT, UIKey.SHIFT_LEFT);
        keyMap.put(GLFW.GLFW_KEY_Z, UIKey.Z);
        keyMap.put(GLFW.GLFW_KEY_X, UIKey.X);
        keyMap.put(GLFW.GLFW_KEY_C, UIKey.C);
        keyMap.put(GLFW.GLFW_KEY_V, UIKey.V);
        keyMap.put(GLFW.GLFW_KEY_B, UIKey.B);
        keyMap.put(GLFW.GLFW_KEY_N, UIKey.N);
        keyMap.put(GLFW.GLFW_KEY_M, UIKey.M);
        keyMap.put(GLFW.GLFW_KEY_RIGHT_SHIFT, UIKey.SHIFT_RIGHT);
        keyMap.put(GLFW.GLFW_KEY_LEFT_ALT, UIKey.ALT_LEFT);
        keyMap.put(GLFW.GLFW_KEY_SPACE, UIKey.SPACE);
        keyMap.put(GLFW.GLFW_KEY_UP, UIKey.ARROW_UP);
        keyMap.put(GLFW.GLFW_KEY_DOWN, UIKey.ARROW_DOWN);
        keyMap.put(GLFW_KEY_LEFT, UIKey.ARROW_LEFT);
        keyMap.put(GLFW_KEY_RIGHT, UIKey.ARROW_RIGHT);
        keyMap.put(GLFW.GLFW_KEY_1, UIKey.ONE);
        keyMap.put(GLFW.GLFW_KEY_2, UIKey.TWO);
        keyMap.put(GLFW.GLFW_KEY_3, UIKey.THREE);
        keyMap.put(GLFW.GLFW_KEY_4, UIKey.FOUR);
        keyMap.put(GLFW.GLFW_KEY_5, UIKey.FIVE);
        keyMap.put(GLFW.GLFW_KEY_6, UIKey.SIX);
        keyMap.put(GLFW.GLFW_KEY_7, UIKey.SEVEN);
        keyMap.put(GLFW.GLFW_KEY_8, UIKey.EIGHT);
        keyMap.put(GLFW.GLFW_KEY_9, UIKey.NINE);
        keyMap.put(GLFW.GLFW_KEY_0, UIKey.ZERO);
        keyMap.put(GLFW.GLFW_KEY_SLASH, UIKey.SLASH);
        keyMap.put(GLFW.GLFW_KEY_F1, UIKey.F1);
        keyMap.put(GLFW.GLFW_KEY_F2, UIKey.F2);
        keyMap.put(GLFW.GLFW_KEY_F3, UIKey.F3);
        keyMap.put(GLFW.GLFW_KEY_F4, UIKey.F4);
        keyMap.put(GLFW.GLFW_KEY_F5, UIKey.F5);
        keyMap.put(GLFW.GLFW_KEY_F6, UIKey.F6);
        keyMap.put(GLFW.GLFW_KEY_F7, UIKey.F7);
        keyMap.put(GLFW.GLFW_KEY_F8, UIKey.F8);
        keyMap.put(GLFW.GLFW_KEY_F9, UIKey.F9);
        keyMap.put(GLFW.GLFW_KEY_F10, UIKey.F10);
        keyMap.put(GLFW.GLFW_KEY_F11, UIKey.F11);
        keyMap.put(GLFW.GLFW_KEY_F12, UIKey.F12);
    }
}
