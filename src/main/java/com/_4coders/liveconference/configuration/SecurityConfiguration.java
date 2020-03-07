package com._4coders.liveconference.configuration;

import com._4coders.liveconference.entities.account.AccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountDetailsService accountDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requiresChannel().anyRequest().requiresSecure();
        http.sessionManagement().maximumSessions(2);
        http.cors();
        http.authorizeRequests().antMatchers("/flogger/accounts/register").permitAll()
                .antMatchers("/flogger/accounts/activation_code").permitAll()
                .antMatchers("/flogger/accounts/activation_code/update").permitAll()
                .anyRequest().authenticated().and().httpBasic().and().formLogin().disable().csrf().disable();
//        http.authorizeRequests().antMatchers("flogger/accounts/register").permitAll().
//                anyRequest().permitAll().and().httpBasic().and().formLogin().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration = corsConfiguration.applyPermitDefaultValues();
        corsConfiguration.setExposedHeaders(Collections.singletonList("X-Auth-Token"));
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}

