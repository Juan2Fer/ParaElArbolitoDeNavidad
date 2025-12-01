package Trabajo3CesII.Repository;

import Trabajo3CesII.Models.Diagnostico;
import Trabajo3CesII.Models.NivelUrgencia;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class DiagnosticoRepositorio implements DAO<Diagnostico> {

    private JdbcTemplate jdbcTemplate;

    public DiagnosticoRepositorio(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    // mapea filas de resultados de una consulta SQL a objetos Java
    RowMapper<Diagnostico> rowMapper = (rs, rowNum) -> {

        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setId(rs.getInt("id_diagnostico"));
        diagnostico.setVehiculoId(rs.getInt("id_vehiculo"));
        diagnostico.setFechaDiagnostico(rs.getDate("fecha_diagnostico").toLocalDate());
        diagnostico.setProblemaDetectado(rs.getString("problema_detectado"));
        diagnostico.setNivelUrgencia(NivelUrgencia.valueOf(rs.getString("nivel_urgencia")));
        diagnostico.setObservaciones(rs.getString("observaciones"));

        return diagnostico;
    };

    @Override
    public List<Diagnostico> list() {
        String sql = "SELECT id_diagnostico, id_vehiculo, fecha_diagnostico, problema_detectado, nivel_urgencia, observaciones from diagnostico";
        return jdbcTemplate.query(sql,rowMapper);
    }

    @Override
    public Integer crear(Diagnostico diagnostico) {

        String sql = "INSERT INTO diagnostico (id_vehiculo, fecha_diagnostico, problema_detectado, nivel_urgencia, observaciones) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, diagnostico.getVehiculoId());
            ps.setDate(2, Date.valueOf(diagnostico.getFechaDiagnostico()));
            ps.setString(3, diagnostico.getProblemaDetectado());
            ps.setString(4, diagnostico.getNivelUrgencia().name());
            ps.setString(5, diagnostico.getObservaciones());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public Optional<Diagnostico> buscar(Integer id) {
        String sql = "SELECT id_diagnostico, id_vehiculo, fecha_diagnostico, problema_detectado, nivel_urgencia, observaciones FROM diagnostico WHERE id_diagnostico = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public String actualizar(Diagnostico diagnostico, Integer id) {
        String sql = "UPDATE diagnostico SET problema_detectado = ?, nivel_urgencia = ?, observaciones = ? WHERE id_diagnostico = ?";
        int update = jdbcTemplate.update(sql,diagnostico.getProblemaDetectado(), diagnostico.getNivelUrgencia().toString(), diagnostico.getObservaciones(), id);
        return update == 1 ? "Diagnóstico actualizado id: " + diagnostico.getId() : "No se pudo actualizar diagnóstico id: " + diagnostico.getId();
    }

    @Override
    public String eliminar(Integer id) {
        String sql = "DELETE FROM diagnostico WHERE id_diagnostico = ?";
        int delete = jdbcTemplate.update(sql,id);
        return delete == 1 ? "Se eliminó el diagnóstico con id: " + id : "No se pudo eliminar el diagnóstico con id: " + id;
    }

    @Override
    public Integer getTotalElements() {
        String sql = "SELECT COUNT(*) FROM diagnostico";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public List<Diagnostico> myPagination(int pageNumber, int pageSize) {
        int offset = 0;
        if (pageNumber > 1) {
            offset = (pageNumber - 1) * pageSize;
        }
        String sql = "SELECT id_diagnostico, id_vehiculo, fecha_diagnostico, problema_detectado, nivel_urgencia, observaciones FROM diagnostico ORDER BY id_diagnostico DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[] {pageSize, offset}, rowMapper);
    }

    public Optional<Diagnostico> buscarPorVehiculo(Integer id) {
        String sql = "SELECT id_diagnostico, id_vehiculo, fecha_diagnostico, problema_detectado, nivel_urgencia, observaciones FROM diagnostico WHERE id_vehiculo = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst();
    }
}