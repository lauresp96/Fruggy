package com.eoi.Fruggy.repositorios;

import com.eoi.Fruggy.entidades.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RepoUsuario extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Page<Usuario> findByEmailContainingIgnoreCase(String email, Pageable pageable);


}
