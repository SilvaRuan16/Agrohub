package br.com.agrohub.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.agrohub.demo.dto.CompanyProfileResponseDTO;
import br.com.agrohub.demo.mappers.CompanyMapper;
import br.com.agrohub.demo.models.Company;
// OBS: Assumindo que você tem repositórios para Company e Pedidos (Histórico de Vendas)
import br.com.agrohub.demo.models.Pedido;
import br.com.agrohub.demo.models.Address; 
import br.com.agrohub.demo.repository.CompanyRepository;
import br.com.agrohub.demo.repository.PedidoRepository; 
// OBS: Assumindo que você tem um repositório para Address (ou CompanyAddress)
// Para o CompanyMapper que criamos, ele espera List<Address>

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PedidoRepository pedidoRepository; // Para buscar o histórico de vendas
    private final CompanyMapper companyMapper;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, PedidoRepository pedidoRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.pedidoRepository = pedidoRepository;
        this.companyMapper = companyMapper;
    }

    /**
     * Busca o perfil completo de uma empresa, incluindo seus dados, endereços e histórico de vendas.
     * @param companyId O ID da empresa.
     * @return O DTO de resposta com todos os dados.
     * @throws RuntimeException se a empresa não for encontrada.
     */
    public CompanyProfileResponseDTO getCompanyProfile(Long companyId) {
        
        // 1. Buscar a Entidade Principal (Company)
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com o ID: " + companyId));

        // 2. Buscar Entidades Relacionadas: Histórico de Vendas (Pedidos)
        // OBS: Empresas vendem, então o histórico de vendas são os pedidos onde a empresa é a vendedora.
        // Assumindo que o PedidoRepository tem um método para buscar pedidos por ID da empresa do produto.
        // A maneira mais simples é buscar todos os pedidos e filtrar/mapear. Usaremos um mock simples de pedidos
        // ou você deve ter um método no PedidoRepository, como findByEmpresaId(companyId) ou similar.
        
        // **MOCK TEMPORÁRIO** (Substitua por uma chamada real ao repositório se tiver o método)
        List<Pedido> historicoVendas = pedidoRepository.findAll(); // MUDAR ISSO SE HOUVER MÉTODO CUSTOMIZADO

        // 3. Buscar Entidades Relacionadas: Endereços
        // Assumindo que a entidade Company tem um relacionamento OneToMany ou ManyToMany direto com Address (sem tabela de ligação como o cliente).
        // OBS: Você deve ter um método no CompanyRepository ou um CompanyAddressRepository para buscar esses endereços.
        // Usaremos o getEnderecos() da entidade Company como um placeholder que precisa ser implementado na Company.java
        
        // **MOCK TEMPORÁRIO** (MUDAR ISSO se o mapeamento for diferente)
        // O mapeamento mais comum é: Company tem List<Address> ou Company tem List<CompanyAddress>
        List<Address> addresses = List.of(); // Assumindo que você pegaria isso de company.getEnderecos() ou um repositório

        // 4. Mapear para o DTO de Perfil
        // O mapper lida com a conversão de User (dentro da Company) e o Histórico de Vendas
        return companyMapper.toCompanyProfileResponseDTO(
            company.getUser(), 
            company, 
            historicoVendas, 
            addresses // Certifique-se de que CompanyMapper está esperando List<Address>
        );
    }
}