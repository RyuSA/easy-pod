package com.github.ryusa.vulture.easypod.controller;

import java.net.InetAddress;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/pod")
public class PodInformationController {

    private final ApplicationEventPublisher eventPublisher;

    public PodInformationController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @GetMapping(path = "/os")
    public String os() {
        return System.getProperty("os.name").toLowerCase();
    }

    @GetMapping(path = "/hostname")
    public String hostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "failed to get hostname";
    }

    @GetMapping(path = "/ipaddr")
    public String ipaddr() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "failed to get hostname";
    }

    @GetMapping(path = "/sleep")
    public String sleep(@RequestParam int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "bad ... morning";
        }
        return "good morning";
    }

    static class StatusUpdateRequest {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    @PostMapping("/health/readiness")
    public String readiness(@RequestBody StatusUpdateRequest request) {
        switch (request.getStatus()) {
            case "up":
                AvailabilityChangeEvent.publish(this.eventPublisher, "user request",
                        ReadinessState.ACCEPTING_TRAFFIC);
                break;
            case "down":
                AvailabilityChangeEvent.publish(this.eventPublisher, "user request",
                        ReadinessState.REFUSING_TRAFFIC);
                break;
            default:
                break;
        }
        return "done";
    }

    @PostMapping("/health/liveness")
    public String liveness(@RequestBody StatusUpdateRequest request) {
        switch (request.getStatus()) {
            case "up":
                AvailabilityChangeEvent.publish(this.eventPublisher, "user request",
                        LivenessState.CORRECT);
                break;
            case "down":
                AvailabilityChangeEvent.publish(this.eventPublisher, "user request",
                        LivenessState.BROKEN);
                break;
            default:
                break;
        }
        return "done";
    }

}
