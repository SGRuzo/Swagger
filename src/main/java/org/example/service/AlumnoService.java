package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.Alumno;
import org.example.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlumnoService {
    private final AlumnoRepository alumnoRepository;

    @Autowired
    public AlumnoService(AlumnoRepository alumnoRepository){
        this.alumnoRepository = alumnoRepository;
    }
    @Transactional
    public Alumno crearAlumno(Alumno alumno){
        return alumnoRepository.save(alumno);
    }
    public List<Alumno> obterTodoslosAlumnos() {
        return alumnoRepository.findAll();
    }

    public Optional<Alumno> obterPersoaPorId(Long id) { // Cambiado a Long
        return alumnoRepository.findById(id);
    }
}