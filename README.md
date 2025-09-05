# Auto-Escola API - Sistema Completo de Agendamento

## Descrição do Projeto

API REST desenvolvida com Spring Boot para sistema completo de agendamento de instruções de auto-escola. Este projeto implementa funcionalidades para cadastramento, listagem, atualização e exclusão de instrutores e alunos, além de agendamento e cancelamento de instruções.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **Spring Boot Validation**
- **MySQL**
- **Flyway** (para controle de migrações)
- **Lombok**
- **Maven**

## Estrutura do Projeto

### Entidades Principais

#### Instrutor
- **Atributos**: nome, email, CNH, especialidade, endereço, ativo
- **Especialidades**: MOTOS, CARROS, VANS, CAMINHÕES
- **Validações**: email válido, CNH com 9-11 dígitos
- **Soft Delete**: Campo `ativo` para exclusão lógica

#### Aluno
- **Atributos**: nome, email, CPF, telefone, endereço, ativo
- **Validações**: email válido, CPF com 11 dígitos, telefone com 10-11 dígitos
- **Soft Delete**: Campo `ativo` para exclusão lógica
- **Regras**: Máximo 2 instruções por dia

#### Instrução
- **Atributos**: aluno, instrutor, dataHora, ativo, motivoCancelamento
- **Regras de Negócio**: 
  - Funcionamento: Segunda a Sábado, 06:00 às 21:00
  - Duração: 1 hora fixa
  - Antecedência mínima: 30 minutos para agendamento
  - Antecedência mínima: 24 horas para cancelamento

#### Endereço (Embeddable)
- **Atributos**: logradouro, número, complemento, bairro, cidade, UF, CEP
- **Validações**: CEP com 8 dígitos
- **Obrigatórios**: logradouro, bairro, cidade, UF, CEP

### Arquitetura

O projeto segue as melhores práticas do Spring Boot com:

- **Entities**: Classes JPA para mapeamento das tabelas
- **DTOs**: Records para transferência de dados (DadosCadastro*, DadosListagem*, DadosAtualizacao*)
- **Repositories**: Interfaces JPA Repository para acesso aos dados
- **Controllers**: Classes REST Controller para exposição das APIs
- **Services**: Camada de serviço com regras de negócio
- **Migrations**: Scripts Flyway para controle de versão do banco

## Endpoints da API

### Instrutores
- `POST /instrutores` - Cadastrar novo instrutor
- `GET /instrutores` - Listar todos os instrutores ativos

### Alunos
- `POST /alunos` - Cadastrar novo aluno
- `GET /alunos` - Listar alunos ativos (paginado, ordenado por nome)
- `PUT /alunos/{id}` - Atualizar dados do aluno (nome, telefone, endereço)
- `DELETE /alunos/{id}` - Excluir aluno (soft delete)
- `GET /alunos/{id}` - Detalhar aluno específico

### Instruções
- `POST /instrucoes` - Agendar nova instrução
- `PUT /instrucoes/{id}/cancelar` - Cancelar instrução existente

## Regras de Negócio Implementadas

### Cadastro de Alunos
- Todos os campos obrigatórios exceto número e complemento do endereço
- Validação de email e CPF únicos
- Telefone com 10-11 dígitos

### Listagem de Alunos
- Ordenação crescente por nome
- Paginação com 10 registros por página
- Exibe apenas nome, email e CPF
- Filtra apenas alunos ativos

### Atualização de Alunos
- Permite alterar: nome, telefone, endereço
- **NÃO permite alterar**: email, CPF
- Validações mantidas nos campos alteráveis

### Exclusão de Alunos
- Exclusão lógica (soft delete)
- Aluno fica inativo no sistema
- Dados preservados no banco

### Agendamento de Instruções
- Horário de funcionamento: Segunda a Sábado, 06:00 às 21:00
- Antecedência mínima: 30 minutos
- Máximo 2 instruções por aluno por dia
- Instrutor não pode ter conflito de horário
- Escolha automática de instrutor se não especificado
- Validação de alunos e instrutores ativos

### Cancelamento de Instruções
- Antecedência mínima: 24 horas
- Motivo obrigatório: ALUNO_DESISTIU, INSTRUTOR_CANCELOU, OUTROS
- Exclusão lógica da instrução

## Configuração do Banco de Dados

```properties
spring.datasource.url=jdbc:mysql://localhost/spring_boot_project
spring.datasource.username=root
spring.datasource.password=fiap
```

## Como Executar

1. Certifique-se de ter o MySQL rodando na porta padrão (3306)
2. Crie o banco de dados `spring_boot_project`
3. Execute o comando: `./mvnw spring-boot:run`

## Exemplos de Uso

### Cadastrar Instrutor
```json
POST /instrutores
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "cnh": "12345678901",
  "especialidade": "CARROS",
  "endereco": {
    "logradouro": "Rua das Flores",
    "numero": "123",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "uf": "SP",
    "cep": "01234567"
  }
}
```

### Cadastrar Aluno
```json
POST /alunos
{
  "nome": "Maria Santos",
  "email": "maria@email.com",
  "cpf": "12345678901",
  "telefone": "11987654321",
  "endereco": {
    "logradouro": "Avenida Paulista",
    "numero": "1000",
    "bairro": "Bela Vista",
    "cidade": "São Paulo",
    "uf": "SP",
    "cep": "01310100"
  }
}
```

### Listar Alunos (com paginação)
```
GET /alunos?page=0&size=10&sort=nome,asc
```

### Atualizar Aluno
```json
PUT /alunos/1
{
  "nome": "Maria Santos Silva",
  "telefone": "11999887766",
  "endereco": {
    "logradouro": "Rua Nova",
    "numero": "456",
    "bairro": "Vila Nova",
    "cidade": "São Paulo",
    "uf": "SP",
    "cep": "01234567"
  }
}
```

### Agendar Instrução
```json
POST /instrucoes
{
  "aluno": {"id": 1},
  "instrutor": {"id": 1},
  "dataHora": "2025-09-10T14:00:00"
}
```

### Cancelar Instrução
```json
PUT /instrucoes/1/cancelar
{
  "motivo": "ALUNO_DESISTIU"
}
```

## Integrantes do Grupo

RM550188 / Gustavo Vegi
Pedro Henrique Silva de Morais / RM98804
Lucas Rodrigues Delfino/ RM550196
Luisa Cristina dos Santos Neves/ RM551889
Gabriel aparecido Cassalho Xavier / RM99794

## Disciplina

**SOA e WebServices**  
**Professor**: Carlos Eduardo Machado de Oliveira  
**Instituição**: FIAP  
**Checkpoint**: 1 - Desenvolvimento de API REST com Spring Boot

## Data de Entrega

06/09/2025
