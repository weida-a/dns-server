package com.weida.dns.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author weida
 */
public class UdpSender {

    public static void send(DatagramPacket dp, DatagramSocket socket) {
        try {
            socket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
