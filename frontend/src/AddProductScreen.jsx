import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Typography, TextField, Button, Grid, Select, MenuItem, FormControl, InputLabel, TextareaAutosize } from '@mui/material';
import styled from 'styled-components';
import axios from 'axios';
import CompanyHeader from './CompanyHeader'; // Importação do novo cabeçalho

// --- Styled Components (Reutilizados) ---
// Removido o Styled 'Header' simples, pois usaremos o CompanyHeader
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

const API_ADD_PRODUCT_URL = 'http://localhost:8080/api/v1/product/add';

// --- Componente Principal ---

export default function AddProductScreen({ isEdit = false }) {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: '', internalCode: '', category: '', priceCompra: '',
    priceVenda: '', priceMinVenda: '', maxDiscount: '', quantityEstoque: '',
    quantityMinEstoque: '', margemLucro: '', formaPagamento: '', description: '',
    produtor: '', municipio: '', cnpj: '', tipoProduto: '',
    rua: '', numero: '', bairro: '', cidade: '', estado: '', cep: '',
    telefone: '', email: '', redeSocial: '', website: '', link: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('Dados do Produto para API:', formData);
    
    try {
        const url = isEdit ? `${API_ADD_PRODUCT_URL}/${'id_produto'}` : API_ADD_PRODUCT_URL;
        const method = isEdit ? axios.put : axios.post;

        const response = await method(url, formData);

        console.log('Operação de Produto bem-sucedida:', response.data);
        alert(isEdit ? 'Produto editado com sucesso!' : 'Produto cadastrado com sucesso!');
        navigate('/dashboard'); 

    } catch (error) {
        console.error('Erro na operação de produto:', error);
        alert('Falha na operação. Verifique o console para mais detalhes.');
    }
  };
  
  const handleClear = () => {
    setFormData({
        name: '', internalCode: '', category: '', priceCompra: '',
        priceVenda: '', priceMinVenda: '', maxDiscount: '', quantityEstoque: '',
        quantityMinEstoque: '', margemLucro: '', formaPagamento: '', description: '',
        produtor: '', municipio: '', cnpj: '', tipoProduto: '',
        rua: '', numero: '', bairro: '', cidade: '', estado: '', cep: '',
        telefone: '', email: '', redeSocial: '', website: '', link: ''
    });
  };

  return (
    <RegisterContainer>
      {/* NOVO: Usando o CompanyHeader para navegação */}
      <CompanyHeader /> 

      <FormContainer component="form" onSubmit={handleSubmit}>
        <Typography variant="h5" gutterBottom style={{ color: '#1a4314', marginBottom: '20px' }}>
          Informações sobre o produto
        </Typography>

        <Grid container spacing={3}>
          {/* BLOCO 1: Informações Básicas e Preços */}
          <Grid item xs={12} sm={6}>
            <TextField label="Nome do produto" name="name" value={formData.name} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Preço de compra" name="priceCompra" value={formData.priceCompra} onChange={handleChange} fullWidth margin="normal" />
            <TextField label="Preço mín de venda" name="priceMinVenda" value={formData.priceMinVenda} onChange={handleChange} fullWidth margin="normal" />
            <TextField label="Quantidade estoque" name="quantityEstoque" value={formData.quantityEstoque} onChange={handleChange} fullWidth margin="normal" />
            
            <Box display="flex" alignItems="center" my={2} gap={1}>
                <Button variant="outlined" component="label">
                    Escolher Arquivo (Imagem)
                    <input type="file" hidden onChange={handleChange} name="imagem" />
                </Button>
                <Button variant="outlined" style={{ flexGrow: 1 }}>Visualizar imagem</Button>
            </Box>
          </Grid>

          {/* BLOCO 2: Códigos, Margem e Desconto */}
          <Grid item xs={12} sm={6}>
            <TextField label="Código interno" name="internalCode" value={formData.internalCode} onChange={handleChange} fullWidth margin="normal" />
            <FormControl fullWidth margin="normal">
                <InputLabel>Categoria</InputLabel>
                <Select label="Categoria" name="category" value={formData.category} onChange={handleChange}>
                    <MenuItem value="">Selecione</MenuItem>
                    <MenuItem value="raiz">Raízes</MenuItem>
                    <MenuItem value="graos">Grãos</MenuItem>
                </Select>
            </FormControl>
            <TextField label="Preço de venda" name="priceVenda" value={formData.priceVenda} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Margem lucro (%)" name="margemLucro" value={formData.margemLucro} onChange={handleChange} fullWidth margin="normal" />
            <TextField label="Quantidade mín em estoque" name="quantityMinEstoque" value={formData.quantityMinEstoque} onChange={handleChange} fullWidth margin="normal" />
            
            <TextField label="Valor máx desconto (%)" name="maxDiscount" value={formData.maxDiscount} onChange={handleChange} fullWidth margin="normal" />
            <Button variant="contained" fullWidth style={{ backgroundColor: '#ffb74d', marginTop: '10px' }}>Aplicar desconto dinâmico</Button>
          </Grid>
        </Grid>

        {/* BLOCO 3: Descrição e Forma de Pagamento */}
        <Box my={3}>
            <FormControl fullWidth margin="normal">
                <InputLabel>Forma de pagamento</InputLabel>
                <Select label="Forma de pagamento" name="formaPagamento" value={formData.formaPagamento} onChange={handleChange}>
                    <MenuItem value="">Selecione Forma de pagamento</MenuItem>
                    <MenuItem value="pix">Pix</MenuItem>
                    <MenuItem value="boleto">Boleto</MenuItem>
                </Select>
            </FormControl>
            <Typography variant="subtitle1" sx={{ mt: 2, mb: 1 }}>Descrição</Typography>
            <TextareaAutosize
                minRows={5}
                placeholder="Descrição geral do produto"
                name="description"
                value={formData.description}
                onChange={handleChange}
                style={{ width: '100%', padding: '10px', border: '1px solid #ccc', borderRadius: '4px' }}
            />
        </Box>
        
        {/* BLOCO 4: Informações Adicionais (Opcional) */}
        <Typography variant="h6" gutterBottom style={{ color: '#1a4314', borderBottom: '1px solid #ddd', paddingBottom: '5px', marginTop: '20px', marginBottom: '15px' }}>
          Informações Adicionais (Opcional)
        </Typography>
        <Grid container spacing={3}>
            <Grid item xs={12} sm={6}>
                <TextField label="Produtor" name="produtor" value={formData.produtor} onChange={handleChange} fullWidth margin="normal" />
                <TextField label="Cnpj" name="cnpj" value={formData.cnpj} onChange={handleChange} fullWidth margin="normal" />
                <TextField label="Rua" name="rua" value={formData.rua} onChange={handleChange} fullWidth margin="normal" />
                <TextField label="Bairro" name="bairro" value={formData.bairro} onChange={handleChange} fullWidth margin="normal" />
                <TextField label="Estado" name="estado" value={formData.estado} onChange={handleChange} fullWidth margin="normal" />
                <TextField label="Telefone" name="telefone" value={formData.telefone} onChange={handleChange} fullWidth margin="normal" />
                <TextField label="Rede Social" name="redeSocial" value={formData.redeSocial} onChange={handleChange} fullWidth margin="normal" />
                <TextField label="Preço de compra" name="priceCompra" value={formData.priceCompra} onChange={handleChange} fullWidth margin="normal" />
            </Grid>
            <Grid item xs={12} sm={6}>
                <TextField label="Município" name="municipio" value={formData.municipio} onChange={handleChange} fullWidth margin="normal" />
                <TextField label="Tipo do produto" name="tipoProduto" value={formData.tipoProduto} onChange={handleChange} fullWidth margin="normal" />
                <TextField label="Número" name="numero" value={formData.numero} onChange={handleChange} fullWidth margin="normal" />
                <TextField label="Cidade" name="cidade" value={formData.cidade} onChange={handleChange} fullWidth margin="normal" />
                <TextField label="Cep" name="cep" value={formData.cep} onChange={handleChange} fullWidth margin="normal" />
                <TextField label="Email" name="email" value={formData.email} onChange={handleChange} fullWidth margin="normal" />
                <TextField label="Código interno" name="internalCode" value={formData.internalCode} onChange={handleChange} fullWidth margin="normal" />
                <FormControl fullWidth margin="normal">
                    <InputLabel>Selecionar</InputLabel>
                    <Select label="Selecionar" name="link" value={formData.link} onChange={handleChange}>
                        <MenuItem value="">Link</MenuItem>
                        <MenuItem value="1">Link 1</MenuItem>
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