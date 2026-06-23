package com.proint.walletly.service;

import com.proint.walletly.dto.meta.MetaDTO;
import com.proint.walletly.mapper.MetaMapper;
import com.proint.walletly.model.Meta;
import com.proint.walletly.repository.MetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MetaService {

    private final MetaRepository metaRepository;
    private final MetaMapper metaMapper;

    @Autowired
    public MetaService(MetaRepository metaRepository, MetaMapper metaMapper) {
        this.metaRepository = metaRepository;
        this.metaMapper = metaMapper;
    }

    public MetaDTO save(MetaDTO dto) {
        Meta meta = metaMapper.toEntity(dto);
        Meta saved = metaRepository.save(meta);
        return metaMapper.toDTO(saved);
    }

    public Optional<MetaDTO> findById(Long id) {
        return metaRepository.findById(id)
                .map(metaMapper::toDTO);
    }

    public Page<MetaDTO> findAll(Pageable pageable) {
        return metaRepository.findAll(pageable)
                .map(metaMapper::toDTO);
    }

    public MetaDTO update(Long id, MetaDTO dto) {
        Meta meta = metaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta não encontrada com o ID " + id));
        metaMapper.updateEntityFromDTO(dto, meta);
        Meta updated = metaRepository.save(meta);
        return metaMapper.toDTO(updated);
    }

    public void deleteById(Long id) {
        metaRepository.deleteById(id);
    }
}
