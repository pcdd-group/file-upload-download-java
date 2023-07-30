package org.pcdd.fileuploaddownload;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author pcdd
 * create at 2023/07/30 07:16
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 设置允许跨域的接口路径
                .allowedOriginPatterns("*") // 允许所有域进行跨域请求
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的请求方法
                .allowedHeaders("*") // 允许的请求头，默认情况下允许所有
                .allowCredentials(true) // 是否允许发送认证信息（如 Cookie 或 HTTP 认证）
                .maxAge(3600); // 预检请求的缓存时间，单位秒
    }

}

