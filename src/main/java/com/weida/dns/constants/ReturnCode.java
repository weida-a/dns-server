package com.weida.dns.constants;

/**
 * 返回码 由服务器设置，共16个值，其中6～15被保留使用
 *
 * @author weida
 */
public class ReturnCode {

    // 成功
    public static byte SUCCESS = 0;
    // 格式错误：名称服务器不能解析查询
    public static byte FORMAT_ERROR = 1;
    // 服务器故障：由于服务器的问题，服务器不能处理这个查询
    public static byte SERVER_ERROR = 2;
    // 名称错误：仅对来自权威名称服务器的响应有意义，
    public static byte NAME_ERROR = 3;
    // 未实现
    public static byte NOT_IMPLEMENTED = 4;
    // 拒绝
    public static byte REFUSED = 5;

}
