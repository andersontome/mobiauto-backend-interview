package com.api.revenda.mobiauto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.revenda.mobiauto.model.Loja;

@Repository
public interface LojaRepository extends JpaRepository<Loja, Long> {
	
}