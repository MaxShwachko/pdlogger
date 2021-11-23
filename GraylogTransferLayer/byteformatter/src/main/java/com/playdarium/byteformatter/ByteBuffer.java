package com.playdarium.byteformatter;

public class ByteBuffer {

    private static final int INITIAL_SIZE = 4096;
    private static final float GROWTH_FACTOR = 1.5f;

    private byte[] buffer;

    private int position;

    public int getPosition() {
        return position;
    }

    public static ByteBuffer createDefault() {
        return new ByteBuffer(INITIAL_SIZE);
    }

    public static ByteBuffer create(byte[] buffer)
    {
        return new ByteBuffer(buffer);
    }

    public void resize() {
        this.buffer = new byte[INITIAL_SIZE];
        position = 0;
    }

    public void resize(byte[] buffer) {
        this.buffer = buffer;
        position = 0;
    }

    public void clear() {
        this.buffer = null;
        position = 0;
    }

    private ByteBuffer(int initialSize)
    {
        this.buffer = new byte[initialSize];
        position = 0;
    }

    private ByteBuffer(byte[] buffer)
    {
        this.buffer = buffer;
        position = 0;
    }

    public int getLength() {
        return buffer.length;
    }

    public byte readByte()
    {
        if (position >= buffer.length)
            throw new IndexOutOfBoundsException("NetworkReader:ReadByte out of range:" + this);
        return buffer[position++];
    }

    public void readBytes(byte[] buffer, int count)
    {
        if (position + count > this.buffer.length)
            throw new IndexOutOfBoundsException("NetworkReader:ReadBytes out of range: (" + count + ") " + this);
        for (int index = 0; index < count; ++index)
            buffer[index] = this.buffer[position + index];
        position += count;
    }

    public byte[] asArraySegment()
    {
        byte[] bytes = new byte[position];
        System.arraycopy(buffer, 0, bytes, 0, position);
        return bytes;
    }

    public void writeByte(byte value)
    {
        writeCheckForSpace(1);
        buffer[position] = value;
        ++position;
    }

    public void writeByte2(byte value0, byte value1)
    {
        writeCheckForSpace(2);
        buffer[position] = value0;
        buffer[position + 1] = value1;
        position += 2;
    }

    public void writeByte4(byte value0, byte value1, byte value2, byte value3)
    {
        writeCheckForSpace(4);
        buffer[position] = value0;
        buffer[position + 1] = value1;
        buffer[position + 2] = value2;
        buffer[position + 3] = value3;
        position += 4;
    }

    public void writeByte8(
            byte value0,
            byte value1,
            byte value2,
            byte value3,
            byte value4,
            byte value5,
            byte value6,
            byte value7
    )
    {
        writeCheckForSpace(8);
        buffer[position] = value0;
        buffer[position + 1] = value1;
        buffer[position + 2] = value2;
        buffer[position + 3] = value3;
        buffer[position + 4] = value4;
        buffer[position + 5] = value5;
        buffer[position + 6] = value6;
        buffer[position + 7] = value7;
        position += 8;
    }

    public void writeBytesAtOffset(byte[] buffer, int targetOffset, int count)
    {
        int num = count + targetOffset;
        writeCheckForSpace(num);
        if (targetOffset == 0 && count == buffer.length) {
            System.arraycopy(buffer, 0, this.buffer, 0, position);
        }
        else {
            for (int index = 0; index < count; ++index)
                this.buffer[targetOffset + index] = buffer[index];
        }

        if (num <= position)
            return;
        position = num;
    }

    public void writeBytes(byte[] buffer, int count)
    {
        writeCheckForSpace(count);
        if (count == buffer.length)
            System.arraycopy(buffer, 0, this.buffer, position, buffer.length);
        else
            for (int index = 0; index < count; ++index)
                this.buffer[position + index] = buffer[index];

        position += count;
    }

    private void writeCheckForSpace(int count)
    {
        if (position + count < buffer.length)
            return;

        int length = (int) Math.ceil(buffer.length * GROWTH_FACTOR);
        while (position + count >= length)
        {
            length = (int) Math.ceil(length * GROWTH_FACTOR);
        }


        byte[] numArray = new byte[length];
        System.arraycopy(buffer, 0, numArray, 0, length);
        buffer = numArray;
    }

    public void seekZero() {
        position = 0;
    };

    public void skip(int length) {
        position += length;
    }

    public void replace(byte[] buffer)
    {
        this.buffer = buffer;
        position = 0;
    }

    @Override
    public String toString() {
        return String.format("NetBuf sz:%d pos:%d", buffer.length, position);
    }

}
