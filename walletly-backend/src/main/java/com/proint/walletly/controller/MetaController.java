package com.proint.walletly.controller;

import com.proint.walletly.dto.meta.MetaDTO;
import com.proint.walletly.service.MetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metas")
@RequiredArgsConstructor
public class MetaController {

    private final MetaService metaService;

    @GetMapping
    public ResponseEntity<List<MetaDTO>> findAllByUser() {
        return ResponseEntity.ok(metaService.findAllByUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(metaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MetaDTO> save(@Valid @RequestBody MetaDTO metaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metaService.save(metaDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetaDTO> update(@PathVariable Long id, @Valid @RequestBody MetaDTO metaDTO) {
        return ResponseEntity.ok(metaService.update(id, metaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        metaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
