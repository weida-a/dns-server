package com.weida.dns.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * DNS 消息格式
 *
 * @author weida
 */
@Data
@ToString
public class Msg {
    private Header header;
    private List<Query> queries;
    private List<Answer> answers;
    private List<Authority> authorities;
    private List<Addition> additions;
}
