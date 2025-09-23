import MenuBar from "./Others/MenuBar";
import Menuzinho from "./Others/Menuzinho";
import '../Components/Style/CadastroProdutos.css';

export default function CadastroProdutos() {
    return <>
        <MenuBar />
        <div className="form_cadastroProdutos">
            <Menuzinho />
            <label className="titulo">Informações sobre o produto</label>
            <div className="camposCadastroProdutos">
                <div className="divs">
                    <div className="divEscrever">
                        <label className="tituloCampo">Nome</label><br></br>
                        <input className="inputCampo" type="text" placeholder="Nome do produto" />
                    </div>
                    <div className="divisao"></div>
                    <div className="divEscrever">
                        <label className="tituloCampo">Código interno</label>
                        <input className="inputCampo" type="text" placeholder="Código interno" />
                    </div>
                </div>
                <div className="divs">
                    <div className="divEscrever">
                        <label className="tituloCampo">Unidade de medida</label><br></br>
                        <select className="inputCampo" id="tipo-usuario" name="tipo-usuario">
                            <option value="" disabled selected>Selecionar</option>
                            <option value="moderador">Vendedor</option>
                            <option value="cliente">Cliente</option>
                        </select>
                    </div>
                    <div className="divisao"></div>
                    <div className="divEscrever">
                        <label className="tituloCampo">Categoria</label><br></br>
                        <select className="inputCampo" id="tipo-usuario" name="tipo-usuario">
                            <option value="" disabled selected>Selecionar</option>
                            <option value="moderador">Vendedor</option>
                            <option value="cliente">Cliente</option>
                        </select>
                    </div>
                </div>
                <div className="divs">
                    <div className="divEscrever">
                        <label className="tituloCampo">Compra</label>
                        <input className="inputCampo" type="text" placeholder="Preço de compra" />
                    </div>
                    <div className="divisao"></div>
                    <div className="divEscrever">
                        <label className="tituloCampo">Venda</label><br></br>
                        <input className="inputCampo" type="text" placeholder="Preço de venda" />
                    </div>
                </div>
                <div className="divsPersonalizado">
                    <div className="divP">
                        <div className="divEscrever">
                            <label className="tituloCampo">Minimo</label><br></br>
                            <input className="inputCampo" type="text" placeholder="Preço minimo de venda" />
                        </div>
                        <div className="divEscrevers">
                            <label className="tituloCampo">Estoque</label>
                            <input className="inputCampo" type="text" placeholder="Quantidade de estoque" />
                        </div>
                    </div>
                    <div className="divisao"></div>
                    <div className="pk">
                        <label className="tituloCampo">Margem de lucro (%)</label>
                        <div className="divDecoracao">
                            <input className="inputCampoK" type="text" placeholder="Margem de lucro (%)" />
                            <div className="t">
                                Informe o valor da margem de lucro desejada (%) — esse reajuste será aplicado sobre o valor mínimo de venda.
                            </div>
                        </div>
                    </div>
                </div>
                <div className="divs">
                    <div className="divEscrever">
                        <label className="tituloCampo">Imagem</label>
                        <input className="inputCampo" type="text" placeholder="Escolher arquivo" />
                    </div>
                    <div className="divisao"></div>
                    <div className="divEscrever">
                        <label className="tituloCampo">Código interno</label>
                        <input className="inputCampo" type="text" placeholder="Código interno" />
                    </div>
                </div>
                <div className="divs">
                    <button className="formatButton">Visualizar imagem</button>
                    <div className="divisao"></div>
                    <button className="formatButton">Aplicar desconto dinâmico</button>
                </div>
                <div className="divFG">
                    <label className="tituloCampo">Descrição</label>
                    <textarea className="inputCampoArea" placeholder="Descrição"></textarea>
                </div>
                <div className="divF">
                    <label className="titulo">Informações adicionais</label>
                </div>
                <div className="divs">
                    <div className="divEscrever">
                        <label className="tituloCampo">Produtor</label>
                        <input className="inputCampo" type="text" placeholder="Produtor" />
                    </div>
                    <div className="divisao"></div>
                    <div className="divEscrever">
                        <label className="tituloCampo">Municipio</label>
                        <input className="inputCampo" type="text" placeholder="Municipio" />
                    </div>
                </div>
                <div className="divs">
                    <div className="divEscrever">
                        <label className="tituloCampo">Endereço</label>
                        <input className="inputCampo" type="text" placeholder="Endereço" />
                    </div>
                    <div className="divisao"></div>
                    <div className="divEscrever">
                        <label className="tituloCampo">CNPJ do produtor</label>
                        <input className="inputCampo" type="text" placeholder="CNPJ" />
                    </div>
                </div>
                <div className="divs">
                    <div className="divEscrever">
                        <label className="tituloCampo">Contato</label>
                        <input className="inputCampo" type="text" placeholder="Contato" />
                    </div>
                    <div className="divisao"></div>
                    <div className="divEscrever">
                        <label className="tituloCampo">Tipo do produto</label>
                        <input className="inputCampo" type="text" placeholder="Tipo do produto" />
                    </div>
                </div>
                <div className="divs">
                    <div className="divEscrever">
                        <label className="tituloCampo">Classificação</label>
                        <input className="inputCampo" type="text" placeholder="Classificação" />
                    </div>
                    <div className="divisao"></div><div className="divEscrever"></div>
                </div>
                <div className="save">
                    <button className="formatButtonS">
                        Salvar produto
                    </button>
                </div>
            </div>
        </div>
    </>
}