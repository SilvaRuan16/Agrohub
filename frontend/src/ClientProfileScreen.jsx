import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Typography, Paper, IconButton, List, Divider } from '@mui/material'; 
import { ArrowBack } from '@mui/icons-material';
import styled from 'styled-components';

// --- Mocks para Simulação ---

const clientDataMock = {
    nome: 'Carlos Winer Amorim',
    razaoSocial: 'Agro Hub Soluções LTDA', // Exemplo de nome da empresa (se for PJ)
    nomeFantasia: 'Agro Hub',
    telefone: '(27) 43205-4312',
    email: 'carlos.w.amorim@gmail.com',
    dataCriacao: '14/08/1999', // Data de cadastro/criação da conta
    logoSrc: 'https://via.placeholder.com/100x100?text=AGRO+HUB', // Logo
};

const historyMock = [
    { id: 1, item: 'Café saca', quantity: 4, price: 'R$ 142,78', date: '11/09/2022' },
    { id: 2, item: 'Mandioca tipo A', quantity: 100, price: 'R$ 1.590,00', date: '25/09/2022' },
    { id: 3, item: 'Semente de Milho', quantity: 2, price: 'R$ 500,00', date: '01/10/2022' },
    { id: 4, item: 'Adubo NPK', quantity: 5, price: 'R$ 800,00', date: '05/10/2022' },
    { id: 5, item: 'Trigo Fardo', quantity: 50, price: 'R$ 950,00', date: '10/10/2022' },
];

// --- Styled Components ---

const ProfileContainer = styled(Box)`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  min-height: calc(100vh - 100px); 
  background-color: #f5f5f5;
`;

const ContentWrapper = styled(Box)`
  max-width: 600px;
  width: 100%;
  margin-top: 20px;
`;

const BackButtonRow = styled(Box)`
    align-self: flex-start;
    padding-left: 20px;
    margin-bottom: 20px;
`;

const LogoDisplay = styled(Box)`
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
`;

const ProfileCard = styled(Paper)`
  background-color: #1a4314; /* Cor escura do protótipo */
  color: #ffffff;
  padding: 15px 25px;
  margin-bottom: 30px;
  border-radius: 8px;
`;

const ProfileItem = styled(Box)`
  display: flex;
  justify-content: space-between;
  padding: 5px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  &:last-child {
    border-bottom: none;
  }
`;

const HistoryTitle = styled(Typography)`
  && {
    font-weight: bold;
    color: #1a4314;
    text-align: center;
    margin: 20px 0;
  }
`;

const HistoryContainer = styled(Box)`
  border: 1px solid #ddd;
  border-radius: 8px;
  max-height: 400px; 
  overflow-y: auto;
`;

const HistoryCard = styled(Paper)`
  background-color: #1a4314;
  color: #ffffff;
  padding: 15px 20px;
  margin: 0;
  border-radius: 0;
  &:not(:last-child) {
    border-bottom: 4px solid #f5f5f5; /* Separador claro entre cards */
  }
`;

const HistoryItem = styled(Box)`
  display: flex;
  justify-content: space-between;
  padding: 3px 0;
`;

// --- Componente Principal ---

export default function ClientProfileScreen() {
    const navigate = useNavigate();

    return (
        <ProfileContainer>
            <ContentWrapper>
                
                {/* 1. Botão de Voltar (Seta) */}
                <BackButtonRow>
                    <IconButton onClick={() => navigate('/client/dashboard')} color="default">
                        <ArrowBack />
                    </IconButton>
                </BackButtonRow>

                {/* 2. Logo/Avatar */}
                <LogoDisplay>
                    <Box 
                        component="img"
                        src={clientDataMock.logoSrc}
                        alt="Logo Agro Hub"
                        sx={{
                            width: 120, 
                            height: 120, 
                            borderRadius: '50%',
                            bgcolor: '#fcfcfc',
                            p: 2,
                            boxShadow: '0 4px 8px rgba(0,0,0,0.1)'
                        }}
                    />
                </LogoDisplay>

                {/* 3. Dados do Cliente (Profile Card) */}
                <ProfileCard elevation={3}>
                    {/* Linha com Nome Completo (ou Razão Social, dependendo do tipo de usuário) */}
                    <ProfileItem>
                        <Typography variant="body1">Razão social:</Typography>
                        <Typography variant="body1" fontWeight="bold">{clientDataMock.nome || clientDataMock.razaoSocial}</Typography>
                    </ProfileItem>
                    <ProfileItem>
                        <Typography variant="body1">Nome fantasia:</Typography>
                        <Typography variant="body1" fontWeight="bold">{clientDataMock.nomeFantasia}</Typography>
                    </ProfileItem>
                    <ProfileItem>
                        <Typography variant="body1">Telefone:</Typography>
                        <Typography variant="body1">{clientDataMock.telefone}</Typography>
                    </ProfileItem>
                    <ProfileItem>
                        <Typography variant="body1">E-mail:</Typography>
                        <Typography variant="body1">{clientDataMock.email}</Typography>
                    </ProfileItem>
                    <ProfileItem>
                        <Typography variant="body1">Data de criação:</Typography>
                        <Typography variant="body1">{clientDataMock.dataCriacao}</Typography>
                    </ProfileItem>
                </ProfileCard>
                
                {/* 4. Histórico de Compra/Pedidos */}
                <HistoryTitle variant="h6">
                    Histórico de compra
                </HistoryTitle>
                
                <HistoryContainer>
                    <List disablePadding>
                        {historyMock.map((item, index) => (
                            <React.Fragment key={item.id}>
                                <HistoryCard elevation={0}>
                                    <HistoryItem>
                                        <Typography variant="body2">Pedido:</Typography>
                                        <Typography variant="body2" fontWeight="bold">{item.item}</Typography>
                                    </HistoryItem>
                                    <HistoryItem>
                                        <Typography variant="body2">Quantidade:</Typography>
                                        <Typography variant="body2">{item.quantity}</Typography>
                                    </HistoryItem>
                                    <HistoryItem>
                                        <Typography variant="body2">Preço:</Typography>
                                        <Typography variant="body2">{item.price}</Typography>
                                    </HistoryItem>
                                    <HistoryItem>
                                        <Typography variant="body2">Data da compra:</Typography>
                                        <Typography variant="body2">{item.date}</Typography>
                                    </HistoryItem>
                                </HistoryCard>
                                {index < historyMock.length - 1 && <Divider component="li" sx={{ m: 0, p: 0 }} />}
                            </React.Fragment>
                        ))}
                    </List>
                </HistoryContainer>

            </ContentWrapper>
        </ProfileContainer>
    );
}