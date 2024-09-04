package com.eoi.Fruggy.apis;

import com.eoi.Fruggy.entidades.Cesta;
import com.eoi.Fruggy.entidades.Usuario;
import com.eoi.Fruggy.servicios.SrvcCesta;
import com.eoi.Fruggy.servicios.SrvcUsuario;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/cestas")
public class APICesta {

    private final SrvcCesta srvcCesta;
    private final SrvcUsuario srvcUsuario;


    public APICesta(SrvcCesta srvcCesta, SrvcUsuario srvcUsuario) {
        this.srvcCesta = srvcCesta;
        this.srvcUsuario = srvcUsuario;
    }

    // Crear una nueva cesta
    @PostMapping
    public Cesta crearCesta(@RequestParam Long usuarioId, @RequestBody Cesta cesta) throws Exception {
        Usuario usuario = srvcUsuario.encuentraPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        cesta.setUsuario(usuario);
        cesta.setFecha(LocalDateTime.now());
        return (Cesta) srvcCesta.guardar(cesta);
    }

    // Obtener todas las cestas de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<Cesta> obtenerCestasPorUsuario(@PathVariable Long usuarioId) {
        Usuario usuario = srvcUsuario.encuentraPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<Cesta> cestas = srvcCesta.buscarEntidades();
        return cestas.stream()
                .filter(cesta -> cesta.getUsuario().getId().equals(usuarioId))
                .toList(); // Filtrando las cestas por el ID del usuario
    }

    // Obtener una cesta por ID
    @GetMapping("/{id}")
    public Cesta obtenerCesta(@PathVariable Long id) throws Throwable {
        return (Cesta) srvcCesta.encuentraPorId(id)
                .orElseThrow(() -> new RuntimeException("Cesta no encontrada"));
    }

    // Actualizar una cesta
    @PutMapping("/{id}")
    public Cesta actualizarCesta(@PathVariable Long id, @RequestBody Cesta cestaActualizada) throws Throwable {
        Cesta cestaExistente = (Cesta) srvcCesta.encuentraPorId(id).orElseThrow(() -> new RuntimeException("Cesta no encontrada"));
        cestaExistente.setNombre(cestaActualizada.getNombre());
        return (Cesta) srvcCesta.guardar(cestaExistente);
    }

    // Eliminar una cesta
    @DeleteMapping("/{id}")
    public void eliminarCesta(@PathVariable Long id) throws Throwable {
        srvcCesta.encuentraPorId(id)
                .orElseThrow(() -> new RuntimeException("Cesta no encontrada"));
        srvcCesta.eliminarPorId(id);
    }
}
