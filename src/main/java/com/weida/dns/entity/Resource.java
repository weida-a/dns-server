package com.weida.dns.entity;

import lombok.Data;
import lombok.ToString;

/**
 * DNS 资源实体
 *
 * @author weida
 */
@Data
@ToString
public class Resource {

    // 域名，可变长度
    private String name;
    // 返回类型：16bit
    private short type;
    // 地址类型，通常设置为0x0001,16bit
    private short clazz;
    // 有效期 32-bit 无符号
    private int ttl;
    // 资源数据长度，16bit
    private short length;
    // 资源数据，可变长度
    private String resource;
    // 资源数据字节数组，可变长度
    private byte[] rByteArr;

}
