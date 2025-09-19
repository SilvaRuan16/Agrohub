import { Link } from "react-router-dom";
import '../Style/Menuzinho.css';

export default function Menuzinho() {
    return <>
        <div className="menuzinho">
            <div className="telas1">
                <Link className="produtos" to={"/inicio/produtos"}>Produtos</Link>
                <Link className="adicionar" to={"/inicio/adicionar_produtos"}>Adicionar Produtos</Link>
            </div>
            <div className="perfil1">
                <Link className="perfil" to={"/inicio/perfil"}>Perfil</Link>
            </div>
        </div>
    </>
}