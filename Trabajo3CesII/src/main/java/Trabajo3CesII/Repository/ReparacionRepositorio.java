package Trabajo3CesII.Repository;

import Trabajo3CesII.Models.Reparacion;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ReparacionRepositorio implements DAO<Reparacion>{

    private JdbcTemplate jdbcTemplate;

    public ReparacionRepositorio(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    // mapea filas de resultados de una consulta SQL a objetos Java
    RowMapper<Reparacion> rowMapper = (rs, rowNum) -> {

        Reparacion reparacion = new Reparacion();
        reparacion.setId(rs.getInt("id_reparacion"));
        reparacion.setDescripcionReparacion(rs.getString("descripcion_reparacion"));
        reparacion.setCosto(rs.getDouble("costo"));
        reparacion.setTiempoEstimado(rs.getInt("tiempo_estimado"));
        reparacion.setVehiculoId(rs.getInt("id_vehiculo"));
        reparacion.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());

        return reparacion;
    };

    @Override
    public List<Reparacion> list() {
        String sql = "SELECT id_reparacion, descripcion_reparacion, costo, tiempo_estimado, id_vehiculo, fecha_inicio from reparacion";
        return jdbcTemplate.query(sql,rowMapper);
    }

    @Override
    public Integer crear(Reparacion reparacion) {

        String sql = "INSERT INTO reparacion (descripcion_reparacion, costo, tiempo_estimado, id_vehiculo, fecha_inicio) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, reparacion.getDescripcionReparacion());
            ps.setDouble(2, reparacion.getCosto());
            ps.setInt(3, reparacion.getTiempoEstimado());
            ps.setInt(4, reparacion.getVehiculoId());
            ps.setDate(5, Date.valueOf(reparacion.getFechaInicio()));
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public Optional<Reparacion> buscar(Integer id) {
        String sql = "SELECT id_reparacion, descripcion_reparacion, costo, tiempo_estimado, id_vehiculo, fecha_inicio FROM reparacion WHERE id_reparacion = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst();
    }

    public Optional<Reparacion> buscar2(Integer vehiculoId, Integer id) {
        String sql = "SELECT id_reparacion, descripcion_reparacion, costo, tiempo_estimado, id_vehiculo, fecha_inicio FROM reparacion WHERE id_reparacion = ? AND id_vehiculo = ?";
        return jdbcTemplate.query(sql, rowMapper, id, vehiculoId)
                .stream()
                .findFirst();
    }

    @Override
    public String actualizar(Reparacion reparacion, Integer id) {
        String sql = "UPDATE reparacion SET descripcion_reparacion = ?, costo = ?, tiempo_estimado = ?, fecha_inicio = ? WHERE id_reparacion = ?";
        int update = jdbcTemplate.update(sql,reparacion.getDescripcionReparacion(), reparacion.getCosto(), reparacion.getTiempoEstimado(), Date.valueOf(reparacion.getFechaInicio()), id);
        return update == 1 ? "Reparación actualizada id: " + reparacion.getId() : "No se pudo actualizar reparación id: " + reparacion.getId();
    }

    @Override
    public String eliminar(Integer id) {
        String sql = "DELETE FROM reparacion WHERE id_reparacion = ?";
        int delete = jdbcTemplate.update(sql,id);
        return delete == 1 ? "Se eliminó la reparación con id: " + id : "No se pudo eliminar la reparación con id: " + id;
    }

    public Integer getTotalElements() {
        return 0;
    }

    public Integer getTotalElements2(int id) {
        String sql = "SELECT COUNT(*) FROM reparacion WHERE id_vehiculo = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id);
    }

    public List<Reparacion> myPagination(int pageNumber, int pageSize) {
        return new ArrayList<>();
    }

    public List<Reparacion> myPagination2(int id, int pageNumber, int pageSize) {
        int offset = 0;
        if (pageNumber > 1) {
            offset = (pageNumber - 1) * pageSize;
        }
        String sql = "SELECT * FROM reparacion WHERE id_vehiculo = ? ORDER BY id_reparacion DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[] {id, pageSize, offset},
                new BeanPropertyRowMapper<>(Reparacion.class));
    }
}