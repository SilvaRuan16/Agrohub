import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Box, Typography, Grid, Button, Paper, Rating, Accordion, AccordionSummary, AccordionDetails, Select, MenuItem, FormControl, InputLabel, TextField, Card, CardMedia } from '@mui/material';
import { ExpandMore, ShoppingCart, LocalShipping, Payment } from '@mui/icons-material';
import styled from 'styled-components';

// --- Mocks de Dados ---

// Dados mockados para um produto (simulando a busca por ID)
const productMock = {
    id: 1,
    name: 'Tomate orgânico caixa 15kg',
    price: 'R$ 45,99',
    description: "Descrição do produto: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec odio. Praesent libero. Sed cursus ante dapibus diam. Sed nisi. Nulla quis sem at nibh elementum imperdiet. Duis sagittis ipsum. Praesent mauris. Fusce nec tellus sed augue semper porta. Mauris massa. Vestibulum lacinia arcu eget nulla. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.",
    details: 'A polpa do tomate é macia e sem fibras, rica em vitaminas do complexo B e minerais. Recomendado especialmente para papinhas e molhos.',
    rating: 4.5,
    images: [
        'https://via.placeholder.com/200x200?text=Tomate+1',
        'https://via.placeholder.com/200x200?text=Tomate+2',
        'https://via.placeholder.com/200x200?text=Tomate+3',
        'https://via.placeholder.com/200x200?text=Tomate+4',
        'https://via.placeholder.com/200x200?text=Tomate+5',
    ],
    companyInfo: {
        produtor: 'Fazenda Sol Nascente',
        municipio: 'Vila Velha/ES',
        telefone: '(27) 98765-4321',
        tipoProduto: 'Orgânico Certificado',
    }
};

// Mock para a seção "Outros produtos desta empresa"
const relatedProductsMock = [
    { id: 2, name: 'Cenoura 45kg', price: 'R$ 65,30', rating: 3.5, image: 'https://via.placeholder.com/150x150?text=Cenoura' },
    { id: 3, name: 'Alface 210 un.', price: 'R$ 25,60', rating: 4.8, image: 'https://via.placeholder.com/150x150?text=Alface' },
    { id: 4, name: 'Banana prata', price: 'R$ 45,10', rating: 5, image: 'https://via.placeholder.com/150x150?text=Banana' },
    { id: 5, name: 'Abacaxi', price: 'R$ 15,90', rating: 4.1, image: 'https://via.placeholder.com/150x150?text=Abacaxi' },
    { id: 6, name: 'Salsa', price: 'R$ 65,30', rating: 4.5, image: 'https://via.placeholder.com/150x150?text=Salsa' },
    { id: 7, name: 'Queijo', price: 'R$ 255,70', rating: 5, image: 'https://via.placeholder.com/150x150?text=Queijo' },
    { id: 8, name: 'Salame', price: 'R$ 212,99', rating: 4.7, image: 'https://via.placeholder.com/150x150?text=Salame' },
    { id: 9, name: 'Café', price: 'R$ 299,99', rating: 4.8, image: 'https://via.placeholder.com/150x150?text=Cafe' },
    { id: 10, name: 'Picanha', price: 'R$ 299,99', rating: 4.1, image: 'https://via.placeholder.com/150x150?text=Picanha' },
];

// --- Styled Components ---

const DetailContainer = styled(Box)`
    padding: 20px;
    max-width: 1400px;
    margin: 0 auto;
`;

const MainGrid = styled(Grid)`
    margin-top: 20px;
`;

const CheckoutBox = styled(Paper)`
    background-color: #e8f5e9; /* Fundo claro para contraste, como no protótipo */
    padding: 20px;
    position: sticky;
    top: 120px; /* Abaixo do menu fixo */
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
`;

const PriceDisplay = styled(Typography)`
    && {
        color: #1a4314;
        font-weight: bold;
        margin-bottom: 5px;
    }
`;

const AddToCartButton = styled(Button)`
    && {
        background-color: #1a4314; /* Verde Escuro */
        color: white;
        margin-top: 10px;
        padding: 10px;
        font-weight: bold;
        &:hover {
            background-color: #2e7d32;
        }
    }
`;

const BuyNowButton = styled(Button)`
    && {
        background-color: #ffb74d; /* Laranja de destaque */
        color: #1a4314;
        margin-top: 10px;
        padding: 10px;
        font-weight: bold;
        &:hover {
            background-color: #ff9800;
        }
    }
`;

const ProductDetailCard = styled(Card)`
    text-align: center;
    box-shadow: none;
    .MuiCardContent-root {
        padding: 8px;
    }
    
    &:hover {
        opacity: 0.9;
        cursor: pointer;
    }
`;

// --- Componente Principal ---

export default function ClientProductDetailScreen() {
    const { id } = useParams(); // Pega o ID da URL
    const navigate = useNavigate();
    const [mainImage, setMainImage] = useState(productMock.images[0]);
    const [quantity, setQuantity] = useState(1);
    const [paymentMethod, setPaymentMethod] = useState('');

    // Simulação da chamada API: const [product, setProduct] = useState({}); useEffect(() => { fetchProduct(id) }, [id]);
    const product = productMock;

    const handleAddToCart = () => {
        console.log(`Adicionar ${quantity}x do produto ID ${id} ao carrinho.`);
    };

    const handleBuyNow = () => {
        console.log(`Comprar agora: ${product.name}, Quantidade: ${quantity}`);
        // Redireciona para o checkout
        navigate('/client/checkout');
    };

    const renderRelatedProducts = () => (
        <Box mt={5}>
            <Typography variant="h5" gutterBottom sx={{ color: '#1a4314', fontWeight: 'bold', mb: 3 }}>
                Outros produtos desta empresa
            </Typography>
            <Grid container spacing={2}>
                {relatedProductsMock.slice(0, 5).map((related) => ( // Exibe apenas 5 por linha
                    <Grid item key={related.id} xs={2.4} sm={2.4} md={2.4} lg={2.4}>
                        <ProductDetailCard onClick={() => navigate(`/client/product/${related.id}`)}>
                            <CardMedia
                                component="img"
                                height="80"
                                image={related.image}
                                alt={related.name}
                                sx={{ objectFit: 'contain' }}
                            />
                            <Box sx={{ p: 1 }}>
                                <Typography variant="caption" sx={{ display: 'block' }}>{related.name}</Typography>
                                <Rating value={related.rating} readOnly size="small" precision={0.5} sx={{ my: 0.5 }} />
                                <Typography variant="body2" color="primary" fontWeight="bold">{related.price}</Typography>
                                <Button size="small" variant="contained" style={{ backgroundColor: '#1a4314', marginTop: '5px' }}>
                                    Adicionar ao carrinho
                                </Button>
                            </Box>
                        </ProductDetailCard>
                    </Grid>
                ))}
            </Grid>
        </Box>
    );
    
    // Função para renderizar o bloco de checkout (canto superior direito)
    const renderCheckoutBlock = () => (
        <CheckoutBox elevation={5}>
            <PriceDisplay variant="h4">{product.price}</PriceDisplay>
            
            <FormControl fullWidth margin="normal">
                <InputLabel id="quantity-label">Quantidade</InputLabel>
                <Select
                    labelId="quantity-label"
                    value={quantity}
                    label="Quantidade"
                    onChange={(e) => setQuantity(e.target.value)}
                >
                    {[...Array(10).keys()].map(i => (
                        <MenuItem key={i + 1} value={i + 1}>{i + 1} KG</MenuItem>
                    ))}
                </Select>
            </FormControl>
            
            <Typography variant="h6" sx={{ mt: 2 }}>
                Total (10 KG)
            </Typography>
            <Typography variant="h5" color="error" fontWeight="bold">
                R$ 459,90
            </Typography>

            <BuyNowButton fullWidth onClick={handleBuyNow} startIcon={<Payment />}>
                Comprar agora
            </BuyNowButton>
            
            <AddToCartButton fullWidth variant="contained" onClick={handleAddToCart} startIcon={<ShoppingCart />}>
                Adicionar ao carrinho
            </AddToCartButton>
            
            <Box mt={3}>
                <Typography variant="subtitle1" fontWeight="bold" sx={{ color: '#1a4314' }}>
                    Formas de pagamento
                </Typography>
                <Select
                    fullWidth
                    size="small"
                    value={paymentMethod}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                    displayEmpty
                >
                    <MenuItem value="">Selecione a forma</MenuItem>
                    <MenuItem value="pix">Pix (2% desc.)</MenuItem>
                    <MenuItem value="boleto">Boleto</MenuItem>
                </Select>
            </Box>
            
            <Box mt={3}>
                <Typography variant="subtitle1" fontWeight="bold" sx={{ color: '#1a4314' }}>
                    Frete e Entrega
                </Typography>
                <TextField 
                    size="small"
                    label="Calcular Frete (CEP)"
                    fullWidth
                    // Aplique a máscara de CEP aqui (ex: usar react-text-mask ou inputProps)
                    placeholder="00000-000"
                />
                <Button 
                    fullWidth 
                    size="small" 
                    variant="outlined" 
                    sx={{ mt: 1, borderColor: '#1a4314', color: '#1a4314' }}
                    startIcon={<LocalShipping />}
                >
                    Calcular
                </Button>
            </Box>
        </CheckoutBox>
    );

    return (
        <DetailContainer>
            <MainGrid container spacing={4}>
                {/* COLUNA 1: Detalhes do Produto (Imagens e Info) */}
                <Grid item xs={12} md={7} lg={8}>
                    {/* Imagem Principal */}
                    <Box sx={{ border: '1px solid #ddd', p: 2, display: 'flex', justifyContent: 'center' }}>
                        <img src={mainImage} alt={product.name} style={{ maxWidth: '90%', maxHeight: '400px', objectFit: 'contain' }} />
                    </Box>

                    {/* Galeria de Thumbnails */}
                    <Box display="flex" justifyContent="center" gap={1} mt={2}>
                        {product.images.map((img, index) => (
                            <img
                                key={index}
                                src={img}
                                alt={`Thumbnail ${index + 1}`}
                                onClick={() => setMainImage(img)}
                                style={{ width: 60, height: 60, objectFit: 'cover', cursor: 'pointer', border: img === mainImage ? '2px solid #1a4314' : '1px solid #ddd' }}
                            />
                        ))}
                    </Box>

                    {/* Título, Preço e Avaliação */}
                    <Typography variant="h4" sx={{ mt: 4, color: '#1a4314', fontWeight: 'bold' }}>
                        {product.name}
                    </Typography>
                    <Typography variant="h5" color="primary" sx={{ mb: 1 }}>
                        {product.price}
                    </Typography>
                    <Rating value={product.rating} readOnly precision={0.5} />
                    
                    {/* Descrição */}
                    <Typography variant="body1" sx={{ mt: 2, color: 'text.secondary' }}>
                        **Descrição do produto**
                    </Typography>
                    <Typography variant="body1" sx={{ mt: 1, lineHeight: 1.6 }}>
                        {product.description}
                    </Typography>

                    {/* INFORMAÇÕES ADICIONAIS (Accordion Expansível) */}
                    <Box mt={3}>
                        <Accordion sx={{ backgroundColor: '#c8e6c9', border: 'none' }}>
                            <AccordionSummary expandIcon={<ExpandMore />} sx={{ color: '#1a4314', fontWeight: 'bold' }}>
                                <Typography variant="h6">Informações da Empresa</Typography>
                            </AccordionSummary>
                            <AccordionDetails>
                                <Grid container spacing={2}>
                                    <Grid item xs={12} sm={6}>
                                        <Typography variant="body2" fontWeight="bold">Produtor:</Typography>
                                        <Typography variant="body2">{product.companyInfo.produtor}</Typography>
                                    </Grid>
                                    <Grid item xs={12} sm={6}>
                                        <Typography variant="body2" fontWeight="bold">Telefone:</Typography>
                                        <Typography variant="body2">{product.companyInfo.telefone}</Typography>
                                    </Grid>
                                    <Grid item xs={12} sm={6}>
                                        <Typography variant="body2" fontWeight="bold">Município:</Typography>
                                        <Typography variant="body2">{product.companyInfo.municipio}</Typography>
                                    </Grid>
                                    <Grid item xs={12} sm={6}>
                                        <Typography variant="body2" fontWeight="bold">Tipo do produto:</Typography>
                                        <Typography variant="body2">{product.companyInfo.tipoProduto}</Typography>
                                    </Grid>
                                </Grid>
                            </AccordionDetails>
                        </Accordion>
                    </Box>
                    
                    {/* INFORMAÇÕES TÉCNICAS (Bloco Fixo) */}
                    <Box mt={3}>
                         <Typography variant="subtitle1" fontWeight="bold" sx={{ color: '#1a4314', borderBottom: '1px solid #ddd', pb: 1 }}>
                            Informações adicionais
                        </Typography>
                        <Typography variant="body2" sx={{ mt: 1 }}>
                           * Informação Técnica 1: Valor
                        </Typography>
                         <Typography variant="body2">
                           * Informação Técnica 2: Valor
                        </Typography>
                    </Box>
                    
                </Grid>

                {/* COLUNA 2: Bloco de Checkout (Fixo no topo) */}
                <Grid item xs={12} md={5} lg={4}>
                    {renderCheckoutBlock()}
                </Grid>
            </MainGrid>
            
            {/* SEÇÃO INFERIOR: Outros Produtos da Empresa */}
            {renderRelatedProducts()}

        </DetailContainer>
    );
}