package com.weida.dns;

import com.weida.dns.net.UdpReceiver;

/**
 * @author weida
 */
public class DnsStarter {
    public static void main(String[] args) {
        new UdpReceiver().run();
    }
}
