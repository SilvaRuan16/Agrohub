package br.com.agrohub.demo.controller;

import br.com.agrohub.demo.dto.LoginDTO;
import br.com.agrohub.demo.service.LoginService;

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
public class LoginController {

    @Autowired
    LoginService LoginService;

    @GetMapping("/login")
    public LoginDTO getTodos(@RequestParam String username) throws Exception {
        LoginDTO dto = new LoginDTO();
        dto.setUsername(username);
        return LoginService.getTodos(dto);
    }

    @GetMapping("/login/{id}")
    public LoginDTO buscarPorId(@PathVariable Long id) throws Exception {
        return LoginService.buscarPorId(id);
    }

    @PostMapping("/login")
    public LoginDTO cadastrar(@RequestBody LoginDTO entityDTO) throws Exception {
        return LoginService.cadastrar(entityDTO);
    }

    @PutMapping("/login/{id}")
    public LoginDTO atualizar(@PathVariable Long id, @RequestBody LoginDTO newData) throws Exception {
        return LoginService.atualizar(id, newData);
    }

    @DeleteMapping("/login/{id}")
    public void deletar(@PathVariable Long id) throws Exception {
        LoginService.deletar(id);
    }

}
