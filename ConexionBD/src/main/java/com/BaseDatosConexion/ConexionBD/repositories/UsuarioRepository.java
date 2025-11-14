package com.BaseDatosConexion.ConexionBD.repositories;

import com.BaseDatosConexion.ConexionBD.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
}
