import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Typography, TextField, Button, Grid, Select, MenuItem, FormControl, InputLabel, TextareaAutosize, Alert } from '@mui/material';
import styled from 'styled-components';
import axios from 'axios';
import CompanyHeader from './CompanyHeader';

// --- Styled Components (Reutilizados) ---

const RegisterContainer = styled(Box)`
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f5f5f5;
`;

const FormContainer = styled(Box)`
  flex-grow: 1;
  padding: 20px 40px;
  background-color: #fcfcfc;
  
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
`;

// Rota corrigida para o padrão RESTful (POST /api/v1/products)
const API_ADD_PRODUCT_URL = 'http://localhost:8080/api/v1/products';

const initialFormData = {
  name: '',
  description: '',
  price: '',
  stock: '',
  productType: '',
  additionalInfo: '',
  link: '',
  discount: '',
};

export default function AddProductScreen() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState(initialFormData);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleClear = () => {
    setFormData(initialFormData);
    setError('');
    setSuccess('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      // 🔑 CORREÇÃO CRÍTICA: Obtém o token JWT e o anexa ao cabeçalho Authorization
      const token = localStorage.getItem('token');

      if (!token) {
        setError('Você não está logado. Por favor, faça login novamente.');
        return;
      }

      const response = await axios.post(
        API_ADD_PRODUCT_URL,
        formData,
        {
          headers: {
            Authorization: `Bearer ${token}`, // Envia o token para o backend
            'Content-Type': 'application/json',
          }
        }
      );

      if (response.status === 201) {
        setSuccess('Produto adicionado com sucesso!');
        handleClear();
        // Opcional: navegar para o dashboard após sucesso
        setTimeout(() => navigate('/dashboard'), 2000);
      }
    } catch (err) {
      console.error('Erro na operação de produto:', err);
      // Trata erros de validação (400) ou segurança (403)
      if (err.response && err.response.status === 403) {
        setError('Acesso negado. Sua conta não tem permissão para adicionar produtos. (Verifique o token e a ROLE)');
      } else if (err.response && err.response.data && err.response.data.message) {
        setError(err.response.data.message); // Exibe mensagem de erro do backend
      } else {
        setError('Falha ao adicionar produto. Verifique sua conexão ou o servidor.');
      }
    }
  };

  return (
    <RegisterContainer>
      <CompanyHeader />
      <FormContainer component="form" onSubmit={handleSubmit}>
        <Typography variant="h4" gutterBottom style={{ color: '#1a4314', marginTop: '10px' }}>
          Cadastro de Novo Produto
        </Typography>
        <Typography variant="subtitle1" gutterBottom style={{ color: '#555' }}>
          Preencha os campos abaixo para listar seu produto no catálogo.
        </Typography>

        {/* Alerts de Erro/Sucesso */}
        {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
        {success && <Alert severity="success" sx={{ mt: 2 }}>{success}</Alert>}

        <Grid container spacing={3} mt={2}>
          {/* Nome do Produto */}
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Nome do Produto"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </Grid>

          {/* Tipo do Produto */}
          <Grid item xs={12} sm={6}>
            <FormControl fullWidth required variant="outlined" margin="normal">
              <InputLabel>Tipo do Produto</InputLabel>
              <Select label="Tipo do Produto" name="productType" value={formData.productType} onChange={handleChange}>
                <MenuItem value="">Selecione o Tipo</MenuItem>
                <MenuItem value="GRAOS">Grãos</MenuItem>
                <MenuItem value="FRUTAS">Frutas</MenuItem>
                <MenuItem value="VERDURAS">Verduras</MenuItem>
                <MenuItem value="ANIMAIS">Animais</MenuItem>
              </Select>
            </FormControl>
          </Grid>

          {/* Preço */}
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="Preço (R$)"
              name="price"
              type="number"
              value={formData.price}
              onChange={handleChange}
              required
            />
          </Grid>

          {/* Estoque */}
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="Estoque (Unidades)"
              name="stock"
              type="number"
              value={formData.stock}
              onChange={handleChange}
              required
            />
          </Grid>

          {/* Desconto (Opcional) */}
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="Desconto (%)"
              name="discount"
              type="number"
              value={formData.discount}
              onChange={handleChange}
            />
          </Grid>

          {/* Descrição */}
          <Grid item xs={12}>
            <Typography variant="subtitle2" gutterBottom mt={1}>Descrição Detalhada</Typography>
            <TextareaAutosize
              minRows={5}
              placeholder="Descreva seu produto, benefícios e especificações..."
              style={{ width: '100%', padding: '10px', borderColor: '#ccc', borderRadius: '4px' }}
              name="description"
              value={formData.description}
              onChange={handleChange}
              required
            />
          </Grid>

          {/* Informação Adicional (Opcional) */}
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Informação Adicional (Ex: Certificação)"
              name="additionalInfo"
              value={formData.additionalInfo}
              onChange={handleChange}
            />
          </Grid>

          {/* Link (Opcional) */}
          <Grid item xs={12} sm={6}>
            <FormControl fullWidth variant="outlined" margin="normal">
              <InputLabel>Link de Vídeo/Documentação (Opcional)</InputLabel>
              <Select label="Link de Vídeo/Documentação (Opcional)" name="link" value={formData.link} onChange={handleChange}>
                <MenuItem value="">Nenhum Link</MenuItem>
                <MenuItem value="1">Link de Exemplo 1</MenuItem>
                <MenuItem value="2">Link de Exemplo 2</MenuItem>
              </Select>
            </FormControl>
          </Grid>
        </Grid>

        {/* Botões de Ação */}
        <Box display="flex" justifyContent="flex-start" gap={2} mt={4}>
          <Button variant="contained" style={{ backgroundColor: '#e57373', color: 'white' }} onClick={() => navigate('/dashboard')}>
            Cancelar e voltar
          </Button>
          <Button variant="contained" style={{ backgroundColor: '#ffb74d', color: 'white' }} onClick={handleClear}>
            Limpar Campos
          </Button>
          <Button type="submit" variant="contained" style={{ backgroundColor: '#1a4314', color: 'white' }}>
            Salvar
          </Button>
        </Box>

        {/* Rodapé interno com texto do projeto */}
        <Box mt={4} py={3} sx={{ fontSize: '0.75rem', color: '#555', borderTop: '1px solid #eee' }}>
          <Typography variant="caption" display="block">
            &copy; AgroHub - Projeto Acadêmico
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