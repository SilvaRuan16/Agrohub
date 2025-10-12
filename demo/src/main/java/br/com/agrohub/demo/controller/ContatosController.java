package br.com.agrohub.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.agrohub.demo.dto.ContatoDTO;
import br.com.agrohub.demo.service.ContatosService;

@RestController
public class ContatosController {

    @Autowired
    ContatosService contatosService;

    @GetMapping("/contatos")
    public ContatoDTO buscarTodos(@RequestParam String email) throws Exception {
        ContatoDTO dto = new ContatoDTO();
        dto.setEmail(email);
        return contatosService.buscarTodos(dto);
    }

    @GetMapping("/contatos/{id}")
    public ContatoDTO buscarPorId(@PathVariable Long id_contato) throws Exception {
        return contatosService.buscarPorId(id_contato);
    }

    @PostMapping("/contatos")
    public ContatoDTO cadastrar(@RequestBody ContatoDTO entityDTO) throws Exception {
        return contatosService.cadastrar(entityDTO);
    }

    @PutMapping("/contatos/{id}")
    public ContatoDTO atualizar(@PathVariable Long id_contato, @RequestBody ContatoDTO newData) throws Exception {
        return contatosService.atualizar(id_contato, newData);
    }

    @DeleteMapping("/contatos/{id}")
    public void deletar(@PathVariable Long id_contato) throws Exception {
        contatosService.deletar(id_contato);
    }

}
