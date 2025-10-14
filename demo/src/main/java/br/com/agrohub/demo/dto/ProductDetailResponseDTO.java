package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ProductDetailResponseDTO implements Serializable {

    // 1. DADOS BÁSICOS DO PRODUTO 
    private Long id;
    private String nome;
    private String descricao;
    private String detalhes;
    private BigDecimal precoVenda;
    private String unidadeMedida;
    private Integer quantidadeEstoque;
    private Double descontoMaximo; // Adicionado: MaxPercentage do Discount Model

    // 2. MÍDIA E AVALIAÇÕES
    private List<String> imagensUrls;
    private Double ratingMedio;
    private Integer totalAvaliacoes;
    private List<ComentarioDTO> comentarios; 

    // 3. INFORMAÇÕES DA EMPRESA/PRODUTOR
    private CompanyResumeDTO empresa; // Substitui idEmpresa e nomeEmpresa
    private String municipioEmpresa;
    
    // CAMPOS DE ENDEREÇO E CNPJ ESTAVAM FALTANDO:
    private String cnpjProdutor;
    private EnderecoDTO enderecoProdutor;
    
    // 4. INFORMAÇÕES ADICIONAIS/LOGÍSTICAS
    private String categoria; // Onde você mapeou 'tipoProduto'
    private String tipoProduto;
    private String nomeProdutor; // Corrigido de 'produtor' para 'nomeProdutor' para evitar confusão de setProdutor
    private String informacaoTecnica; 

    // Construtor Padrão
    public ProductDetailResponseDTO() {
    }

    // OMITINDO CONSTRUTOR COMPLETO para simplificar (o Mapper usa Setters)
    
    // =================================================================
    // GETTERS E SETTERS
    // Omitidos para brevidade, mas devem existir todos. Incluindo os novos:
    // =================================================================

    // MÉTODOS REQUERIDOS PELO MAPPER:
    
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricaoCurta(String descricaoCurta) { this.descricao = descricaoCurta; }
    public void setDescricaoDetalhada(String descricaoDetalhada) { this.detalhes = descricaoDetalhada; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }
    public void setImagensUrls(List<String> imagensUrls) { this.imagensUrls = imagensUrls; }
    public void setRatingMedio(Double ratingMedio) { this.ratingMedio = ratingMedio; }
    public void setTotalAvaliacoes(Integer totalAvaliacoes) { this.totalAvaliacoes = totalAvaliacoes; }
    public void setComentarios(List<ComentarioDTO> comentarios) { this.comentarios = comentarios; }
    public void setEmpresa(CompanyResumeDTO empresa) { this.empresa = empresa; }
    
    // CORREÇÃO DE NOMENCLATURA:
    public void setNomeProdutor(String nomeProdutor) { this.nomeProdutor = nomeProdutor; }
    
    // NOVOS SETTERS PARA RESOLVER ERROS DE SÍMBOLO:
    public void setCnpjProdutor(String cnpjProdutor) { this.cnpjProdutor = cnpjProdutor; }
    public void setEnderecoProdutor(EnderecoDTO enderecoProdutor) { this.enderecoProdutor = enderecoProdutor; }

    // MANTENDO SETTER DE MUNICÍPIO QUE JÁ EXISTIA:
    public void setMunicipioEmpresa(String municipioEmpresa) { this.municipioEmpresa = municipioEmpresa; }
    public void setTipoProduto(String tipoProduto) { this.tipoProduto = tipoProduto; }
    public void setDescontoMaximo(Double descontoMaximo) { this.descontoMaximo = descontoMaximo; }
    
    // ... Todos os demais Getters e Setters devem existir
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getDetalhes() { return detalhes; }
    public BigDecimal getPrecoVenda() { return precoVenda; }
    public String getUnidadeMedida() { return unidadeMedida; }
    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public Double getDescontoMaximo() { return descontoMaximo; }
    public List<String> getImagensUrls() { return imagensUrls; }
    public Double getRatingMedio() { return ratingMedio; }
    public Integer getTotalAvaliacoes() { return totalAvaliacoes; }
    public List<ComentarioDTO> getComentarios() { return comentarios; }
    public CompanyResumeDTO getEmpresa() { return empresa; }
    public String getMunicipioEmpresa() { return municipioEmpresa; }
    public String getCnpjProdutor() { return cnpjProdutor; }
    public EnderecoDTO getEnderecoProdutor() { return enderecoProdutor; }
    public String getCategoria() { return categoria; }
    public String getTipoProduto() { return tipoProduto; }
    public String getNomeProdutor() { return nomeProdutor; }
    public String getInformacaoTecnica() { return informacaoTecnica; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setInformacaoTecnica(String informacaoTecnica) { this.informacaoTecnica = informacaoTecnica; }
    // Adicione aqui todos os demais getters e setters que faltam...
    
}
