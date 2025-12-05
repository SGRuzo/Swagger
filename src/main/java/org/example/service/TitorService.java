package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.Titor;
import org.example.repository.TitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TitorService {

    private final TitorRepository titorRepository;

    @Autowired
    public TitorService(TitorRepository titorRepository) {
        this.titorRepository = titorRepository;
    }

    @Transactional
    public Titor crearOActualizarTutores(Titor titor) {
        return titorRepository.save(titor);
    }

    public List<Titor> obtenerTodosLosTutores() {
        return titorRepository.findAll();
    }


    public Optional<Titor> obtenerTutorPorId(Long id) {
        return titorRepository.findById(id);
    }

    public Optional<Titor> obtenerTutorConAlumnos(Long id) {
        return titorRepository.findById(id);
    }

    public void eliminarTutorPorId(Long id) {
        titorRepository.deleteById(id);
    }
}