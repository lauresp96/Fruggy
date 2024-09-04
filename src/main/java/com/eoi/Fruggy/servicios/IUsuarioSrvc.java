package com.eoi.Fruggy.servicios;

import com.eoi.Fruggy.entidades.Usuario;

public interface IUsuarioSrvc {
    /**
     * Codifica la contraseña del usuario proporcionado.
     * @param usuario El usuario cuya contraseña debe ser codificada.
     * @return La contraseña codificada.
     */
    public String getEncodedPassword (Usuario usuario);
}
