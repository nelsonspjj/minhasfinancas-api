package com.njunior.minhasfinancas.model.entity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.convert.Jsr310Converters;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario", schema="financas")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="nome")
	@Setter
	private String nome;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "senha")
	@JsonIgnore
	private String senha;
	
	@Column(name = "data_cadastro")
	private LocalDate dataCadastro;
	
}
