package com.mf.xgim.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 *
 * @author xgimenez
 * @see com.mf.xgim
 * @since 1.0.0
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"com.mf.xgim.model", "com.mf.xgim.repository",
        "com.mf.xgim.respository", "com.mf.xgim.controller"})
public class WebMVCConfiguration {
}
