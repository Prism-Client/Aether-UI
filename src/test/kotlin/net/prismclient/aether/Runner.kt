package net.prismclient.aether

import net.prismclient.aether.ui.UICore
import net.prismclient.aether.ui.UICore.Properties.activeScreen
import net.prismclient.aether.ui.UICore.Properties.updateMouse
import net.prismclient.aether.ui.UICore.Properties.updateSize
import net.prismclient.aether.ui.callback.UICoreCallback
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNLEFT
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNTOP
import net.prismclient.aether.ui.util.UIKey
import net.prismclient.aether.ui.util.extensions.asRGBA
import net.prismclient.aether.ui.util.extensions.renderer
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWScrollCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.Platform
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.round

/**
 * An example runner class with LWJGL 3 & glfw
 */
object Runner {
    var mouseX = 0.0
        private set
    var mouseY = 0.0
        private set
    var framebufferWidth = 0
    var framebufferHeight = 0
    var contentScaleX = 0f
    var contentScaleY = 0f
    var actualFps = 0
    var fps = 0
    var lastSecond = System.currentTimeMillis()
    var core: UICore? = null

    @JvmStatic
    fun main(args: Array<String>) {
        GLFWErrorCallback.createPrint().set()

        if (!GLFW.glfwInit())
            throw RuntimeException("Failed to init GLFW")
        if (Platform.get() === Platform.MACOSX) {
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2)
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE)
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)
        }

        GLFW.glfwWindowHint(GLFW.GLFW_SCALE_TO_MONITOR, GLFW.GLFW_TRUE)

        val window = GLFW.glfwCreateWindow(1000, 600, "Demo", MemoryUtil.NULL, MemoryUtil.NULL)

        if (window == MemoryUtil.NULL) {
            GLFW.glfwTerminate()
            throw RuntimeException()
        }

        GLFW.glfwSetKeyCallback(window) { windowHandle: Long, keyCode: Int, scancode: Int, action: Int, mods: Int ->
            if (keyCode == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
                UICore.displayScreen(ExampleScreen())
            }
        }

        GLFW.glfwSetMouseButtonCallback(window) { window1: Long, button: Int, action: Int, mods: Int ->
            //if (button == 0) { // 1 = Down, 0 = Up // 0 - LMB, 1 - RMB //
                if (action == 1) {
                    core!!.mousePressed(button)
                } else {
                    core!!.mouseReleased()
                }
            //}
        }
        GLFW.glfwSetCursorPosCallback(window) { handle: Long, xpos: Double, ypos: Double ->
            mouseX = xpos
            mouseY = ypos
            core!!.mouseMoved(mouseX.toFloat(), mouseY.toFloat())
        }

        GLFW.glfwSetWindowContentScaleCallback(window) { handle: Long, xscale: Float, yscale: Float ->
            contentScaleX = xscale
            contentScaleY = yscale
        }

        GLFW.glfwSetFramebufferSizeCallback(window) { handle: Long, width: Int, height: Int ->
            framebufferWidth = (width / contentScaleX).toInt()
            framebufferHeight = (height / contentScaleY).toInt()
            core!!.update(framebufferWidth.toFloat(), framebufferHeight.toFloat(), max(contentScaleX, contentScaleY))
        }



        GLFW.glfwSetScrollCallback(window) { window1: Long, xoffset: Double, yoffset: Double ->
            core!!.mouseScrolled(yoffset.toFloat())
        }

        GLFW.glfwMakeContextCurrent(window)
        GL.createCapabilities()
        GLFW.glfwSetTime(0.0)
        GLFW.glfwSwapInterval(0)

        core = UICore(NanoVGRenderer())

        MemoryStack.stackPush().use {
            val fw = it.mallocInt(1)
            val fh = it.mallocInt(1)
            val sx = it.mallocFloat(1)
            val sy = it.mallocFloat(1)
            GLFW.glfwGetFramebufferSize(window, fw, fh)
            framebufferWidth = fw[0]
            framebufferHeight = fh[0]
            GLFW.glfwGetWindowContentScale(window, sx, sy)
            contentScaleX = sx[0]
            contentScaleY = sy[0]

            core!!.update(framebufferHeight / contentScaleX, framebufferHeight / contentScaleY, max(contentScaleX, contentScaleY))
        }

        UICore.displayScreen(ExampleScreen())

        while (!GLFW.glfwWindowShouldClose(window)) {
            updateSize(framebufferWidth / contentScaleX, framebufferHeight / contentScaleY, max(contentScaleX, contentScaleY))
            updateMouse(mouseX.toFloat(), mouseY.toFloat())

            core!!.renderFrames()

            GL11.glViewport(0, 0, framebufferWidth, framebufferHeight)
            GL11.glClearColor(0.3f, 0.3f, 0.3f, 0f)
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT)

            renderer {
                beginFrame(UICore.width, UICore.height, UICore.devicePxRatio)
                renderImage("background", 0f, 0f, UICore.width, UICore.height)
                endFrame()
            }

            core!!.render()

            GLFW.glfwSwapBuffers(window)
            GLFW.glfwPollEvents()
        }
        GL.setCapabilities(null)
        Callbacks.glfwFreeCallbacks(window)
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)!!.free()
    }
}