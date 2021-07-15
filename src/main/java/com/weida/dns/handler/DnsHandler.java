package com.weida.dns.handler;

import com.weida.dns.constants.MsgType;
import com.weida.dns.constants.OpCode;
import com.weida.dns.constants.ResponseType;
import com.weida.dns.constants.ReturnCode;
import com.weida.dns.entity.*;
import com.weida.dns.name.NameFinder;
import com.weida.dns.net.UdpSender;
import com.weida.dns.protocol.ProtocolProcessor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DNS 消息处理
 *
 * @author weida
 */
public class DnsHandler {
    private ProtocolProcessor processor;

    private Header header;
    private List<Query> queries;
    private List<Answer> answers;
    private List<Authority> authorities;
    private List<Addition> additions;

    private DatagramSocket socket;
    private InetAddress address;
    private int port;

    private boolean success = false;

    /**
     * 处理消息
     *
     * @param dp     udp 数据包
     * @param socket 套接字
     */
    public void handleReception(DatagramPacket dp, DatagramSocket socket) {
        this.processor = new ProtocolProcessor();

        this.address = dp.getAddress();
        this.port = dp.getPort();
        this.socket = socket;

        Msg msg = processor.decode(new Packet(dp.getData()));

        queries = (null == msg.getQueries()) ? new ArrayList<>() : msg.getQueries();
        answers = (null == msg.getAnswers()) ? new ArrayList<>() : msg.getAnswers();
        authorities = (null == msg.getAuthorities()) ? new ArrayList<>() : msg.getAuthorities();
        additions = (null == msg.getAdditions()) ? new ArrayList<>() : msg.getAdditions();
        header = msg.getHeader();

        if (null == header) {
            throw new RuntimeException("非法DNS数据包！");
        }
        // 截断包处理
        if (1 == header.getTruncation()) {
            // todo 转到截断包处理
        }
        msgType(header.getMsgType());
    }

    /**
     * 处理请求、响应
     *
     * @param msgType 消息类型
     */
    private void msgType(byte msgType) {
        switch (msgType) {
            case MsgType.REQUEST:
                handleRequest();
            case MsgType.RESPONSE:
                handleResponse();
        }
    }

    /**
     * 处理响应
     */
    private void handleResponse() {
        // todo 处理DNS响应信息
    }

    /**
     * 处理查询请求
     */
    private void handleRequest() {
        /// 判断查询方式，并做出处理
        opCode(header.getOpCode());
        // 处理头部信息：官方应答、递归能力、返回码
        header.setMsgType(MsgType.RESPONSE);
        header.setAnswerCount(0);
        header.setAuthorityCount(0);
        header.setAdditionalCount(0);

        Msg msg = new Msg();
        if (null != answers && answers.size() > 0) {
            header.setAnswerCount(answers.size());
            header.setIsAuthority((byte) 0);
            msg.setAnswers(answers);
            success = true;
        }
        if (null != authorities && authorities.size() > 0) {
            header.setAuthorityCount(authorities.size());
            header.setIsAuthority((byte) 1);
            msg.setAuthorities(authorities);
            success = true;
        }
        if (null != additions && additions.size() > 0) {
            header.setAdditionalCount(additions.size());
            header.setIsAuthority((byte) 0);
            msg.setAdditions(additions);
            success = true;
        }

        if (success) {
            header.setReturnCode(ReturnCode.SUCCESS);
        } else {
            header.setIsAuthority((byte) 0);
            header.setReturnCode(ReturnCode.SERVER_ERROR);
        }
        msg.setHeader(header);
        msg.setQueries(queries);

        Packet packet = processor.encode(msg);

        if (header.getAuthorityCount() > 0)
            System.out.print("命中资源\n");

        System.out.println("==========================================================================");
        System.out.println("IP: http:/" + address + ":" + port + " 正在查询域名: " + queries.get(0).getName());
        System.out.println("Time: " + new Date());
        System.out.println("Result: " + success);
        System.out.println("==========================================================================");

        UdpSender.send(new DatagramPacket(packet.getData(), 0, packet.getBufLength(), address, port), socket);
    }


    private void opCode(byte opCode) {
        switch (opCode) {
            case OpCode.QUERY:
                query();
            case OpCode.IQUERY:
                iquery();
            case OpCode.STATUS:
                status();
        }
    }

    /**
     * 服务器状态查询
     */
    private void status() {
        // todo 服务器状态查询实现
    }

    /**
     * 反向查询
     */
    private void iquery() {
        // todo 反向查询实现
    }

    /**
     * 正向查询
     */
    private void query() {
        // 多个查询
        for (Query query : queries) {
            String name = query.getName();
            short type = query.getType();
            short clazz = query.getClazz();
            Authority authority = getResource(name, type, clazz);
            if (null != authority) {
                authorities.add(authority);
            } else {
                // todo 查不到域名的处理结果:可以问其他dns服务器，保存事务ID、IP、端口即可
            }
        }
    }

    /**
     * 获取自家资源的方法
     */
    private Authority getResource(String name, short type, short clazz) {
        switch (type) {
            case ResponseType.A:
                return NameFinder.getA(name, clazz);
//            case ResponseType.NS:
//                return NameFinder.getNs(name, clazz);
//            case ResponseType.CNAME:
//                return NameFinder.getCname(name, clazz);
//            case ResponseType.PTR:
//                return NameFinder.getPtr(name, clazz);
//            case ResponseType.MX:
//                return NameFinder.getMx(name, clazz);
            case ResponseType.AAAA:
                return NameFinder.getAaaa(name, clazz);
//            case ResponseType.SRV:
//                return NameFinder.getSrv(name, clazz);
//            case ResponseType.IXFR:
//                return NameFinder.getIxfr(name, clazz);
//            case ResponseType.AXFR:
//                return NameFinder.getAxfr(name, clazz);
//            case ResponseType.ALL:
//                return NameFinder.getAll(name, clazz);
        }
        return null;
    }


}
