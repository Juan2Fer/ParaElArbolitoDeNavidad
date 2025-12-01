package Trabajo3CesII.Controllers;

import Trabajo3CesII.Models.EstadoVehiculo;
import Trabajo3CesII.Models.Mecanico;
import Trabajo3CesII.Models.Vehiculo;
import Trabajo3CesII.Repository.MecanicoRepositorio;
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
@RequestMapping("/mecanico")
public class Controlador4 {

    private VehiculoRepositorio vehiculoRepositorio;
    private MecanicoRepositorio mecanicoRepositorio;

    String resultado = "";

    public Controlador4(VehiculoRepositorio vehiculoRepositorio, MecanicoRepositorio mecanicoRepositorio) {
        this.vehiculoRepositorio = vehiculoRepositorio;
        this.mecanicoRepositorio = mecanicoRepositorio;
    }

    @GetMapping("/peticion1")
    public String peticion1(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "3") int pageSize,
                            Model model){
        List<Mecanico> mecanicos = mecanicoRepositorio.myPagination(page, pageSize);
        int totalMecanicos = mecanicoRepositorio.getTotalElements();
        int totalPages = (int) Math.ceil((double) totalMecanicos / pageSize);

        model.addAttribute("botonIngresar",true);
        model.addAttribute("editarYeliminar",false);
        model.addAttribute("mecanicos", mecanicos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("mecanico", new Mecanico());
        return "vista6";
    }

    @PostMapping("/procesarMecanico")
    public String procesarMecanico(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "3") int pageSize,
                                   @RequestParam("accion") String accion,
                                   @RequestParam("id") int id,
                                   @Valid @ModelAttribute("mecanico") Mecanico mecanico,
                                   BindingResult mecanicoBindingResult,
                                   Model model) {
        List<Mecanico> mecanicos = mecanicoRepositorio.myPagination(page, pageSize);
        int totalMecanicos = mecanicoRepositorio.getTotalElements();
        int totalPages = (int) Math.ceil((double) totalMecanicos / pageSize);

        if (mecanicoBindingResult.hasErrors()) {
            for (FieldError error : mecanicoBindingResult.getFieldErrors()) {
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

            model.addAttribute("mecanicos", mecanicos);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageSize", pageSize);
            return "vista6";
        }

        if(accion.equals("editar")){
            List<String> especialidades = mecanicoRepositorio.getEspecialidades();

            if (especialidades.contains(mecanico.getEspecialidad().name())) {
                resultado = "Ya existe un mecánico con especialidad: " + mecanico.getEspecialidad();
            } else {
                resultado = mecanicoRepositorio.actualizar(mecanico, mecanico.getId());
            }

        } else if(accion.equals("eliminar")) {
            resultado = mecanicoRepositorio.eliminar(mecanico.getId());
        } else if(accion.equals("ingresar")) {

            List<String> especialidades = mecanicoRepositorio.getEspecialidades();

            if (especialidades.contains(mecanico.getEspecialidad().name())) {
                resultado = "Ya existe un mecánico con especialidad: " + mecanico.getEspecialidad();
            } else {
                int idMecanico = mecanicoRepositorio.crear(mecanico);
                mecanico.setId(idMecanico);
                resultado = "Se ingresó el mecánico " + mecanico.getNombre() + " con id: " + idMecanico;
            }
        }

        mecanicos = mecanicoRepositorio.myPagination(page, pageSize);
        totalMecanicos = mecanicoRepositorio.getTotalElements();
        totalPages = (int) Math.ceil((double) totalMecanicos / pageSize);

        model.addAttribute("mecanicos", mecanicos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("botonIngresar",true);
        model.addAttribute("editarYeliminar",false);
        model.addAttribute("resultado",resultado);
        Mecanico mecanicoNuevo = new Mecanico();
        model.addAttribute("mecanico", mecanicoNuevo);
        return "vista6";
    }

    @PostMapping("/buscarMecanico")
    public String buscarMecanico(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "3") int pageSize,
                                 @RequestParam("id") int id,
                                 Model model) {
        resultado = "";

        Optional<Mecanico> optionalMecanico = mecanicoRepositorio.buscar(id);

        Mecanico mecanico = optionalMecanico.orElse(new Mecanico());

        if (mecanico.getId() == 0) {
            resultado = " No existe un mecánico con id: " + id;
        }

        List<Mecanico> mecanicos = mecanicoRepositorio.myPagination(page, pageSize);
        int totalMecanicos = mecanicoRepositorio.getTotalElements();
        int totalPages = (int) Math.ceil((double) totalMecanicos / pageSize);

        model.addAttribute("mecanicos", mecanicos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("botonIngresar",false);
        model.addAttribute("editarYeliminar",true);
        model.addAttribute("resultado",resultado);
        model.addAttribute("mecanico",mecanico);
        return "vista6";
    }

    @GetMapping("/asignarMecanico")
    public ModelAndView asignarMecanico(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "3") int pageSize,
                                        @RequestParam("idVehiculo") int idVehiculo,
                                        Model model) {

        ModelAndView modelAndView = new ModelAndView();

        Optional<Vehiculo> vehiculoOptional = vehiculoRepositorio.buscar(idVehiculo);
        Vehiculo vehiculo = vehiculoOptional.orElse(new Vehiculo());

        if(vehiculo.getPlaca() == null){
            List<EstadoVehiculo> estadoOptions = List.of(EstadoVehiculo.values());
            resultado = "No se encontró un vehículo con id: " + idVehiculo;
            modelAndView.setViewName("vista1");
            modelAndView.addObject("resultado",resultado);
            modelAndView.addObject("nuevoVehiculo", new Vehiculo());
            modelAndView.addObject("estadoOptions", estadoOptions);
        } else {
            List<Mecanico> mecanicos = mecanicoRepositorio.myPagination(page, pageSize);
            int totalMecanicos = mecanicoRepositorio.getTotalElements();
            int totalPages = (int) Math.ceil((double) totalMecanicos / pageSize);

            List<Mecanico> mecanicosVehiculo = mecanicoRepositorio.getMecanicosByVehiculoId2(idVehiculo);

            modelAndView.setViewName("vista7");
            modelAndView.addObject("mecanicosVehiculo", mecanicosVehiculo);
            modelAndView.addObject("mecanicos", mecanicos);
            modelAndView.addObject("currentPage", page);
            modelAndView.addObject("totalPages", totalPages);
            modelAndView.addObject("pageSize", pageSize);
            modelAndView.addObject("idVehiculo", idVehiculo);
        }
        return modelAndView;
    }

    @PostMapping("/agregarMecanico")
    public String agregarMecanico(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "3") int pageSize,
                                  @RequestParam("idVehiculo") int idVehiculo,
                                  @RequestParam("idMecanico") int idMecanico,
                                  Model model) {

        List<Integer> mecanicos = mecanicoRepositorio.getMecanicoIdsByVehiculoId(idVehiculo);
        if (mecanicos.contains(idMecanico)) {
            resultado = "El vehículo con id: "+idVehiculo+" ya tiene el mecánico con id: "+idMecanico;
        } else {
            int resultadoInsert = mecanicoRepositorio.insertVehiculoMecanico(idVehiculo,idMecanico);
            if (resultadoInsert == 1) {
                resultado = "El vehículo con id: "+idVehiculo+" tiene un NUEVO mecánico con id: "+idMecanico;
            }
        }

        List<Mecanico> mecanicos2 = mecanicoRepositorio.myPagination(page, pageSize);
        int totalMecanicos = mecanicoRepositorio.getTotalElements();
        int totalPages = (int) Math.ceil((double) totalMecanicos / pageSize);

        List<Mecanico> mecanicosVehiculo = mecanicoRepositorio.getMecanicosByVehiculoId2(idVehiculo);

        model.addAttribute("resultado", resultado);
        model.addAttribute("idVehiculo", idVehiculo);
        model.addAttribute("mecanicosVehiculo", mecanicosVehiculo);
        model.addAttribute("mecanicos", mecanicos2);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        return "vista7";
    }

    @PostMapping("/eliminarMecanico")
    public String eliminarMecanico(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "3") int pageSize,
                                   @RequestParam("idVehiculo") int idVehiculo,
                                   @RequestParam("idMecanico") int idMecanico,
                                   Model model) {

        List<Integer> mecanicos = mecanicoRepositorio.getMecanicoIdsByVehiculoId(idVehiculo);
        if (!mecanicos.contains(idMecanico)) {
            resultado = "No se puede eliminar, El vehículo con id: "+idVehiculo+" NO tiene el mecánico con id: "+idMecanico;
        } else {
            int resultadoDelete = mecanicoRepositorio.deleteVehiculoMecanico(idVehiculo,idMecanico);
            if (resultadoDelete == 1) {
                resultado = "El vehículo con id: "+idVehiculo+" ha eliminado el mecánico con id: "+idMecanico;
            }
        }

        List<Mecanico> mecanicos2 = mecanicoRepositorio.myPagination(page, pageSize);
        int totalMecanicos = mecanicoRepositorio.getTotalElements();
        int totalPages = (int) Math.ceil((double) totalMecanicos / pageSize);

        List<Mecanico> mecanicosVehiculo = mecanicoRepositorio.getMecanicosByVehiculoId2(idVehiculo);

        model.addAttribute("resultado", resultado);
        model.addAttribute("idVehiculo", idVehiculo);
        model.addAttribute("mecanicosVehiculo", mecanicosVehiculo);
        model.addAttribute("mecanicos", mecanicos2);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        return "vista7";
    }
}
