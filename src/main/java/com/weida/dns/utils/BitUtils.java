package com.weida.dns.utils;

/**
 * 字节操作工具
 *
 * @author weida
 */
public class BitUtils {

    public static short getShort(byte b1, byte b2) {
        return (short) (((b1 & 0xff) << 8) | (b2 & 0xff));
    }

    public static byte[] getByteArr(short s) {
        return new byte[]{
                (byte) ((s >> 8) & 0xff),
                (byte) (s & 0xff)
        };
    }

    /**
     * @param start 从0开始
     */
    public static byte getBit(byte data, int start, int length) {
        if (8 < (start + length))
            throw new IndexOutOfBoundsException("(start + length) <= 8");
        return (byte) ((data >> (8 - start - length)) - (data >> (start + length)));
    }

    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(1 & 0xff));
        System.out.println(Integer.toBinaryString(-123 & 0xff));


        byte b0 = (byte) 0B00001011;
        byte b1 = (byte) 0B10001011;
        short b01 = 0B0000101110001011;
        short b10 = (short) 0B1000101100001011;
        // ====================

        System.out.println(Integer.toBinaryString(getBit(b0, 4, 4)));


        // ====================
        System.out.println(getShort(b0, b1));
        System.out.println(b01);
        System.out.println(getShort(b1, b0));
        System.out.println(b10);

        System.out.println(b0);
        System.out.println(b1);
        for (byte b : getByteArr(getShort(b0, b1))) {
            System.out.println(b);
        }


    }


}
