package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.model.Titor;
import org.example.repository.TitorRepository;
import org.example.service.TitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(TitorController.MAPPING)
public class TitorController {

    public static final String MAPPING = "/titor";

    @Autowired
    private TitorRepository titorRepository;

    @Autowired
    private TitorService titorService;

    @Operation(summary = "Método que saúda")
    @PostMapping("/saudo")
    public String saudo() {
        return "Boas";
    }

    @Operation(summary = "Crear un nuevo titores")
    @PostMapping("/")
    public Titor crearTutor(@RequestBody Titor titores) {
        return titorService.crearOActualizarTutores(titores);
    }

    @Operation(summary = "Obtener todos los titores")
    @GetMapping("/")
    public List<Titor> obtenerTodosLosTutores() {
        return titorService.obtenerTodosLosTutores();
    }

    @Operation(summary = "Obtener titores por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Titor> obtenerTutorPorId(@PathVariable Long id) {
        Optional<Titor> titor = titorService.obtenerTutorPorId(id);
        return titor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // NUEVO ENDPOINT: Obtener titor con sus alumnos
    @Operation(summary = "Obtener un titor y todos sus alumnos")
    @GetMapping("/{id}/con-alumnos")
    public ResponseEntity<Titor> obtenerTutorConAlumnos(@PathVariable Long id) {
        Optional<Titor> titor= titorService.obtenerTutorConAlumnos(id);
        return titor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar un titor")
    @PutMapping("/{id}")
    public ResponseEntity<Titor> actualizarTutor(@PathVariable Long id, @RequestBody Titor titoresDetails) {
        Optional<Titor> titoresOptional = titorService.obtenerTutorPorId(id);
        if (titoresOptional.isPresent()) {
            Titor titor = titoresOptional.get();
            titor.setNome(titoresDetails.getNome());
            titor.setApelidos(titoresDetails.getApelidos()); // Actualizar apellidos también

            Titor titoresActualizado = titorService.crearOActualizarTutores(titor);
            return ResponseEntity.ok(titoresActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un titor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTutorPorId(@PathVariable Long id) {
        Optional<Titor> titor = titorService.obtenerTutorPorId(id);
        if (titor.isPresent()) {
            // Verificar si tiene alumnos antes de eliminar
            if (!titor.get().getAlumnos().isEmpty()) {
                // Podría devolver un error 409 Conflict o similar
                return ResponseEntity.badRequest().build();
            }
            titorService.eliminarTutorPorId(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}