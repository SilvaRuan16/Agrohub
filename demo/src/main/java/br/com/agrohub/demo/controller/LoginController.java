package br.com.agrohub.demo.controller;

import java.math.BigInteger;
import java.util.List;

import br.com.agrohub.demo.model.Login;
import br.com.agrohub.demo.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    LoginService LoginService;

    @GetMapping("/login")
    public List<Login> getTodos(@RequestParam String userAccess) throws Exception {
        return LoginService.getTodos();
    }

    @GetMapping("/login/{id}")
    public Login buscarPorId(@PathVariable BigInteger id) throws Exception {
        return LoginService.buscarPorId(id);
    }

    @PostMapping("/login")
    public login cadastrar(@RequestBody Login login) throws Exception {
        return LoginService.cadastrar(login);
    }

    @PutMapping("/login/{id}")
    public Login atualizar(@PathVariable BigInteger id, @RequestBody Login login) throws Exception {
        return LoginService.atualizar(id, login);
    }

    @DeleteMapping("/login/{id}")
    public void deletar(@PathVariable BigInteger id) throws Exception {
        LoginService.deletar(id);
    }

    

}
