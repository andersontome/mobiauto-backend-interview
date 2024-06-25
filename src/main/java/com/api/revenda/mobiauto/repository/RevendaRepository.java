package com.api.revenda.mobiauto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.revenda.mobiauto.model.Revenda;

@Repository
public interface RevendaRepository extends JpaRepository<Revenda, Long> {
    Optional<Revenda> findByCnpj(String cnpj);
    Optional<Revenda> findByCodigo(String codigo);
}
