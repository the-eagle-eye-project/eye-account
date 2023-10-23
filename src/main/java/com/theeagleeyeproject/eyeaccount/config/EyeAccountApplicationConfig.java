package com.theeagleeyeproject.eyeaccount.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ApplicationObjectMapper.class)
@ComponentScan(basePackages = "com.theeagleeyeproject.eyeaccount")
public class EyeAccountApplicationConfig {
}
