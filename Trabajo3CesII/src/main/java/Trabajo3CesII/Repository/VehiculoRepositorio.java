package Trabajo3CesII.Repository;

import Trabajo3CesII.Models.EstadoVehiculo;
import Trabajo3CesII.Models.Vehiculo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class VehiculoRepositorio implements DAO<Vehiculo> {

    private static final Logger log = LoggerFactory.getLogger(VehiculoRepositorio.class);
    private JdbcTemplate jdbcTemplate;

    public VehiculoRepositorio(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Vehiculo> rowMapper = (rs,rowNum) -> {

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(rs.getInt("id_vehiculo"));
        vehiculo.setPlaca(rs.getString("placa"));
        vehiculo.setMarca(rs.getString("marca"));
        vehiculo.setModelo(rs.getString("modelo"));
        vehiculo.setEstadoVehiculo(EstadoVehiculo.valueOf(rs.getString("estado_vehiculo")));

        return vehiculo;
    };

    @Override
    public List<Vehiculo> list() {
        String sql = "SELECT id_vehiculo, placa, marca, modelo, estado_vehiculo from vehiculo";
        return jdbcTemplate.query(sql,rowMapper);
    }

    @Override
    public Integer crear(Vehiculo vehiculo) {

        String sql = "INSERT INTO vehiculo (placa, marca, modelo, estado_vehiculo) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, vehiculo.getPlaca());
            ps.setString(2, vehiculo.getMarca());
            ps.setString(3, vehiculo.getModelo());
            ps.setString(4, vehiculo.getEstadoVehiculo().name());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public Optional<Vehiculo> buscar(Integer id) {
        String sql = "SELECT id_vehiculo, placa, marca, modelo, estado_vehiculo FROM vehiculo WHERE id_vehiculo = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public String actualizar(Vehiculo vehiculo, Integer id) {
        String sql = "UPDATE vehiculo SET placa = ?, marca = ?, modelo = ?, estado_vehiculo = ? WHERE id_vehiculo = ?";
        int update = jdbcTemplate.update(sql,vehiculo.getPlaca(), vehiculo.getMarca(), vehiculo.getModelo(), vehiculo.getEstadoVehiculo().toString(), id);
        return update == 1 ? "Vehículo actualizado: " + vehiculo.getPlaca() : "No se pudo actualizar: " + vehiculo.getPlaca();
    }

    @Override
    public String eliminar(Integer id) {
        String sql = "DELETE FROM vehiculo WHERE id_vehiculo = ?";
        int delete = jdbcTemplate.update(sql,id);
        return delete == 1 ? "Se eliminó el vehículo con id: " + id : "No se pudo eliminar el vehículo con id: " + id;
    }

    @Override
    public Integer getTotalElements() {
        String sql = "SELECT COUNT(*) FROM vehiculo";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public List<Vehiculo> myPagination(int pageNumber, int pageSize) {
        int offset = 0;
        if (pageNumber > 1) {
            offset = (pageNumber - 1) * pageSize;
        }
        String sql = "SELECT * FROM vehiculo ORDER BY id_vehiculo DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[] {pageSize, offset},
                new BeanPropertyRowMapper<>(Vehiculo.class));
    }
}