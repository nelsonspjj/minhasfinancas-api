package com.njunior.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.njunior.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
