import { Alert, Box, Button, CircularProgress, Divider, Grid, Paper, Typography } from '@mui/material';
import axios from 'axios';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import CompanyHeader from './CompanyHeader';

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

// --- Constante da API (CORREÇÃO) ---
const API_BASE_URL = 'http://localhost:8080';

// --- Componente Principal ---

export default function CompanyDashboardScreen() {
    const navigate = useNavigate();

    // ESTADO PARA ARMAZENAR PRODUTOS, LOADING E ERRO
    const [products, setProducts] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    // HOOK PARA BUSCAR OS DADOS DA API
    useEffect(() => {
        const fetchCompanyProducts = async () => {

            // 1. OBTÉM O TOKEN E O TIPO DE USUÁRIO (Cliente ou Empresa)
            const authToken = localStorage.getItem('authToken');
            const userType = localStorage.getItem('userType');

            // 2. VERIFICA AUTENTICAÇÃO E TIPO DE USUÁRIO
            if (!authToken || userType !== 'EMPRESA') {
                // Se não há token ou se o token é de um CLIENTE tentando acessar o dashboard de EMPRESA
                setError('Acesso não autorizado. Por favor, faça login com a conta correta.');
                setIsLoading(false);
                return;
            }

            try {
                setIsLoading(true);

                // 🎯 CORREÇÃO FINAL: Usando a URL BASE + o PATH exato do controller
                const response = await axios.get(`${API_BASE_URL}/api/v1/companies/dashboard`, {
                    headers: {
                        Authorization: `Bearer ${authToken}` // Enviando o JWT
                    }
                });

                setProducts(response.data);
                setError(null);

            } catch (err) {
                console.error("Falha ao buscar produtos:", err);
                let errorMessage = 'Falha ao buscar produtos.';

                if (err.response) {
                    if (err.response.status === 401) {
                        // Token rejeitado (inválido ou expirado). Limpa a sessão e notifica o usuário.
                        localStorage.removeItem('authToken');
                        localStorage.removeItem('userType');
                        errorMessage = 'Sessão expirada. Por favor, faça login novamente.';
                    } else if (err.response.status === 403) {
                        // Permissão negada
                        errorMessage = 'Acesso negado. Você não tem permissão para esta rota.';
                    } else if (err.response.data && err.response.data.message) {
                        errorMessage = err.response.data.message;
                    } else {
                        errorMessage = `Erro de API: ${err.response.status}. Tente novamente.`;
                    }
                }

                setError(errorMessage);
            } finally {
                setIsLoading(false);
            }
        };

        fetchCompanyProducts();
    }, [navigate]); // Mantido 'navigate' nas dependências

    const handleAddProduct = () => {
        navigate('/company/add-product');
    };

    // FUNÇÃO DE RENDERIZAÇÃO DE UM ÚNICO PRODUTO
    const renderProductDetails = (product) => (
        <ProductDetailCard key={product.id}>
            <Grid container spacing={3}>
                <Grid item xs={12} sm={3} display="flex" flexDirection="column" alignItems="center">
                    <Box
                        sx={{
                            width: 150,
                            height: 150,
                            bgcolor: '#eee',
                            borderRadius: '8px',
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                            overflow: 'hidden'
                        }}
                    >
                        <img
                            src={product.imagensUrls && product.imagensUrls.length > 0 ? product.imagensUrls[0] : 'https://via.placeholder.com/150x150?text=Sem+Imagem'}
                            alt={product.nome}
                            style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                        />
                    </Box>
                    <Typography variant="h6" style={{ marginTop: '10px' }}>
                        Código: {product.codigoInterno || product.id}
                    </Typography>
                </Grid>

                <Grid item xs={12} sm={9}>
                    <Typography variant="h5" gutterBottom style={{ color: '#1a4314', fontWeight: 'bold' }}>
                        {product.nome}
                    </Typography>

                    <Box display="flex" flexWrap="wrap" gap={4} my={2}>
                        <Box>
                            <Typography variant="body2" color="textSecondary">Categoria</Typography>
                            <Typography variant="body1">{product.categoria || 'N/A'}</Typography>
                        </Box>
                        <Box>
                            <Typography variant="body2" color="textSecondary">Margem de lucro (%)</Typography>
                            <Typography variant="body1">{product.margemLucro ? `${Number(product.margemLucro).toFixed(2)}%` : 'N/A'}</Typography>
                        </Box>
                        <Box>
                            <Typography variant="body2" color="textSecondary">Preço de Venda</Typography>
                            <Typography variant="body1" color="primary">
                                R$ {Number(product.precoVenda).toFixed(2)} / {product.unidadeMedida}
                            </Typography>
                        </Box>
                        <Box>
                            <Typography variant="body2" color="textSecondary">Quantidade em estoque</Typography>
                            <Typography variant="body1">{product.quantidadeEstoque || 0} {product.unidadeMedida}</Typography>
                        </Box>
                    </Box>

                    <Divider sx={{ my: 2 }} />

                    <Typography variant="body2" color="textSecondary">Descrição do Produto</Typography>
                    <Typography variant="body1" sx={{ mt: 1 }}>
                        {product.descricao || product.detalhes || 'Sem descrição.'}
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

    // RENDERIZAÇÃO CONDICIONAL
    const renderContent = () => {
        if (isLoading) {
            return (
                <EmptyStateContainer>
                    <CircularProgress />
                    <Typography variant="h6" color="textSecondary" sx={{ mt: 2 }}>
                        Buscando seus produtos...
                    </Typography>
                </EmptyStateContainer>
            );
        }

        if (error) {
            return (
                <EmptyStateContainer>
                    <Alert severity="error">{error}</Alert>
                    <Button
                        variant="outlined"
                        onClick={() => navigate('/login')} // Agora redireciona para o login
                        style={{ marginTop: '15px' }}
                    >
                        TENTAR NOVAMENTE (OU VÁ PARA OUTRA TELA)
                    </Button>
                </EmptyStateContainer>
            );
        }

        if (products.length === 0) {
            return (
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
            );
        }

        return (
            <Box>
                <Typography variant="h4" gutterBottom style={{ color: '#1a4314', marginTop: '10px' }}>
                    Seu Estoque
                </Typography>
                <Button
                    variant="contained"
                    onClick={handleAddProduct}
                    style={{
                        backgroundColor: '#1a4314',
                        color: 'white',
                        marginBottom: '20px'
                    }}
                >
                    + Adicionar Novo Produto
                </Button>
                {products.map((p) => renderProductDetails(p))}
            </Box>
        );
    };

    return (
        <GlobalContainer>
            <CompanyHeader />
            <ContentContainer>
                {renderContent()}
            </ContentContainer>
            <Footer>
                copyright AgroHub - 2025
            </Footer>
        </GlobalContainer>
    );
}