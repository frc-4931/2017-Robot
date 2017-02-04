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
 * Describes the actions that are common to all Pixy controllers.
 * For the hardware implementation, see {@link HardwarePixy}.
 *
 * @author Adam Gausmann
 */
public interface Pixy {

    /**
     * Gets the blocks detected from the most recent frame of video.
     */
    PixyBlock[] getBlocks();

    /**
     * Sets the position of the pan/tilt servos.
     *
     * @param pan A number between 0 (far left) and 1000 (far right).
     * @param tilt A number between 0 (bottom) and 1000 (top).
     */
    void setServoPosition(int pan, int tilt);

    /**
     * Sets the camera's brightness setting.
     *
     * @param brightness The brightness of the camera, from 0 to 255.
     */
    void setCameraBrightness(int brightness);

    /**
     * Sets the color of the Pixy's onboard LED.
     *
     * @param red The brightness of the red component, from 0 to 255.
     * @param green The brightness of the green component, from 0 to 255.
     * @param blue The brightness of the blue component, from 0 to 255.
     */
    void setLedColor(int red, int green, int blue);
}
