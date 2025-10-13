import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Typography, Button, TextField, IconButton, Menu, MenuItem, InputAdornment } from '@mui/material';
import { Search, FilterList, Person, Home, Add } from '@mui/icons-material';
import styled from 'styled-components';

// --- Styled Components ---

const HeaderContainer = styled(Box)`
  background-color: #1a4314; /* Cor de fundo escura, como em p00.png */
  color: white;
  padding: 10px 20px;
  display: flex;
  flex-direction: column;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
`;

const TopRow = styled(Box)`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  margin-bottom: 8px; /* Espaço entre a barra de busca e os botões */

  @media (max-width: 600px) {
    flex-wrap: wrap;
  }
`;

const LogoBox = styled(Box)`
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  flex-shrink: 0;
`;

const SearchBox = styled(Box)`
  display: flex;
  flex-grow: 1;
  max-width: 600px;
  margin: 0 15px;

  @media (max-width: 900px) {
    width: 100%;
    order: 3; /* Move a busca para baixo em telas menores */
    margin: 10px 0;
  }
`;

const StyledTextField = styled(TextField)`
  /* Estiliza o campo de busca */
  & .MuiOutlinedInput-root {
    background-color: #ffffff; 
    border-radius: 4px;
    padding-right: 0 !important;
    height: 40px; 
  }
  & .MuiOutlinedInput-input {
    padding: 10px 14px;
    height: 20px;
  }
`;

const StyledSearchButton = styled(IconButton)`
  && {
    background-color: #f0f0f0;
    border-radius: 0 4px 4px 0;
    padding: 8px;
    height: 40px;
    width: 40px;
  }
  &:hover {
    background-color: #e0e0e0;
  }
`;

const AuthBox = styled(Box)`
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
`;

const NavRow = styled(Box)`
  display: flex;
  align-items: center;
  gap: 15px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding-top: 5px;
  
  @media (max-width: 900px) {
    justify-content: space-between;
    width: 100%;
  }
`;

// --- Componente Principal ---

export default function CompanyHeader() {
  const navigate = useNavigate();
  const [filterAnchorEl, setFilterAnchorEl] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  const openFilter = Boolean(filterAnchorEl);

  const handleFilterClick = (event) => {
    setFilterAnchorEl(event.currentTarget);
  };

  const handleFilterClose = (filterType) => {
    setFilterAnchorEl(null);
    if (filterType) {
        // Lógica de filtro aqui
        console.log(`Filtrar por: ${filterType}`);
    }
  };
  
  const handleSearch = () => {
      // Lógica de busca aqui
      console.log(`Pesquisando por: ${searchQuery}`);
  };
  
  // --- Funções de Navegação Atualizadas ---

  const handleLogout = () => {
      // Lógica de logout (limpar token, etc.)
      navigate('/'); // Vai para a tela de Login
  };
  
  const handleDashboard = () => {
      navigate('/dashboard'); // Vai para o Dashboard de Produtos
  };
  
  const handleAddProduct = () => {
      navigate('/add-product'); // Vai para a tela de Cadastro de Produto
  };
  
  const handleProfile = () => {
      navigate('/profile'); // Vai para a tela de Perfil da Empresa
  };

  // ----------------------------------------

  return (
    <HeaderContainer>
      <TopRow>
        <LogoBox onClick={handleDashboard}>
          {/* Simulação do Logo Agro Hub */}
          <Home style={{ color: '#c8e6c9' }} /> 
          <Typography variant="h6" style={{ fontWeight: 'bold', fontSize: '1rem' }}>
            Seja Bem vindo
          </Typography>
        </LogoBox>

        <SearchBox>
          <Button
            id="filter-button"
            aria-controls={openFilter ? 'filter-menu' : undefined}
            aria-haspopup="true"
            aria-expanded={openFilter ? 'true' : undefined}
            onClick={handleFilterClick}
            variant="contained"
            startIcon={<FilterList />}
            style={{ 
                backgroundColor: 'white', 
                color: '#1a4314',
                textTransform: 'none',
                height: '40px',
                borderRadius: '4px 0 0 4px',
                padding: '0 10px',
            }}
          >
            Filtrar
          </Button>
          <Menu
            id="filter-menu"
            anchorEl={filterAnchorEl}
            open={openFilter}
            onClose={() => handleFilterClose(null)}
            MenuListProps={{
              'aria-labelledby': 'filter-button',
            }}
          >
            <MenuItem onClick={() => handleFilterClose('Nome')}>Nome</MenuItem>
            <MenuItem onClick={() => handleFilterClose('Categoria')}>Categoria</MenuItem>
          </Menu>

          <StyledTextField
            variant="outlined"
            placeholder="Pesquisar"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            fullWidth
            InputProps={{
              endAdornment: (
                <InputAdornment position="end" style={{ margin: 0, padding: 0 }}>
                    <StyledSearchButton onClick={handleSearch}>
                        <Search style={{ color: '#1a4314' }} />
                    </StyledSearchButton>
                </InputAdornment>
              ),
            }}
          />
        </SearchBox>

        {/* Botões de Ação Superior */}
        <AuthBox>
          <Button 
            variant="contained" 
            onClick={handleLogout} // LIGAÇÃO COM A FUNÇÃO DE SAIR
            style={{ 
                backgroundColor: '#c8e6c9', 
                color: '#1a4314', 
                fontWeight: 'bold',
                textTransform: 'none' 
            }}
          >
            Sair
          </Button>
          <IconButton onClick={handleProfile} style={{ color: '#c8e6c9' }}> {/* LIGAÇÃO COM A FUNÇÃO DE PERFIL */}
            <Person fontSize="large" />
          </IconButton>
        </AuthBox>
      </TopRow>
      
      {/* SEGUNDA LINHA: Botões de Navegação */}
      <NavRow>
        <Button 
            variant="contained" 
            onClick={handleDashboard} // LIGAÇÃO COM A FUNÇÃO DE DASHBOARD
            startIcon={<Home />}
            style={{ backgroundColor: '#4a90e2', color: 'white' }}
        >
            Produtos
        </Button>
        <Button 
            variant="contained" 
            onClick={handleAddProduct} // LIGAÇÃO COM A FUNÇÃO DE ADICIONAR PRODUTO
            startIcon={<Add />}
            style={{ backgroundColor: '#2e7d32', color: 'white' }}
        >
            Adicionar produto
        </Button>
        <Typography 
            variant="body2" 
            onClick={handleProfile} // LIGAÇÃO COM A FUNÇÃO DE PERFIL
            style={{ 
                marginLeft: 'auto', 
                textDecoration: 'underline',
                cursor: 'pointer' 
            }}
        >
            Perfil
        </Typography>
      </NavRow>
      
    </HeaderContainer>
  );
}