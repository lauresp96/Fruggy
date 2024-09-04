package com.eoi.Fruggy.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

//Configuración de carga de archivos para la aplicación.
@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(10)); // Tamaño máximo de archivo 10 mb
        factory.setMaxRequestSize(DataSize.ofMegabytes(10)); // Tamaño máximo de solicitud 10 mb
        return factory.createMultipartConfig();
    }
}
