import '../Style/MenuBar.css';
import Logo from '../Image/Logo_AgroHub.png';
import User from '../Image/do-utilizador.png';

export default function MenuBar() {
    return <>
        <div className="bg_menu">
            <div className='startMenuBar'>
                <div className='picture_div'>
                    <img className="logo" src={Logo} alt="Logo"/>
                </div>
                <h3 className='saudacao'>Seja bem vindo</h3>
            </div>
            <div className='endMenuBar'>
                <input type='search' className='pesquisar' placeholder='Pesquisar'/>
                <button className='buttonMenuBar'>Pesquisar</button>
                <button className='buttonMenuBar'>Login</button>
                <div className='picture_div2'>
                    <img className="logo" src={User} alt="Logo"/>
                </div>
            </div>
        </div>
    </>
}