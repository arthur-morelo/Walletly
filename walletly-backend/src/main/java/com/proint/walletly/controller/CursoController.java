package com.proint.walletly.controller;

import com.proint.walletly.dto.curso.CursoDTO;
import com.proint.walletly.service.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<CursoDTO>> findAll() {
        return ResponseEntity.ok(cursoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CursoDTO> save(@Valid @RequestBody CursoDTO cursoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.save(cursoDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoDTO> update(@PathVariable Long id, @Valid @RequestBody CursoDTO cursoDTO) {
        return ResponseEntity.ok(cursoService.update(id, cursoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
