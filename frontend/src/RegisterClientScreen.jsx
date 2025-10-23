import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import InputMask from 'react-input-mask';

import { ArrowBack } from '@mui/icons-material';

// --- Constantes de Estilo ---
const PRIMARY_COLOR = '#1a4314'; // Verde Escuro
const LIGHT_COLOR = '#c8e6c9'; // Verde Claro (Texto do Footer)
const WARNING_COLOR = '#f97316'; // Laranja (Botão Limpar Campos)
const ERROR_COLOR = '#ef4444'; // Vermelho (Botão Cancelar)

// --- Componente de Input Mascarado Reutilizável ---
const MaskedInput = React.forwardRef((props, ref) => {
    return (
        <InputMask 
            {...props} 
            ref={ref}
            maskChar={null}
            // Estilo simples de input para formulário centralizado
            className="w-full px-4 py-2 border border-gray-400 rounded-sm focus:outline-none focus:ring-1 focus:ring-green-700 shadow-sm"
        />
    );
});


// --- Componente Principal ---

export default function RegisterClientScreen() {
    const navigate = useNavigate();
    const API_URL = 'http://localhost:8080/api/v1/clients/register'; 
    
    const [formData, setFormData] = useState({
        nomeCompleto: '', cpf: '', rg: '', cnpj: '', 
        email: '', senha: '', dataNascimento: '', telefone: '',
        redeSocial: '', website: '', 
        rua: '', numero: '', bairro: '', cidade: '', estado: '', cep: '', complemento: '',
    });
    
    const [status, setStatus] = useState({ type: '', message: '' });
    const [selectedFile, setSelectedFile] = useState(null); 

    // ... [Funções handleChange, handleClear e handleRegister permanecem as mesmas] ...
    const handleChange = (e) => {
        const { name, value, files } = e.target;
        
        if (name === 'imagem' && files && files.length > 0) {
            setSelectedFile(files[0]);
        } else {
            setFormData(prev => ({ ...prev, [name]: value }));
        }
        setStatus({ type: '', message: '' }); 
    };
    
    const handleClear = () => {
        setFormData({
            nomeCompleto: '', cpf: '', rg: '', cnpj: '',
            email: '', senha: '', dataNascimento: '', telefone: '',
            redeSocial: '', website: '', rua: '', numero: '',
            bairro: '', cidade: '', estado: '', cep: '', complemento: '',
        });
        setSelectedFile(null); 
        setStatus({ type: '', message: '' });
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        setStatus({ type: 'info', message: 'Tentando cadastrar cliente...' });
        
        const clientData = {
            nomeCompleto: formData.nomeCompleto, 
            senha: formData.senha, 
            dataNascimento: formData.dataNascimento, 
            
            cpf: formData.cpf.replace(/[^0-9]/g, ''), 
            rg: formData.rg.replace(/[^0-9]/g, ''), 
            cnpj: formData.cnpj.replace(/[^0-9]/g, ''), 
            
            contact: { 
                email: formData.email,
                telefone: formData.telefone.replace(/[^0-9]/g, ''),
                redeSocial: formData.redeSocial,
                website: formData.website,
            },

            endereco: { 
                rua: formData.rua,
                numero: formData.numero,
                bairro: formData.bairro,
                cidade: formData.cidade,
                estado: formData.estado,
                cep: formData.cep.replace(/[^0-9]/g, ''), 
                complemento: formData.complemento, 
            }
        };
        
        console.log('Payload final enviado para a API:', clientData);

        try {
            const response = await fetch(API_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(clientData),
            });

            if (response.ok) {
                console.log('✅ Cadastro realizado com sucesso!');
                setStatus({ type: 'success', message: 'Cadastro realizado com sucesso! Redirecionando...' });
                setTimeout(() => navigate('/'), 2000); 
            } else {
                const errorText = await response.text();
                let errorMessage = `Erro ${response.status}: ${errorText.substring(0, 100)}...`;
                
                try {
                    const errorData = JSON.parse(errorText);
                    errorMessage = `Erro (${response.status}): ${errorData.message || 'Erro desconhecido.'}`;
                } catch (e) { /* Não é JSON */ }
                
                console.error('❌ Falha na Requisição (Status:', response.status, ') Resposta Bruta:', errorText);
                setStatus({ type: 'error', message: `Falha: ${errorMessage}. Verifique o console para detalhes.` });
            }
        } catch (error) {
            console.error('❌ Erro de Rede ou CORS:', error);
            setStatus({ type: 'error', message: 'Erro de comunicação. Verifique se o Back-end está rodando e se o CORS está configurado.' });
        }
    };
    
    // Função auxiliar para renderizar um input padrão
    // Todos os campos agora ocupam a largura total do container do formulário
    const renderInputWithLabel = (label, name, value, type = 'text', required = false, maxLength = null) => (
        <div className="flex flex-col space-y-1 w-full">
            <label htmlFor={name} className="text-sm font-medium text-gray-800">
                {label}
                {required && <span className="text-red-500">*</span>}
            </label>
            <input
                id={name}
                name={name}
                type={type}
                value={value}
                onChange={handleChange}
                required={required}
                maxLength={maxLength}
                placeholder={label}
                className="w-full px-4 py-2 border border-gray-400 rounded-sm focus:outline-none focus:ring-1 focus:ring-green-700 transition duration-150 shadow-sm"
            />
        </div>
    );
    
    // Função auxiliar para input mascarado
    const renderMaskedField = (label, name, value, mask, required = false) => (
        <div className="flex flex-col space-y-1 w-full">
            <label htmlFor={name} className="text-sm font-medium text-gray-800">
                {label}
                {required && <span className="text-red-500">*</span>}
            </label>
            <MaskedInput 
                mask={mask} 
                name={name}
                value={value}
                onChange={handleChange}
                required={required}
                placeholder={label} 
            />
        </div>
    );


    return (
        <div className="min-h-screen flex flex-col bg-gray-100">
            
            {/* CABEÇALHO (Menu Verde Escuro) */}
            <header style={{ backgroundColor: PRIMARY_COLOR }} className="text-white p-4 sm:p-5 flex items-center shadow-lg w-full">
                <button onClick={() => navigate('/register')} className="text-white mr-4 p-1 rounded-full hover:bg-green-700 transition duration-150">
                    <ArrowBack fontSize="large" />
                </button>
                <h1 className="text-xl sm:text-2xl font-semibold">
                    Registrar Clientes
                </h1>
            </header>

            {/* CONTEÚDO PRINCIPAL (FORMULÁRIO SIMPLIFICADO E CENTRALIZADO) */}
            <main className="flex-grow p-4 sm:p-8 flex justify-center">
                {/* Form Container: Max-w-lg (Centralizado) */}
                <form onSubmit={handleRegister} className="w-full max-w-lg bg-white shadow-xl rounded-lg p-6 sm:p-8 space-y-4">
                    
                    <h2 className="text-2xl font-bold text-center mb-6" style={{ color: PRIMARY_COLOR }}>
                        Dados de Cadastro
                    </h2>

                    {/* Área de Status/Feedback */}
                    {status.message && (
                        <div className={`p-3 rounded-md border-l-4 font-medium text-sm ${
                            status.type === 'error' ? 'bg-red-100 border-red-500 text-red-700' :
                            status.type === 'success' ? 'bg-green-100 border-green-500 text-green-700' :
                            'bg-blue-100 border-blue-500 text-blue-700'
                        } mb-4`}>
                            {status.message}
                        </div>
                    )}
                    
                    {/* Campos do Formulário em Linhas (Estrutura Simples) */}
                    <div className="space-y-4">
                        
                        {/* ------------------- DADOS PESSOAIS ------------------- */}
                        <div className="pt-2">
                             <h3 className="text-lg font-semibold mb-3 pb-1 border-b border-gray-300" style={{ color: PRIMARY_COLOR }}>
                                Informações Pessoais
                            </h3>
                        </div>
                        
                        {renderInputWithLabel("Nome Completo", "nomeCompleto", formData.nomeCompleto, 'text', true)}
                        {renderMaskedField("CPF", "cpf", formData.cpf, "999.999.999-99", true)}
                        {renderInputWithLabel("RG", "rg", formData.rg, 'text', true)}
                        {renderInputWithLabel("CNPJ (Opcional - Caso queira comprar como empresa)", "cnpj", formData.cnpj, 'text', false)}
                        {renderInputWithLabel("Data de nascimento", "dataNascimento", formData.dataNascimento, 'date', false)}
                        
                        {/* ------------------- CONTATO E LOGIN ------------------- */}
                        <div className="pt-4">
                             <h3 className="text-lg font-semibold mb-3 pb-1 border-b border-gray-300" style={{ color: PRIMARY_COLOR }}>
                                Contato e Login
                            </h3>
                        </div>

                        {renderInputWithLabel("E-mail", "email", formData.email, 'email', true)}
                        {renderInputWithLabel("Senha", "senha", formData.senha, 'password', true)}
                        {renderMaskedField("Telefone", "telefone", formData.telefone, "(99) 99999-9999", true)}
                        {renderInputWithLabel("Rede social (Opcional)", "redeSocial", formData.redeSocial)}
                        {renderInputWithLabel("Url site (Opcional)", "website", formData.website, 'url')}

                        {/* ------------------- ENDEREÇO ------------------- */}
                        <div className="pt-4">
                             <h3 className="text-lg font-semibold mb-3 pb-1 border-b border-gray-300" style={{ color: PRIMARY_COLOR }}>
                                Endereço
                            </h3>
                        </div>

                        {renderMaskedField("CEP", "cep", formData.cep, "99999-999", true)}
                        {renderInputWithLabel("Rua", "rua", formData.rua, 'text', true)}
                        <div className="flex gap-4">
                            {renderInputWithLabel("Número", "numero", formData.numero, 'text', true)}
                            {renderInputWithLabel("Estado (Ex: SP)", "estado", formData.estado, 'text', true, 2)}
                        </div>
                        {renderInputWithLabel("Bairro", "bairro", formData.bairro, 'text', true)}
                        {renderInputWithLabel("Cidade", "cidade", formData.cidade, 'text', true)}
                        {renderInputWithLabel("Complemento (Ex: Apto 101)", "complemento", formData.complemento)}

                        {/* ------------------- IMAGEM ------------------- */}
                        <div className="pt-4">
                             <h3 className="text-lg font-semibold mb-3 pb-1 border-b border-gray-300" style={{ color: PRIMARY_COLOR }}>
                                Imagem de Perfil
                            </h3>
                        </div>
                        <div className="flex items-center space-x-4">
                            <label 
                                className="flex-grow px-4 py-2 border border-gray-400 rounded-sm cursor-pointer text-gray-500 bg-gray-50 hover:bg-gray-100 transition duration-150 text-sm" 
                                htmlFor="imagem-upload"
                            >
                                {selectedFile ? `Arquivo Selecionado: ${selectedFile.name}` : 'Escolher Arquivo'}
                                <input type="file" hidden onChange={handleChange} name="imagem" id="imagem-upload" accept="image/*" />
                            </label>
                            <button type="button" 
                                style={{ backgroundColor: PRIMARY_COLOR }}
                                className="px-4 py-2 text-white font-medium rounded-sm shadow-sm hover:bg-green-800 transition duration-150 text-sm">
                                Visualizar
                            </button>
                        </div>

                    </div>

                    {/* Botões de Ação */}
                    <div className="flex flex-wrap justify-start gap-4 pt-6 border-t border-gray-300 mt-6">
                        <button type="button" onClick={() => navigate('/register')}
                            style={{ backgroundColor: ERROR_COLOR }}
                            className="px-6 py-2 text-white font-semibold rounded-md shadow-md hover:opacity-90 transition duration-150 text-sm">
                            Cancelar e voltar
                        </button>
                        <button type="button" onClick={handleClear}
                            style={{ backgroundColor: WARNING_COLOR }}
                            className="px-6 py-2 text-white font-semibold rounded-md shadow-md hover:opacity-90 transition duration-150 text-sm">
                            Limpar Campos
                        </button>
                        <button type="submit"
                            style={{ backgroundColor: PRIMARY_COLOR }}
                            className="px-8 py-2 text-white font-semibold rounded-md shadow-md hover:bg-green-800 transition duration-150 text-sm">
                            Salvar
                        </button>
                    </div>

                    {/* Rodapé interno (Texto do Projeto) */}
                    <div className="mt-8 pt-4 text-xs text-gray-500 border-t border-gray-200 leading-relaxed">
                        <p className="font-bold">&copy; AgroHub - Projeto Acadêmico 2025</p>
                        <p className="mt-1">Este é um protótipo acadêmico...</p>
                    </div>

                </form>
            </main>
            
            {/* RODAPÉ EXTERNO (Copyright Verde Escuro) */}
            <footer style={{ backgroundColor: PRIMARY_COLOR, color: LIGHT_COLOR }} className="p-4 text-center text-xs mt-auto shadow-inner">
                copyright AgroHub - 2025
            </footer>
        </div>
    );
}