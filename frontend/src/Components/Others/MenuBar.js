import '../Style/MenuBar.css';
import Logo from '../Image/Logo_AgroHub.png';
import User from '../Image/Logo_User.png';
import Pesquisar from '../Image/lupa.png';

export default function MenuBar() {
    return <>
        <div className="bg_menu">
            <div className='startMenuBar'>
                <div className='picture_div'>
                    <img className="logo" src={Logo} alt="Logo" />
                </div>
                <h3 className='saudacao'>Seja bem vindo</h3>
            </div>
            <div className='endMenuBar'>
                <div className='sessionSeach'>
                    <input type='search' className='pesquisar' placeholder='Pesquisar' />
                    <button className='buttonMenuBarSearch'>
                        <img className='lupa' src={Pesquisar} alt="Search"/>
                    </button>
                </div>
                <div className='buttonsEnd'>
                    <button className='buttonMenuBarAcess'>Login</button>
                    <div className='picture_div2'>
                        <img className="User" src={User} alt="Logo" />
                    </div>
                </div>
            </div>
        </div>
    </>
}