import Logo from './Image/Logo_AgroHub.png';
import './Style/Div1.css';

export default function Div1() {
    return <>
        <div className="div_1">
            <img className="logo" src={Logo} alt="Logo"/>
        </div>
    </>
}