package br.com.agrohub.demo.repository; // <<< PACOTE CORRIGIDO

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agrohub.demo.models.AdditionalInfo;

// Gerencia o acesso à tabela 'informacoes_adicionais'.
// Deve ser uma interface e estender JpaRepository.
public interface AdditionalInfoRepository extends JpaRepository<AdditionalInfo, Long> {
    
}
