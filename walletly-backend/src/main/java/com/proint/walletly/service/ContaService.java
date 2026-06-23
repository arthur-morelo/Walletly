package com.proint.walletly.service;

import com.proint.walletly.dto.conta.ContaDTO;
import com.proint.walletly.mapper.ContaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.proint.walletly.repository.ContaRepository;

import com.proint.walletly.model.Conta;
import java.util.Optional;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final ContaMapper contaMapper;
    private final com.proint.walletly.utils.SecurityUtils securityUtils;

    @Autowired
    public ContaService(ContaRepository contaRepository, ContaMapper contaMapper, com.proint.walletly.utils.SecurityUtils securityUtils) {
        this.contaRepository = contaRepository;
        this.contaMapper = contaMapper;
        this.securityUtils = securityUtils;
    }

    public ContaDTO save(ContaDTO dto) {
        com.proint.walletly.model.User usuarioLogado = securityUtils.getAuthenticatedUser();
        Conta conta = contaMapper.toEntity(dto);
        conta.setUsuario(usuarioLogado); // Força o usuário logado
        Conta saved = contaRepository.save(conta);
        return contaMapper.toDTO(saved);
    }

    public Optional<ContaDTO> findById(Long id) {
        return contaRepository.findById(id)
                .map(contaMapper::toDTO);
    }

    public Page<ContaDTO> findAll(Pageable pageable) {
        com.proint.walletly.model.User usuarioLogado = securityUtils.getAuthenticatedUser();
        return contaRepository.findByUsuario(usuarioLogado, pageable)
                .map(contaMapper::toDTO);
    }

    public ContaDTO update(Long id, ContaDTO dto) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada com o ID " + id));
        contaMapper.updateEntityFromDTO(dto, conta);
        Conta updated = contaRepository.save(conta);
        return contaMapper.toDTO(updated);
    }

    public void deleteById(Long id) {
        contaRepository.deleteById(id);
    }
}