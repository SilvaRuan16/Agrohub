package br.com.agrohub.demo.controller;

import java.math.BigInteger;
import java.util.List;

import br.com.agrohub.demo.models.LoginModel;
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
    public List<LoginModel> getTodos(@RequestParam String userAccess) throws Exception {
        LoginModel loginModel = new LoginModel();
        loginModel.setUserAccess(userAccess);
        return LoginService.getTodos(loginModel);
    }

    @GetMapping("/login/{id}")
    public LoginModel buscarPorId(@PathVariable BigInteger id) throws Exception {
        return LoginService.buscarPorId(id);
    }

    @PostMapping("/login")
    public LoginModel cadastrar(@RequestBody LoginModel userAccess) throws Exception {
        return LoginService.cadastrar(userAccess);
    }

    @PutMapping("/login/{id}")
    public LoginModel atualizar(@PathVariable BigInteger id, @RequestBody LoginModel userAccess) throws Exception {
        return LoginService.atualizar(id, userAccess);
    }

    @DeleteMapping("/login/{id}")
    public void deletar(@PathVariable BigInteger id) throws Exception {
        LoginService.deletar(id);
    }

}
