package Trabajo3CesII.Models;

import jakarta.validation.constraints.*;

import java.io.Serializable;

public class Mecanico implements Serializable {
    @NotNull(message = "Id es obligatorio")
    @Min(value = 0, message = "Valor debe ser positivo")
    private Integer id;

    @NotBlank(message = "Nombre es obligatorio")
    @Size(min = 5, max = 100, message = "Nombre de 5-100 letras")
    private String nombre;

    @NotNull(message = "Especialidad es obligatoria")
    private Especialidad especialidad;

    @NotNull(message = "Años de experiencia es obligatorio")
    @Min(value = 0, message = "Años de experiencia debe ser 0 o más")
    @Max(value = 60, message = "Años de experiencia máximo 60")
    private Integer añosExperiencia;

    @NotNull(message = "Estado es obligatorio")
    private EstadoMecanico estado;

    public Mecanico() {
        this.id = 0;
    }

    public Mecanico(String nombre, Especialidad especialidad, Integer añosExperiencia, EstadoMecanico estado) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.añosExperiencia = añosExperiencia;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Integer getAñosExperiencia() {
        return añosExperiencia;
    }

    public void setAñosExperiencia(Integer añosExperiencia) {
        this.añosExperiencia = añosExperiencia;
    }

    public EstadoMecanico getEstado() {
        return estado;
    }

    public void setEstado(EstadoMecanico estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "id= " + id +
                ", nombre= " + nombre +
                ", especialidad= " + especialidad +
                ", añosExperiencia= " + añosExperiencia +
                ", estado= " + estado;
    }
}
