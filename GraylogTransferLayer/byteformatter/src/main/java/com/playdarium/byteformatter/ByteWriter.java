package com.playdarium.byteformatter;

import org.apache.commons.io.EndianUtils;

import java.nio.charset.StandardCharsets;

public class ByteWriter {

    private ByteBuffer buffer;

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public static ByteWriter createDefault() {
        return new ByteWriter();
    }

    public static ByteWriter create(byte[] bytes) {
        ByteWriter writer = new ByteWriter();
        ByteBuffer buffer = ByteBuffer.create(bytes);
        writer.setBuffer(buffer);
        return writer;
    }

    public static ByteWriter create(ByteBuffer buffer) {
        ByteWriter writer = new ByteWriter();
        writer.setBuffer(buffer);
        return writer;
    }

    private ByteWriter() {

    }


    public void clear() {
        buffer = null;
    }

    public long getPosition() {
        return buffer.getPosition();
    }

    public byte[] array() {
        return buffer.asArraySegment();
    }

    public void writePackedInt(int value) {
        if (value <= 240) {
            write((byte) value);
        } else if (value <= 2287) {
            write((byte) ((value - 240) / 256 + 241));
            write((byte) ((value - 240) % 256));
        } else if (value <= 67823) {
            write((byte) 249);
            write((byte) ((value - 2288) / 256));
            write((byte) ((value - 2288) % 256));
        } else if (value <= 16777215) {
            write((byte) 250);
            write((byte) (value & Byte.MAX_VALUE));
            write((byte) ((value >> 8) & Byte.MAX_VALUE));
            write((byte) ((value >> 16) & Byte.MAX_VALUE));
        } else {
            write((byte) 251);
            write((byte) (value & Byte.MAX_VALUE));
            write((byte) ((value >> 8) & Byte.MAX_VALUE));
            write((byte) ((value >> 16) & Byte.MAX_VALUE));
            write((byte) ((value >> 24) & Byte.MAX_VALUE));
        }
    }

    public void writePackedLong(long value) {
        if (value <= 240L) {
            write((byte) value);
        } else if (value <= 2287L) {
            write((byte) ((value - 240L) / 256L + 241L));
            write((byte) ((value - 240L) % 256L));
        } else if (value <= 67823L) {
            write((byte) 249);
            write((byte) ((value - 2288L) / 256L));
            write((byte) ((value - 2288L) % 256L));
        } else if (value <= 16777215L) {
            write((byte) 250);
            write((byte) (value & Byte.MAX_VALUE));
            write((byte) ((value >> 8) & Byte.MAX_VALUE));
            write((byte) ((value >> 16) & Byte.MAX_VALUE));
        } else if (value <= Integer.MAX_VALUE) {
            write((byte) 251);
            write((byte) (value & Byte.MAX_VALUE));
            write((byte) ((value >> 8) & Byte.MAX_VALUE));
            write((byte) ((value >> 16) & Byte.MAX_VALUE));
            write((byte) ((value >> 24) & Byte.MAX_VALUE));
        } else if (value <= 1099511627775L) {
            write((byte) 252);
            write((byte) (value & Byte.MAX_VALUE));
            write((byte) ((value >> 8) & Byte.MAX_VALUE));
            write((byte) ((value >> 16) & Byte.MAX_VALUE));
            write((byte) ((value >> 24) & Byte.MAX_VALUE));
            write((byte) ((value >> 32) & Byte.MAX_VALUE));
        } else if (value <= 281474976710655L) {
            write((byte) 253);
            write((byte) (value & Byte.MAX_VALUE));
            write((byte) ((value >> 8) & Byte.MAX_VALUE));
            write((byte) ((value >> 16) & Byte.MAX_VALUE));
            write((byte) ((value >> 24) & Byte.MAX_VALUE));
            write((byte) ((value >> 32) & Byte.MAX_VALUE));
            write((byte) ((value >> 40) & Byte.MAX_VALUE));
        } else if (value <= 72057594037927935L) {
            write((byte) 254);
            write((byte) (value & Byte.MAX_VALUE));
            write((byte) ((value >> 8) & Byte.MAX_VALUE));
            write((byte) ((value >> 16) & Byte.MAX_VALUE));
            write((byte) ((value >> 24) & Byte.MAX_VALUE));
            write((byte) ((value >> 32) & Byte.MAX_VALUE));
            write((byte) ((value >> 40) & Byte.MAX_VALUE));
            write((byte) ((value >> 48) & Byte.MAX_VALUE));
        } else {
            write(Byte.MAX_VALUE);
            write((byte) (value & Byte.MAX_VALUE));
            write((byte) ((value >> 8) & Byte.MAX_VALUE));
            write((byte) ((value >> 16) & Byte.MAX_VALUE));
            write((byte) ((value >> 24) & Byte.MAX_VALUE));
            write((byte) ((value >> 32) & Byte.MAX_VALUE));
            write((byte) ((value >> 40) & Byte.MAX_VALUE));
            write((byte) ((value >> 48) & Byte.MAX_VALUE));
            write((byte) ((value >> 56) & Byte.MAX_VALUE));
        }
    }

    public void write(char value) {
        buffer.writeByte((byte) value);
    }

    public void write(byte value) {
        buffer.writeByte(value);
    }

    public void write(short value) {
        byte[] bytes = new byte[2];
        EndianUtils.writeSwappedShort(bytes, 0, value);
        buffer.writeByte2(bytes[0], bytes[1]);
    }

    public void write(int value) {
        byte[] bytes = new byte[4];
        EndianUtils.writeSwappedInteger(bytes, 0, value);

        buffer.writeByte4(bytes[0], bytes[1], bytes[2], bytes[3]);
    }

    public void write(long value) {
        byte[] bytes = new byte[8];
        EndianUtils.writeSwappedLong(bytes, 0, value);
        buffer.writeByte8(bytes[0], bytes[1], bytes[2], bytes[3],
                          bytes[4], bytes[5], bytes[6], bytes[7]);
    }


    public void write(Float value) {
        if (value != null) {
            write(true);
            write(value.intValue());
        } else
            write(false);
    }

    public void write(boolean value) {
        buffer.writeByte(value ? (byte) 1 : (byte) 0);
    }

    public void write(byte[] buffer, int count) {
        this.buffer.writeBytes(buffer, count);
    }

    public void write(byte[] buffer, int offset, int count) {
        this.buffer.writeBytesAtOffset(buffer, offset, count);
    }

    public void writeBytesAndSize(byte[] buffer, int count) {
        if (buffer == null || count == 0) {
            write(0);
        } else {
            write(count);
            this.buffer.writeBytes(buffer, count);
        }
    }

    public void write(byte[] buffer) {
        if (buffer == null) {
            write(0);
        } else {
            write(buffer.length);
            this.buffer.writeBytes(buffer, buffer.length);
        }
    }

    public void write(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        writeBytesAndSize(bytes, bytes.length);
    }

    public void write(Integer value) {
        if (value != null) {
            write(true);
            write(value);
        } else
            write(false);
    }

    public void write(int[] array) {
        write(array.length);
        for (int j : array) {
            write(j);
        }
    }

    public void write(Integer[] array) {
        write(array.length);
        for (Integer integer : array) {
            write(integer);
        }
    }

    public void write(float[] array) {
        write(array.length);
        for (float v : array) {
            write(v);
        }
    }

    public void write(Float[] array) {
        write(array.length);
        for (Float aFloat : array) {
            write(aFloat);
        }
    }

    public void skip(int length){
         this.buffer.skip(length);
    }

    public void seekZero(){
        this.buffer.seekZero();
    }

}

