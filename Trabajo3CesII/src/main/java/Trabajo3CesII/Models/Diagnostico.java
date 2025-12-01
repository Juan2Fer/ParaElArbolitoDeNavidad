package Trabajo3CesII.Models;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Diagnostico implements Serializable {

    @NotNull(message = "Id es obligatorio")
    @Min(value = 0, message = "Valor debe ser positivo")
    private Integer id;

    @NotNull(message = "Id del vehículo es obligatorio")
    @Min(value = 0, message = "Valor debe ser positivo")
    private Integer vehiculoId;

    @NotNull(message = "Fecha obligatoria")
    @PastOrPresent(message = "Fecha debe ser hoy o en el pasado")
    private LocalDate fechaDiagnostico;

    @NotBlank(message = "Problema detectado es obligatorio")
    @Size(min = 10, max = 255, message = "Problema de 10-255 letras")
    private String problemaDetectado;

    @NotNull(message = "Nivel de urgencia es obligatorio")
    private NivelUrgencia nivelUrgencia;

    @Size(max = 500, message = "Observaciones máximo 500 letras")
    private String observaciones;

    public Diagnostico(){
        this.id = 0;
    }

    public Diagnostico(Integer vehiculoId, LocalDate fechaDiagnostico, String problemaDetectado, NivelUrgencia nivelUrgencia, String observaciones) {
        this.vehiculoId = vehiculoId;
        this.fechaDiagnostico = fechaDiagnostico;
        this.problemaDetectado = problemaDetectado;
        this.nivelUrgencia = nivelUrgencia;
        this.observaciones = observaciones;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(Integer vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public LocalDate getFechaDiagnostico() {
        return fechaDiagnostico;
    }

    public void setFechaDiagnostico(LocalDate fechaDiagnostico) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        CharSequence charSequenceDate = fechaDiagnostico.format(formatter);
        this.fechaDiagnostico = LocalDate.parse(charSequenceDate, formatter);
    }

    public String getProblemaDetectado() {
        return problemaDetectado;
    }

    public void setProblemaDetectado(String problemaDetectado) {
        this.problemaDetectado = problemaDetectado;
    }

    public NivelUrgencia getNivelUrgencia() {
        return nivelUrgencia;
    }

    public void setNivelUrgencia(NivelUrgencia nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return  "id= " + id +
                ", vehiculoId= " + vehiculoId +
                ", problemaDetectado= " + problemaDetectado +
                ", nivelUrgencia= " + nivelUrgencia +
                ", fechaDiagnostico= " + fechaDiagnostico +
                ", observaciones= " + observaciones;
    }
}
