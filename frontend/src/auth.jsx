import axios from 'axios';

// 1. Cria uma instância base do Axios
const auth = axios.create({
    baseURL: 'http://localhost:8080/api/v1', // Define a URL base para todas as chamadas
});

// 2. Configura o Interceptor de Requisição
auth.interceptors.request.use(
    (config) => {
        // Tenta obter o token do armazenamento local
        const token = localStorage.getItem('jwtToken'); 

        // Se o token existe, ele é anexado ao cabeçalho Authorization
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default auth;