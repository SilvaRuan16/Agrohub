package br.com.agrohub.demo.dto; 

import java.io.Serializable;
import java.time.LocalDate; 
// Certifique-se de que estes imports estejam corretos
// import br.com.agrohub.demo.dto.ContactDTO;
// import br.com.agrohub.demo.dto.EnderecoDTO; 

public class ClientRegisterRequestDTO implements Serializable {

    // 1. CAMPOS DE USUÁRIO (Tabela: USUARIOS)
    // O Front-end agora só envia 'senha' no nível raiz para criar o User.
    // O 'email' será pego do objeto 'contact' pelo Mapper.
    private String senha;
    private String cpf; 

    // 2. CAMPOS DE CLIENTE (Tabela: CLIENTES)
    private String nomeCompleto;
    private String rg;
    private String cnpj; // Incluído com base no Front-end
    private LocalDate dataNascimento;

    // ---------------------------------------------------------------------

    // 3. ⭐ CORREÇÃO PRINCIPAL: OBJETO ANINHADO PARA CONTATO ⭐
    // Esta propriedade 'contact' mapeia o objeto que vem no JSON do Front-end.
    private ContactDTO contact; 

    // 4. SUB-DTO DE ENDEREÇO (Tabela: ENDERECOS)
    private EnderecoDTO endereco;

    // Construtor padrão
    public ClientRegisterRequestDTO() {
    }

    // Construtor com todos os campos (útil para testes e services)
    public ClientRegisterRequestDTO(String senha, String cpf, String nomeCompleto, String rg, String cnpj,
            LocalDate dataNascimento, ContactDTO contact, EnderecoDTO endereco) {
        this.senha = senha;
        this.cpf = cpf;
        this.nomeCompleto = nomeCompleto;
        this.rg = rg;
        this.cnpj = cnpj;
        this.dataNascimento = dataNascimento;
        this.contact = contact;
        this.endereco = endereco;
    }

    // Getters e Setters
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    
    // NOVO: Getters e Setters para o objeto de Contato
    public ContactDTO getContact() { return contact; }
    public void setContact(ContactDTO contact) { this.contact = contact; }
    
    public EnderecoDTO getEndereco() { return endereco; }
    public void setEndereco(EnderecoDTO endereco) { this.endereco = endereco; }
    
    // Os getters/setters antigos para email, telefone, redeSocial e website foram removidos.
}