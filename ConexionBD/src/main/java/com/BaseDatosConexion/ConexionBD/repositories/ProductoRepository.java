package com.BaseDatosConexion.ConexionBD.repositories;

import com.BaseDatosConexion.ConexionBD.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
