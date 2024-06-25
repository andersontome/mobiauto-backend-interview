package com.api.revenda.mobiauto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.revenda.mobiauto.model.Revenda;
import com.api.revenda.mobiauto.repository.RevendaRepository;

import jakarta.transaction.Transactional;

@Service
public class RevendaService {

    @Autowired
    private RevendaRepository revendaRepository;

    public List<Revenda> listarTodas() {
        return revendaRepository.findAll();
    }

    public Optional<Revenda> buscarPorId(Long id) {
        return revendaRepository.findById(id);
    }

    public Optional<Revenda> buscarPorCnpj(String cnpj) {
        return revendaRepository.findByCnpj(cnpj);
    }

    public Optional<Revenda> buscarPorCodigo(String codigo) {
        return revendaRepository.findByCodigo(codigo);
    }

    @Transactional
    public Revenda salvar(Revenda revenda) {
        return revendaRepository.save(revenda);
    }

    @Transactional
    public void excluir(Long id) {
        revendaRepository.deleteById(id);
    }
}
