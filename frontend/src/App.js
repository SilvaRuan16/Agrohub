import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./Components/Login";
import RedefinirSenha from "./Components/RedefinirSenha";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login/>}/>
        <Route path="/refefinir_senha" element={<RedefinirSenha/>}/>
        <Route path="#" element={"Page not found"}/>
      </Routes>
    </BrowserRouter>
  );
}