import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Typography, Button, Paper, Grid, Divider } from '@mui/material';
import styled from 'styled-components';
import CompanyHeader from './CompanyHeader'; // Importação do novo cabeçalho

// --- Mocks para Simulação ---

// Simula se há produtos cadastrados (true = simula pp9.png inferior, false = simula pp9.png superior)
const hasProductsMock = true; 

// Dados mockados de um produto (baseado em pp9.png inferior)
const productMock = {
    code: '094CH9',
    name: 'Mandioca tipo A',
    category: 'Raízes e Tubérculos',
    description: 'A aipim mandioca é uma variedade da mandioca de polpa amarela, macia e sem fibras, conhecida por seu sabor levemente adocicado. É rica em vitaminas do complexo B e minerais, além de conter baixo teor de ácido cianídrico, sendo segura para consumo "in natura" e recomendada especialmente para papinhas.',
    price: 'R$ 15,90/kg',
    maxDiscount: '2%',
    margin: '30%',
    quantity: 1500,
    unit: 'KG',
    minStock: 200,
    imageSrc: 'https://via.placeholder.com/150x150?text=Mandioca', // Simulação
};

// --- Styled Components ---

const GlobalContainer = styled(Box)`
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f5f5f5;
`;

const ContentContainer = styled(Box)`
  flex-grow: 1;
  padding: 20px 40px;
  background-color: #fcfcfc;
  
  @media (max-width: 600px) {
    padding: 10px 20px;
  }
`;

const EmptyStateContainer = styled(Box)`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  min-height: 50vh;
`;

const ProductDetailCard = styled(Paper)`
  padding: 30px;
  margin-top: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
`;

const Footer = styled(Box)`
  background-color: #1a4314;
  color: #c8e6c9;
  padding: 30px 40px;
  font-size: 0.8rem;
  text-align: center;
  flex-shrink: 0;
`;

// --- Componente Principal ---

export default function CompanyDashboardScreen() {
    const navigate = useNavigate();
    // Apenas lemos o estado, mantendo o mock
    const [products] = useState(hasProductsMock ? [productMock] : []);

    const handleAddProduct = () => {
        navigate('/add-product');
    };
    
    // Função para renderizar a tela de produtos (pp9.png inferior)
    const renderProductDetails = (product) => (
        <ProductDetailCard>
            <Grid container spacing={3}>
                <Grid item xs={12} sm={3} display="flex" flexDirection="column" alignItems="center">
                    <Box 
                        sx={{ 
                            width: 150, 
                            height: 150, 
                            bgcolor: 'lightblue', 
                            borderRadius: '8px',
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                            overflow: 'hidden'
                        }}
                    >
                        {/* Imagem do produto */}
                        <img 
                            src={product.imageSrc} 
                            alt={product.name} 
                            style={{ maxWidth: '100%', maxHeight: '100%' }}
                        />
                    </Box>
                    <Typography variant="h6" style={{ marginTop: '10px' }}>
                        Código: {product.code}
                    </Typography>
                </Grid>

                <Grid item xs={12} sm={9}>
                    <Typography variant="h5" gutterBottom style={{ color: '#1a4314', fontWeight: 'bold' }}>
                        {product.name}
                    </Typography>
                    
                    <Box display="flex" flexWrap="wrap" gap={4} my={2}>
                        <Box>
                            <Typography variant="body2" color="textSecondary">Categoria</Typography>
                            <Typography variant="body1">{product.category}</Typography>
                        </Box>
                        <Box>
                            <Typography variant="body2" color="textSecondary">Margem de lucro (%)</Typography>
                            <Typography variant="body1">{product.margin}</Typography>
                        </Box>
                        <Box>
                            <Typography variant="body2" color="textSecondary">Preço de Venda</Typography>
                            <Typography variant="body1" color="primary">{product.price}</Typography>
                        </Box>
                        <Box>
                            <Typography variant="body2" color="textSecondary">Quantidade em estoque</Typography>
                            <Typography variant="body1">{product.quantity} {product.unit}</Typography>
                        </Box>
                    </Box>

                    <Divider sx={{ my: 2 }} />

                    <Typography variant="body2" color="textSecondary">Descrição do Produto</Typography>
                    <Typography variant="body1" sx={{ mt: 1 }}>
                        {product.description}
                    </Typography>
                    
                    <Box mt={3} display="flex" gap={2}>
                        <Button variant="contained" style={{ backgroundColor: '#ffb74d' }}>
                            Editar produto
                        </Button>
                        <Button variant="contained" color="error">
                            Excluir produto
                        </Button>
                    </Box>
                </Grid>
            </Grid>
        </ProductDetailCard>
    );

    return (
        <GlobalContainer>
            {/* NOVO: Usando o CompanyHeader para navegação */}
            <CompanyHeader /> 

            <ContentContainer>
                {products.length === 0 ? (
                    // VISÃO 1: ESTADO VAZIO (pp9.png superior)
                    <EmptyStateContainer>
                        <Typography variant="h6" color="textSecondary" gutterBottom>
                            Nenhum produto cadastrado
                        </Typography>
                        <Button 
                            variant="contained" 
                            onClick={handleAddProduct}
                            style={{ 
                                backgroundColor: '#1a4314', 
                                color: 'white', 
                                padding: '10px 30px',
                                marginTop: '15px' 
                            }}
                        >
                            Adicionar produto
                        </Button>
                    </EmptyStateContainer>
                ) : (
                    // VISÃO 2: PRODUTOS CADASTRADOS (pp9.png inferior)
                    <Box>
                        <Typography variant="h4" gutterBottom style={{ color: '#1a4314', marginTop: '10px' }}>
                            Seu Estoque
                        </Typography>
                        {products.map((p, index) => (
                            <React.Fragment key={index}>
                                {renderProductDetails(p)}
                            </React.Fragment>
                        ))}
                    </Box>
                )}
            </ContentContainer>
            
            <Footer>
                copyright AgroHub - 2025
            </Footer>
        </GlobalContainer>
    );
}