package br.com.agrohub.demo.dto; // PACOTE CORRETO: product

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ProductDetailResponseDTO implements Serializable {

    // 1. DADOS BÁSICOS DO PRODUTO (ClientProductDetailScreen.jsx)
    private Long id;
    private String nome;
    private String descricao;
    private String detalhes; // Campo 'details' no mock (informações ricas/nutricionais)
    private BigDecimal precoVenda;
    private String unidadeMedida; // Ex: "KG", "UN"
    private Integer quantidadeEstoque;

    // 2. MÍDIA E AVALIAÇÕES
    private List<String> imagensUrls;
    private Double ratingMedio;
    private Integer totalAvaliacoes;
    private List<ComentarioDTO> comentarios; // Usando o DTO que já criamos

    // 3. INFORMAÇÕES DA EMPRESA/PRODUTOR
    private Long idEmpresa;
    private String nomeEmpresa; // Nome Fantasia ou Razão Social
    private String municipioEmpresa;
    private String telefoneEmpresa;

    // 4. INFORMAÇÕES ADICIONAIS/LOGÍSTICAS
    private String categoria;
    private String tipoProduto;
    private String produtor; // O campo 'produtor' na tela AddProductScreen
    private String informacaoTecnica; // Informações adicionais

    // Construtor Padrão
    public ProductDetailResponseDTO() {
    }

    // Construtor com todos os campos (ADICIONADO)
    public ProductDetailResponseDTO(Long id, String nome, String descricao, String detalhes, BigDecimal precoVenda,
            String unidadeMedida, Integer quantidadeEstoque, List<String> imagensUrls, Double ratingMedio,
            Integer totalAvaliacoes, List<ComentarioDTO> comentarios, Long idEmpresa, String nomeEmpresa,
            String municipioEmpresa, String telefoneEmpresa, String categoria, String tipoProduto, String produtor,
            String informacaoTecnica) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.detalhes = detalhes;
        this.precoVenda = precoVenda;
        this.unidadeMedida = unidadeMedida;
        this.quantidadeEstoque = quantidadeEstoque;
        this.imagensUrls = imagensUrls;
        this.ratingMedio = ratingMedio;
        this.totalAvaliacoes = totalAvaliacoes;
        this.comentarios = comentarios;
        this.idEmpresa = idEmpresa;
        this.nomeEmpresa = nomeEmpresa;
        this.municipioEmpresa = municipioEmpresa;
        this.telefoneEmpresa = telefoneEmpresa;
        this.categoria = categoria;
        this.tipoProduto = tipoProduto;
        this.produtor = produtor;
        this.informacaoTecnica = informacaoTecnica;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Getters e Setters restantes (COMPLETADOS)
    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public List<String> getImagensUrls() {
        return imagensUrls;
    }

    public void setImagensUrls(List<String> imagensUrls) {
        this.imagensUrls = imagensUrls;
    }

    public Double getRatingMedio() {
        return ratingMedio;
    }

    public void setRatingMedio(Double ratingMedio) {
        this.ratingMedio = ratingMedio;
    }

    public Integer getTotalAvaliacoes() {
        return totalAvaliacoes;
    }

    public void setTotalAvaliacoes(Integer totalAvaliacoes) {
        this.totalAvaliacoes = totalAvaliacoes;
    }

    public List<ComentarioDTO> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<ComentarioDTO> comentarios) {
        this.comentarios = comentarios;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getMunicipioEmpresa() {
        return municipioEmpresa;
    }

    public void setMunicipioEmpresa(String municipioEmpresa) {
        this.municipioEmpresa = municipioEmpresa;
    }

    public String getTelefoneEmpresa() {
        return telefoneEmpresa;
    }

    public void setTelefoneEmpresa(String telefoneEmpresa) {
        this.telefoneEmpresa = telefoneEmpresa;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(String tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public String getProdutor() {
        return produtor;
    }

    public void setProdutor(String produtor) {
        this.produtor = produtor;
    }

    public String getInformacaoTecnica() {
        return informacaoTecnica;
    }

    public void setInformacaoTecnica(String informacaoTecnica) {
        this.informacaoTecnica = informacaoTecnica;
    }
}