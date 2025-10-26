import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import InputMask from 'react-input-mask';
import styled from 'styled-components';
import { 
  Box, Typography, TextField, Button, Grid, IconButton, Alert, CircularProgress 
} from '@mui/material';
import { ArrowBack } from '@mui/icons-material';

// --- Styled Components (Mesmo padrão da tela de empresa) ---
const RegisterContainer = styled(Box)`
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f5f5f5;
`;

const Header = styled(Box)`
  background-color: #1a4314;
  color: white;
  padding: 15px 30px;
  display: flex;
  align-items: center;
`;

const FormContainer = styled(Box)`
  flex-grow: 1;
  padding: 40px;
  max-width: 1000px;
  margin: 0 auto;

  @media (max-width: 600px) {
    padding: 20px;
  }
`;

const Footer = styled(Box)`
  background-color: #1a4314;
  color: #c8e6c9;
  padding: 20px 40px;
  font-size: 0.75rem;
  text-align: center;
  flex-shrink: 0;
`;

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
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    if (name === 'imagem' && files?.length > 0) {
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
    setStatus({ type: '', message: '' });
    setLoading(true);

    const payload = {
      nomeCompleto: formData.nomeCompleto,
      senha: formData.senha,
      dataNascimento: formData.dataNascimento,
      cpf: formData.cpf.replace(/\D/g, ''),
      rg: formData.rg.replace(/\D/g, ''),
      cnpj: formData.cnpj.replace(/\D/g, ''),
      contact: {
        email: formData.email,
        telefone: formData.telefone.replace(/\D/g, ''),
        redeSocial: formData.redeSocial,
        website: formData.website,
      },
      endereco: {
        rua: formData.rua,
        numero: formData.numero,
        bairro: formData.bairro,
        cidade: formData.cidade,
        estado: formData.estado,
        cep: formData.cep.replace(/\D/g, ''),
        complemento: formData.complemento,
      },
    };

    try {
      await axios.post(API_URL, payload);
      setStatus({ type: 'success', message: 'Cliente cadastrado com sucesso!' });
      setLoading(false);
      navigate('/');
    } catch (error) {
      console.error("Erro no cadastro:", error);
      const errorMsg = error.response?.data?.message || "Ocorreu um erro inesperado. Verifique os dados.";
      setStatus({ type: 'error', message: errorMsg });
      setLoading(false);
    }
  };

  return (
    <RegisterContainer>
      <Header>
        <IconButton onClick={() => navigate('/register')} style={{ color: 'white', marginRight: '10px' }}>
          <ArrowBack />
        </IconButton>
        <Typography variant="h6">Registrar Clientes</Typography>
      </Header>

      <FormContainer component="form" onSubmit={handleRegister}>
        {status.message && (
          <Alert severity={status.type} sx={{ mb: 3 }}>
            {status.message}
          </Alert>
        )}

        <Grid container spacing={3}>
          {/* COLUNA 1 - Dados pessoais e contato */}
          <Grid item xs={12} sm={6}>
            <Typography 
              variant="h6" 
              gutterBottom 
              style={{ color: '#1a4314', borderBottom: '1px solid #ddd', paddingBottom: '5px', marginBottom: '15px' }}
            >
              Informações Pessoais
            </Typography>

            <TextField label="Nome Completo" name="nomeCompleto" value={formData.nomeCompleto} onChange={handleChange} fullWidth margin="normal" required />

            <InputMask mask="999.999.999-99" value={formData.cpf} onChange={handleChange} name="cpf">
              {(inputProps) => <TextField {...inputProps} label="CPF" fullWidth margin="normal" required />}
            </InputMask>

            <TextField label="RG" name="rg" value={formData.rg} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="CNPJ (Opcional)" name="cnpj" value={formData.cnpj} onChange={handleChange} fullWidth margin="normal" />
            <TextField 
              label="Data de Nascimento" 
              name="dataNascimento" 
              type="date" 
              value={formData.dataNascimento} 
              onChange={handleChange} 
              fullWidth 
              margin="normal" 
              InputLabelProps={{ shrink: true }} 
            />

            <Typography 
              variant="h6" 
              gutterBottom 
              style={{ color: '#1a4314', borderBottom: '1px solid #ddd', paddingBottom: '5px', marginTop: '30px', marginBottom: '15px' }}
            >
              Contato e Login
            </Typography>

            <TextField label="E-mail" name="email" value={formData.email} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Senha" name="senha" type="password" value={formData.senha} onChange={handleChange} fullWidth margin="normal" required />
            <InputMask mask="(99) 99999-9999" value={formData.telefone} onChange={handleChange} name="telefone">
              {(inputProps) => <TextField {...inputProps} label="Telefone" fullWidth margin="normal" required />}
            </InputMask>
            <TextField label="Rede Social (Opcional)" name="redeSocial" value={formData.redeSocial} onChange={handleChange} fullWidth margin="normal" />
            <TextField label="URL Site (Opcional)" name="website" value={formData.website} onChange={handleChange} fullWidth margin="normal" />
          </Grid>

          {/* COLUNA 2 - Endereço e imagem */}
          <Grid item xs={12} sm={6}>
            <Typography 
              variant="h6" 
              gutterBottom 
              style={{ color: '#1a4314', borderBottom: '1px solid #ddd', paddingBottom: '5px', marginBottom: '15px' }}
            >
              Endereço
            </Typography>

            <InputMask mask="99999-999" value={formData.cep} onChange={handleChange} name="cep">
              {(inputProps) => <TextField {...inputProps} label="CEP" fullWidth margin="normal" required />}
            </InputMask>

            <TextField label="Rua" name="rua" value={formData.rua} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Número" name="numero" value={formData.numero} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Bairro" name="bairro" value={formData.bairro} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Cidade" name="cidade" value={formData.cidade} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Estado" name="estado" value={formData.estado} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Complemento" name="complemento" value={formData.complemento} onChange={handleChange} fullWidth margin="normal" />

            <Typography 
              variant="h6" 
              gutterBottom 
              style={{ color: '#1a4314', borderBottom: '1px solid #ddd', paddingBottom: '5px', marginTop: '30px', marginBottom: '15px' }}
            >
              Imagem de Perfil
            </Typography>

            <Box display="flex" flexDirection="column" alignItems="center" gap={2}>
              <Button
                variant="contained"
                component="label"
                sx={{ backgroundColor: '#1a4314', color: 'white', '&:hover': { backgroundColor: '#245a1d' } }}
              >
                Escolher Imagem
                <input type="file" hidden name="imagem" accept="image/*" onChange={handleChange} />
              </Button>
              {selectedFile && (
                <Box sx={{ mt: 2 }}>
                  <Typography variant="body2">{selectedFile.name}</Typography>
                  <img
                    src={URL.createObjectURL(selectedFile)}
                    alt="preview"
                    style={{ width: '100%', maxWidth: '250px', borderRadius: '8px', marginTop: '10px' }}
                  />
                </Box>
              )}
            </Box>
          </Grid>
        </Grid>

        {/* Botões */}
        <Box display="flex" justifyContent="flex-start" gap={2} mt={4}>
          <Button 
            variant="contained" 
            style={{ backgroundColor: '#e57373', color: 'white' }}
            onClick={() => navigate('/register')}
          >
            Cancelar e voltar
          </Button>
          <Button 
            variant="contained" 
            style={{ backgroundColor: '#ffb74d', color: 'white' }} 
            onClick={handleClear}
          >
            Limpar Campos
          </Button>
          <Button 
            type="submit" 
            variant="contained" 
            style={{ backgroundColor: '#1a4314', color: 'white' }} 
            disabled={loading}
          >
            {loading ? <CircularProgress size={24} color="inherit" /> : 'Salvar'}
          </Button>
        </Box>

        {/* Rodapé interno */}
        <Box mt={4} py={3} sx={{ fontSize: '0.75rem', color: '#555', borderTop: '1px solid #eee' }}>
          <Typography variant="caption" display="block">
            &copy; AgroHub - Projeto Acadêmico 2025
          </Typography>
          <Typography variant="caption" display="block">
            Este é um protótipo acadêmico desenvolvido para propor soluções inovadoras para o setor agroalimentar. 
            Todos os conteúdos e funcionalidades têm caráter experimental e educacional.
          </Typography>
        </Box>
      </FormContainer>

      <Footer>
        copyright AgroHub - 2025
      </Footer>
    </RegisterContainer>
  );
}
