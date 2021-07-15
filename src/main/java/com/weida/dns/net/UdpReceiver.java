package com.weida.dns.net;

import com.weida.dns.handler.DnsHandler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;

/**
 * @author weida
 */
public class UdpReceiver {

    private static final int PORT = 53;

    public void run() {
        try {
            System.out.println("DNS服务器运行于IP: http://" + Inet4Address.getLocalHost().getHostAddress() + " PORT: " + PORT);
            System.out.println("开始体验吧！");
            DatagramSocket socket = new DatagramSocket(PORT);
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            while (true) {
                socket.receive(packet);
                DnsHandler handler = new DnsHandler();
                handler.handleReception(packet, socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
