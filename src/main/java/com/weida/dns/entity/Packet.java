package com.weida.dns.entity;

/**
 * @author weida
 */
public class Packet {

    // 数据数组
    byte[] buf;
    // 数组长度
    int length;
    // 有效长度
    int bufLength;

    public Packet(byte[] buf) {
        setData(buf);
    }

    public Packet(int length) {
        setData(length);
    }

    public void addByte(byte b) {
        // 数组长度校验，防止不够用
        if (bufLength > this.length) {
            throw new IndexOutOfBoundsException("Packet 长度不够！");
        }
        this.buf[bufLength++] = (byte) (b & 0xff);
    }

    public void addByteArr(byte[] bytes) {
        if (null == bytes)
            return;
        for (byte b : bytes) {
            addByte(b);
        }
    }

    private void setData(int length) {
        this.buf = new byte[length];
        this.length = length;
        this.bufLength = 0;
    }

    private void setData(byte[] buf) {
        if (null == buf) {
            throw new IllegalArgumentException("数据不为空！");
        }
        this.buf = buf;
        this.length = buf.length;
        this.bufLength = buf.length;
    }

    /**
     * 返回有效数据字节数组
     *
     * @return 有效数据字节数组
     */
    public byte[] getData() {
        byte[] bytes = new byte[bufLength];
        System.arraycopy(buf, 0, bytes, 0, bufLength);
        return bytes;
    }

    public int getLength() {
        return length;
    }

    public int getBufLength() {
        return bufLength;
    }

}
