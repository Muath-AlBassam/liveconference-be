package com._4coders.liveconference.configuration;

import com._4coders.liveconference.entities.ipAddress.IpAddressConnectionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:static/ipdate_configuration.properties")
public class IpDataConfiguration {
    @Autowired
    private Environment environment;

    @Bean
    public IpAddressConnectionInfo ipAddressConnectionInfo() {
        return new IpAddressConnectionInfo(environment.getProperty("ipdate_url"), environment.getProperty("ipdate_get_ip_info_full_url"),
                environment.getProperty("ipdate_api_key"), environment.getProperty("ipdate_api_key_string"));
    }
}
