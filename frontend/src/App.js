import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./Components/Login";
import RedefinirSenha from "./Components/RedefinirSenha";
import InicioEmpresa from "./Components/Inicio";
import Conta from "./Components/Conta";
import Produtos from "./Components/Produtos";
import CadastroProdutos from "./Components/CadastroProdutos";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login/>}/>
        <Route path="/refefinir_senha" element={<RedefinirSenha/>}/>
        <Route path="/inicio" element={<InicioEmpresa/>}/>
        <Route path="/criar_conta" element={<Conta/>}/>
        <Route path="/inicio/produtos" element={<Produtos/>}/>
        <Route path="/inicio/cadastrar_produtos" element={<CadastroProdutos/>}/>
        <Route path="#" element={"Page not found"}/>
      </Routes>
    </BrowserRouter>
  );
}