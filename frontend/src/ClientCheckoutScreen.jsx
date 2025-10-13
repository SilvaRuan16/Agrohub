import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
// Removida: AccountBalanceWallet (não estava sendo usada)
import { Box, Typography, Button, Paper, Grid, IconButton } from '@mui/material';
import { ArrowBack, CreditCard, AttachMoney, QrCode, ReceiptLong } from '@mui/icons-material';
import styled from 'styled-components';

// --- Styled Components ---

const CheckoutContainer = styled(Box)`
    padding: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
    min-height: calc(100vh - 100px); 
    background-color: #f5f5f5; 
`;

const PaymentBlock = styled(Paper)`
    padding: 30px;
    margin-top: 30px;
    max-width: 500px;
    width: 100%;
    border-radius: 8px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    text-align: center;
`;

const PaymentTitleButton = styled(Button)`
    && {
        background-color: #ffc107; 
        color: #1a4314; 
        font-weight: bold;
        padding: 15px 30px;
        font-size: 1.2rem;
        box-shadow: none;
        pointer-events: none; 
        margin-bottom: 20px;
    }
`;

const PaymentOptionBox = styled(Box)`
    display: flex;
    flex-direction: column;
    align-items: center;
    cursor: pointer;
    padding: 10px 0;
    border-radius: 8px;
    transition: background-color 0.2s;

    &:hover {
        background-color: #eee;
    }

    &.selected {
        background-color: #c8e6c9; 
        border: 2px solid #1a4314; 
    }
`;

const ConfirmButton = styled(Button)`
    && {
        background-color: #1a4314; 
        color: white;
        padding: 12px 0;
        margin-top: 30px;
        font-size: 1.1rem;
        &:hover {
            background-color: #2e7d32;
        }
    }
`;

// --- Dados Mockados ---
const paymentMethods = [
    { id: 'credit', name: 'Crédito', icon: <CreditCard fontSize="large" sx={{ color: '#1a4314' }} /> },
    { id: 'debit', name: 'Débito', icon: <CreditCard fontSize="large" sx={{ color: '#039be5' }} /> },
    { id: 'boleto', name: 'Boleto', icon: <ReceiptLong fontSize="large" sx={{ color: '#ef6c00' }} /> },
    { id: 'pix', name: 'PIX', icon: <QrCode fontSize="large" sx={{ color: '#38bdf8' }} /> },
    { id: 'cheque', name: 'Cheque', icon: <AttachMoney fontSize="large" sx={{ color: '#546e7a' }} /> },
];


// --- Componente Principal ---

export default function ClientCheckoutScreen() {
    const navigate = useNavigate();
    const [selectedMethod, setSelectedMethod] = useState('pix');
    // REMOVIDO: setTotalAmount (função não usada)
    const [totalAmount] = useState(459.90); // Total fixo por enquanto

    const handleConfirmPayment = () => {
        console.log(`Pagamento de R$ ${totalAmount} confirmado via ${selectedMethod}`);
        navigate('/client/order-success');
    };

    return (
        <CheckoutContainer>
            {/* Botão de Voltar */}
             <Box sx={{ position: 'absolute', top: 20, left: 20 }}>
                 <IconButton onClick={() => navigate(-1)} size="large" sx={{ color: '#1a4314', bgcolor: 'rgba(255, 255, 255, 0.7)' }}>
                    <ArrowBack fontSize="large" />
                </IconButton>
            </Box>

            <PaymentTitleButton variant="contained">
                Pagamento
            </PaymentTitleButton>

            <PaymentBlock elevation={3}>
                {/* Nome do Cliente (Mockado) */}
                <Typography variant="h5" sx={{ mb: 3, fontWeight: 'bold' }}>
                    Carlos Winer Amorim
                </Typography>

                {/* Grid de Opções de Pagamento */}
                <Grid container spacing={2} justifyContent="center" sx={{ mb: 3 }}>
                    {paymentMethods.map((method) => (
                        <Grid item xs={2} key={method.id} onClick={() => setSelectedMethod(method.id)}>
                            <PaymentOptionBox className={selectedMethod === method.id ? 'selected' : ''}>
                                {method.icon}
                                <Typography variant="caption" sx={{ mt: 0.5 }}>{method.name}</Typography>
                            </PaymentOptionBox>
                        </Grid>
                    ))}
                </Grid>

                {/* Exibição do Valor */}
                <Box sx={{ border: '1px solid #ddd', p: 2, borderRadius: '4px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#fff' }}>
                    <Typography variant="h5" sx={{ color: '#1a4314', fontWeight: 'bold' }}>
                         R$
                    </Typography>
                    <Typography variant="h4" color="error" fontWeight="bold">
                        {totalAmount.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
                    </Typography>
                </Box>


                <ConfirmButton
                    variant="contained"
                    fullWidth
                    onClick={handleConfirmPayment}
                >
                    Confirmar
                </ConfirmButton>
                
            </PaymentBlock>
            
            <Box mt={3} p={2} sx={{ fontSize: '0.8rem', color: 'text.secondary', textAlign: 'center' }}>
                *Pagamentos via PIX possuem 5% de desconto.
            </Box>

        </CheckoutContainer>
    );
}