package com.weida.dns.name;

import com.weida.dns.constants.ResponseType;
import com.weida.dns.entity.Authority;
import com.weida.dns.entity.Packet;
import org.apache.commons.lang3.StringUtils;


/**
 * 域名正向查询
 *
 * @author weida
 */
public class NameFinder {

    private static final int TTL = 100;
    private static final RuleManager rule = RuleManager.getInstance();

    public static Authority getA(String name, short clazz) {
        String ip = rule.getIpv4(name);
        if (!StringUtils.isBlank(ip)) {
            String[] res = ip.split("\\.");
            Packet packet = new Packet(res.length);
            for (String r : res) {
                packet.addByte((byte) Integer.parseInt(r));
            }
            return getAuthority(name, ResponseType.A, clazz, ip, packet);
        }
        return null;
    }

    public static Authority getAaaa(String name, short clazz) {
        String ip = rule.getIpv6(name);
        if (!StringUtils.isBlank(ip)) {
            String[] res = ip.split(":");
            Packet packet = new Packet(128);
            for (String re : res) {
                re = re.toLowerCase();
                int length = re.toCharArray().length;
                for (int i = 0; i < 4 - length; i++) {
                    re = "0" + re;
                }
                int[] temp = new int[4];
                for (int i = 0; i < re.toCharArray().length; i++) {
                    char c = re.toCharArray()[i];
                    if (c < 58) {
                        temp[i] = c - 48;
                    } else {
                        temp[i] = c - 87;
                    }
                }
                packet.addByte((byte) ((temp[0] << 4) + temp[1]));
                packet.addByte((byte) ((temp[2] << 4) + temp[3]));
            }
            return getAuthority(name, ResponseType.AAAA, clazz, ip, packet);
        }
        return getA(name, clazz);
    }

    private static Authority getAuthority(String name, short type, short clazz, String ip, Packet packet) {
        Authority a = new Authority();
        a.setName(name);
        a.setType(type);
        a.setClazz(clazz);
        a.setTtl(TTL);
        a.setLength((short) packet.getBufLength());
        a.setResource(ip);
        a.setRByteArr(packet.getData());
        return a;
    }

}
