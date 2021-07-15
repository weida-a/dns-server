package com.weida.dns.constants;

/**
 * DNS 响应类型
 *
 * @author weida
 */
public class ResponseType {
    public static final short A = 1;
    public static final short NS = 2;
    public static final short CNAME = 5;
    public static final short PTR = 12;
    public static final short MX = 15;
    public static final short AAAA = 28;
    public static final short SRV = 33;
    public static final short IXFR = 251;
    public static final short AXFR = 252;
    public static final short ALL = 255;
}
