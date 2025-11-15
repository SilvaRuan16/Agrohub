package br.com.agrohub.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty; // Importe esta classe
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ProductCreateRequestDTO implements Serializable {

    // 1. DADOS BÁSICOS DO PRODUTO (Tabela: PRODUTOS)
    @JsonProperty("nome") // Força o mapeamento do campo "nome" do JSON
    private String nome;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("categoriaId")
    private Long categoriaId;

    @JsonProperty("tipoProdutoId")
    private Long tipoProdutoId;

    // 2. DADOS DE PREÇO E ESTOQUE
    @JsonProperty("precoVenda")
    private BigDecimal precoVenda;

    @JsonProperty("quantidadeEstoque")
    private Integer quantidadeEstoque;

    @JsonProperty("unidadeMedida")
    private String unidadeMedida;

    @JsonProperty("quantidadeMinEstoque")
    private Integer quantidadeMinEstoque;

    // 3. IMAGENS (Tabela: IMAGENS)
    @JsonProperty("imagensUrls")
    private List<String> imagensUrls;

    // 4. INFORMAÇÕES ADICIONAIS/LOGÍSTICAS
    @JsonProperty("produtor")
    private String produtor;

    @JsonProperty("municipio")
    private String municipio;

    @JsonProperty("logisticaId")
    private Long logisticaId;

    @JsonProperty("descontoId")
    private Long descontoId;

    @JsonProperty("linkAdicional")
    private String linkAdicional;

    // Construtor padrão (CRÍTICO para desserialização)
    public ProductCreateRequestDTO() {
    }

    // --- GETTERS E SETTERS ---
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