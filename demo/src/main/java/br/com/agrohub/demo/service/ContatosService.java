package br.com.agrohub.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.agrohub.demo.dto.ContatoDTO;
import br.com.agrohub.demo.exception.ContatoDuplicadoException;
import br.com.agrohub.demo.models.Contatos;
import br.com.agrohub.demo.repository.ContatosRepository;

@Service
public class ContatosService extends GenericService<Contatos, ContatoDTO> {

    @Autowired
    private ContatosRepository contatosRepository;

    public ContatoDTO buscarTodos(@RequestParam ContatoDTO dto2) {
        Contatos contato = contatosRepository.findByEmail(dto2.getEmail());
        ContatoDTO dto = this.toDTO(contato);
        return dto;
    }

    public ContatoDTO buscarPorId(Long id_contato) throws Exception {
        Optional<Contatos> contato = contatosRepository.findById(id_contato);
        if (!contato.isPresent()) {
            throw new Exception("ID inválido.");
        }
        return toDTO(contato.get());
    }

    public void validar(ContatoDTO dto) throws Exception {
        if (contatosRepository.existsByEmail(dto.getEmail())) {
            throw new ContatoDuplicadoException(dto.getEmail());
        }
    }

    public ContatoDTO cadastrar(ContatoDTO entityDTO) throws Exception {

        validar(entityDTO);  
        try{    
            return toDTO(contatosRepository.save(toEntity(entityDTO)));
        }catch(Exception e){
            throw new Exception("Erro ao salvar o estado.");
        }
    }

    public ContatoDTO atualizar(Long id_contato, ContatoDTO newData) throws Exception {
        Contatos existing = contatosRepository.findById(id_contato)
                .orElseThrow(() -> new Exception("ID inválido."));

        if (!existing.getEmail().equals(newData.getEmail()) &&
                contatosRepository.existsByEmail(newData.getEmail())) {
            throw new ContatoDuplicadoException(newData.getEmail());
        }
        newData.setId_contato(id_contato);

        try {
            return toDTO(contatosRepository.save(toEntity(newData)));
        } catch (Exception e) {
            throw new Exception("A alteração não foi realizada: " + e.getMessage(), e);
        }
    }

    public String deletar(Long id) throws Exception {
        contatosRepository.deleteById(id);
        return "Excluído";
    }


}
