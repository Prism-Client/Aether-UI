package net.prismclient.aether

import net.prismclient.aether.UICore.Companion.activeScreen
import net.prismclient.aether.UICore.Companion.instance
import net.prismclient.aether.ui.callback.UICoreCallback
import net.prismclient.aether.ui.util.UIKey
import net.prismclient.aether.ui.util.extensions.asRGBA
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.Platform

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
        if (!GLFW.glfwInit()) {
            throw RuntimeException("Failed to init GLFW")
        }
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
        GLFW.glfwSetCharCallback(window) { window1: Long, codepoint: Int -> core!!.keyPressed(UIKey.UNKNOWN, codepoint.toChar()) }
        GLFW.glfwSetKeyCallback(window) { windowHandle: Long, keyCode: Int, scancode: Int, action: Int, mods: Int ->
            if (keyCode == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
                activeScreen = ExampleScreen()
                return@glfwSetKeyCallback
            }
            if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT) {
                if (action == GLFW.GLFW_PRESS) {
                    core!!.shiftHeld = true
                } else if (action == GLFW.GLFW_RELEASE) {
                    core!!.shiftHeld = false
                }
            }
            if (keyCode == GLFW.GLFW_KEY_LEFT_CONTROL) {
                if (action == GLFW.GLFW_PRESS) {
                    core!!.ctrlHeld = true
                } else if (action == GLFW.GLFW_RELEASE) {
                    core!!.ctrlHeld = false
                }
            }
            if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
                val keyName = GLFW.glfwGetKeyName(keyCode, scancode)
                val key = keyMap[keyCode]
                if (key != null && key === UIKey.SPACE) {
                    return@glfwSetKeyCallback
                }
                core!!.keyPressed(key ?: UIKey.DEFAULT, '\u0000')
            }
        }
        GLFW.glfwSetMouseButtonCallback(window) { window1: Long, button: Int, action: Int, mods: Int ->
            if (button == 0) { // 1 = Down, 0 = Up // 0 - LMB, 1 - RMB //
                if (action == 1) {
                    core!!.mousePressed(mouseX.toFloat(), mouseY.toFloat())
                } else if (action == 0) {
                    core!!.mouseReleased(mouseX.toFloat(), mouseY.toFloat())
                }
            }
        }
        GLFW.glfwSetCursorPosCallback(window) { handle: Long, xpos: Double, ypos: Double ->
            mouseX = xpos
            mouseY = ypos
            core!!.mouseMoved(mouseX.toFloat(), mouseY.toFloat())
        }
        GLFW.glfwSetScrollCallback(window) { handle: Long, xscroll: Double, yscroll: Double -> core!!.mouseScrolled(mouseX.toFloat(), mouseY.toFloat(), yscroll.toFloat()) }
        GLFW.glfwSetFramebufferSizeCallback(window) { handle: Long, w: Int, h: Int ->
            framebufferWidth = w
            framebufferHeight = h
            UICore.width = w / contentScaleX
            UICore.height = h / contentScaleX
            core!!.update()
        }
        GLFW.glfwSetWindowContentScaleCallback(window) { handle: Long, xscale: Float, yscale: Float ->
            contentScaleX = xscale
            contentScaleY = yscale
            core!!.update()
            UICore.contentScaleX = contentScaleX
            UICore.contentScaleY = contentScaleY
        }
        val stack = MemoryStack.stackPush()

        try {
            val fw = stack.mallocInt(1)
            val fh = stack.mallocInt(1)
            val sx = stack.mallocFloat(1)
            val sy = stack.mallocFloat(1)
            GLFW.glfwGetFramebufferSize(window, fw, fh)
            framebufferWidth = fw[0]
            framebufferHeight = fh[0]
            GLFW.glfwGetWindowContentScale(window, sx, sy)
            contentScaleX = sx[0]
            contentScaleY = sy[0]
            UICore.contentScaleX = contentScaleX
            UICore.contentScaleY = contentScaleY
        } finally {
            stack.pop()
        }

        GLFW.glfwMakeContextCurrent(window)
        GL.createCapabilities()
        GLFW.glfwSetTime(0.0)
        GLFW.glfwSwapInterval(0)

        // Create the core
        core = UICore(NanoVGRenderer(), UICoreCallbackImpl())
        UICore.width = framebufferWidth / contentScaleX
        UICore.height = framebufferHeight / contentScaleY

        // Set the active screen (set width, and height first)
        activeScreen = ExampleScreen()
        while (!GLFW.glfwWindowShouldClose(window)) {
            val t = GLFW.glfwGetTime()
            val n = System.nanoTime()
            val width = (framebufferWidth / contentScaleX).toInt()
            val height = (framebufferHeight / contentScaleY).toInt()
            UICore.width = width.toFloat()
            UICore.height = height.toFloat()
            UICore.mouseX = mouseX.toFloat()
            UICore.mouseY = mouseY.toFloat()
            core!!.renderContent()
            GL11.glViewport(0, 0, framebufferWidth, framebufferHeight)
            GL11.glClearColor(0.3f, 0.3f, 0.3f, 0f)
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT)
            core!!.beginFrame(width.toFloat(), height.toFloat(), Math.max(contentScaleX, contentScaleY))
            core!!.render(width.toFloat(), height.toFloat())
            core!!.endFrame()
            GLFW.glfwSwapBuffers(window)
            GLFW.glfwPollEvents()
            if (lastSecond + 1000L <= System.currentTimeMillis()) {
                lastSecond = System.currentTimeMillis()
                fps = actualFps
                actualFps = 0
            } else {
                actualFps++
            }
            instance.animationLock.release()
        }
        GL.setCapabilities(null)
        Callbacks.glfwFreeCallbacks(window)
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)!!.free()
    }

    val width: Float
        get() = framebufferWidth / contentScaleX
    val height: Float
        get() = framebufferHeight / contentScaleY
    private val keyMap: MutableMap<Int, UIKey> = HashMap()

    init {
        keyMap[GLFW.GLFW_KEY_ESCAPE] = UIKey.ESCAPE
        keyMap[GLFW.GLFW_KEY_BACKSPACE] = UIKey.BACKSPACE
        keyMap[GLFW.GLFW_KEY_Q] = UIKey.Q
        keyMap[GLFW.GLFW_KEY_W] = UIKey.W
        keyMap[GLFW.GLFW_KEY_E] = UIKey.E
        keyMap[GLFW.GLFW_KEY_R] = UIKey.R
        keyMap[GLFW.GLFW_KEY_T] = UIKey.T
        keyMap[GLFW.GLFW_KEY_Y] = UIKey.Y
        keyMap[GLFW.GLFW_KEY_U] = UIKey.U
        keyMap[GLFW.GLFW_KEY_I] = UIKey.I
        keyMap[GLFW.GLFW_KEY_O] = UIKey.O
        keyMap[GLFW.GLFW_KEY_P] = UIKey.P
        keyMap[GLFW.GLFW_KEY_ENTER] = UIKey.ENTER
        keyMap[GLFW.GLFW_KEY_LEFT_CONTROL] = UIKey.CONTROL_LEFT
        keyMap[GLFW.GLFW_KEY_A] = UIKey.A
        keyMap[GLFW.GLFW_KEY_S] = UIKey.S
        keyMap[GLFW.GLFW_KEY_D] = UIKey.D
        keyMap[GLFW.GLFW_KEY_F] = UIKey.F
        keyMap[GLFW.GLFW_KEY_G] = UIKey.G
        keyMap[GLFW.GLFW_KEY_H] = UIKey.H
        keyMap[GLFW.GLFW_KEY_J] = UIKey.J
        keyMap[GLFW.GLFW_KEY_K] = UIKey.K
        keyMap[GLFW.GLFW_KEY_L] = UIKey.L
        keyMap[GLFW.GLFW_KEY_LEFT_SHIFT] = UIKey.SHIFT_LEFT
        keyMap[GLFW.GLFW_KEY_Z] = UIKey.Z
        keyMap[GLFW.GLFW_KEY_X] = UIKey.X
        keyMap[GLFW.GLFW_KEY_C] = UIKey.C
        keyMap[GLFW.GLFW_KEY_V] = UIKey.V
        keyMap[GLFW.GLFW_KEY_B] = UIKey.B
        keyMap[GLFW.GLFW_KEY_N] = UIKey.N
        keyMap[GLFW.GLFW_KEY_M] = UIKey.M
        keyMap[GLFW.GLFW_KEY_RIGHT_SHIFT] = UIKey.SHIFT_RIGHT
        keyMap[GLFW.GLFW_KEY_LEFT_ALT] = UIKey.ALT_LEFT
        keyMap[GLFW.GLFW_KEY_SPACE] = UIKey.SPACE
        keyMap[GLFW.GLFW_KEY_UP] = UIKey.ARROW_UP
        keyMap[GLFW.GLFW_KEY_DOWN] = UIKey.ARROW_DOWN
        keyMap[GLFW.GLFW_KEY_LEFT] = UIKey.ARROW_LEFT
        keyMap[GLFW.GLFW_KEY_RIGHT] = UIKey.ARROW_RIGHT
        keyMap[GLFW.GLFW_KEY_1] = UIKey.ONE
        keyMap[GLFW.GLFW_KEY_2] = UIKey.TWO
        keyMap[GLFW.GLFW_KEY_3] = UIKey.THREE
        keyMap[GLFW.GLFW_KEY_4] = UIKey.FOUR
        keyMap[GLFW.GLFW_KEY_5] = UIKey.FIVE
        keyMap[GLFW.GLFW_KEY_6] = UIKey.SIX
        keyMap[GLFW.GLFW_KEY_7] = UIKey.SEVEN
        keyMap[GLFW.GLFW_KEY_8] = UIKey.EIGHT
        keyMap[GLFW.GLFW_KEY_9] = UIKey.NINE
        keyMap[GLFW.GLFW_KEY_0] = UIKey.ZERO
        keyMap[GLFW.GLFW_KEY_SLASH] = UIKey.SLASH
        keyMap[GLFW.GLFW_KEY_F1] = UIKey.F1
        keyMap[GLFW.GLFW_KEY_F2] = UIKey.F2
        keyMap[GLFW.GLFW_KEY_F3] = UIKey.F3
        keyMap[GLFW.GLFW_KEY_F4] = UIKey.F4
        keyMap[GLFW.GLFW_KEY_F5] = UIKey.F5
        keyMap[GLFW.GLFW_KEY_F6] = UIKey.F6
        keyMap[GLFW.GLFW_KEY_F7] = UIKey.F7
        keyMap[GLFW.GLFW_KEY_F8] = UIKey.F8
        keyMap[GLFW.GLFW_KEY_F9] = UIKey.F9
        keyMap[GLFW.GLFW_KEY_F10] = UIKey.F10
        keyMap[GLFW.GLFW_KEY_F11] = UIKey.F11
        keyMap[GLFW.GLFW_KEY_F12] = UIKey.F12
    }

    private class UICoreCallbackImpl : UICoreCallback {
        override fun getPixelColor(x: Float, y: Float): Int {
            val color = intArrayOf(0, 0, 0)
            GL11.glReadPixels(x.toInt(), framebufferHeight - y.toInt(), 1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_INT, color)
            return asRGBA(color[0], color[1], color[2], 255)
        }
    }
}