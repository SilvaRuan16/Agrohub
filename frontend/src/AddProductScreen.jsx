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

// 🔑 CHAVES DO JSON EM PORTUGUÊS para alinhar com o @JsonProperty do DTO
const initialFormData = {
  nome: '', // Corresponde a name no DTO
  descricao: '', // Corresponde a shortDescription no DTO
  precoVenda: '', // Corresponde a salePrice no DTO
  quantidadeEstoque: '', // Corresponde a initialStock no DTO
  unidadeMedida: '', // CRÍTICO: Corresponde a unitOfMeasurement no DTO
  tipoProdutoId: '', // Corresponde a productTypeId no DTO
  produtor: '', // Corresponde a produtorName no DTO
  linkAdicional: '', // Corresponde a linkAdicional no DTO
  descontoId: '', // Corresponde a discountId no DTO
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
      // 🔑 CORREÇÃO CRÍTICA DO TOKEN: Usa 'authToken' ou 'token' como fallback
      const token = localStorage.getItem('authToken') || localStorage.getItem('token');

      if (!token) {
        setError('Você não está logado. Por favor, faça login novamente.');
        return;
      }

      // 🔑 CORREÇÃO DE TIPOS: Converte strings para números
      const dataToSend = {
        ...formData,
        precoVenda: formData.precoVenda ? parseFloat(formData.precoVenda) : null,
        quantidadeEstoque: formData.quantidadeEstoque ? parseInt(formData.quantidadeEstoque, 10) : null,
        tipoProdutoId: formData.tipoProdutoId ? parseInt(formData.tipoProdutoId, 10) : null,
        descontoId: formData.descontoId ? parseInt(formData.descontoId, 10) : null,
      };

      const response = await axios.post(
        API_ADD_PRODUCT_URL,
        dataToSend, // Envia o objeto corrigido com nomes e tipos certos
        {
          headers: {
            Authorization: `Bearer ${token}`, // Envia o token para o backend
            'Content-Type': 'application/json',
          }
        }
      );

      if (response.status === 201) {
        setSuccess('Produto adicionado com sucesso!');
        handleClear(); // Limpa o formulário

        // 🎯 AQUI É O PONTO CRÍTICO: Redirecionamento após 2 segundos
        setTimeout(() => navigate('/company/dashboard'), 2000);
      }
    } catch (err) {
      console.error('Erro na operação de produto:', err);

      let errorMessage = 'Falha ao adicionar produto. Verifique sua conexão ou o servidor.';
      if (err.response) {
        if (err.response.status === 403) {
          errorMessage = 'Acesso negado. Sua conta não tem permissão para adicionar produtos. (Verifique o token e a ROLE)';
        } else if (err.response.data && err.response.data.message) {
          errorMessage = err.response.data.message;
        } else if (err.response.status === 400) {
          errorMessage = 'Dados inválidos. Por favor, verifique se todos os campos estão preenchidos corretamente.';
        }
      }
      setError(errorMessage);
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
              name="nome" // Chave em português
              value={formData.nome}
              onChange={handleChange}
              required
            />
          </Grid>

          {/* Tipo do Produto */}
          <Grid item xs={12} sm={6}>
            <FormControl fullWidth required variant="outlined" margin="normal">
              <InputLabel>Tipo do Produto</InputLabel>
              <Select label="Tipo do Produto" name="tipoProdutoId" value={formData.tipoProdutoId} onChange={handleChange}>
                <MenuItem value="">Selecione o Tipo</MenuItem>
                <MenuItem value="1">Grãos</MenuItem>
                <MenuItem value="2">Frutas</MenuItem>
                <MenuItem value="3">Verduras</MenuItem>
                <MenuItem value="4">Animais</MenuItem>
              </Select>
            </FormControl>
          </Grid>

          {/* Preço */}
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="Preço (R$)"
              name="precoVenda" // Chave em português
              type="number"
              value={formData.precoVenda}
              onChange={handleChange}
              required
            />
          </Grid>

          {/* Estoque */}
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="Estoque (Unidades)"
              name="quantidadeEstoque" // Chave em português
              type="number"
              value={formData.quantidadeEstoque}
              onChange={handleChange}
              required
            />
          </Grid>

          {/* Unidade de Medida (CRÍTICO) */}
          <Grid item xs={12} sm={4}>
            <FormControl fullWidth required variant="outlined">
              <InputLabel>Unidade de Medida</InputLabel>
              <Select
                label="Unidade de Medida"
                name="unidadeMedida" // Chave em português
                value={formData.unidadeMedida}
                onChange={handleChange}
              >
                <MenuItem value="">Selecione</MenuItem>
                <MenuItem value="KG">Quilograma (KG)</MenuItem>
                <MenuItem value="UN">Unidade (UN)</MenuItem>
                <MenuItem value="LT">Litro (LT)</MenuItem>
                <MenuItem value="SC">Saca</MenuItem>
              </Select>
            </FormControl>
          </Grid>

          {/* Desconto (Opcional) */}
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="Desconto (%)"
              name="descontoId" // Chave em português
              type="number"
              value={formData.descontoId}
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
              name="descricao" // Chave em português
              value={formData.descricao}
              onChange={handleChange}
              required
            />
          </Grid>

          {/* Informação Adicional (Produtor) */}
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Produtor / Informação Adicional (Ex: Certificação)"
              name="produtor" // Chave em português
              value={formData.produtor}
              onChange={handleChange}
            />
          </Grid>

          {/* Link (Opcional) */}
          <Grid item xs={12} sm={6}>
            <FormControl fullWidth variant="outlined" margin="normal">
              <InputLabel>Link de Vídeo/Documentação (Opcional)</InputLabel>
              <Select label="Link de Vídeo/Documentação (Opcional)" name="linkAdicional" value={formData.linkAdicional} onChange={handleChange}>
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