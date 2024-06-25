package com.api.revenda.mobiauto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.revenda.mobiauto.model.Oportunidade;
import com.api.revenda.mobiauto.model.Usuario;
import com.api.revenda.mobiauto.model.Oportunidade.StatusOportunidade;

@Repository
public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {
    long countByResponsavelAndStatus(Usuario responsavel, StatusOportunidade status);
    Optional<Oportunidade> findTopByResponsavelOrderByDataAtribuicaoDesc(Usuario responsavel);
}
