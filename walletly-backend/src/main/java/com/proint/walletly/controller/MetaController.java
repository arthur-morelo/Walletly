package com.proint.walletly.controller;

import com.proint.walletly.dto.meta.MetaDTO;
import com.proint.walletly.service.MetaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/metas")
public class MetaController {

    private final MetaService metaService;

    @Autowired
    public MetaController(MetaService metaService) {
        this.metaService = metaService;
    }

    @PostMapping
    public ResponseEntity<MetaDTO> createMeta(@Valid @RequestBody MetaDTO meta) {
        MetaDTO savedMeta = metaService.save(meta);
        return new ResponseEntity<>(savedMeta, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetaDTO> getMetaById(@PathVariable Long id) {
        Optional<MetaDTO> meta = metaService.findById(id);
        return meta.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<MetaDTO>> getAllMetas(Pageable pageable) {
        Page<MetaDTO> metas = metaService.findAll(pageable);
        return ResponseEntity.ok(metas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetaDTO> updateMeta(@PathVariable Long id, @Valid @RequestBody MetaDTO metaDetails) {
        MetaDTO updatedMeta = metaService.update(id, metaDetails);
        return ResponseEntity.ok(updatedMeta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeta(@PathVariable Long id) {
        metaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
