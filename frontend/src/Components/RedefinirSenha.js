import Div1 from "./Others/Div1";
import Div2 from "./Others/Div2";
import './Style/Div2.css';
import Container from "./Others/Container";
import './Style/Frames.css';
import Screen from './Others/Screen';

export default function RedefinirSenha() {
    const screenWidth = Screen();
    const isMobile = screenWidth < 768;
    return (
        <Container>
            {!isMobile && <Div1 />}
            <Div2>
                <h1>Redefinir Senha</h1>
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
                            <label className="labelInput" htmlFor='email'>E-mail</label>
                            <input className="email" type="email" name="email" id="email" placeholder="E-mail" />
                            <label className="labelInput" htmlFor='senha'>Senha</label>
                            <input className="senha" type="password" name="senha" id="senha" placeholder="SENHA" />
                        </div>
                        <button className="entrar" type="submit">
                            Salvar
                        </button>
                    </div>
                </form>
            </Div2>
        </Container>
    );
}