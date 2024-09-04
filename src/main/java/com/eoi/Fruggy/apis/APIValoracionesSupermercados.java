package com.eoi.Fruggy.apis;

import com.eoi.Fruggy.entidades.Supermercado;
import com.eoi.Fruggy.entidades.ValoracionSupermercado;
import com.eoi.Fruggy.servicios.SrvcValSupermercado;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/valoraciones")

public class APIValoracionesSupermercados {

    private final SrvcValSupermercado valSupermercadoSrvc;

    public APIValoracionesSupermercados(SrvcValSupermercado valSupermercadoSrvc) {
        this.valSupermercadoSrvc = valSupermercadoSrvc;
    }


    @GetMapping("/supermercados/{supermercadoId}")
    public List<ValoracionSupermercado> obtenerValoracionesPorSupermercado(@PathVariable Long supermercadoId) {
        return valSupermercadoSrvc.obtenerValoracionesPorSupermercado(supermercadoId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ValoracionSupermercado> obtenerValoracionPorId(@PathVariable Long id) {
        Optional<ValoracionSupermercado> valoracion = valSupermercadoSrvc.encuentraPorId(id);
        return valoracion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/supermercados/{supermercadoId}")
    public ResponseEntity<ValoracionSupermercado> crearValoracion(@PathVariable Long supermercadoId, @RequestBody ValoracionSupermercado valoracion) {
        try {
            valoracion.setSupermercado(new Supermercado());
            valoracion.getSupermercado().setId(supermercadoId);
            ValoracionSupermercado nuevaValoracion = valSupermercadoSrvc.guardar(valoracion);
            return ResponseEntity.ok(nuevaValoracion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ValoracionSupermercado> actualizarValoracion(@PathVariable Long id, @RequestBody ValoracionSupermercado valoracion) {
        try {
            Optional<ValoracionSupermercado> valoracionExistente = valSupermercadoSrvc.encuentraPorId(id);
            if (valoracionExistente.isPresent()) {
                ValoracionSupermercado valoracionActualizada = valoracionExistente.get();
                valoracionActualizada.setComentario(valoracion.getComentario());
                valoracionActualizada.setNota(valoracion.getNota());
                valSupermercadoSrvc.guardar(valoracionActualizada);
                return ResponseEntity.ok(valoracionActualizada);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarValoracion(@PathVariable Long id) {
        valSupermercadoSrvc.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

}
