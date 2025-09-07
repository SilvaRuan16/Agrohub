import { Link } from "react-router-dom";
import Div1 from "./Div1";
import "./Style/Frames.css";
import Container from "./Container";
import Div2 from "./Div2";
import './Style/Div2.css';
import Screen from './Screen';

export default function Login() {
    const screenWidth = Screen();
    const isMobile = screenWidth < 768;
    return (
        <Container>
            {!isMobile && <Div1 />}
            <Div2>
                <h1>Login</h1>
                <form className="div_3" action="" method="get">
                    <div className="div_3">
                        <div className="div_user">
                            <label className="labelInput" htmlFor='cpf_cnpj'>Usuario</label>
                            <input className="cpf_cnpj" type="text" placeholder="CPF/CNPJ" />
                        </div>
                        <div className="checkbox">
                            <input type="radio" id="cpf" name="userType" />
                            <label htmlFor="cpf">CPF</label>
                            <input type="radio" id="cnpj" name="userType" />
                            <label htmlFor="cnpj">CNPJ</label>
                        </div>
                        <div className="div_user">
                            <label className="labelInput" htmlFor='senha'>Senha</label>
                            <input className="senha" type="password" name="senha" id="senha" placeholder="SENHA" />
                        </div>
                        <button className="entrar" type="submit">
                            ENTRAR
                        </button>
                        <Link to={"/refefinir_senha"}>Redefinir Senha</Link>
                    </div>
                </form>
            </Div2>
        </Container>
    );
}