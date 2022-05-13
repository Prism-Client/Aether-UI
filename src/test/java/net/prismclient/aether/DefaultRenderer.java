package net.prismclient.aether;

import net.prismclient.aether.ui.renderer.UIRenderer;
import net.prismclient.aether.ui.renderer.image.UIImageData;
import net.prismclient.aether.ui.renderer.other.UIContentFBO;
import net.prismclient.aether.ui.util.extensions.ColorKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.nanovg.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBTruetype;

import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.opengl.GL11.*;

// TODO: Scaled Content for framebuffer
/**
 * [DefaultRenderer] is an example implementation for the renderer
 * of Aether UI which uses NanoVG.
 *
 * @author sen
 * @since 4/23/2022
 */
public class DefaultRenderer extends UIRenderer {
    private static final int maxRows = 100; /* Max rows created from [wrapString] */
    private final long ctx = NanoVGGL3.nnvgCreate(NanoVGGL3.NVG_ANTIALIAS);

    private final HashMap<UIContentFBO, NVGLUFramebuffer> fbos = new HashMap<>();

    private final NVGColor color = NVGColor.create();
    private final NVGColor none = NVGColor.create();
    private final NVGColor outlineColor = NVGColor.create();

    private final NVGPaint paint = NVGPaint.create();

    public DefaultRenderer() {
        nvgRGBAf(0, 0, 0, 0, none);
    }

    @Override
    public void beginFrame(float width, float height, float devicePixelRatio) {
        nvgBeginFrame(ctx, width, height, devicePixelRatio);
    }

    @Override
    public void endFrame() {
        nvgEndFrame(ctx);
    }

    @Override
    public void color(int color) {
        super.color(color);
        nvgRGBA((byte) r(), (byte) g(), (byte) b(), (byte) a(), this.color);
    }

    @NotNull
    @Override
    public UIContentFBO createContentFBO(float width, float height) {
        if (width <= 0 || height <= 0) {
            throw new RuntimeException("Failed to create the framebuffer. It must have a width and height greater than 0");
        }
        NVGLUFramebuffer framebuffer = nvgluCreateFramebuffer(
                ctx,
                (int) width, //(int) (width * UICore.Companion.getContentScaleX()),
                (int) height, //(int) (height * UICore.Companion.getContentScaleY()),
                NVG_IMAGE_REPEATX | NVG_IMAGE_REPEATY
        );
        if (framebuffer == null) {
            throw new RuntimeException("Failed to create the framebuffer");
        }
        UIContentFBO fbo = new UIContentFBO(
                framebuffer.fbo(),
                width,
                height
        );
        fbos.put(fbo, framebuffer);
        return fbo;
    }

    @Override
    public void deleteContentFBO(UIContentFBO fbo) {
        NVGLUFramebuffer framebuffer = fbos.get(fbo);
        if (framebuffer != null) {
            nvgluDeleteFramebuffer(ctx, framebuffer);
            return;
        }
        System.out.println("Failed to delete the framebuffer because it was not found.");
    }

    @Override
    public void bindContentFBO(@NotNull UIContentFBO fbo) {
        nvgluBindFramebuffer(ctx, fbos.get(fbo));
        glViewport(0, 0, (int) (fbo.getWidth()), (int) (fbo.getHeight()));
        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    @Override
    public void unbindContentFBO() {
        nvgluBindFramebuffer(ctx, null);
    }

    @Override
    public void renderFBO(@NotNull UIContentFBO fbo, float x, float y, float width, float height, float topLeft, float topRight, float bottomRight, float bottomLeft) {
        nvgImagePattern(ctx, x, y, width, height, 0, fbos.get(fbo).image(), 1f, paint);
        nvgBeginPath(ctx);
        color(-1);
        paint.innerColor(color);
        paint.outerColor(color);
        nvgRoundedRectVarying(ctx, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft);
        nvgFillPaint(ctx, paint);
        nvgFill(ctx);
    }

    @Override
    public void stroke(float strokeWidth, int strokeColor) {
        super.stroke(strokeWidth, strokeColor);
        nvgRGBA((byte) net.prismclient.aether.ui.util.extensions.ColorKt.getRed(strokeColor), (byte) ColorKt.getGreen(strokeColor), (byte) ColorKt.getBlue(strokeColor), (byte) ColorKt.getAlpha(strokeColor), outlineColor);
    }

    @Override
    public void save() {
        nvgSave(ctx);
    }

    @Override
    public void restore() {
        nvgRestore(ctx);
    }

    @Override
    public void translate(float x, float y) {
        nvgTranslate(ctx, x, y);
    }

    @Override
    public void rotate(float angle) {
        nvgRotate(ctx, angle);
    }

    @Override
    public void rotate(double angle) {
        nvgRotate(ctx, (float) Math.toRadians(angle));
    }

    @Override
    public void scale(float x, float y) {
        nvgScale(ctx, x, y);
    }

    @Override
    public void scissor(float x, float y, float width, float height) {
        nvgScissor(ctx, x, y, width, height);
    }

    @Override
    public void rect(float x, float y, float width, float height) {
        nvgBeginPath(ctx);
        nvgRect(ctx, x, y, width, height);
        fill();
    }

    @Override
    public void rect(float x, float y, float width, float height, float radius) {
        rect(x, y, width, height, radius, radius, radius, radius);
    }

    @Override
    public void rect(float x, float y, float width, float height, float topLeft, float topRight, float bottomRight, float bottomLeft) {
        nvgBeginPath(ctx);
        if (width <= 1f || height <= 1f) {
            // Rendering 1px and below causes half transparent draws
            // because of how NanoVG renders antialiasing
            nvgShapeAntiAlias(ctx, false);
        }
        nvgRoundedRectVarying(ctx, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft);
        fill();
    }

    @Override
    public void linearGradient(float x, float y, float width, float height, float radius, float gradientX, float gradientY, float gradientWidth, float gradientHeight, int color1, int color2) {
        linearGradient(x, y, width, height, radius, radius, radius, radius, gradientX, gradientY, gradientWidth, gradientHeight, color1, color2);
    }

    @Override
    public void linearGradient(float x, float y, float width, float height, float topLeft, float topRight, float bottomRight, float bottomLeft, float gradientX, float gradientY, float gradientWidth, float gradientHeight, int color1, int color2) {
        NVGColor c1 = NVGColor.calloc();
        NVGColor c2 = NVGColor.calloc();
        NVGPaint p = NVGPaint.calloc();

        nvgRGBA((byte) net.prismclient.aether.ui.util.extensions.ColorKt.getRed(color1), (byte) net.prismclient.aether.ui.util.extensions.ColorKt.getGreen(color1), (byte) net.prismclient.aether.ui.util.extensions.ColorKt.getBlue(color1), (byte) net.prismclient.aether.ui.util.extensions.ColorKt.getAlpha(color1), c1);
        nvgRGBA((byte) net.prismclient.aether.ui.util.extensions.ColorKt.getRed(color2), (byte) net.prismclient.aether.ui.util.extensions.ColorKt.getGreen(color2), (byte) net.prismclient.aether.ui.util.extensions.ColorKt.getBlue(color2), (byte) net.prismclient.aether.ui.util.extensions.ColorKt.getAlpha(color2), c2);

        nvgLinearGradient(ctx, gradientX, gradientY, gradientWidth, gradientHeight, c1, c2, p);
        nvgBeginPath(ctx);
        nvgRoundedRectVarying(ctx, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft);
        nvgFillPaint(ctx, p);
        nvgFill(ctx);

        c1.free();
        c2.free();
        p.free();
    }

    @Override
    public void ellipse(float x, float y, float width, float height) {
        nvgBeginPath(ctx);
        nvgEllipse(ctx, x, y, width, height);
        fill();
    }

    @Override
    public void srect(float x, float y, float width, float height) {
        nvgBeginPath(ctx);
        nvgRect(ctx, x, y, width, height);
        stroke();
    }

    @Override
    public void srect(float x, float y, float width, float height, float radius) {
        nvgBeginPath(ctx);
        nvgRoundedRect(ctx, x, y, width, height, radius);
        stroke();
    }

    @Override
    public void srect(float x, float y, float width, float height, float topLeft, float topRight, float bottomRight, float bottomLeft) {
        nvgBeginPath(ctx);
        nvgRoundedRectVarying(ctx, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft);
        stroke();
    }

    @Override
    public void sellipse(float x, float y, float width, float height) {
        nvgBeginPath(ctx);
        nvgEllipse(ctx, x, y, width, height);
        stroke();
    }

    @Override
    public boolean loadImage(@NotNull String imageName, @Nullable ByteBuffer imageData, int imageFlags) {
        if (imageData == null) {
            System.out.println("Failed to load image with name: " + imageName + " as it was null");
            return false;
        }

        int[] width = new int[]{0};
        int[] height = new int[]{0};
        int[] channels = new int[]{0};

        imageData = STBImage.stbi_load_from_memory(imageData, width, height, channels, 4);

        if (imageData == null) {
            System.out.println("Failed to parse file. Is it corrupted?");
            return false;
        }

        images.put(imageName,
                new UIImageData(
                        nvgCreateImageRGBA(ctx, width[0], height[0], imageFlags, imageData), width[0], height[0], imageData));

        return true;
    }

    @Override
    public void renderImage(@NotNull String imageName, float x, float y, float width, float height, float radius) {
        UIImageData img = images.get(imageName); int handle = 0;
        if (img != null) handle = img.getHandle();
        nvgImagePattern(ctx, x, y, width, height, 0, handle, 1f, paint);
        paint.innerColor(color);
        paint.outerColor(color);
        nvgBeginPath(ctx);
        nvgRoundedRect(ctx, x, y, width, height, radius);
        nvgFillPaint(ctx, paint);
        nvgFill(ctx);
    }

    @Override
    public void deleteImage(@NotNull String imageName) {
        // TODO Deleting images not yet implemented
    }

    @Override
    public boolean loadFont(@NotNull String fontName, @Nullable ByteBuffer fontData) {
        System.out.println("Loading font: " + fontName);
        if (fontData == null) {
            System.out.println("Failed to load font " + fontName + " as it was not found.");
            return false;
        }

        nvgCreateFontMem(ctx, fontName, fontData, 0);

        return true;
    }

    private NVGTextRow.Buffer rows = NVGTextRow.create(maxRows);
    private float[] ascender = new float[]{0};
    private float[] decender = new float[]{0};
    private float[] lineHeight = new float[]{0};

    @Override
    public void renderString(@NotNull String text, float x, float y) {
        nvgFontBlur(ctx, 0);
        nvgFontFace(ctx, fontName);
        nvgFontSize(ctx, fontSize);
        nvgTextAlign(ctx, fontAlignment);
        nvgFillColor(ctx, color);
        nvgTextLetterSpacing(ctx, fontSpacing);
        nvgText(ctx, x, y, text);
    }

    @Override
    public void wrapString(@NotNull String text, float x, float y, float width, float splitHeight) {
        // Calculate the rows
        int nrows = nvgTextBreakLines(ctx, text, width, rows);

        // Set the font state
        nvgFontBlur(ctx, 0);
        nvgFontFace(ctx, fontName);
        nvgFontSize(ctx, fontSize);
        nvgTextAlign(ctx, fontAlignment);
        nvgFillColor(ctx, color);
        nvgTextLetterSpacing(ctx, fontSpacing);

        // Get the metrics of the line
        nvgTextMetrics(ctx, ascender, decender, lineHeight);

        // Iterate through the rows
        for (int i = 0; i < nrows; i++) {
            NVGTextRow row = rows.get(i);

            nnvgText(ctx, x, y, row.start(), row.end()); // Render the text

            y += lineHeight[0] + splitHeight; // Increase by the font height plus the split height
        }
    }

    @Override
    public float stringWidth(@NotNull String text) {
        nvgFontBlur(ctx, 0);
        nvgFontFace(ctx, fontName);
        nvgFontSize(ctx, fontSize);
        nvgTextAlign(ctx, fontAlignment);
        nvgFillColor(ctx, none);
        nvgTextLetterSpacing(ctx, fontSpacing);
        return nvgText(ctx, 0, 0, text);
    }

    @Override
    public float stringHeight(@NotNull String text) {
        nvgTextMetrics(ctx, null, null, lineHeight);
        return lineHeight[0];
    }

    @Override
    public float stringAscender() {
        nvgTextMetrics(ctx, ascender, null, null);
        return ascender[0];
    }

    @Override
    public float stringDescender() {
        nvgTextMetrics(ctx, null, decender, null);
        return Math.abs(decender[0]);
    }

    private void fill() {
        nvgFillColor(ctx, color);
        nvgFill(ctx);
    }

    private void stroke() {
        nvgStrokeWidth(ctx, strokeWidth);
        nvgStrokeColor(ctx, outlineColor);
        nvgStroke(ctx);
    }
}
