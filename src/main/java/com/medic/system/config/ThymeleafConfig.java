package com.medic.system.config;

import com.medic.system.helpers.ThymeleafHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class ThymeleafConfig implements WebMvcConfigurer {
    private final ThymeleafHelper thymeleafHelper;
    private final ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    public void configureViewResolver(SpringTemplateEngine templateEngine) {
        thymeleafViewResolver.setStaticVariables(Collections.singletonMap("utils", thymeleafHelper));
    }
}