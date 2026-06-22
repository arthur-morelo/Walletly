package com.proint.walletly.service;

import com.proint.walletly.dto.meta.MetaDTO;
import com.proint.walletly.mapper.MetaMapper;
import com.proint.walletly.model.Meta;
import com.proint.walletly.model.User;
import com.proint.walletly.repository.MetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaService {

    private final MetaRepository metaRepository;
    private final MetaMapper metaMapper;
    private final AuthService authService;

    @Transactional(readOnly = true)
    public List<MetaDTO> findAllByUser() {
        User user = authService.getAuthenticatedUser();
        return metaMapper.toDTOList(metaRepository.findByUser(user));
    }

    @Transactional(readOnly = true)
    public MetaDTO findById(Long id) {
        User user = authService.getAuthenticatedUser();
        Meta meta = metaRepository.findById(id)
                .filter(m -> m.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Meta não encontrada ou acesso negado."));
        return metaMapper.toDTO(meta);
    }

    @Transactional
    public MetaDTO save(MetaDTO metaDTO) {
        User user = authService.getAuthenticatedUser();
        Meta meta = metaMapper.toEntity(metaDTO);
        meta.setUser(user);
        
        if (meta.getSavedValue() == null) {
            meta.setSavedValue(BigDecimal.ZERO);
        }
        
        Meta savedMeta = metaRepository.save(meta);
        return metaMapper.toDTO(savedMeta);
    }

    @Transactional
    public MetaDTO update(Long id, MetaDTO metaDTO) {
        User user = authService.getAuthenticatedUser();
        Meta meta = metaRepository.findById(id)
                .filter(m -> m.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Meta não encontrada ou acesso negado."));
        
        meta.setName(metaDTO.name());
        meta.setGoalValue(metaDTO.goalValue());
        
        if (metaDTO.savedValue() != null) {
            meta.setSavedValue(metaDTO.savedValue());
        }
        
        return metaMapper.toDTO(metaRepository.save(meta));
    }

    @Transactional
    public void delete(Long id) {
        User user = authService.getAuthenticatedUser();
        Meta meta = metaRepository.findById(id)
                .filter(m -> m.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Meta não encontrada ou acesso negado."));
        metaRepository.delete(meta);
    }
}
