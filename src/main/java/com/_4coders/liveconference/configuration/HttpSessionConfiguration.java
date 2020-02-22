package com._4coders.liveconference.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.HttpSessionEventPublisher;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import java.util.HashMap;

@Configuration
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1200, cleanupCron = "0 0 6 * * ?")
@EnableSpringHttpSession
public class HttpSessionConfiguration {

    /*So that the sessionId won't be saved as a cookie but it will be sent as an x-auth-token in the header */
    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public MapSessionRepository sessionRepository() {
        return new MapSessionRepository(new HashMap());
    }


}
