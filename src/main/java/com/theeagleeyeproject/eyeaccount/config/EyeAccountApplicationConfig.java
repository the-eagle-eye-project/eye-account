package com.theeagleeyeproject.eyeaccount.config;

import com.theeagleeyeproject.eaglewings.config.EagleWingsConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import({ApplicationObjectMapper.class, EagleWingsConfig.class})
@ComponentScan(basePackages = "com.theeagleeyeproject.eyeaccount")
@PropertySource("classpath:application-${EAGLE-ENV}.properties")
public class EyeAccountApplicationConfig {
}
