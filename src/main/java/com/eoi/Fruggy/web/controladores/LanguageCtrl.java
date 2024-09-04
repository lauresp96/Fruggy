package com.eoi.Fruggy.web.controladores;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;

@Controller
public class LanguageCtrl {


    // Método para cambiar de idiomas ES/EN
    @GetMapping("/language/{lang}")
    public RedirectView changeLanguage(@PathVariable String lang,
                                       @RequestParam(value = "redirect", defaultValue = "/") String redirectUrl,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        // Establecer el nuevo idioma
        Locale locale = new Locale(lang);
        LocaleContextHolder.setLocale(locale);
        request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);

        // Redirigir a la URL proporcionada, o a la página de inicio si no se proporciona URL
        return new RedirectView(redirectUrl, true);
    }
}
