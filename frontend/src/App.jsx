import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

// -----------------------------------------------------------------
// 1. IMPORTAÇÕES DOS COMPONENTES REAIS
//    (Todos os arquivos JSX presentes na sua pasta src em x1.png)
// -----------------------------------------------------------------

// Layout
import ClientLayout from './ClientLayout'; 

// Telas de Autenticação/Registro
import LoginScreen from './LoginScreen';                 // p1.png
import ForgotPasswordScreen from './ForgotPasswordScreen'; // p3.png
import AccountSelectionScreen from './AccountSelectionScreen'; // Seleção Empresa/Cliente
import RegisterCompanyScreen from './RegisterCompanyScreen'; // p2.png (Esquerda)
import RegisterClientScreen from './RegisterClientScreen';   // p2.png (Direita)

// Telas do Cliente (Comprador)
import ClientDashboardScreen from './ClientDashboardScreen';      // Catálogo (h1.png)
import ClientProductDetailScreen from './ClientProductDetailScreen'; // Detalhes (h2.png)
import ClientCheckoutScreen from './ClientCheckoutScreen';        // Pagamento (h3.png)
import ClientProfileScreen from './ClientProfileScreen';          // Perfil/Histórico (p555.png)

// Telas da Empresa (Produtor/Vendedor)
import CompanyDashboardScreen from './CompanyDashboardScreen'; // Gerenciamento (pp9.png)
import AddProductScreen from './AddProductScreen';             // Adicionar Produto (pp1.png)
import CompanyProfileScreen from './CompanyProfileScreen';     // Perfil Empresa

// -----------------------------------------------------------------
// 2. COMPONENTES FALTANTES (Placeholders, pois não estão no seu x1.png)
// -----------------------------------------------------------------
// *Você não criou estes arquivos (Carrinho, Suporte, Sucesso) no x1.png, 
//  então vamos mantê-los como placeholders temporariamente.*
const ClientCartScreen = () => <div>Carrinho de Compras</div>;
const ClientSupportScreen = () => <div>Suporte ao Cliente</div>;
const OrderSuccessScreen = () => <div>Pedido Concluído com Sucesso!</div>;


// -----------------------------------------------------------------
// 3. COMPONENTE PRINCIPAL (APP)
// -----------------------------------------------------------------
function App() {
  return (
    <BrowserRouter>
      <Routes>
        
        {/* ======================================================= */}
        {/* ROTAS DE AUTENTICAÇÃO E CADASTRO */}
        {/* ======================================================= */}
        <Route path="/" element={<LoginScreen />} />
        <Route path="/forgot-password" element={<ForgotPasswordScreen />} /> 
        <Route path="/register" element={<AccountSelectionScreen />} />
        <Route path="/register/company" element={<RegisterCompanyScreen />} />
        <Route path="/register/client" element={<RegisterClientScreen />} />
        
        {/* ======================================================= */}
        {/* ROTAS DA EMPRESA (COMPANY) - Produtor/Vendedor */}
        {/* ======================================================= */}
        {/* Nota: Você precisará de um CompanyLayout se quiser menu/header fixo aqui. */}
        <Route path="/company/dashboard" element={<CompanyDashboardScreen />} />
        <Route path="/company/add-product" element={<AddProductScreen />} />
        <Route path="/company/profile" element={<CompanyProfileScreen />} />
        
        {/* ======================================================= */}
        {/* ROTAS DO CLIENTE (CLIENT) */}
        {/* ======================================================= */}
        <Route path="/client" element={<ClientLayout />}>
            <Route index element={<ClientDashboardScreen />} /> 
            <Route path="dashboard" element={<ClientDashboardScreen />} />
            
            {/* Fluxo de Compra */}
            <Route path="product/:id" element={<ClientProductDetailScreen />} />
            <Route path="cart" element={<ClientCartScreen />} />
            <Route path="checkout" element={<ClientCheckoutScreen />} />
            <Route path="order-success" element={<OrderSuccessScreen />} />

            {/* Outras Telas */}
            <Route path="profile" element={<ClientProfileScreen />} />
            <Route path="support" element={<ClientSupportScreen />} />
        </Route>
        
        {/* ======================================================= */}
        {/* Rota de 404 (Wildcard) */}
        {/* ======================================================= */}
        <Route path="*" element={
          <div style={{ padding: '50px', textAlign: 'center' }}>
            <h2>404 - Página Não Encontrada</h2>
          </div>
        } />
      </Routes>
    </BrowserRouter>
  );
}

export default App;