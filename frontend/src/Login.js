export default function Login() {
    return <>
        <div className="container">
            <div>
                <img src="../src/Logo_AgroHub.png" alt="Logo"/>
            </div>
            <div>
                <h1>Login</h1>
                <div className="div_campos_login">
                    <form action="" method="get">
                        <input type="text" placeholder="CPF/CNPJ"/>
                        <input type="password" name="senha" id="senha" placeholder="Senha"/>
                        <button>
                            Entrar
                        </button>
                        <a href="#">Esqueci a senha</a>
                    </form>
                </div>
            </div>
        </div>
    </>
}