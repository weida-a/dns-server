package com.weida.dns.name;

import com.fasterxml.jackson.core.type.TypeReference;
import com.weida.dns.utils.JsonUtils;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author weida
 */
public class Config {

    public static List<Host> getHosts() {

        File home = new File(System.getProperty("user.home"));
        for (File file : home.listFiles()) {
            Path configPath = Paths.get(file.getAbsolutePath() + "/dnshost.config");
            File configFile = configPath.toFile();
            if (configFile.exists()) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    Files.copy(configPath, out);
                    String hosts = out.toString();
                    if (StringUtils.isBlank(hosts))
                        return null;
                    List<Host> list = JsonUtils.jsonToObj(hosts, new TypeReference<List<Host>>() {
                    });
                    System.out.println(list.toString());
                    return list;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    @Data
    @ToString
    static class Host {
        private String name;
        private Integer type;
        private String ip;
    }
}
