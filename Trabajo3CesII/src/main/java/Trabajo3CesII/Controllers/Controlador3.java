package Trabajo3CesII.Controllers;

import Trabajo3CesII.Models.EstadoVehiculo;
import Trabajo3CesII.Models.Reparacion;
import Trabajo3CesII.Models.Vehiculo;
import Trabajo3CesII.Repository.ReparacionRepositorio;
import Trabajo3CesII.Repository.VehiculoRepositorio;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reparacion")
public class Controlador3 {

    private VehiculoRepositorio vehiculoRepositorio;
    private ReparacionRepositorio reparacionRepositorio;

    List<EstadoVehiculo> estadoOptions = List.of(EstadoVehiculo.values());

    String resultado = "";

    public Controlador3(VehiculoRepositorio vr, ReparacionRepositorio rr){
        this.vehiculoRepositorio = vr;
        this.reparacionRepositorio = rr;
    }

    @GetMapping("/ingresar")
    public ModelAndView ingresarReparacion(@RequestParam int id) {

        ModelAndView modelAndView = new ModelAndView();

        Optional<Vehiculo> vehiculoOptional = vehiculoRepositorio.buscar(id);
        Vehiculo vehiculo = vehiculoOptional.orElse(new Vehiculo());

        if(vehiculo.getPlaca() == null){
            resultado = "No se encontró un vehículo con id: " + id;
            modelAndView.setViewName("vista1");
            modelAndView.addObject("resultado",resultado);
            modelAndView.addObject("nuevoVehiculo", new Vehiculo());
            modelAndView.addObject("estadoOptions", estadoOptions);
        } else {

            Integer reparaciones = reparacionRepositorio.getTotalElements2(id);

            if (reparaciones == 0) {
                resultado = "El vehículo con id: " + id + " no ha ingresado reparaciones";
            } else {
                resultado = "El vehículo con id: " + id + " tiene " + reparaciones +" reparaciones";
            }

            Reparacion reparacion = new Reparacion();
            reparacion.setVehiculoId(id);

            modelAndView.setViewName("vista4");
            modelAndView.addObject("botonIngresar",true);
            modelAndView.addObject("editarYeliminar",false);
            modelAndView.addObject("resultado",resultado);
            modelAndView.addObject("reparacion",reparacion);
            modelAndView.addObject("vehiculoId",id);
        }
        return modelAndView;
    }

    @PostMapping("/procesarReparacion")
    public String procesarReparacion(@RequestParam("accion") String accion,
                                     @RequestParam("id") int id,
                                     @RequestParam("vehiculoId") int vehiculoId,
                                     @Valid @ModelAttribute("reparacion") Reparacion reparacion,
                                     BindingResult reparacionBindingResult,
                                     Model model) {

        if (reparacionBindingResult.hasErrors()) {
            for (FieldError error : reparacionBindingResult.getFieldErrors()) {
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
            Integer reparaciones = reparacionRepositorio.getTotalElements2(vehiculoId);

            if (reparaciones == 0) {
                resultado = "El vehículo con id: " + vehiculoId + " no ha ingresado reparaciones";
            } else {
                resultado = "El vehículo con id: " + vehiculoId + " tiene " + reparaciones +" reparaciones";
            }
            model.addAttribute("resultado",resultado);
            model.addAttribute("vehiculoId",vehiculoId);
            return "vista4";
        }

        if(accion.equals("editar")){
            resultado = reparacionRepositorio.actualizar(reparacion, reparacion.getId());
        } else if(accion.equals("eliminar")) {
            resultado = reparacionRepositorio.eliminar(reparacion.getId());
        } else if(accion.equals("ingresar")) {

            int idReparacion = reparacionRepositorio.crear(reparacion);

            reparacion.setId(idReparacion);
            resultado = "Se ingresó una reparación con id: " + idReparacion;
        }

        model.addAttribute("botonIngresar",true);
        model.addAttribute("editarYeliminar",false);
        model.addAttribute("resultado",resultado);
        model.addAttribute("vehiculoId",vehiculoId);
        Reparacion reparacionNueva = new Reparacion();
        reparacionNueva.setVehiculoId(reparacion.getVehiculoId());
        model.addAttribute("reparacion", reparacionNueva);
        return "vista4";
    }

    @GetMapping("/verReparaciones")
    public String verReparaciones(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "3") int pageSize,
                                  @RequestParam("vehiculoId") int vehiculoId,
                                  Model model) {
        List<Reparacion> reparaciones = reparacionRepositorio.myPagination2(vehiculoId, page, pageSize);

        int totalReparaciones = reparacionRepositorio.getTotalElements2(vehiculoId);
        int totalPages = (int) Math.ceil((double) totalReparaciones / pageSize);

        model.addAttribute("reparaciones", reparaciones);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("vehiculoId", vehiculoId);
        return "vista5";
    }

    @GetMapping("/buscarReparacion")
    public String buscarReparacion(@RequestParam("vehiculoId") int vehiculoId,
                                   @RequestParam("id") int id,
                                   Model model) {

        Optional<Reparacion> optionalReparacion = reparacionRepositorio.buscar2(vehiculoId, id);

        Reparacion reparacion = optionalReparacion.orElse(new Reparacion());

        if (reparacion.getId() == 0) {
            reparacion.setVehiculoId(vehiculoId);
            resultado = "El vehículo con id: " + vehiculoId + " no tiene una reparación con id: " + id;
        } else {
            resultado = "El vehículo con id: " + vehiculoId + " tiene una reparación con id: " + id;
        }
        model.addAttribute("botonIngresar",false);
        model.addAttribute("editarYeliminar",true);
        model.addAttribute("resultado",resultado);
        model.addAttribute("reparacion",reparacion);
        model.addAttribute("vehiculoId",vehiculoId);
        return "vista4";
    }
}