package com.eoi.Fruggy.apis;

import com.eoi.Fruggy.entidades.Categoria;
import com.eoi.Fruggy.servicios.SrvcCategoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

 // Controlador REST para gestionar las operaciones relacionadas con las categorías.
@RestController
@RequestMapping("/api")
public class APICategorias { // necesitamos este api para JS

    @Autowired
    private SrvcCategoria srvcCategoria;

    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> getCategorias() {
        // Llama al servicio para buscar todas las entidades de tipo 'Categoria'.
        List<Categoria> categorias = srvcCategoria.buscarEntidades();
        // Retorna la respuesta HTTP con la lista de categorías y el código de estado HTTP 200.
        return ResponseEntity.ok(categorias);
    }
}
