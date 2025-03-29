/*
 * Copyright (c) 2024-2025 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.linusdev.lutils.image.wip_webp.reader;

import org.jetbrains.annotations.NotNull;

public class BitReader {

    private final static byte @NotNull [] OR_LEFT = new byte[] {
            (byte) ~0xFF,
            (byte) ~0x7F,
            (byte) ~0x3F,
            (byte) ~0x1F,
            (byte) ~0x0F,
            (byte) ~0x07,
            (byte) ~0x03,
            (byte) ~0x01,
    };

    private final static byte @NotNull [] OR_RIGHT = new byte[] {
            (byte) 0xFF,
            (byte) 0x7F,
            (byte) 0x3F,
            (byte) 0x1F,
            (byte) 0x0F,
            (byte) 0x07,
            (byte) 0x03,
            (byte) 0x01,
            (byte) 0x00,
    };

    private final byte @NotNull [] data;

    private int bitPos = 0;
    private int bytePos = 0;

    public BitReader(byte @NotNull [] data) {
        this.data = data;
    }

    public byte readByte() {
        return readBitsToByte(8);
    }

    public byte readBitsToByte(int bitCount) {
        assert bitCount <= 8 && bitCount > 0;

        byte ub = 0;
        int remaining = _bitsRemaining();
        if(remaining > 0) {
            ub = (byte) (ub | _readRemainingBits(bitCount) & 0xFF);
            bitCount -= remaining;
            if(bitCount <= 0) return ub;
        }

        if(bitCount == 8) return _readByte();
        return (byte) ((ub << bitCount) | _readBits(bitCount));
    }

    public int readBitsToInt(int bitCount) {
        assert bitCount <= 32 && bitCount > 0;

        int ui = 0;
        int remaining = _bitsRemaining();
        if(remaining > 0) {
            ui = ui | _readRemainingBits(bitCount) & 0xFF;
            bitCount -= remaining;
            if(bitCount <= 0) return ui;
        }

        int byteCount = bitCount / 8;
        bitCount = bitCount - (byteCount * 8);

        if(byteCount >= 1) ui = (ui << 8) | _readByte() & 0xFF;
        if(byteCount >= 2) ui = (ui << 8) | _readByte() & 0xFF;
        if(byteCount >= 3) ui = (ui << 8) | _readByte() & 0xFF;
        if(byteCount == 4) ui = (ui << 8) | _readByte() & 0xFF;

        ui = (ui << bitCount) | _readBits(bitCount);

        return ui;
    }

    private int _bitsRemaining() {
        assert bitPos <= 7;
        return bitPos == 0 ? 0 : 8 - bitPos;
    }

    private byte _readRemainingBits(int max) {
        assert bitPos != 0;
        assert bitPos <= 7;

        int tooMuch = Math.max(0, _bitsRemaining() - max);

        byte ret = (byte) (data[bytePos] & OR_RIGHT[bitPos] & OR_LEFT[OR_LEFT.length - tooMuch]);
        bitPos = 8 - tooMuch;
        if(bitPos == 0) bytePos++;
        return ret;
    }

    private byte _readBits(int bitCount) {
        assert bitCount <= 7;
        assert bitPos == 0;

        bitPos = (bitPos + bitCount) % 8;
        return (byte) ((byte) (data[bytePos] & OR_LEFT[bitCount]) >>> (8 - bitCount));
    }

    private byte _readByte() {
        return data[bytePos++];
    }
}
