package br.com.agrohub.demo.controller;

import br.com.agrohub.demo.dto.UsuarioDTO;
import br.com.agrohub.demo.service.UsuariosService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuariosController {

    @Autowired
    UsuariosService usuariosService;

    @GetMapping("/login")
    public UsuarioDTO getTodos(@RequestParam String email) throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail(email);
        return usuariosService.getTodos(dto);
    }

    @GetMapping("/login/{id}")
    public UsuarioDTO buscarPorId(@PathVariable Long id_usuario) throws Exception {
        return usuariosService.buscarPorId(id_usuario);
    }

    @PostMapping("/login")
    public UsuarioDTO cadastrar(@RequestBody UsuarioDTO entityDTO) throws Exception {
        return usuariosService.cadastrar(entityDTO);
    }

    @PutMapping("/login/{id}")
    public UsuarioDTO atualizar(@PathVariable Long id_usuario, @RequestBody UsuarioDTO newData) throws Exception {
        return usuariosService.atualizar(id_usuario, newData);
    }

    @DeleteMapping("/login/{id}")
    public void deletar(@PathVariable Long id_usuario) throws Exception {
        usuariosService.deletar(id_usuario);
    }

}
