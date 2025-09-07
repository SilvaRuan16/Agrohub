import { Link } from "react-router-dom";
import Div_1 from "./div_1";

import "./Style/Login.css";

export default function Login() {
    return (
        <div className="container">
            <Div_1/>
            <div className="div_2">
                <h1>Login</h1>
                <form action="" method="get">
                    <div className="div_3">
                        <input className="cpf_cnpj" type="text" placeholder="CPF/CNPJ" />
                        <div className="checkbox">
                            <input type="radio" />
                            <label htmlFor="cpf">CPF</label>
                            <input type="radio" />
                            <label htmlFor="cnpj">CNPJ</label>
                        </div>
                        <input className="senha" type="password" name="senha" id="senha" placeholder="SENHA" />
                        <button className="entrar" type="submit">
                            ENTRAR
                        </button>
                        <Link to={"/refefinir_senha"}>Redefinir Senha</Link>
                    </div>
                </form>
            </div>
        </div>
    );
}