/*
 * Copyright (c) 2016 FRC Team 4931
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.frc4931.robot.components;

/**
 * A block that is detected by the Pixy controller. Often returned by {@link Pixy#getBlocks()}; you will hardly ever
 * have to instantiate this yourself.
 */
public class PixyBlock {
    private final BlockType type;
    private final int signature;
    private final int centerX;
    private final int centerY;
    private final int width;
    private final int height;

    public PixyBlock(BlockType type, int signature, int centerX, int centerY, int width, int height) {
        this.type = type;
        this.signature = signature;
        this.centerX = centerX;
        this.centerY = centerY;
        this.width = width;
        this.height = height;
    }

    public BlockType getType() {
        return type;
    }

    /**
     * Gets this block's signature - a number between 1 and 7 specifying which of the color signatures Pixy recognized
     * in this block.
     *
     * @return The block's signature.
     */
    public int getSignature() {
        return signature;
    }

    /**
     * Gets the X coordinate of the center of the block.
     *
     * @return The coordinate, measured in pixels starting from the leftmost side of the image.
     */
    public int getCenterX() {
        return centerX;
    }

    /**
     * Gets the Y coordinate of the center of the block.
     *
     * @return The coordinate, measured in pixels starting from the topmost side of the image.
     */
    public int getCenterY() {
        return centerY;
    }

    /**
     * Gets the width of the block.
     *
     * @return The width, measured in pixels.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the block.
     *
     * @return The height, mesaured in pixels.
     */
    public int getHeight() {
        return height;
    }

    public enum BlockType {
        NORMAL,
        COLOR_CODE
    }
}
