import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { TextField, Button, Typography, Box, Select, MenuItem, FormControl, InputLabel } from '@mui/material';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import InputMask from 'react-input-mask';
import styled from 'styled-components';
import axios from 'axios';

// --- Styled Components CORRIGIDOS ---

const GlobalContainer = styled(Box)`
  display: flex;
  min-height: 100vh;
  flex-direction: column;
  background-color: #f5f5f5;
`;

const ContentArea = styled(Box)`
  display: flex;
  flex-grow: 1; /* Permite que ocupe todo o espaço restante */
  /* Remove a altura fixa para evitar conflitos */
  
  @media (max-width: 900px) {
    flex-direction: column;
    align-items: center;
  }
`;

const LeftPanel = styled(Box)`
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #fcfcfc;
  padding: 40px;
  /* Garante que o painel esquerdo ocupe a altura total do ContentArea */
  min-height: 100%; 

  @media (max-width: 900px) {
    min-height: 250px;
    padding: 20px;
    width: 100%;
    order: 2;
  }
`;

const RightPanel = styled(Box)`
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 20px;
  background-color: #ffffff;
  /* Garante que o painel direito ocupe a altura total do ContentArea */
  min-height: 100%; 

  @media (max-width: 900px) {
    padding: 20px;
    width: 100%;
    order: 1;
  }
`;

const LoginFormContainer = styled(Box)`
  padding: 40px 30px;
  width: 100%;
  max-width: 400px;
  background-color: #fff9f0;
  border-radius: 8px;
`;

const Footer = styled(Box)`
  background-color: #1a4314;
  color: #c8e6c9;
  padding: 30px 40px;
  font-size: 0.8rem;
  text-align: center;
  /* Garante que o footer tenha sempre a mesma altura para o cálculo do ContentArea */
  flex-shrink: 0;
`;

const AgroHubLogo = styled.div`
  width: 200px;
  height: auto;
  /* Simulação da Logo */
  img {
    max-width: 100%;
    height: auto;
  }
`;

// --- Constantes e Lógica (Inalteradas) ---

const API_BASE_URL = 'http://localhost:8080/api/v1/auth';

export default function LoginScreen() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    cpfCnpj: '',
    userType: '',
    password: '',
  });
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const getMask = (value) => {
    const digits = value.replace(/\D/g, '');
    if (digits.length <= 11) {
      return '999.999.999-99';
    }
    return '99.999.999/9999-99';
  };

  const handleChange = (event) => {
    const name = event.target.name || event.target.id;
    const value = event.target.value;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    if (error) setError('');
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    setLoading(true);

    const loginPayload = {
      identifier: formData.cpfCnpj.replace(/\D/g, ''),
      userType: formData.userType,
      password: formData.password,
    };

    try {
      const response = await axios.post(`${API_BASE_URL}/login`, loginPayload);

      console.log('Login bem-sucedido:', response.data);
      navigate('/dashboard'); 

    } catch (err) {
      let errorMessage = 'Erro ao conectar com a API. Tente novamente.';
      if (err.response && err.response.data) {
        errorMessage = err.response.data.message || 'Credenciais inválidas ou erro desconhecido.';
      }
      console.error('Erro de Login:', err);
      setError(errorMessage);

    } finally {
      setLoading(false);
    }
  };

  return (
    <GlobalContainer>
      <ContentArea>
        {/* Painel Esquerdo: Logo */}
        <LeftPanel>
          <AgroHubLogo>
            <Typography variant="h3" style={{ color: '#1a4314', fontWeight: 'bold' }}>AGRO</Typography>
            <Typography variant="h3" style={{ color: '#1a4314', fontWeight: 'bold' }}>HUB</Typography>
            {/* Insira sua imagem real da logo aqui */}
          </AgroHubLogo>
        </LeftPanel>

        {/* Painel Direito: Formulário de Login */}
        <RightPanel>
          <LoginFormContainer component="form" onSubmit={handleSubmit}>
            <Typography variant="h5" gutterBottom align="center" style={{ color: '#1a4314', marginBottom: '20px' }}>
              Login
            </Typography>

            {/* Campos do Formulário */}
            <FormControl fullWidth margin="normal" required>
              <InputMask
                mask={getMask(formData.cpfCnpj)}
                value={formData.cpfCnpj}
                onChange={handleChange}
                name="cpfCnpj"
                maskChar={null}
              >
                {(inputProps) => (
                  <TextField
                    {...inputProps}
                    label="CPF/CNPJ"
                    variant="outlined"
                    fullWidth
                    inputProps={{ maxLength: 18 }}
                  />
                )}
              </InputMask>
            </FormControl>

            <FormControl fullWidth margin="normal" required variant="outlined">
              <InputLabel id="user-type-label">Tipo de Usuário</InputLabel>
              <Select
                labelId="user-type-label"
                id="user-type"
                value={formData.userType}
                onChange={handleChange}
                label="Tipo de Usuário"
                name="userType"
              >
                <MenuItem value="">
                  <em>Selecione um usuário</em>
                </MenuItem>
                <MenuItem value={'cliente'}>Cliente</MenuItem>
                <MenuItem value={'empresa'}>Empresa</MenuItem>
              </Select>
            </FormControl>

            <TextField
              label="Senha"
              variant="outlined"
              fullWidth
              margin="normal"
              required
              type={showPassword ? 'text' : 'password'}
              name="password"
              value={formData.password}
              onChange={handleChange}
              InputProps={{
                endAdornment: (
                  <Button
                    onClick={() => setShowPassword(!showPassword)}
                    tabIndex={-1} 
                    aria-label={showPassword ? 'Esconder senha' : 'Mostrar senha'}
                    style={{ minWidth: 'auto', padding: '5px' }}
                  >
                    {showPassword ? <VisibilityOff /> : <Visibility />}
                  </Button>
                ),
              }}
            />

            {error && (
              <Typography color="error" variant="body2" align="center" style={{ marginTop: '10px' }}>
                {error}
              </Typography>
            )}

            <Button
              type="submit"
              fullWidth
              variant="contained"
              disabled={loading}
              style={{
                marginTop: '20px',
                padding: '10px 0',
                backgroundColor: '#1a4314',
                fontSize: '1rem',
              }}
            >
              {loading ? 'Entrando...' : 'Entrar'}
            </Button>

            {/* Links Adicionais */}
            <Box textAlign="center" marginTop="15px">
              <Button color="primary" onClick={() => navigate('/forgot-password')} style={{ textTransform: 'none', color: '#1a4314' }}>
                Esqueci a senha
              </Button>
            </Box>

            <Box textAlign="center" marginTop="25px">
              <Typography variant="body2" style={{ color: '#555' }}>
        Não possui uma conta?{' '}
        <Button 
            onClick={() => navigate('/register')} 
            style={{ 
                color: '#1a4314', 
                textDecoration: 'none', 
                fontWeight: 'bold', 
                padding: '0 5px', // Ajusta o padding do botão
                minWidth: 'auto',
                textTransform: 'none' // Remove o texto em caixa alta padrão do Button
            }}
        >
            Criar conta
        </Button>
    </Typography>
            </Box>
          </LoginFormContainer>
        </RightPanel>
      </ContentArea>

      {/* Rodapé Global */}
      <Footer>
        <Typography variant="caption" display="block">
            &copy; AgroHub - Projeto Acadêmico
        </Typography>
        <Typography variant="caption" display="block" style={{ marginTop: '5px' }}>
            copyright AgroHub - 2025
        </Typography>
      </Footer>
    </GlobalContainer>
  );
}