import { useState } from "react";
import MenuBar from "./Others/MenuBar";
import Menuzinho from "./Others/Menuzinho";

const mockProdutos = [{ id: 1, nome: "Produto A" }, { id: 2, nome: "Produto B" }];

export default function Produtos() {
    const [produtos, setProdutos] = useState([]);

    const salvarProduto = () => {
        setProdutos(mockProdutos);
    };

    if (produtos.length === 0) {
        return (
            <>
                <MenuBar />
                <div>
                    <Menuzinho />
                    <h1>Nenhum produto cadastrado!</h1>
                    <button onClick={salvarProduto}>Adicionar produtos (exemplo)</button>
                </div>
            </>
        );
    } else {
        return (
            <>
                <MenuBar />
                <div>
                    <Menuzinho />
                    <h1>Produtos</h1>
                    {/* Renderize sua tabela de produtos aqui */}
                </div>
            </>
        );
    }
}