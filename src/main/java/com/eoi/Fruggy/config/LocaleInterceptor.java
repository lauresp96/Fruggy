package com.eoi.Fruggy.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/*Interceptor que se encarga de interceptar las solicitudes HTTP antes de que lleguen al controlador.
 Este interceptor establece un atributo "currentUrl" en cada solicitud entrante para poder acceder a la URL actual.
 Hecho para poder cambiar de idiomas ES/EN
 */
@Component
public class LocaleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("currentUrl", request.getRequestURI());
        return true;
    }

}
