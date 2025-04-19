# Mussarrellos - Sistema de Pizzaria Online

![Logotipo](https://via.placeholder.com/150x150.png?text=Mussarrellos)

> Sistema completo para gerenciamento de pedidos de pizzaria, incluindo interface de cliente e administração.

## 📋 Visão Geral

Mussarrellos é um sistema completo para pizzarias que desejam oferecer serviços online. Desenvolvido com arquitetura moderna, o sistema permite que clientes façam pedidos de pizza, acompanhem o status de seus pedidos, e que a administração da pizzaria gerencie o catálogo de produtos e pedidos de forma eficiente.

## 🏗️ Arquitetura

O projeto é estruturado em três componentes principais:

### 1. Backend (Java Spring Boot)

Backend desenvolvido seguindo princípios de **Domain-Driven Design (DDD)** e **Clean Architecture**, proporcionando uma separação clara de responsabilidades e facilitando a evolução do sistema.

**Principais características:**
- Arquitetura hexagonal com camadas bem definidas
- Implementação de padrões como Repository, Adapter e Value Objects
- Programação reativa com Project Reactor
- Eventos de domínio para comunicação entre contextos
- Validação de regras de negócio explícitas
- Outbox Pattern para garantir consistência entre dados e eventos

**Organização em módulos:**
- `modules/user`: Gerenciamento de usuários e autenticação
- `modules/products`: Catálogo de produtos (pizzas e tamanhos)
- `modules/orders`: Gerenciamento de pedidos

### 2. Frontend (JavaScript)

Interface de usuário intuitiva para clientes realizarem pedidos e acompanharem seu status.

**Principais características:**
- Design responsivo para funcionamento em dispositivos móveis e desktop
- Framework moderno para construção de interfaces
- Organização em módulos funcionais
- Comunicação com API do backend

### 3. Banco de Dados (PostgreSQL)

Banco de dados relacional estruturado em esquemas temáticos para melhor organização e manutenção.

**Principais esquemas:**
- `users`: Armazena informações de clientes e usuários
- `products`: Catálogo de pizzas e tamanhos disponíveis
- `orders`: Pedidos e itens de pedido

## 🚀 Funcionalidades

### Para Clientes
- Cadastro e autenticação
- Navegação pelo catálogo de pizzas
- Personalização de pedidos (tamanho, quantidade)
- Acompanhamento do status do pedido
- Histórico de pedidos anteriores

### Para Administradores
- Gerenciamento de catálogo de produtos
- Visualização e processamento de pedidos
- Relatórios de vendas
- Gerenciamento de usuários

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.x**
- **Spring WebFlux** (programação reativa)
- **Spring Data R2DBC**
- **MapStruct** para mapeamentos
- **JWT** para autenticação

### Frontend
- **HTML5, CSS3, JavaScript**
- **Fetch API** para comunicação com o backend

### Banco de Dados
- **PostgreSQL 14+**

## 📦 Estrutura do Projeto

```
mussarrellos/
├── data/                          # Diretório principal
│   ├── backend/                   # Código-fonte do backend
│   │   └── src/
│   │       └── main/
│   │           ├── java/com/mussarrellos/backend/
│   │           │   ├── buildingblocks/      # Componentes base da arquitetura
│   │           │   └── modules/            # Módulos do sistema
│   │           │       ├── user/           # Módulo de usuários
│   │           │       │   ├── api/        # Controladores REST
│   │           │       │   ├── application/ # Casos de uso (commands/queries)
│   │           │       │   ├── domain/     # Entidades, regras e eventos
│   │           │       │   └── infra/      # Adaptadores e modelos de persistência
│   │           │       ├── products/       # Módulo de produtos
│   │           │       └── orders/         # Módulo de pedidos
│   │           └── resources/
│   ├── database/                  # Scripts e migrações de banco de dados
│   └── frontend/                  # Código-fonte do frontend
│       ├── app/
│       │   ├── auth/              # Autenticação e gestão de usuários
│       │   ├── checkout/          # Fluxo de checkout e pagamento
│       │   └── utils/             # Utilidades comuns
│       ├── styles/                # Folhas de estilo CSS
│       └── static/                # Recursos estáticos
└── README.md                      # Este arquivo
```

## ⚙️ Configuração e Execução

### Pré-requisitos
- Java 17+
- PostgreSQL 14+
- Node.js 18+ (para ferramentas de build do frontend)

### Banco de Dados
1. Crie um banco de dados PostgreSQL
2. Execute os scripts em `data/database/schemas.sql`

### Backend
```bash
cd data/backend
./mvnw spring-boot:run
```

### Frontend
```bash
cd data/frontend
# Servir os arquivos com seu servidor web preferido
```

## 📝 Convenções e Padrões de Código

### Gerais
- **Clean Code**: Código legível e bem documentado
- **SOLID**: Princípios de design de software
- **DRY**: Don't Repeat Yourself
- **KISS**: Keep It Simple, Stupid

### Específicas
- **Domain-Driven Design**: Entidades, Agregados, Value Objects, Serviços de Domínio
- **Convenções de nomenclatura**: camelCase para Java, kebab-case para arquivos web
- **Testes automatizados**: Unitários e de integração
- **Padronização de commits**: Mensagens descritivas seguindo padrões convencionais

## 🔒 Segurança

- Senhas criptografadas com BCrypt
- Autenticação baseada em JWT
- Validação de entrada em todas as operações
- Proteção contra ataques CSRF e XSS

## 👥 Contribuição

1. Clone o repositório
2. Crie um branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas alterações (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para o branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes. 