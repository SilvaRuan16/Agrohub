// RegisterClientScreen.js (CORRIGIDO)

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Typography, TextField, Button, Grid, IconButton, Alert, Paper } from '@mui/material';
import { ArrowBack } from '@mui/icons-material';
import styled from 'styled-components';
import InputMask from 'react-input-mask';

// --- Styled Components (Estilos de Layout/Cores) ---

// Cor principal da marca
const PRIMARY_COLOR = '#1a4314'; // Verde Escuro
const LIGHT_COLOR = '#c8e6c9'; // Verde Claro

const RegisterContainer = styled(Box)`
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f0f4f7; /* Fundo mais suave */
`;

const Header = styled(Box)`
  background-color: ${PRIMARY_COLOR};
  color: white;
  padding: 15px 30px;
  display: flex;
  align-items: center;
`;

const FormWrapper = styled(Paper)`
  /* Cria o 'card' do formulário */
  margin: 30px auto;
  padding: 40px;
  width: 100%;
  max-width: 1200px; /* Limita a largura do formulário em telas grandes */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  
  @media (max-width: 900px) {
    margin: 20px;
    padding: 20px;
  }
`;

const Footer = styled(Box)`
  background-color: ${PRIMARY_COLOR};
  color: ${LIGHT_COLOR};
  padding: 20px 40px;
  font-size: 0.75rem;
  text-align: center;
  margin-top: auto; /* Garante que o footer fique no final */
`;

// --- Componente de Input Mascarado Reutilizável ---
const MaskedInput = (props) => {
    // eslint-disable-next-line no-unused-vars
    const { inputRef, ...other } = props; 
    return <InputMask {...other} />;
};


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

    const handleChange = (e) => {
      const { name, value, files } = e.target;
      
      if (name === 'imagem' && files && files.length > 0) {
        setSelectedFile(files[0]);
      } else {
        setFormData(prev => ({ ...prev, [name]: value }));
      }
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
        
        // ⭐ CORREÇÃO: ENVIANDO O OBJETO 'contact' ANINHADO PARA O BACK-END
        contact: { 
          email: formData.email, // Movemos para dentro de contact
          telefone: formData.telefone.replace(/[^0-9]/g, ''), // Movemos para dentro de contact
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
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(clientData),
        });

        if (response.ok) {
          console.log('✅ Cadastro realizado com sucesso!');
          setStatus({ type: 'success', message: 'Cadastro realizado com sucesso! Redirecionando...' });
          setTimeout(() => navigate('/login'), 2000); 
        } else {
          const errorText = await response.text();
          let errorMessage = `Erro ${response.status}: ${errorText.substring(0, 100)}...`;
          
          try {
            const errorData = JSON.parse(errorText);
            errorMessage = `Erro (${response.status}): ${errorData.message || 'Erro desconhecido.'}`;
          } catch (e) { /* Não é JSON */ }
          
          console.error('❌ Falha na Requisição (Status:', response.status, ') Resposta Bruta:', errorText);
          setStatus({ 
            type: 'error', 
            message: `Falha: ${errorMessage}. Verifique o console para detalhes.`
          });
        }
      } catch (error) {
        console.error('❌ Erro de Rede ou CORS:', error);
        setStatus({ type: 'error', message: 'Erro de comunicação. Verifique se o Back-end está rodando e se o CORS está configurado.' });
      }
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

    return (
      <RegisterContainer>
        {/* CABEÇALHO */}
        <Header>
          <IconButton onClick={() => navigate('/register')} style={{ color: 'white', marginRight: '10px' }}>
            <ArrowBack />
          </IconButton>
          <Typography variant="h6">
            Registrar Cliente <span style={{fontSize: '0.8em', opacity: 0.8}}>- Dados Completo</span>
          </Typography>
        </Header>

        {/* CONTEÚDO PRINCIPAL (FORMULÁRIO) */}
        <FormWrapper component="form" onSubmit={handleRegister}>
          <Typography variant="h5" component="h1" gutterBottom align="center" color={PRIMARY_COLOR}>
            Dados de Cadastro
          </Typography>
          
          {/* Area de Status/Feedback */}
          {status.message && (
            <Box mb={3}>
              <Alert severity={status.type} variant="outlined">{status.message}</Alert>
            </Box>
          )}
          
          <Grid container spacing={4}> {/* Aumentei o espaçamento entre as linhas/colunas */}
            {/* ------------------- Seção de Dados Pessoais / Documentos ------------------- */}
            <Grid item xs={12}>
              <Typography variant="subtitle1" sx={{ mt: 2, mb: 1, borderBottom: `2px solid ${PRIMARY_COLOR}`, color: PRIMARY_COLOR }}>
                1. Informações Pessoais e de Login
              </Typography>
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField label="Nome Completo" name="nomeCompleto" value={formData.nomeCompleto} onChange={handleChange} fullWidth required />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField label="E-mail" name="email" value={formData.email} onChange={handleChange} fullWidth required type="email" />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField label="Senha" name="senha" value={formData.senha} onChange={handleChange} fullWidth required type="password" />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField 
                label="Data de Nascimento (AAAA-MM-DD)" 
                name="dataNascimento" 
                value={formData.dataNascimento} 
                onChange={handleChange} 
                fullWidth 
                placeholder="Ex: 1990-12-31"
              />
            </Grid>
            
            <Grid item xs={12} sm={6}>
              {/* CPF com Máscara */}
              <TextField 
                label="CPF" 
                name="cpf"
                value={formData.cpf} 
                onChange={handleChange} 
                fullWidth 
                required
                InputProps={{
                  inputComponent: MaskedInput,
                  inputProps: { mask: "999.999.999-99", name: "cpf" }
                }}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField label="RG" name="rg" value={formData.rg} onChange={handleChange} fullWidth required />
            </Grid>

            <Grid item xs={12} sm={6}>
              {/* CNPJ com Máscara */}
              <TextField 
                label="CNPJ (Opcional)" 
                name="cnpj" 
                value={formData.cnpj}
                onChange={handleChange}
                fullWidth 
                InputProps={{
                  inputComponent: MaskedInput,
                  inputProps: { mask: "99.999.999/9999-99", name: "cnpj" }
                }}
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              {/* Telefone com Máscara */}
              <TextField 
                label="Telefone" 
                name="telefone"
                value={formData.telefone} 
                onChange={handleChange} 
                fullWidth 
                required
                InputProps={{
                  inputComponent: MaskedInput,
                  inputProps: { mask: "(99) 99999-9999", name: "telefone" }
                }}
              />
            </Grid>

            {/* ------------------- Seção de Endereço ------------------- */}
            <Grid item xs={12}>
              <Typography variant="subtitle1" sx={{ mt: 2, mb: 1, borderBottom: `2px solid ${PRIMARY_COLOR}`, color: PRIMARY_COLOR }}>
                2. Endereço
              </Typography>
            </Grid>
            <Grid item xs={12} sm={4}>
              {/* CEP com Máscara */}
              <TextField 
                label="CEP" 
                name="cep"
                value={formData.cep} 
                onChange={handleChange} 
                fullWidth 
                required
                InputProps={{
                  inputComponent: MaskedInput,
                  inputProps: { mask: "99999-999", name: "cep" }
                }}
              />
            </Grid>
            <Grid item xs={12} sm={8}>
              <TextField label="Rua" name="rua" value={formData.rua} onChange={handleChange} fullWidth required />
            </Grid>

            <Grid item xs={12} sm={4}>
              <TextField label="Número" name="numero" value={formData.numero} onChange={handleChange} fullWidth required />
            </Grid>
            <Grid item xs={12} sm={8}>
              <TextField label="Complemento (Ex: Apto 101)" name="complemento" value={formData.complemento} onChange={handleChange} fullWidth />
            </Grid>
            
            <Grid item xs={12} sm={4}>
              <TextField label="Bairro" name="bairro" value={formData.bairro} onChange={handleChange} fullWidth required />
            </Grid>
            <Grid item xs={12} sm={5}>
              <TextField label="Cidade" name="cidade" value={formData.cidade} onChange={handleChange} fullWidth required />
            </Grid>
            <Grid item xs={12} sm={3}>
              <TextField label="Estado (Ex: SP)" name="estado" value={formData.estado} onChange={handleChange} fullWidth required inputProps={{ maxLength: 2 }} />
            </Grid>


            {/* ------------------- Seção de Informações Adicionais ------------------- */}
            <Grid item xs={12}>
              <Typography variant="subtitle1" sx={{ mt: 2, mb: 1, borderBottom: `2px solid ${PRIMARY_COLOR}`, color: PRIMARY_COLOR }}>
                3. Opcionais
              </Typography>
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField label="Rede Social (Opcional)" name="redeSocial" value={formData.redeSocial} onChange={handleChange} fullWidth />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField label="Website (Opcional)" name="website" value={formData.website} onChange={handleChange} fullWidth />
            </Grid>

            <Grid item xs={12}>
              <Box display="flex" alignItems="center" my={1}>
                <Button variant="outlined" component="label" sx={{ mr: 2, color: PRIMARY_COLOR, borderColor: PRIMARY_COLOR, '&:hover': { borderColor: PRIMARY_COLOR, backgroundColor: 'rgba(26, 67, 20, 0.04)' } }}>
                  Escolher Imagem de Perfil
                  <input type="file" hidden onChange={handleChange} name="imagem" accept="image/*" />
                </Button>
                <Typography variant="body2" color="textSecondary">
                  {selectedFile ? `Arquivo Selecionado: ${selectedFile.name}` : 'Nenhuma imagem selecionada.'}
                </Typography>
              </Box>
            </Grid>

          </Grid>

          {/* Botões de Ação */}
          <Box display="flex" justifyContent="flex-start" gap={2} mt={4} flexWrap="wrap">
            <Button variant="outlined" color="error" onClick={() => navigate('/register')}>
              Cancelar e Voltar
            </Button>
            <Button variant="outlined" color="warning" onClick={handleClear}>
              Limpar Campos
            </Button>
            <Button type="submit" variant="contained" sx={{ backgroundColor: PRIMARY_COLOR, color: 'white', '&:hover': { backgroundColor: '#38761d' } }}>
              Salvar Cadastro
            </Button>
          </Box>
          
          {/* Rodapé interno com texto do projeto */}
          <Box mt={6} py={3} sx={{ fontSize: '0.75rem', color: '#555', borderTop: '1px solid #eee' }}>
            <Typography variant="caption" display="block">
              &copy; AgroHub - Projeto Acadêmico 2025
            </Typography>
            <Typography variant="caption" display="block" sx={{ mt: 1 }}>
              Este é um protótipo acadêmico... [texto completo da descrição do projeto].
            </Typography>
          </Box>
        </FormWrapper>
        
        {/* RODAPÉ EXTERNO */}
        <Footer>
          copyright AgroHub - 2025
        </Footer>
      </RegisterContainer>
    );
}