package com.playdarium.byteformatter;

import org.apache.commons.io.EndianUtils;

import java.nio.charset.StandardCharsets;

public class ByteReader {
    private ByteBuffer buffer;

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public static ByteReader createDefault() {
        return new ByteReader();
    }

    public static ByteReader create(byte[] bytes) {
        ByteReader reader = new ByteReader();
        ByteBuffer buffer = ByteBuffer.create(bytes);
        reader.setBuffer(buffer);
        return reader;
    }

    public static ByteReader create(ByteBuffer buffer) {
        ByteReader reader = new ByteReader();
        reader.setBuffer(buffer);
        return reader;
    }

    private ByteReader() {

    }

    public void clear() {
        buffer = null;
    }


    public long getPosition() {
        return buffer.getPosition();
    }

    public int getLength() {
        return buffer.getLength();
    }

    public void seekZero()
    {
        buffer.seekZero();
    }

    public long readableBytes() {
        return Math.max(0, buffer.getLength() - buffer.getPosition());
    }

    public void skip(int length) {
        buffer.skip(length);
    }

    public void replace(byte[] buffer)
    {
        this.buffer.replace(buffer);
    }

    public byte readByte() {
        return buffer.readByte();
    }

    public void skipByte() {
        buffer.skip(1);
    }

    public byte[] array() {
        return buffer.asArraySegment();
    }

    public short readShort() {
        byte[] shortBytes = new byte[2];
        for (int i = 0; i < 2; i++) {
            byte b = buffer.readByte();
            shortBytes[i] = b;
        }
        return EndianUtils.readSwappedShort(shortBytes, 0);
    }


    public void skipShort() {
        buffer.skip(2);
    }

    public int readInt() {
        byte[] intBytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            byte b = buffer.readByte();
            intBytes[i] = b;
        }
        return EndianUtils.readSwappedInteger(intBytes, 0);

    }

    public float readFloat() {
        byte[] floatBytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            byte b = buffer.readByte();
            floatBytes[i] = b;
        }
        return EndianUtils.readSwappedFloat(floatBytes, 0);
    }

    public void skipInt() {
        buffer.skip(4);
    }

    public long readLong() {
        byte[] longBytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            byte b = buffer.readByte();
            longBytes[i] = b;
        }
        return EndianUtils.readSwappedLong(longBytes, 0);
    }

    public void skipLong() {
        buffer.skip(8);
    }

    public char readChar() {
        return (char) buffer.readByte();
    }

    public void skipChar() {
        buffer.skip(1);
    }

    public boolean readBoolean() {
        return buffer.readByte() == 1;
    }

    public void skipBoolean() {
        buffer.skip(1);
    }

    public byte[] readBytes(int count)
    {
        if (count < 0)
            throw new IndexOutOfBoundsException("NetworkReader ReadBytes " + count);
        byte[] buffer = new byte[count];
        this.buffer.readBytes(buffer, count);
        return buffer;
    }

    public byte[] readBytesAndSize()
    {
        int num = readInt();
        return num == 0 ? new byte[0] : readBytes(num);
    }

    public void skipBytesAndSize()
    {
        int length = readInt();
        buffer.skip(length);
    }

    public String readString()
    {
        byte[] bytes = readBytesAndSize();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public void skipString() {
        skipBytesAndSize();
    }

    public Integer readNullableInt()
    {
        boolean hasValue = readBoolean();
        return hasValue ? readInt() : null;
    }

    public void skipNullableInt()
    {
        boolean hasValue = readBoolean();
        if (hasValue)
            buffer.skip(4);
    }

    public int[] readIntArray()
    {
        int length = readInt();
        int[] array = new int[length];
        for (int i = 0; i < length; i++)
        {
            array[i] = readInt();
        }
        return array;
    }

    public void skipIntArray()
    {
        int length = readInt();
        buffer.skip(length * 4);
    }

    public Integer[] readNullableIntArray()
    {
        int length = readInt();
        Integer[] array = new Integer[length];
        for (int i = 0; i < length; i++)
        {
            array[i] = readNullableInt();
        }
        return array;
    }

    public void skipNullableIntArray()
    {
        int length = readInt();
        for (int i = 0; i < length; i++)
        {
            skipNullableInt();
        }
    }
}
