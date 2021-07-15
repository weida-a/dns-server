package com.weida.dns.entity;

import lombok.Data;
import lombok.ToString;

/**
 * DNS 查询实体
 *
 * @author weida
 */
@Data
@ToString
public class Query {

    // 域名，可变长度
    private String name;
    // 期待的返回类型：16bit
    private short type;
    // 地址类型，通常设置为0x0001,表示互联网地址，16bit
    private short clazz;

}
