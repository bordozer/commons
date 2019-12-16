package com.bordozer.commons.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public final class IpUtils
{

    private IpUtils() {
    }

    public static Ip getIp() {

        try {
            InetAddress ip = InetAddress.getLocalHost();
            String hostname = ip.getHostName();
            log.info("Service network. IP: {}, Hostname: {}", ip, hostname);
            return new Ip(ip.getHostAddress(), hostname);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return new Ip("unknown", "unknown");
    }

    @Getter
    @RequiredArgsConstructor
    @ToString
    public static class Ip {
        private final String ip;
        private final String hostname;
    }
}
