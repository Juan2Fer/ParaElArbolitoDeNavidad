package Trabajo3CesII.Models;

import jakarta.validation.constraints.*;

import java.io.Serializable;

public class Vehiculo implements Serializable {

    @NotNull(message = "Id es obligatorio")
    @Min(value = 0, message = "Valor debe ser positivo")
    private Integer id = 0;

    @NotBlank(message = "Placa es obligatoria")
    @Size(min = 6, max = 10, message = "Placa de 6-10 caracteres")
    private String placa;

    @NotBlank(message = "Marca es obligatoria")
    @Size(min = 3, max = 50, message = "Marca de 3-50 letras")
    private String marca;

    @NotBlank(message = "Modelo es obligatorio")
    @Size(min = 2, max = 50, message = "Modelo de 2-50 letras")
    private String modelo;

    @NotNull(message = "Estado del vehículo es obligatorio")
    private EstadoVehiculo estadoVehiculo;

    public Vehiculo() {
    }

    public Vehiculo(String placa, String marca, String modelo, EstadoVehiculo estadoVehiculo) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.estadoVehiculo = estadoVehiculo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public EstadoVehiculo getEstadoVehiculo() {
        return estadoVehiculo;
    }

    public void setEstadoVehiculo(EstadoVehiculo estadoVehiculo) {
        this.estadoVehiculo = estadoVehiculo;
    }

    @Override
    public String toString() {
        return  "[id= " + id +
                ", placa= " + placa +
                ", marca= " + marca +
                ", modelo= " + modelo +
                ", estadoVehiculo= " + estadoVehiculo + "]";
    }
}
