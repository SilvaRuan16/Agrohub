import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./Components/Login";
import RedefinirSenha from "./Components/RedefinirSenha";
import InicioEmpresa from "./Components/Inicio";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login/>}/>
        <Route path="/refefinir_senha" element={<RedefinirSenha/>}/>
        <Route path="/inicio" element={<InicioEmpresa/>}/>
        <Route path="#" element={"Page not found"}/>
      </Routes>
    </BrowserRouter>
  );
}