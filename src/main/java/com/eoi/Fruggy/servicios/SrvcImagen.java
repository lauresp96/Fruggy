package com.eoi.Fruggy.servicios;

import com.eoi.Fruggy.entidades.Imagen;
import com.eoi.Fruggy.entidades.Producto;
import com.eoi.Fruggy.entidades.Supermercado;
import com.eoi.Fruggy.repositorios.RepoImagen;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class SrvcImagen extends AbstractSrvc<Imagen, Long, RepoImagen> {
    protected SrvcImagen(RepoImagen repoImagen) {
        super(repoImagen);
    }

    private final Path LOCATION = Paths.get("D:/ficheros");

    public Imagen guardarImagen(MultipartFile file, Supermercado supermercado) throws Exception {
        // Genera un nombre único para la imagen
        String nombreArchivo = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path destino = this.LOCATION.resolve(nombreArchivo);

        // Guarda la imagen en el sistema de archivos
        Files.copy(file.getInputStream(), destino);

        // Crea la entidad Imagen
        Imagen imagen = new Imagen();
        imagen.setNombreArchivo(nombreArchivo);
        imagen.setRutaImagen(nombreArchivo);
        imagen.setSupermercado(supermercado);

        // Guarda la entidad en la base de datos
        return guardar(imagen);
    }

    public Set<Imagen> buscarPorSupermercado(Supermercado supermercado) {
        return getRepo().findBySupermercado(supermercado);
    }

    public Imagen guardarImagenProducto(MultipartFile file, Producto producto) throws Exception {
        // Genera un nombre único para la imagen
        String nombreArchivo = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path destino = this.LOCATION.resolve(nombreArchivo);
        // Guarda la imagen en el sistema de archivos
        Files.copy(file.getInputStream(), destino);
        // Crea la entidad Imagen
        Imagen imagen = new Imagen();
        imagen.setNombreArchivo(nombreArchivo);
        imagen.setRutaImagen(nombreArchivo);
        imagen.setProductos(producto);

        // Guarda la entidad en la base de datos
        return guardar(imagen);
    }

    public Set<Imagen> buscarPorProducto(Producto producto) {
        return getRepo().findByProductos(producto);
    }

}