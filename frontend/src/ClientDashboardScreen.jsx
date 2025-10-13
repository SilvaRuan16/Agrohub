import React from 'react';
import { useNavigate } from 'react-router-dom';
// Removido 'Chip' da importação abaixo
import { Box, Typography, Grid, Card, CardContent, CardMedia, Button, Rating } from '@mui/material'; 
import { ShoppingCart } from '@mui/icons-material';
import styled from 'styled-components';

// --- Dados Mockados ---

const productsMock = [
    { id: 1, name: 'Tomate orgânico caixa 15kg', price: 'R$ 45,99', rating: 4.5, image: 'https://via.placeholder.com/150x150?text=Tomate' },
    { id: 2, name: 'Cenoura a caixa 45KG', price: 'R$ 65,30', rating: 3.5, image: 'https://via.placeholder.com/150x150?text=Cenoura' },
    { id: 3, name: 'Alface 10 un.', price: 'R$ 25,60', rating: 4.8, image: 'https://via.placeholder.com/150x150?text=Alface' },
    { id: 4, name: 'Banana prata caixa 70 unidades', price: 'R$ 45,10', rating: 5, image: 'https://via.placeholder.com/150x150?text=Banana' },
    { id: 5, name: 'Algodão c/ ou sem casca caixa 14kg', price: 'R$ 66,00', rating: 3.0, image: 'https://via.placeholder.com/150x150?text=Algodao' },
    { id: 6, name: 'Salsa para tempero caixa 400 unidades', price: 'R$ 65,30', rating: 4.5, image: 'https://via.placeholder.com/150x150?text=Salsa' },
    { id: 7, name: 'Queijos caixeiro p/ a mesa', price: 'R$ 255,70', rating: 5, image: 'https://via.placeholder.com/150x150?text=Queijo' },
    { id: 8, name: 'Salames caixeiros p/ a mesa', price: 'R$ 212,99', rating: 4.7, image: 'https://via.placeholder.com/150x150?text=Salame' },
    { id: 9, name: 'Café forte caixa 72 unidades 500gr', price: 'R$ 299,99', rating: 4.8, image: 'https://via.placeholder.com/150x150?text=Cafe' },
    { id: 10, name: 'Picanha bovina caixa 32 unidades de 700gr', price: 'R$ 299,99', rating: 4.1, image: 'https://via.placeholder.com/150x150?text=Picanha' },
];

// --- Styled Components (mantidos) ---

const DashboardContainer = styled(Box)`
    padding: 20px;
    max-width: 1400px;
    margin: 0 auto;
`;

const ProductCard = styled(Card)`
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    text-align: center;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s, box-shadow 0.2s;
    
    &:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 15px rgba(0, 0, 0, 0.15);
        cursor: pointer;
    }
`;

const StyledCardMedia = styled(CardMedia)`
    padding-top: 15px;
    object-fit: contain;
    height: 120px; 
`;

const PriceText = styled(Typography)`
    && {
        color: #1a4314; 
        font-weight: bold;
        margin-top: 5px;
        margin-bottom: 5px;
    }
`;

const AddToCartButton = styled(Button)`
    && {
        background-color: #1a4314; /* Verde Escuro */
        color: white;
        text-transform: none;
        margin-top: 10px;
        &:hover {
            background-color: #2e7d32;
        }
    }
`;

// --- Componente Principal (mantido) ---

export default function ClientDashboardScreen() {
    const navigate = useNavigate();

    const handleProductClick = (id) => {
        navigate(`/client/product/${id}`);
    };

    const handleAddToCart = (e, id) => {
        e.stopPropagation();
        console.log(`Adicionar produto ID ${id} ao carrinho.`);
    };

    return (
        <DashboardContainer>
            <Grid container spacing={3}>
                {productsMock.map((product) => (
                    <Grid item key={product.id} xs={12} sm={6} md={4} lg={3} xl={2.4}>
                        <ProductCard onClick={() => handleProductClick(product.id)}>
                            <StyledCardMedia
                                component="img"
                                image={product.image}
                                alt={product.name}
                            />
                            <CardContent sx={{ flexGrow: 1, p: 2 }}>
                                <Typography variant="subtitle1" component="div" sx={{ minHeight: '40px' }}>
                                    {product.name}
                                </Typography>
                                <Rating value={product.rating} readOnly precision={0.5} size="small" sx={{ mt: 1 }} />
                                <PriceText variant="h6">
                                    {product.price}
                                </PriceText>
                                
                                <AddToCartButton
                                    variant="contained"
                                    fullWidth
                                    startIcon={<ShoppingCart />}
                                    onClick={(e) => handleAddToCart(e, product.id)}
                                >
                                    Adicionar ao carrinho
                                </AddToCartButton>
                            </CardContent>
                        </ProductCard>
                    </Grid>
                ))}
            </Grid>
        </DashboardContainer>
    );
}