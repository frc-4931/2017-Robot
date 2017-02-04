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

import edu.wpi.first.wpilibj.SPI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * An implementation of {@link Pixy} that communicates with the Pixy microcontroller.
 *
 * <br>This class implements the protocol found at http://cmucam.org/projects/cmucam5/wiki/Porting_Guide over SPI,
 * which is the officially recommended communication method.
 */
public class HardwarePixy implements Pixy {
    private static final int PIXY_NORMAL_SYNC = 0xaa55;
    private static final int PIXY_CC_SYNC = 0xaa56;
    private static final int PIXY_SYNC_BYTE = 0x5a;
    private static final int PIXY_SYNC_BYTE_DATA = 0x5b;
    private static final int PIXY_SERVO_SYNC = 0xff;
    private static final int PIXY_CAM_BRIGHTNESS_SYNC = 0xfe;
    private static final int PIXY_LED_SYNC = 0xfd;

    private final SPI spi;
    private final Queue<Integer> readQ;
    private final Queue<Byte> writeQ;

    private PixyBlock[] blocks;

    /**
     * Constructs a new instance of HardwarePixy.
     *
     * @param port The SPI port that the Pixy is connected to.
     */
    public HardwarePixy(SPI.Port port) {
        spi = new SPI(port);
        spi.setClockRate(1000000); // 1 MHz
        spi.setMSBFirst();
        spi.setClockActiveHigh();
        spi.setSampleDataOnRising();
        spi.setChipSelectActiveLow();

        readQ = new LinkedList<>();
        writeQ = new LinkedList<>();

        blocks = new PixyBlock[0];
    }

    /**
     * Synchronizes this class's state with the state of the Pixy.
     *
     * <br>This involves reading block data from the Pixy while writing commands to it over the SPI interface.
     */
    public synchronized void sync() {
        int sync = toNextFrame();

        if (sync != 0) {
            List<PixyBlock> newBlocks = new ArrayList<>();

            while (sync != 0) {
                int checksum = readWord();

                if (checksum == PIXY_NORMAL_SYNC || checksum == PIXY_CC_SYNC) {
                    // Newer frame is being sent. This data is now old.
                    newBlocks.clear();
                    sync = checksum;
                    checksum = readWord();
                }

                int signature = readWord();
                int centerX = readWord();
                int centerY = readWord();
                int width = readWord();
                int height = readWord();

                if (checksum == signature + centerX + centerY + width + height) {
                    newBlocks.add(new PixyBlock(
                            sync == PIXY_CC_SYNC ? PixyBlock.BlockType.COLOR_CODE : PixyBlock.BlockType.NORMAL,
                            signature,
                            centerX,
                            centerY,
                            width,
                            height
                    ));
                }

                sync = readWord();
            }

            blocks = new PixyBlock[newBlocks.size()];
            newBlocks.toArray(blocks);
        }

        flush();
    }

    /**
     * Throws away all data received until the next frame is reached or no more data is available.
     * <br>The Pixy protocol specifies that 2 sync words will be the divider between frames, so this method reads
     * data until 2 consecutive sync words are read, or a zero (no data available) is read.
     *
     * <br>Since this method must read the first sync byte of the first block in order to determine when the frame
     * begins, it will return that value so the user can use it as part of the first block.
     *
     * @return The last sync word read, or zero if a zero was read.
     */
    private int toNextFrame() {
        int syncCount = 0;
        int word = 0;

        while (syncCount < 2) {
            word = readWord();
            if (word == 0) {
                break;
            } else if (word == PIXY_NORMAL_SYNC || word == PIXY_CC_SYNC) {
                syncCount++;
            } else {
                syncCount = 0;
            }
        }

        return word;
    }

    /**
     * Gets a word from the queue if it is populated; else, it will use the SPI interface to get information.
     *
     * @return The unsigned 16 bit word read from the Pixy.
     */
    private int readWord() {
        if (readQ.isEmpty()) {
            return readWordHw();
        }

        return readQ.remove();
    }

    /**
     * Uses the SPI interface to get a word from the Pixy.
     *
     * <br>Because SPI is a two-way "transaction" protocol, data from the write queue will be written while the word
     * is being read.
     *
     * @return The unsigned 16 bit word read from the Pixy.
     */
    private int readWordHw() {
        byte[] outBuf = new byte[2];
        byte[] inBuf = new byte[2];

        if (writeQ.isEmpty()) {
            outBuf[0] = PIXY_SYNC_BYTE;
        } else {
            outBuf[0] = PIXY_SYNC_BYTE_DATA;
            outBuf[1] = writeQ.remove();
        }

        spi.transaction(outBuf, inBuf, 2);
        return (Byte.toUnsignedInt(inBuf[0]) << 8) | Byte.toUnsignedInt(inBuf[1]);
    }

    /**
     * Queues a byte to write over the SPI interface when the next transaction runs.
     *
     * @param b The byte to write. Though a 32 bit integer is accepted, it will be truncated to 8 bits.
     */
    private void writeByte(int b) {
        writeQ.add((byte) b);
    }

    /**
     * Queues a word to write over the SPI interface when the next transaction runs.
     *
     * @param s The word to write. Though a 32 bit integer is accepted, it will be truncated to 16 bits.
     */
    private void writeWord(int s) {
        writeQ.add((byte) (s >> 8));
        writeQ.add((byte) s);
    }

    /**
     * Flushes the write queue by writing all of the remaining data in the write queue to the SPI interface.
     *
     * <br>Because SPI is a two-way "transaction" protocol, data will be read from the SPI interface and queued
     * while the write occurs.
     */
    private void flush() {
        while (!writeQ.isEmpty()) {
            readQ.add(readWordHw());
        }
    }

    @Override
    public synchronized PixyBlock[] getBlocks() {
        return blocks.clone();
    }

    @Override
    public synchronized void setServoPosition(int pan, int tilt) {
        writeByte(PIXY_SERVO_SYNC);
        writeWord(pan);
        writeWord(tilt);
    }

    @Override
    public synchronized void setCameraBrightness(int brightness) {
        writeByte(PIXY_CAM_BRIGHTNESS_SYNC);
        writeByte(brightness);
    }

    @Override
    public synchronized void setLedColor(int red, int green, int blue) {
        writeByte(PIXY_LED_SYNC);
        writeByte(red);
        writeByte(green);
        writeByte(blue);
    }
}
