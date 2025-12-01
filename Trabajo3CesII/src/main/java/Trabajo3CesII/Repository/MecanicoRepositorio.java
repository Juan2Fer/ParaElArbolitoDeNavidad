package Trabajo3CesII.Repository;

import Trabajo3CesII.Models.Especialidad;
import Trabajo3CesII.Models.EstadoMecanico;
import Trabajo3CesII.Models.Mecanico;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class MecanicoRepositorio implements DAO<Mecanico> {
    private JdbcTemplate jdbcTemplate;

    public MecanicoRepositorio(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // mapea filas de resultados de una consulta SQL a objetos Java
    RowMapper<Mecanico> rowMapper = (rs, rowNum) -> {

        Mecanico mecanico = new Mecanico();
        mecanico.setId(rs.getInt("id_mecanico"));
        mecanico.setNombre(rs.getString("nombre"));
        mecanico.setEspecialidad(Especialidad.valueOf(rs.getString("especialidad")));
        mecanico.setAñosExperiencia(rs.getInt("años_experiencia"));
        mecanico.setEstado(EstadoMecanico.valueOf(rs.getString("estado")));

        return mecanico;
    };

    @Override
    public List<Mecanico> list() {
        String sql = "SELECT id_mecanico, nombre, especialidad, años_experiencia, estado from mecanico";
        return jdbcTemplate.query(sql,rowMapper);
    }

    @Override
    public Integer crear(Mecanico mecanico) {
        String sql = "INSERT INTO mecanico (nombre, especialidad, años_experiencia, estado) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, mecanico.getNombre());
            ps.setString(2, mecanico.getEspecialidad().name());
            ps.setInt(3, mecanico.getAñosExperiencia());
            ps.setString(4, mecanico.getEstado().name());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public Optional<Mecanico> buscar(Integer id) {
        String sql = "SELECT id_mecanico, nombre, especialidad, años_experiencia, estado FROM mecanico WHERE id_mecanico = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public String actualizar(Mecanico mecanico, Integer id) {
        String sql = "UPDATE mecanico SET nombre = ?, especialidad = ?, años_experiencia = ?, estado = ? WHERE id_mecanico = ?";
        int update = jdbcTemplate.update(sql,mecanico.getNombre(), mecanico.getEspecialidad().toString(), mecanico.getAñosExperiencia(), mecanico.getEstado().toString(), id);
        return update == 1 ? "Mecánico actualizado: " + mecanico.getNombre() : "No se pudo actualizar el mecánico: " + mecanico.getId();
    }

    @Override
    public String eliminar(Integer id) {
        String sql = "DELETE FROM mecanico WHERE id_mecanico = ?";
        int delete = jdbcTemplate.update(sql,id);
        return delete == 1 ? "Se eliminó el mecánico con id: " + id : "No se pudo eliminar el mecánico con id: " + id;
    }

    @Override
    public Integer getTotalElements() {
        String sql = "SELECT COUNT(*) FROM mecanico";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public List<Mecanico> myPagination(int pageNumber, int pageSize) {
        int offset = 0;
        if (pageNumber > 1) {
            offset = (pageNumber - 1) * pageSize;
        }
        String sql = "SELECT id_mecanico, nombre, especialidad, años_experiencia, estado FROM mecanico ORDER BY id_mecanico DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[] {pageSize, offset}, rowMapper);
    }

    public List<String> getEspecialidades() {
        String sql = "SELECT DISTINCT especialidad FROM mecanico";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public int insertVehiculoMecanico(int vehiculoId, int mecanicoId) {
        String sql = "INSERT INTO vehiculo_mecanico (id_vehiculo, id_mecanico) VALUES (?, ?)";
        return jdbcTemplate.update(sql, ps -> {
            ps.setInt(1, vehiculoId);
            ps.setInt(2, mecanicoId);
        });
    }

    public List<Integer> getMecanicoIdsByVehiculoId(int vehiculoId) {
        String sql = "SELECT id_mecanico FROM vehiculo_mecanico WHERE id_vehiculo = ?";
        return jdbcTemplate.query(sql,
                new Object[] { vehiculoId },
                (ResultSet rs, int rowNum) -> rs.getInt("id_mecanico"));
    }

    public List<Mecanico> getMecanicosByVehiculoId2(int vehiculoId) {
        String sql = "SELECT m.id_mecanico, m.nombre, m.especialidad, m.años_experiencia, m.estado FROM mecanico m JOIN vehiculo_mecanico vm ON m.id_mecanico = vm.id_mecanico WHERE vm.id_vehiculo = ?";
        return jdbcTemplate.query(sql, new Object[] { vehiculoId }, (ResultSet rs, int rowNum) -> {
            Mecanico mecanico = new Mecanico();
            mecanico.setId(rs.getInt("id_mecanico"));
            mecanico.setNombre(rs.getString("nombre"));
            mecanico.setEspecialidad(Especialidad.valueOf(rs.getString("especialidad")));
            mecanico.setAñosExperiencia(rs.getInt("años_experiencia"));
            mecanico.setEstado(EstadoMecanico.valueOf(rs.getString("estado")));
            return mecanico;
        });
    }

    public int deleteVehiculoMecanico(int vehiculoId, int mecanicoId) {
        String sql = "DELETE FROM vehiculo_mecanico WHERE id_vehiculo = ? AND id_mecanico = ?";
        return jdbcTemplate.update(sql, vehiculoId, mecanicoId);
    }
}