package com.eoi.Fruggy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


 /* Configuración personalizada para Jackson, la biblioteca de procesamiento de JSON utilizada por Spring.
         Define un bean de ObjectMapper con configuraciones específicas para la serialización y deserialización de objetos, en este caso usado para subcategorias.*/
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Configura para no fallar en la serialización de objetos vacíos.
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // Configura para habilitar la salida de JSON con formato indentado.
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        // Configura para deserializar con el valor raíz desenvuelto.
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        return mapper;
    }
}
