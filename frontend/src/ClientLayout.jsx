import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, AppBar, Toolbar, Typography, Button, IconButton, Drawer, List, ListItem, ListItemText, useMediaQuery, useTheme, TextField, Menu, MenuItem } from '@mui/material';
// Importações estão sendo usadas no Drawer
import { Menu as MenuIcon, ShoppingCart, AccountCircle, HelpOutline, Search, FilterList, Home } from '@mui/icons-material'; 
import { Outlet } from 'react-router-dom';
import styled from 'styled-components';

// --- Styled Components ---

const NavBar = styled(AppBar)`
    background-color: #1a4314 !important; /* Verde Escuro */
`;

const NavLink = styled(Button)`
    && {
        color: #fff; /* Texto branco */
        text-transform: none;
        margin: 0 10px;
        font-weight: bold;
        padding: 5px 10px;
        border-bottom: ${props => (props.$active ? '2px solid #ffb74d' : 'none')}; /* Laranja para ativo */
        
        &:hover {
            background-color: rgba(255, 255, 255, 0.1);
        }
    }
`;

const FilterButton = styled(Button)`
    && {
        background-color: #ffffff;
        color: #1a4314;
        border-radius: 4px;
        height: 40px;
        text-transform: none;
    }
`;

const SearchInput = styled(TextField)`
    .MuiOutlinedInput-root {
        height: 40px;
        padding-right: 0 !important;
        background-color: white;
        border-radius: 4px;
    }
    .MuiOutlinedInput-input {
        padding: 10px 14px;
    }
    .MuiInputAdornment-root {
        height: 40px;
        max-height: 40px;
        padding: 0;
        margin: 0;
    }
    .MuiIconButton-root {
        background-color: #f0f0f0;
        border-radius: 0 4px 4px 0;
        height: 40px;
        width: 40px;
    }
`;

const ContentWrapper = styled(Box)`
    padding-top: 100px; /* Espaço para a barra de navegação dupla */
    min-height: 100vh;
    background-color: #f5f5f5;
`;

const Footer = styled(Box)`
  background-color: #1a4314;
  color: #c8e6c9;
  padding: 20px 40px;
  font-size: 0.8rem;
  text-align: center;
  flex-shrink: 0;
`;

// --- Componente Principal: Layout e Menu do Cliente ---

export default function ClientLayout() {
    const navigate = useNavigate();
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('md')); 
    const [drawerOpen, setDrawerOpen] = useState(false);
    const [filterAnchorEl, setFilterAnchorEl] = useState(null); // Corrigido o nome da variável de estado
    const openFilter = Boolean(filterAnchorEl);

    const handleDrawerToggle = () => { setDrawerOpen(!drawerOpen); };
    const handleFilterClick = (event) => { setFilterAnchorEl(event.currentTarget); };
    
    // CORREÇÃO AQUI: Usando setFilterAnchorEl
    const handleFilterClose = (filterType) => { 
        setFilterAnchorEl(null); 
        if (filterType) { console.log(`Filtrar por: ${filterType}`); } 
    };

    // Definição dos itens de navegação (horizontal, conforme h1.png)
    const navItems = [
        { name: 'Catálogo de empresas', path: '/client/dashboard', icon: <Home /> },
        { name: 'Meu carrinho', path: '/client/cart', icon: <ShoppingCart /> },
        { name: 'Perfil', path: '/client/profile', icon: <AccountCircle /> },
    ];
    
    // Simula a rota ativa para destacar o botão (ex: se a URL for /client/dashboard)
    const activePath = window.location.pathname; 

    const drawer = (
        <Box onClick={handleDrawerToggle} sx={{ width: 250, bgcolor: '#f5f5f5', height: '100%' }}>
            <List>
                <ListItem sx={{ bgcolor: '#1a4314' }}>
                    <Typography variant="h6" color="#c8e6c9">Menu Cliente</Typography>
                </ListItem>
                {navItems.map((item) => (
                    <ListItem button key={item.name} onClick={() => navigate(item.path)}>
                        {item.icon} {/* Usando o ícone, o warning some */}
                        <ListItemText primary={item.name} sx={{ ml: 2 }} />
                    </ListItem>
                ))}
                 <ListItem button onClick={() => navigate('/client/support')}>
                    <HelpOutline /> {/* Usando o ícone, o warning some */}
                    <ListItemText primary="Suporte" sx={{ ml: 2 }} />
                </ListItem>
            </List>
            <Box sx={{ p: 2, textAlign: 'center' }}>
                <Button variant="contained" color="error" onClick={() => navigate('/')}>
                    Sair
                </Button>
            </Box>
        </Box>
    );

    return (
        <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
            <NavBar position="fixed">
                <Toolbar sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', minHeight: '64px' }}>
                    {/* Linha Principal (Topo) */}
                    <Box display="flex" alignItems="center">
                         <Typography variant="h6" sx={{ cursor: 'pointer' }} onClick={() => navigate('/client/dashboard')}>
                            {/* Logo: Simplesmente um texto com margem para simular a imagem */}
                            <span style={{ fontWeight: 'bold' }}>AGRO</span>
                            <span style={{ fontWeight: 'normal' }}>HUB</span>
                         </Typography>
                         <Typography variant="h6" sx={{ ml: 2, cursor: 'pointer' }} onClick={() => navigate('/client/dashboard')}>
                            <span style={{ fontWeight: 'bold' }}>Seja Bem vindo</span>
                         </Typography>
                    </Box>

                    {/* Filtro e Busca (Central) */}
                    <Box display="flex" flexGrow={1} mx={isMobile ? 0 : 4} maxWidth={isMobile ? 'none' : 600}>
                        {!isMobile && (
                            <>
                                <FilterButton 
                                    onClick={handleFilterClick}
                                    startIcon={<FilterList />}
                                    id="filter-button"
                                    aria-controls={openFilter ? 'filter-menu' : undefined}
                                    aria-haspopup="true"
                                    aria-expanded={openFilter ? 'true' : undefined}
                                >
                                    Filtrar
                                </FilterButton>
                                <Menu
                                    id="filter-menu"
                                    anchorEl={filterAnchorEl}
                                    open={openFilter}
                                    onClose={() => handleFilterClose(null)}
                                >
                                    <MenuItem onClick={() => handleFilterClose('Nome')}>Nome</MenuItem>
                                    <MenuItem onClick={() => handleFilterClose('Categoria')}>Categoria</MenuItem>
                                </Menu>
                                <SearchInput 
                                    placeholder="Pesquisar"
                                    variant="outlined"
                                    fullWidth
                                    InputProps={{
                                        endAdornment: (
                                            <IconButton onClick={() => console.log('Buscar')} style={{ color: '#1a4314' }}>
                                                <Search />
                                            </IconButton>
                                        ),
                                        sx: { borderRadius: '0 4px 4px 0' }
                                    }}
                                />
                            </>
                        )}
                    </Box>
                    
                    {/* Botões de Login/Perfil (Direita) */}
                    <Box display="flex" alignItems="center">
                        <Button variant="contained" style={{ backgroundColor: '#c8e6c9', color: '#1a4314', fontWeight: 'bold', textTransform: 'none', marginRight: '10px' }}>
                            Login
                        </Button>
                        <IconButton onClick={() => navigate('/client/profile')} style={{ color: 'white' }}>
                            <AccountCircle fontSize="large" />
                        </IconButton>
                        {isMobile && (
                            <IconButton color="inherit" onClick={handleDrawerToggle} edge="end">
                                <MenuIcon />
                            </IconButton>
                        )}
                    </Box>
                </Toolbar>
                
                {/* Linha de Navegação Secundária (Abaixo da Principal) */}
                <Toolbar sx={{ backgroundColor: 'rgba(0, 0, 0, 0.1)', minHeight: '36px', justifyContent: 'flex-start' }}>
                    {navItems.map((item) => (
                        <NavLink 
                            key={item.name} 
                            onClick={() => navigate(item.path)} 
                            $active={activePath.startsWith(item.path)}
                        >
                            {item.name}
                        </NavLink>
                    ))}
                </Toolbar>
            </NavBar>
            
            <Drawer
                variant="temporary"
                open={drawerOpen}
                onClose={handleDrawerToggle}
                ModalProps={{ keepMounted: true }}
            >
                {drawer}
            </Drawer>

            <ContentWrapper>
                <Outlet /> 
            </ContentWrapper>
            
            <Footer>
                © AgroHub - Projeto Acadêmico | copyright AgroHub - 2025
            </Footer>
        </Box>
    );
}