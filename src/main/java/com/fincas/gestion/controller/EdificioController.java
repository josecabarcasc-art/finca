package com.fincas.gestion.controller;

import com.fincas.gestion.model.Edificio;
import com.fincas.gestion.repository.EdificioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/edificios")
public class EdificioController {

    @Autowired
    private EdificioRepository edificioRepository;

    @GetMapping
    public List<Edificio> listarTodos() {
        return edificioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Edificio> obtenerPorId(@PathVariable String id) {
        Optional<Edificio> edificio = edificioRepository.findById(id);
        return edificio.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Edificio crear(@RequestBody Edificio edificio) {
        return edificioRepository.save(edificio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (edificioRepository.existsById(id)) {
            edificioRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
