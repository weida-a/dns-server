package com.weida.dns.entity;

import lombok.Data;
import lombok.ToString;

/**
 * DNS header （固定12byte）
 *
 * @author weida
 */
@Data
@ToString
public class Header {

    // 事务ID（16位）
    private short transactionId;

    // 消息类型：1bit
    private byte msgType;
    // 操作码：4bit
    private byte opCode;
    // 是否是权威应答：1bit
    private byte isAuthority;
    // 是否截断数据包：1bit
    private byte Truncation;
    // 查询方式（递归查询，迭代查询）：1bit
    private byte recursionDesired;
    // 服务器是否有递归查询能力：1bit
    private byte haveRecursive;
    // 保留字段：3bit
    private byte reserved;
    // 返回码：4bit
    private byte returnCode;

    // 无符号正值，所以用int类型比short方便
    // 请求资源记录数（16位）
    private int questionCount;
    // 响应资源记录数（16位）
    private int answerCount;
    // 权限资源记录数（16位）
    private int authorityCount;
    // 附加资源记录数（16位）
    private int additionalCount;

}
