package Trabajo3CesII.Controllers;

import Trabajo3CesII.Models.EstadoVehiculo;
import Trabajo3CesII.Models.Vehiculo;
import Trabajo3CesII.Repository.VehiculoRepositorio;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
public class Controlador1 {

    private VehiculoRepositorio vehiculoRepositorio;

    List<EstadoVehiculo> estadoOptions = List.of(EstadoVehiculo.values());

    String resultado = "";

    public Controlador1(VehiculoRepositorio vr){
        this.vehiculoRepositorio = vr;
    }

    @GetMapping("/peticion1")
    public String peticion1(Model model){
        resultado = "";
        model.addAttribute("botonIngresar",true);
        model.addAttribute("editarYeliminar",false);
        model.addAttribute("resultado",resultado);
        model.addAttribute("nuevoVehiculo", new Vehiculo());
        model.addAttribute("estadoOptions", estadoOptions);
        return "vista1";
    }

    @PostMapping("/buscarVehiculo")
    public String buscarVehiculo(@RequestParam String id,
                                 Model model){

        Optional<Vehiculo> vehiculoOptional = vehiculoRepositorio.buscar(Integer.valueOf(id));
        Vehiculo vehiculo = vehiculoOptional.orElse(new Vehiculo());

        if(vehiculo.getPlaca() == null){
            resultado = "No se encontró un vehículo con id: " + id;
        } else {
            resultado = "Se encontró un vehículo con placa: " + vehiculo.getPlaca();
        }
        model.addAttribute("botonIngresar",false);
        model.addAttribute("editarYeliminar",true);
        model.addAttribute("resultado",resultado);
        model.addAttribute("nuevoVehiculo", vehiculo);
        model.addAttribute("estadoOptions", estadoOptions);
        return "vista1";
    }

    @PostMapping("/procesarVehiculo")
    public String procesarVehiculo(@RequestParam("accion") String accion,
                                   @RequestParam("id") String id,
                                   @Valid @ModelAttribute("nuevoVehiculo") Vehiculo vehiculo,
                                   BindingResult vehiculoBindingResult,
                                   Model model) {

        if (vehiculoBindingResult.hasErrors()) {
            for (FieldError error : vehiculoBindingResult.getFieldErrors()) {
                System.out.println("Field: " + error.getField());
                System.out.println("Error message: " + error.getDefaultMessage());
            }
            if(accion.equals("ingresar")){
                model.addAttribute("botonIngresar",true);
                model.addAttribute("editarYeliminar",false);
            } else {
                model.addAttribute("botonIngresar",false);
                model.addAttribute("editarYeliminar",true);
            }
            model.addAttribute("estadoOptions", estadoOptions);
            return "vista1";
        }

        if(accion.equals("editar")){
            vehiculo.setId(Integer.parseInt(id));
            resultado = vehiculoRepositorio.actualizar(vehiculo, vehiculo.getId());
        } else if(accion.equals("eliminar")) {
            vehiculo.setId(Integer.parseInt(id));
            resultado = vehiculoRepositorio.eliminar(vehiculo.getId());
        } else if(accion.equals("ingresar")) {
            int idVehiculo = vehiculoRepositorio.crear(vehiculo);
            vehiculo.setId(idVehiculo);
            resultado = "Se ingresó un vehículo con id: " + idVehiculo;
        }

        model.addAttribute("botonIngresar",true);
        model.addAttribute("editarYeliminar",false);
        model.addAttribute("resultado",resultado);
        model.addAttribute("nuevoVehiculo", new Vehiculo());
        model.addAttribute("estadoOptions", estadoOptions);
        return "vista1";
    }

    @GetMapping("/vehiculos")
    public String getVehiculos(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "3") int pageSize,
                               Model model) {
        List<Vehiculo> vehiculos = vehiculoRepositorio.myPagination(page, pageSize);
        int totalVehiculos = vehiculoRepositorio.getTotalElements();
        int totalPages = (int) Math.ceil((double) totalVehiculos / pageSize);

        model.addAttribute("vehiculos", vehiculos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        return "vista2";
    }
}