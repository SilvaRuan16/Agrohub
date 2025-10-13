import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Typography, Button } from '@mui/material';
import styled from 'styled-components';

// --- Styled Components ---
const SelectionContainer = styled(Box)`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #fcfcfc;
`;

const CardContainer = styled(Box)`
  display: flex;
  gap: 30px;
  max-width: 800px;
  width: 90%;
  padding: 20px;

  @media (max-width: 600px) {
    flex-direction: column;
    gap: 15px;
  }
`;

const SelectionCard = styled(Box)`
  flex: 1;
  background-color: #fff;
  padding: 40px 30px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  text-align: center;
  transition: transform 0.2s;
  cursor: pointer;
  
  &:hover {
    transform: translateY(-5px);
  }
`;

const SelectButton = styled(Button)`
  && { /* Sobrescreve o estilo MUI */
    margin-top: 20px;
    background-color: #1a4314; /* Cor escura do tema */
    &:hover {
      background-color: #2e7d32;
    }
  }
`;

// --- Componente Principal ---

export default function AccountSelectionScreen() {
  const navigate = useNavigate();

  const handleSelectType = (type) => {
    // Redireciona para a rota correta
    navigate(`/register/${type}`);
  };

  return (
    <SelectionContainer>
      <Typography variant="h4" gutterBottom style={{ color: '#1a4314', marginBottom: '40px' }}>
        Como você deseja se registrar?
      </Typography>

      <CardContainer>
        {/* Cartão Cliente */}
        <SelectionCard onClick={() => handleSelectType('client')}>
          <Typography variant="h6" style={{ color: '#1a4314', marginBottom: '10px' }}>
            Sou um Cliente
          </Typography>
          <Typography variant="body1" color="textSecondary">
            Cadastre-se como Pessoa Física.
          </Typography>
          <SelectButton variant="contained" fullWidth>
            Cadastro de Cliente
          </SelectButton>
        </SelectionCard>

        {/* Cartão Empresa */}
        <SelectionCard onClick={() => handleSelectType('company')}>
          <Typography variant="h6" style={{ color: '#1a4314', marginBottom: '10px' }}>
            Sou uma Empresa
          </Typography>
          <Typography variant="body1" color="textSecondary">
            Cadastre-se como Pessoa Jurídica.
          </Typography>
          <SelectButton variant="contained" fullWidth>
            Cadastro de Empresa
          </SelectButton>
        </SelectionCard>
      </CardContainer>
      
      {/* Link para voltar ao Login */}
      <Box mt={4}>
        <Button onClick={() => navigate('/')} style={{ color: '#1a4314', textTransform: 'none' }}>
          Voltar ao Login
        </Button>
      </Box>

    </SelectionContainer>
  );
}