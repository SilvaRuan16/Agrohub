import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Typography, TextField, Button, Grid, IconButton } from '@mui/material';
import { ArrowBack } from '@mui/icons-material';
import styled from 'styled-components';
import InputMask from 'react-input-mask';

// --- Styled Components (Reutilizando a base escura) ---

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

// --- Componente Principal ---

export default function RegisterCompanyScreen() {
  const navigate = useNavigate();
  // Estado inicial simulando os campos da imagem
  const [formData, setFormData] = useState({
    razaoSocial: '', nomeFantasia: '', email: '', senha: '',
    cnpj: '', dataFundacao: '', telefone: '', redeSocial: '',
    imagem: '', website: '', rua: '', numero: '',
    bairro: '', cidade: '', estado: '', cep: '',
    nomeRep: '', rgRep: '', cpfRep: '', cargoRep: '',
    telRep: '', emailRep: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleRegister = (e) => {
    e.preventDefault();
    console.log('Dados da Empresa para API:', formData);
    // TODO: Implementar envio POST para a API Spring Boot aqui
    alert('Cadastro de Empresa em andamento! Verifique o console.');
    // navigate('/login-success'); // Opcional: Redirecionar após sucesso
  };
  
  const handleClear = () => {
      setFormData({
          razaoSocial: '', nomeFantasia: '', email: '', senha: '',
          cnpj: '', dataFundacao: '', telefone: '', redeSocial: '',
          imagem: '', website: '', rua: '', numero: '',
          bairro: '', cidade: '', estado: '', cep: '',
          nomeRep: '', rgRep: '', cpfRep: '', cargoRep: '',
          telRep: '', emailRep: ''
      });
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
        <Grid container spacing={3}>
          {/* Coluna 1: Dados da Empresa */}
          <Grid item xs={12} sm={6}>
            <Typography variant="h6" gutterBottom style={{ color: '#1a4314', borderBottom: '1px solid #ddd', paddingBottom: '5px', marginBottom: '15px' }}>
              Dados da Empresa
            </Typography>
            <TextField label="Razão Social" name="razaoSocial" value={formData.razaoSocial} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="E-mail" name="email" value={formData.email} onChange={handleChange} fullWidth margin="normal" required type="email" />
            
            {/* CNPJ com Máscara */}
            <InputMask mask="99.999.999/9999-99" value={formData.cnpj} onChange={handleChange} name="cnpj">
              {(inputProps) => <TextField {...inputProps} label="CNPJ" fullWidth margin="normal" required />}
            </InputMask>
            
            <TextField label="Telefone" name="telefone" value={formData.telefone} onChange={handleChange} fullWidth margin="normal" />
            <Box display="flex" alignItems="center" my={2}>
                <Button variant="outlined" component="label">
                    Selecionar imagem
                    <input type="file" hidden onChange={handleChange} name="imagem" />
                </Button>
                <Button variant="outlined" style={{ marginLeft: '10px' }}>Visualizar imagem</Button>
            </Box>
            <TextField label="URL Site" name="website" value={formData.website} onChange={handleChange} fullWidth margin="normal" />
            <TextField label="Rua" name="rua" value={formData.rua} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Número" name="numero" value={formData.numero} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Cep" name="cep" value={formData.cep} onChange={handleChange} fullWidth margin="normal" required />
            
            {/* Dados do Representante */}
            <Typography variant="h6" gutterBottom style={{ color: '#1a4314', borderBottom: '1px solid #ddd', paddingBottom: '5px', marginTop: '30px', marginBottom: '15px' }}>
              Dados do Representante
            </Typography>
            <TextField label="Cpf do representante" name="cpfRep" value={formData.cpfRep} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Cargo do representante" name="cargoRep" value={formData.cargoRep} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="E-mail do representante" name="emailRep" value={formData.emailRep} onChange={handleChange} fullWidth margin="normal" required type="email" />
          </Grid>

          {/* Coluna 2: Dados Secundários e Endereço */}
          <Grid item xs={12} sm={6}>
            <Typography variant="h6" gutterBottom style={{ color: '#fff' }}>{' '}</Typography> {/* Espaço para alinhar com o título da Coluna 1 */}
            <TextField label="Nome Fantasia" name="nomeFantasia" value={formData.nomeFantasia} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Senha" name="senha" value={formData.senha} onChange={handleChange} fullWidth margin="normal" required type="password" />
            <TextField label="Data de fundação" name="dataFundacao" value={formData.dataFundacao} onChange={handleChange} fullWidth margin="normal" />
            <TextField label="Rede Social (Opcional)" name="redeSocial" value={formData.redeSocial} onChange={handleChange} fullWidth margin="normal" />
            
            <Typography variant="h6" gutterBottom style={{ color: '#fff' }}>{' '}</Typography> {/* Espaço para alinhar a imagem */}

            <TextField label="Bairro" name="bairro" value={formData.bairro} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Cidade" name="cidade" value={formData.cidade} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Estado" name="estado" value={formData.estado} onChange={handleChange} fullWidth margin="normal" required />
            
            <Typography variant="h6" gutterBottom style={{ color: '#fff' }}>{' '}</Typography> {/* Espaço para alinhamento */}
            
            <TextField label="Nome Completo" name="nomeRep" value={formData.nomeRep} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Rg do representante" name="rgRep" value={formData.rgRep} onChange={handleChange} fullWidth margin="normal" required />
            <TextField label="Telefone do representante" name="telRep" value={formData.telRep} onChange={handleChange} fullWidth margin="normal" required />
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
          <Button type="submit" variant="contained" style={{ backgroundColor: '#1a4314', color: 'white' }}>
            Salvar
          </Button>
        </Box>
        
        {/* Rodapé interno com texto do projeto */}
        <Box mt={4} py={3} sx={{ fontSize: '0.75rem', color: '#555', borderTop: '1px solid #eee' }}>
            <Typography variant="caption" display="block">
                &copy; AgroHub - Projeto Acadêmico
            </Typography>
            <Typography variant="caption" display="block">
                Este é um protótipo acadêmico desenvolvido por quatro estudantes com o objetivo de propor soluções inovadoras para o setor agroalimentar. O AgroHub é uma plataforma em fase de desenvolvimento voltada à criação de um marketplace exclusivo para empresas internas dos centros de abastecimento, facilitando a comercialização de produtos de forma eficiente, segura e integrada. Todos os conteúdos, funcionalidades e propostas aqui apresentados têm **caráter educacional e experimental**, não representando ainda uma operação comercial oficial. Agradecemos o interesse e apoio à nossa iniciativa!
            </Typography>
        </Box>
      </FormContainer>

      {/* Rodapé externo (opcional, se quiser manter o padrão) */}
      <Footer>
        copyright AgroHub - 2025
      </Footer>
    </RegisterContainer>
  );
}