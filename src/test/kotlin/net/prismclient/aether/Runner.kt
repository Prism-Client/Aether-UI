package net.prismclient.aether


import net.prismclient.aether.screens.ExampleScreen
import net.prismclient.aether.screens.TestingScreen
import net.prismclient.aether.ui.UICore
import net.prismclient.aether.ui.UICore.Properties.updateMouse
import net.prismclient.aether.ui.util.extensions.renderer
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.Platform
import kotlin.math.max

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

        if (!glfwInit())
            throw RuntimeException("Failed to init GLFW")
        if (Platform.get() === Platform.MACOSX) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        }

        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE)

        val window = glfwCreateWindow(1000, 600, "Demo", MemoryUtil.NULL, MemoryUtil.NULL)

        if (window == MemoryUtil.NULL) {
            glfwTerminate()
            throw RuntimeException()
        }

        glfwSetKeyCallback(window) { _: Long, keyCode: Int, _: Int, action: Int, _: Int ->
            if (keyCode == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                UICore.displayScreen(ExampleScreen())
            }
        }

        glfwSetMouseButtonCallback(window) { _: Long, button: Int, action: Int, _: Int ->
            core!!.mousePressed(button, action == GLFW_RELEASE)
        }
        glfwSetCursorPosCallback(window) { handle: Long, xpos: Double, ypos: Double ->
            mouseX = xpos
            mouseY = ypos
            core!!.mouseMoved(mouseX.toFloat(), mouseY.toFloat())
        }

        glfwSetWindowContentScaleCallback(window) { handle: Long, xscale: Float, yscale: Float ->
            contentScaleX = xscale
            contentScaleY = yscale
        }

        glfwSetFramebufferSizeCallback(window) { handle: Long, width: Int, height: Int ->
            framebufferWidth =width
            framebufferHeight = height
            core!!.update(width / contentScaleX, height / contentScaleY, max(contentScaleX, contentScaleY))
        }

        glfwSetScrollCallback(window) { handle: Long, xscroll: Double, yscroll: Double -> core!!.mouseScrolled(yscroll.toFloat()) }

        glfwMakeContextCurrent(window)
        GL.createCapabilities()
        glfwSetTime(0.0)
        glfwSwapInterval(0)

        core = UICore(NanoVGRenderer())

        MemoryStack.stackPush().use {
            val fw = it.mallocInt(1)
            val fh = it.mallocInt(1)
            val sx = it.mallocFloat(1)
            val sy = it.mallocFloat(1)
            glfwGetFramebufferSize(window, fw, fh)
            framebufferWidth = fw[0]
            framebufferHeight = fh[0]
            glfwGetWindowContentScale(window, sx, sy)
            contentScaleX = sx[0]
            contentScaleY = sy[0]

            core!!.update(framebufferWidth / contentScaleX, framebufferHeight / contentScaleY, max(contentScaleX, contentScaleY))
        }

        UICore.displayScreen(TestingScreen())
//        UICore.displayScreen(ExampleScreen())

        while (!glfwWindowShouldClose(window)) {
            updateMouse(mouseX.toFloat(), mouseY.toFloat())

            core!!.renderFrames()

            GL11.glViewport(0, 0, framebufferWidth, framebufferHeight)
            GL11.glClearColor(0.3f, 0.3f, 0.3f, 0f)
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT)

            renderer {
                beginFrame(framebufferWidth.toFloat() / contentScaleX, framebufferHeight.toFloat() / contentScaleY, UICore.devicePxRatio)
                color(-1)
                renderImage("background", 0f, 0f, UICore.width, UICore.height)
                endFrame()
            }

            core!!.render()

            glfwSwapBuffers(window)
            glfwPollEvents()
        }
        GL.setCapabilities(null)
        Callbacks.glfwFreeCallbacks(window)
        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
    }
}