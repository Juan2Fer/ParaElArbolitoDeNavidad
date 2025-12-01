package Trabajo3CesII.Repository;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    List<T> list();
    Integer crear(T t);
    Optional<T> buscar(Integer id);
    String actualizar(T t, Integer id);
    String eliminar(Integer id);
    Integer getTotalElements();
    List<T> myPagination(int pageNumber, int pageSize);
}