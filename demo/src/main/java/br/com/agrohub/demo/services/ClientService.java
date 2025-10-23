package br.com.agrohub.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.agrohub.demo.dto.ClientProfileResponseDTO;
import br.com.agrohub.demo.dto.ClientRegisterRequestDTO;
import br.com.agrohub.demo.mappers.ClientMapper;
import br.com.agrohub.demo.models.Client;
import br.com.agrohub.demo.models.ClientAddress;
import br.com.agrohub.demo.models.Contact;
import br.com.agrohub.demo.models.Pedido;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.UserType;
import br.com.agrohub.demo.repository.ClientRepository;
import br.com.agrohub.demo.repository.ContactRepository;
import br.com.agrohub.demo.security.AuthSecurity;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ContactRepository contactRepository;
    private final ClientMapper clientMapper;
    private final AuthSecurity authSecurity;

    public ClientService(ClientRepository clientRepository, ContactRepository contactRepository,
            ClientMapper clientMapper, AuthSecurity authSecurity) {
        this.clientRepository = clientRepository;
        this.contactRepository = contactRepository;
        this.clientMapper = clientMapper;
        this.authSecurity = authSecurity;
    }

    @Transactional
    public void registerClient(ClientRegisterRequestDTO requestDTO) {

        Contact contact = clientMapper.toContact(requestDTO.getContact());
        contact = contactRepository.save(contact);

        User newUser = authSecurity.registerNewUser(
                requestDTO.getContact().getEmail(),
                requestDTO.getCpf(),
                requestDTO.getSenha(),
                UserType.CLIENTE);

        Client client = clientMapper.toClient(requestDTO);

        client.setUser(newUser);
        client.setContact(contact);

        String cnpj = client.getCnpj();
        if (cnpj != null && cnpj.trim().isEmpty()) {
            client.setCnpj(null);
        }

        clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public ClientProfileResponseDTO getClientProfile(Long userId) {

        Client client = clientRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cliente não encontrado para o ID de usuário fornecido."));

        List<Pedido> pedidos = client.getPedidos();
        List<ClientAddress> enderecos = client.getEnderecos();

        return clientMapper.toClientProfileDTO(client, pedidos, enderecos);
    }
}