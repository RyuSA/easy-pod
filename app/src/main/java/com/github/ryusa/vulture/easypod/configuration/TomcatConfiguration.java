package com.github.ryusa.vulture.easypod.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.access.tomcat.LogbackValve;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

@Configuration
public class TomcatConfiguration {
    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        tomcatServletWebServerFactory.addContextValves(new LogbackValve());
        return tomcatServletWebServerFactory;
    }    
}
