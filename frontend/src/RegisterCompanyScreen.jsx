import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios'; // Importa axios para a chamada de API
import { Box, Typography, TextField, Button, Grid, IconButton, Alert, CircularProgress } from '@mui/material';
import { ArrowBack } from '@mui/icons-material';
import styled from 'styled-components';
import InputMask from 'react-input-mask';

// --- Styled Components (Mantidos) ---

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

export default function RegisterCompanyScreen() {
  const navigate = useNavigate();

  // Estado inicial alinhado com CompanyRegisterRequestDTO
  const [formData, setFormData] = useState({
    // Top-Level / User / Company
    razaoSocial: '', nomeFantasia: '', email: '', senha: '',
    cnpj: '', dataFundacao: '', telefone: '', website: '', // 'website' mapeia para 'urlSite'
    // Endereço (flat, será aninhado no handleRegister)
    rua: '', numero: '', bairro: '', cidade: '', estado: '', cep: ''
  });

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState({ type: '', text: '' }); // 'success' ou 'error'

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleClear = () => {
    setFormData({
      razaoSocial: '', nomeFantasia: '', email: '', senha: '',
      cnpj: '', dataFundacao: '', telefone: '', website: '',
      rua: '', numero: '', bairro: '', cidade: '', estado: '', cep: ''
    });
    setMessage({ type: '', text: '' });
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setMessage({ type: '', text: '' });
    setLoading(true);

    // 1. Constrói o Payload aninhado conforme CompanyRegisterRequestDTO
    const payload = {
      razaoSocial: formData.razaoSocial,
      nomeFantasia: formData.nomeFantasia,
      cnpj: formData.cnpj.replace(/\D/g, ''), // Remove máscara
      dataFundacao: formData.dataFundacao || null, // API espera YYYY-MM-DD ou null
      urlSite: formData.website, // Mapeado do campo 'website'
      email: formData.email,
      senha: formData.senha,

      // Nested ContactDTO
      contact: {
        email: formData.email,
        telefone: formData.telefone.replace(/\D/g, ''), // Remove máscara
      },

      // Nested EnderecoDTO
      endereco: {
        // Mapeando os campos planos para o EnderecoDTO
        logradouro: formData.rua,
        numero: formData.numero,
        bairro: formData.bairro,
        cidade: formData.cidade,
        estado: formData.estado,
        cep: formData.cep.replace(/\D/g, ''), // Remove máscara
      }
    };

    try {
      // Endpoint POST definido em CompanyController.java
      await axios.post('/api/v1/companies/register', payload);

      // 2. Define a mensagem de sucesso (sem a parte de redirecionamento)
      setMessage({ type: 'success', text: 'Empresa registrada com sucesso!' });

      // 3. Limpa o loading IMEDIATAMENTE (TESTE)
      setLoading(false);

      // 4. Redirecionamento IMEDIATO
      navigate('/');

    } catch (error) {
      console.error("Erro no cadastro:", error);

      // Tratamento de erros
      const errorMsg = error.response?.data?.message || "Ocorreu um erro inesperado no cadastro. Verifique os dados.";

      setMessage({ type: 'error', text: errorMsg });
      setLoading(false); // Volta ao estado normal IMEDIATAMENTE em caso de ERRO
    }
  };

  return (
    <RegisterContainer>
      <Header>
        <IconButton onClick={() => navigate('/register')} style={{ color: 'white', marginRight: '10px' }}>
          <ArrowBack />
        </IconButton>
        <Typography variant="h6">
          Registrar Empresas
        </Typography>
      </Header>

      <FormContainer component="form" onSubmit={handleRegister}>
        {/* Exibição da mensagem de sucesso/erro */}
        {message.text && (
          <Alert severity={message.type} sx={{ mb: 3 }}>
            {message.text}
          </Alert>
        )}

        <Grid container spacing={3}>
          {/* Coluna 1: Dados de Identificação e Contato */}
          <Grid item xs={12} sm={6}>
            <Typography variant="h6" gutterBottom style={{ color: '#1a4314', borderBottom: '1px solid #ddd', paddingBottom: '5px', marginBottom: '15px' }}>
              Dados de Identificação
            </Typography>

            <TextField label="Razão Social" name="razaoSocial" value={formData.razaoSocial} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Nome Fantasia" name="nomeFantasia" value={formData.nomeFantasia} onChange={handleChange} fullWidth margin="normal" required />

            {/* CNPJ com Máscara */}
            <InputMask mask="99.999.999/9999-99" value={formData.cnpj} onChange={handleChange} name="cnpj">
              {(inputProps) => <TextField {...inputProps} label="CNPJ" fullWidth margin="normal" required />}
            </InputMask>

            {/* Data de Fundação com input type="date" */}
            <TextField
              label="Data de fundação"
              name="dataFundacao"
              value={formData.dataFundacao}
              onChange={handleChange}
              fullWidth
              margin="normal"
              type="date"
              InputLabelProps={{ shrink: true }}
            />

            <Typography variant="h6" gutterBottom style={{ color: '#1a4314', borderBottom: '1px solid #ddd', paddingBottom: '5px', marginTop: '30px', marginBottom: '15px' }}>
              Acesso e Contato
            </Typography>
            <TextField label="E-mail" name="email" value={formData.email} onChange={handleChange} fullWidth margin="normal" required type="email" />
            <TextField label="Senha" name="senha" value={formData.senha} onChange={handleChange} fullWidth margin="normal" required type="password" />
            <TextField label="Telefone" name="telefone" value={formData.telefone} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="URL Site" name="website" value={formData.website} onChange={handleChange} fullWidth margin="normal" />

          </Grid>

          {/* Coluna 2: Endereço (Mapeado para EnderecoDTO) */}
          <Grid item xs={12} sm={6}>
            <Typography variant="h6" gutterBottom style={{ color: '#1a4314', borderBottom: '1px solid #ddd', paddingBottom: '5px', marginBottom: '15px' }}>
              Dados de Endereço
            </Typography>

            {/* CEP com Máscara */}
            <InputMask mask="99999-999" value={formData.cep} onChange={handleChange} name="cep">
              {(inputProps) => <TextField {...inputProps} label="CEP" fullWidth margin="normal" required />}
            </InputMask>

            <TextField label="Rua" name="rua" value={formData.rua} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Número" name="numero" value={formData.numero} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Bairro" name="bairro" value={formData.bairro} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Cidade" name="cidade" value={formData.cidade} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Estado" name="estado" value={formData.estado} onChange={handleChange} fullWidth margin="normal" required />

          </Grid>
        </Grid>

        {/* Botões de Ação */}
        <Box display="flex" justifyContent="flex-start" gap={2} mt={4}>
          <Button variant="contained" style={{ backgroundColor: '#e57373', color: 'white' }} onClick={() => navigate('/register')}>
            Cancelar e voltar
          </Button>
          <Button variant="contained" style={{ backgroundColor: '#ffb74d', color: 'white' }} onClick={handleClear}>
            Limpar Campos
          </Button>
          <Button
            type="submit"
            variant="contained"
            style={{ backgroundColor: '#1a4314', color: 'white' }}
            disabled={loading} // Desabilita o botão durante o carregamento
          >
            {loading ? <CircularProgress size={24} color="inherit" /> : 'Salvar'}
          </Button>
        </Box>

        {/* Rodapé interno */}
        <Box mt={4} py={3} sx={{ fontSize: '0.75rem', color: '#555', borderTop: '1px solid #eee' }}>
          <Typography variant="caption" display="block">
            &copy; AgroHub - Projeto Acadêmico
          </Typography>
          <Typography variant="caption" display="block">
            Este é um protótipo acadêmico desenvolvido por quatro estudantes com o objetivo de propor soluções inovadoras para o setor agroalimentar. O AgroHub é uma plataforma em fase de desenvolvimento voltada à criação de um marketplace exclusivo para empresas internas dos centros de abastecimento, facilitando a comercialização de produtos de forma eficiente, segura e integrada. Todos os conteúdos, funcionalidades e propostas aqui apresentados têm **caráter educacional e experimental**, não representando ainda uma operação comercial oficial. Agradecemos o interesse e apoio à nossa iniciativa!
          </Typography>
        </Box>
      </FormContainer>

      {/* Rodapé externo */}
      <Footer>
        copyright AgroHub - 2025
      </Footer>
    </RegisterContainer>
  );
}