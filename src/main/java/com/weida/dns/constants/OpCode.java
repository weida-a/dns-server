package com.weida.dns.constants;

/**
 * 操作码 由消息发起者设置，共16个值，其中3～15被保留使用，4bit
 *
 * @author weida
 */
public class OpCode {
    // 正向查询
    public static final byte QUERY = 0;
    // 反向查询
    public static final byte IQUERY = 1;
    // 服务器状态请求
    public static final byte STATUS = 2;
}
