package com.weida.dns.protocol;


import com.weida.dns.constants.MsgType;
import com.weida.dns.entity.*;
import com.weida.dns.utils.BitUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Dns协议处理器：DNS 消息的编码、解码
 *
 * @author weida
 */
public class ProtocolProcessor {

    private int offset;

    /**
     * 消息编码
     *
     * @return 编码包
     */
    public Packet encode(Msg msg) {
        Packet packet = new Packet(1024);
        packet.addByteArr(encodeHeader(msg.getHeader()));
        packet.addByteArr(encodeQuery(msg.getQueries()));
        packet.addByteArr(encodeResource(msg.getAnswers()));
        packet.addByteArr(encodeResource(msg.getAuthorities()));
        packet.addByteArr(encodeResource(msg.getAdditions()));

        return packet;
    }

    private byte[] encodeResource(List<? extends Resource> resources) {
        if (null == resources)
            return null;
        Packet packet = new Packet(1024);
        for (Resource r : resources) {
            String name = r.getName();
            short type = r.getType();
            short clazz = r.getClazz();
            int ttl = r.getTtl();
            short length = r.getLength();
            byte[] rByteArr = r.getRByteArr();

            byte[] nameByteArr = nameToByteArr(name);
            packet.addByteArr(nameByteArr);
            packet.addByte((byte) (type >> 8));
            packet.addByte((byte) type);
            packet.addByte((byte) (clazz >> 8));
            packet.addByte((byte) clazz);
            packet.addByte((byte) (ttl >> 24));
            packet.addByte((byte) (ttl >> 16));
            packet.addByte((byte) (ttl >> 8));
            packet.addByte((byte) (ttl));
            packet.addByte((byte) (length >> 8));
            packet.addByte((byte) length);
            packet.addByteArr(rByteArr);
        }
        return packet.getData();
    }

    private byte[] encodeQuery(List<Query> queries) {
        if (null == queries)
            return null;
        Packet packet = new Packet(1024);
        for (Query query : queries) {
            String name = query.getName();
            short type = query.getType();
            short clazz = query.getClazz();
            byte[] nameByteArr = nameToByteArr(name);
            packet.addByteArr(nameByteArr);
            packet.addByte((byte) (type >> 8));
            packet.addByte((byte) type);
            packet.addByte((byte) (clazz >> 8));
            packet.addByte((byte) clazz);
        }
        return packet.getData();
    }

    private byte[] encodeHeader(Header header) {
        short transactionId = header.getTransactionId();

        byte msgType = header.getMsgType();
        byte opCode = header.getOpCode();
        byte isAuthority = header.getIsAuthority();
        byte truncation = header.getTruncation();
        byte recursionDesired = header.getRecursionDesired();

        byte haveRecursive = header.getHaveRecursive();
        byte reserved = header.getReserved();
        byte returnCode = header.getReturnCode();

        int questionCount = header.getQuestionCount();
        int answerCount = header.getAnswerCount();
        int authorityCount = header.getAuthorityCount();
        int additionalCount = header.getAdditionalCount();

        byte[] bytes = new byte[12];
        bytes[0] = BitUtils.getByteArr(transactionId)[0];
        bytes[1] = BitUtils.getByteArr(transactionId)[1];
        bytes[2] = (byte) (((byte) (msgType << 7) | (byte) (opCode << 3) | (byte) (isAuthority << 2) | (byte) (truncation << 1) | recursionDesired) & 0xff);
        bytes[3] = (byte) (((byte) (haveRecursive << 7) | returnCode) & 0xff);
        bytes[4] = BitUtils.getByteArr((short) questionCount)[0];
        bytes[5] = BitUtils.getByteArr((short) questionCount)[1];
        bytes[6] = BitUtils.getByteArr((short) answerCount)[0];
        bytes[7] = BitUtils.getByteArr((short) answerCount)[1];
        bytes[8] = BitUtils.getByteArr((short) authorityCount)[0];
        bytes[9] = BitUtils.getByteArr((short) authorityCount)[1];
        bytes[10] = BitUtils.getByteArr((short) additionalCount)[0];
        bytes[11] = BitUtils.getByteArr((short) additionalCount)[1];

        return bytes;
    }

    /**
     * 消息解码
     *
     * @return 消息实体类
     */
    public Msg decode(Packet packet) {
        // 解析头部信息
        Header header = parseHeader(packet);
        // 解析查询实体
        List<Query> queries = parseQuery(packet, header.getQuestionCount());
        Msg msg = new Msg();
        msg.setHeader(header);
        msg.setQueries(queries);

        if (header.getMsgType() == MsgType.RESPONSE) {
            // todo 解析资源实体
//            msg.setAnswers(answers);
//            msg.setAuthorities(authorities);
//            msg.setAdditions(additions);
        }
        return msg;
    }

    /**
     * 解析 dns 头部信息
     *
     * @param packet dns协议数据包
     * @return dns 头部信息
     */
    private Header parseHeader(Packet packet) {
        if (null == packet || packet.getLength() < 13) {
            throw new RuntimeException("非法数据包！");
        }
        byte[] data = packet.getData();

        Header h = new Header();

        h.setTransactionId(BitUtils.getShort(data[0], data[1]));

        // 1
        h.setMsgType(BitUtils.getBit(data[2], 0, 1));
        // 2,3,4,5
        h.setOpCode(BitUtils.getBit(data[2], 1, 4));
        // 6
        h.setIsAuthority(BitUtils.getBit(data[2], 5, 1));
        // 7
        h.setTruncation(BitUtils.getBit(data[2], 6, 1));
        // 8
        h.setRecursionDesired(BitUtils.getBit(data[2], 7, 1));

        // 1
        h.setHaveRecursive(BitUtils.getBit(data[3], 0, 1));
        // 2,3,4
        h.setReserved(BitUtils.getBit(data[3], 1, 3));
        // 5,6,7,8
        h.setReturnCode(BitUtils.getBit(data[3], 4, 4));

        h.setQuestionCount(BitUtils.getShort(data[4], data[5]));
        h.setAnswerCount(BitUtils.getShort(data[6], data[7]));
        h.setAuthorityCount(BitUtils.getShort(data[8], data[9]));
        h.setAdditionalCount(BitUtils.getShort(data[10], data[11]));

        return h;

    }

    /**
     * 解析 dns 查询实体
     *
     * @param packet    dns协议数据包
     * @param questions 查询个数
     * @return 查询实体列表
     */
    private List<Query> parseQuery(Packet packet, Integer questions) {
        if (null == packet || packet.getLength() < 13) {
            throw new RuntimeException("非法数据包！");
        }
        if (null == questions || questions < 1) {
            throw new RuntimeException("非法参数！");
        }
        offset = 12;
        byte[] data = packet.getData();
        List<Query> queries = new ArrayList<>();
        for (int i = 0; i < questions; i++) {
            Query q = new Query();
            q.setName(byteArrToName(data));
            q.setType(BitUtils.getShort(data[++offset], data[++offset]));
            q.setClazz(BitUtils.getShort(data[++offset], data[++offset]));
            queries.add(q);
        }
        return queries;

    }

    /**
     * 解析域名信息，开始前需指定开始字节的偏移量offset
     *
     * @param buf **包含**域名信息的字节数组
     * @return 域名字符串
     */
    private String byteArrToName(byte[] buf) {
        int pointHead = offset;
        offset = buf[pointHead] + pointHead + 1;
        int length = offset - 1 - pointHead;
        byte[] name = new byte[length];
        for (int i = 0; i < length; i++) {
            name[i] = buf[pointHead + 1 + i];
        }
        if (offset > buf.length) {
            throw new RuntimeException("数据包不完整！");
        }
        if (offset < buf.length && 0 != buf[offset]) {
            return new String(name, 0, length, Charset.defaultCharset()) + "." + byteArrToName(buf);
        }
        return new String(name, 0, length, Charset.defaultCharset());
    }

    /**
     * 基于dns协议，获取域名的字节数组
     *
     * @param name 域名字符串
     * @return 域名的字节数组
     */
    private byte[] nameToByteArr(String name) {
        if (StringUtils.isBlank(name)) {
            throw new RuntimeException("name 参数不合法！");
        }
        String[] strs = name.split("\\.");
        Packet packet = new Packet(1024);
        for (String str : strs) {
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            packet.addByte((byte) bytes.length);
            packet.addByteArr(bytes);
        }
        packet.addByte((byte) 0);
        return packet.getData();
    }

}
