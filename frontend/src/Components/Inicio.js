import MenuBar from "./Others/MenuBar";
import Menuzinho from "./Others/Menuzinho";
import '../Components/Style/Inicio.css';

export default function InicioEmpresa() {
    return <>
        <MenuBar/>
        <div className="bg_bodyInicio">
            <Menuzinho/>
            <div className="boasVindas">
                <h2>Seja bem vindo!</h2>
            </div>
        </div>
    </>
}