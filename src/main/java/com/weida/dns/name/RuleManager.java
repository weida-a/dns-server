package com.weida.dns.name;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 域名映射规则管理
 *
 * @author weida
 */
public class RuleManager {

    private static RuleManager ruleManager;
    private static final Map<String, String> ipv4Map = new HashMap<>();
    private static final Map<String, String> ipv6Map = new HashMap<>();

    public static RuleManager getInstance() {
        if (null == ruleManager) {
            ruleManager = new RuleManager();
        }
        return ruleManager;
    }

    public RuleManager() {
        initRule();
    }

    public void rmIpv6(String name) {
        ipv6Map.remove(name);
    }

    public void rmIpv4(String name) {
        ipv4Map.remove(name);
    }

    public void putIpv6(String name, String ip) {
        if (!StringUtils.isBlank(name) && !StringUtils.isBlank(ip)) {
            ipv6Map.put(name, ip);
        }
    }

    public void putIpv4(String name, String ip) {
        if (!StringUtils.isBlank(name) && !StringUtils.isBlank(ip)) {
            ipv4Map.put(name, ip);
        }
    }

    public String getIpv4(String name) {
        if ("dns.weidaa.com".equals(name)) {
            // todo 暂时的更新规则方案
            System.out.println("命中更新规则操作的域名");
            ipv4Map.clear();
            ipv6Map.clear();
            initRule();
        }
        // todo 模糊查询、精准查询实现
        return ipv4Map.get(name);
    }

    public String getIpv6(String name) {
        // todo 模糊查询、精准查询实现
        return ipv6Map.get(name);
    }

    private void initRule() {
        List<Config.Host> hosts = Config.getHosts();
        if (null != hosts && hosts.size() > 0) {
            for (Config.Host host : hosts) {
                if (4 == host.getType()) {
                    ipv4Map.put(host.getName(), host.getIp());
                }
                if (6 == host.getType()) {
                    ipv6Map.put(host.getName(), host.getIp());
                }
            }
        }
    }


}
