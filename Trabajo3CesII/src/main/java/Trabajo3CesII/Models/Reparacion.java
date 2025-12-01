package Trabajo3CesII.Models;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reparacion implements Serializable {

    @NotNull(message = "Id es obligatorio")
    @Min(value = 0, message = "Valor debe ser positivo")
    private Integer id;

    @NotBlank(message = "Descripción es obligatoria")
    @Size(min = 10, max = 255, message = "Descripción de 10-255 letras")
    private String descripcionReparacion;

    @NotNull(message = "Costo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "Costo debe ser mayor a 0")
    private Double costo;

    @NotNull(message = "Tiempo estimado es obligatorio")
    @Min(value = 1, message = "Tiempo mínimo 1 hora")
    @Max(value = 500, message = "Tiempo máximo 500 horas")
    private Integer tiempoEstimado;

    @NotNull(message = "Id del vehículo es obligatorio")
    @Min(value = 0, message = "Valor debe ser positivo")
    private Integer vehiculoId;

    @NotNull(message = "Fecha obligatoria")
    @PastOrPresent(message = "Fecha debe ser hoy o en el pasado")
    private LocalDate fechaInicio;

    public Reparacion(){
        this.id = 0;
    }

    public Reparacion(String descripcionReparacion, Double costo, Integer tiempoEstimado, Integer vehiculoId, LocalDate fechaInicio) {
        this.descripcionReparacion = descripcionReparacion;
        this.costo = costo;
        this.tiempoEstimado = tiempoEstimado;
        this.vehiculoId = vehiculoId;
        this.fechaInicio = fechaInicio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcionReparacion() {
        return descripcionReparacion;
    }

    public void setDescripcionReparacion(String descripcionReparacion) {
        this.descripcionReparacion = descripcionReparacion;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public Integer getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(Integer tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public Integer getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(Integer vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        CharSequence charSequenceDate = fechaInicio.format(formatter);
        this.fechaInicio = LocalDate.parse(charSequenceDate, formatter);
    }

    @Override
    public String toString() {
        return  "id= " + id +
                ", descripcionReparacion= " + descripcionReparacion +
                ", costo= " + costo +
                ", tiempoEstimado= " + tiempoEstimado +
                ", vehiculoId= " + vehiculoId +
                ", fechaInicio= " + fechaInicio;
    }
}
