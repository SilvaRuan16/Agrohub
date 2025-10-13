package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ProductCreateRequestDTO implements Serializable {

    // 1. DADOS BÁSICOS DO PRODUTO (Tabela: PRODUTOS)
    private String nome;
    private String descricao;
    private Long categoriaId; // FK para tabela categorias
    private Long tipoProdutoId; // FK para tabela tipos_produtos

    // 2. DADOS DE PREÇO E ESTOQUE
    private BigDecimal precoVenda;
    private Integer quantidadeEstoque;
    private String unidadeMedida; // Ex: "KG", "UN", "CAIXA"
    private Integer quantidadeMinEstoque; // Para alerta na dashboard da empresa
    
    // 3. IMAGENS (Tabela: IMAGENS)
    private List<String> imagensUrls; 
    
    // 4. INFORMAÇÕES ADICIONAIS/LOGÍSTICAS (Tabela: INFORMACOES_ADICIONAIS/LOGISTICAS)
    // Campos vistos na tela AddProductScreen.jsx:
    private String produtor;
    private String municipio;
    private Long logisticaId; // FK para Logistica
    private Long descontoId; // FK para Desconto (se houver desconto padrão)
    private String linkAdicional; // O campo 'link' na sua tela

    // Construtor padrão
    public ProductCreateRequestDTO() {
    }

    // Construtor com todos os campos (útil para testes e services)
    public ProductCreateRequestDTO(String nome, String descricao, Long categoriaId, Long tipoProdutoId, BigDecimal precoVenda, Integer quantidadeEstoque, String unidadeMedida, Integer quantidadeMinEstoque, List<String> imagensUrls, String produtor, String municipio, Long logisticaId, Long descontoId, String linkAdicional) {
        this.nome = nome;
        this.descricao = descricao;
        this.categoriaId = categoriaId;
        this.tipoProdutoId = tipoProdutoId;
        this.precoVenda = precoVenda;
        this.quantidadeEstoque = quantidadeEstoque;
        this.unidadeMedida = unidadeMedida;
        this.quantidadeMinEstoque = quantidadeMinEstoque;
        this.imagensUrls = imagensUrls;
        this.produtor = produtor;
        this.municipio = municipio;
        this.logisticaId = logisticaId;
        this.descontoId = descontoId;
        this.linkAdicional = linkAdicional;
    }

    // Getters e Setters
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

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Long getTipoProdutoId() {
        return tipoProdutoId;
    }

    public void setTipoProdutoId(Long tipoProdutoId) {
        this.tipoProdutoId = tipoProdutoId;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public Integer getQuantidadeMinEstoque() {
        return quantidadeMinEstoque;
    }

    public void setQuantidadeMinEstoque(Integer quantidadeMinEstoque) {
        this.quantidadeMinEstoque = quantidadeMinEstoque;
    }

    public List<String> getImagensUrls() {
        return imagensUrls;
    }

    public void setImagensUrls(List<String> imagensUrls) {
        this.imagensUrls = imagensUrls;
    }

    public String getProdutor() {
        return produtor;
    }

    public void setProdutor(String produtor) {
        this.produtor = produtor;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public Long getLogisticaId() {
        return logisticaId;
    }

    public void setLogisticaId(Long logisticaId) {
        this.logisticaId = logisticaId;
    }

    public Long getDescontoId() {
        return descontoId;
    }

    public void setDescontoId(Long descontoId) {
        this.descontoId = descontoId;
    }

    public String getLinkAdicional() {
        return linkAdicional;
    }

    public void setLinkAdicional(String linkAdicional) {
        this.linkAdicional = linkAdicional;
    }
}