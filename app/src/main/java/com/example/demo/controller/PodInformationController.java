package com.example.demo.controller;

import java.net.InetAddress;

import org.springframework.web.bind.annotation.GetMapping;

public class PodInformationController {
    @GetMapping(path = "/os")
    public String os() {
        return System.getProperty("os.name").toLowerCase();
    }

    @GetMapping(path = "/hostname")
    public String hostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "failed to get hostname";
    }

    @GetMapping(path = "/ipaddr")
    public String ipaddr() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "failed to get hostname";
    }
}