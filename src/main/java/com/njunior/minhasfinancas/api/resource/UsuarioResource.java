package com.njunior.minhasfinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.njunior.minhasfinancas.api.dto.UsuarioDTO;
import com.njunior.minhasfinancas.exception.ErroAutenticacao;
import com.njunior.minhasfinancas.exception.RegraNegocioExcepction;
import com.njunior.minhasfinancas.model.entity.Usuario;
import com.njunior.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {
	
	private UsuarioService service;
	
	public UsuarioResource(UsuarioService service) {
		this.service = service;
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
		
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		} catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha()).build();
		
		try {
			Usuario ususarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(ususarioSalvo, HttpStatus.CREATED);
			} catch (RegraNegocioExcepction e) {
				return ResponseEntity.badRequest().body(e);
		}
		
	}
}
