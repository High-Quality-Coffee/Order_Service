package com.teamsparta14.order_service.global;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/properties/env.properties") // env.properties 파일 소스 등록
public class PropertyConfig {
}
