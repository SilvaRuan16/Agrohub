import "./Style/Container.css";

export default function Container({children}) {
    return <>
        <div className="container">
            {children}
        </div>
    </>
}