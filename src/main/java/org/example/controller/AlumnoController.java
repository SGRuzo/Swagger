package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.model.Alumno;
import org.example.repository.AlumnoRepository;
import org.example.service.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(AlumnoController.MAPPING)
public class AlumnoController {

    public static final String MAPPING = "/alumno";

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private AlumnoService alumnoServices;

    @Operation(summary = "Método que saúda")
    @PostMapping("/saudo")
    public String saudo() {
        return "Boas";
    }

    @Operation(summary = "Crear un nuevo alumno")
    @PostMapping("/")
    public Alumno crearAlumno(@RequestBody Alumno alumno) {
        return alumnoServices.crearAlumno(alumno);
    }

    @Operation(summary = "Obtener todos los alumnos")
    @GetMapping("/")
    public List<Alumno> obtenerAlumnos() {
        return alumnoServices.obterTodoslosAlumnos();
    }

    @Operation(summary = "Obtener alumno por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> obtenerAlumnoPorId(@PathVariable Long id) {
        Optional<Alumno> alumno = alumnoServices.obterPersoaPorId(id);
        return alumno.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar un alumno")
    @PutMapping("/{id}")
    public ResponseEntity<Alumno> actualizarAlumno(@PathVariable Long id, @RequestBody Alumno alumnoDetails) {
        Optional<Alumno> alumnoOptional = alumnoServices.obterPersoaPorId(id);
        if (alumnoOptional.isPresent()) {
            Alumno alumno = alumnoOptional.get();
            alumno.setNome(alumnoDetails.getNome());
            alumno.setApelidos(alumnoDetails.getApelidos()); // Actualizar apellidos
            if (alumnoDetails.getTitor() != null) {
                alumno.setTitor(alumnoDetails.getTitor()); // Actualizar titor si se proporciona
            }

            Alumno alumnoActualizado = alumnoServices.crearAlumno(alumno);
            return ResponseEntity.ok(alumnoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un alumno")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlumno(@PathVariable Long id) {
        if (alumnoRepository.existsById(id)) {
            alumnoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}