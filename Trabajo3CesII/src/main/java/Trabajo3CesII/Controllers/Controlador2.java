package Trabajo3CesII.Controllers;

import Trabajo3CesII.Models.Diagnostico;
import Trabajo3CesII.Models.EstadoVehiculo;
import Trabajo3CesII.Models.Vehiculo;
import Trabajo3CesII.Repository.DiagnosticoRepositorio;
import Trabajo3CesII.Repository.VehiculoRepositorio;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/diagnostico")
public class Controlador2 {

    private VehiculoRepositorio vehiculoRepositorio;
    private DiagnosticoRepositorio diagnosticoRepositorio;

    List<EstadoVehiculo> estadoOptions = List.of(EstadoVehiculo.values());

    String resultado = "";

    public Controlador2(VehiculoRepositorio vr, DiagnosticoRepositorio dr){
        this.vehiculoRepositorio = vr;
        this.diagnosticoRepositorio = dr;
    }

    @GetMapping("/ingresar")
    public ModelAndView ingresarDiagnostico(@RequestParam int id) {

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

            Optional<Diagnostico> diagnosticoOptional = diagnosticoRepositorio.buscarPorVehiculo(id);
            Diagnostico diagnostico = diagnosticoOptional.orElse(new Diagnostico());
            diagnostico.setVehiculoId(id);

            if (diagnostico.getId() == null){
                resultado = "El vehículo: " + vehiculo.getPlaca() + " no tiene diagnóstico";
                modelAndView.addObject("mostrarBoton",true);
            } else {
                resultado = "El vehículo: " + vehiculo.getPlaca() + " tiene diagnóstico " + diagnostico.getId();
                modelAndView.addObject("mostrarBoton",false);
            }

            modelAndView.setViewName("vista3");
            modelAndView.addObject("resultado",resultado);
            modelAndView.addObject("diagnostico", diagnostico);
        }
        return modelAndView;
    }

    @PostMapping("/procesarDiagnostico")
    public String procesarDiagnostico(@RequestParam("accion") String accion,
                                      @RequestParam("id") String id,
                                      @RequestParam("vehiculoId") String vehiculoId,
                                      @Valid @ModelAttribute("diagnostico") Diagnostico diagnostico,
                                      BindingResult diagnosticoBindingResult,
                                      Model model) {

        if (diagnosticoBindingResult.hasErrors()) {
            return "vista3";
        }

        if(accion.equals("editar")){
            resultado = diagnosticoRepositorio.actualizar(diagnostico, Integer.parseInt(id));
        } else if(accion.equals("eliminar")) {
            resultado = diagnosticoRepositorio.eliminar(Integer.parseInt(id));
        } else if(accion.equals("ingresar")) {
            diagnostico.setVehiculoId(Integer.parseInt(vehiculoId));
            int idDiagnostico = diagnosticoRepositorio.crear(diagnostico);
            diagnostico.setId(idDiagnostico);
            resultado = "Se ingresó el diagnóstico con id: " + idDiagnostico;
        }

        model.addAttribute("resultado",resultado);
        model.addAttribute("diagnostico", diagnostico);
        return "vista3";
    }
}
