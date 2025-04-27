# Mussarrellos - Sistema de Pizzaria Online

![Logotipo](https://via.placeholder.com/150x150.png?text=Mussarrellos)

> Sistema completo para gerenciamento de pedidos de pizzaria, incluindo interface de cliente
>
> **NOTA:** Este projeto foi descontinuado por falta de tempo do desenvolvedor. Alguns módulos do backend e o frontend completo ainda não foram implementados, assim como a configuração do Nginx.

## 📋 Visão Geral

Mussarrellos é um sistema completo para pizzarias que desejam oferecer serviços online. Desenvolvido com arquitetura moderna, o sistema permite que clientes façam pedidos de pizza, acompanhem o status de seus pedidos, e que a administração da pizzaria gerencie o catálogo de produtos e pedidos de forma eficiente.

## 🏗️ Arquitetura

O projeto é estruturado em três componentes principais:

### 1. Backend (Java Spring Boot)

Backend desenvolvido seguindo princípios de **Domain-Driven Design (DDD)** e **Clean Architecture**, proporcionando uma separação clara de responsabilidades e facilitando a evolução do sistema.

**Principais características:**
- Programação reativa com Spring WebFlux
- Implementação de padrões como Repository, Adapter e Value Objects
- Validação de regras de negócio explícitas
- Conexão com banco de dados PostgreSQL via R2DBC

**Organização em módulos:**
- `modules/authentication`: Gerenciamento de autenticação
- `modules/customer`: Gerenciamento de clientes
- `modules/product`: Catálogo de produtos (pizzas e tamanhos)
- `modules/checkout`: Gerenciamento de pedidos e pagamentos
- `modules/cryptography`: Serviços de criptografia

### 2. Frontend (Não implementado completamente)

Interface de usuário planejada para permitir que clientes realizem pedidos e acompanhem seu status.

**Estrutura planejada:**
- Organização em diretórios funcionais (app, styles, static)
- Design responsivo para funcionamento em dispositivos móveis e desktop

### 3. Infraestrutura

**Componentes configurados:**
- PostgreSQL: Banco de dados principal
- pgAdmin: Interface de administração do banco de dados
- Configurações Docker para facilitar o desenvolvimento

**Componentes planejados mas não implementados:**
- Nginx: Para servir o frontend e fazer proxy reverso para o backend

## 🚀 Funcionalidades Planejadas

### Para Clientes
- Cadastro e autenticação
- Navegação pelo catálogo de pizzas
- Personalização de pedidos (tamanho, quantidade)
- Acompanhamento do status do pedido
- Histórico de pedidos anteriores

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 23**
- **Spring Boot 3.4.4**
- **Spring WebFlux** (programação reativa)
- **Spring Data R2DBC**
- **Spring Security**
- **MapStruct** para mapeamentos
- **Lombok** para redução de boilerplate
- **Resilience4j** para implementação de circuit breakers

### Banco de Dados
- **PostgreSQL 15**
- **pgAdmin 4** (interface de administração)

### DevOps
- **Docker & Docker Compose**
- **Vagrant** (para ambientes de desenvolvimento)

## 📦 Estrutura Atual do Projeto

```
mussarrellos/
├── data/                          # Diretório principal
│   ├── backend/                   # Código-fonte do backend
│   │   └── src/
│   │       └── main/
│   │           ├── java/com/mussarrellos/backend/
│   │           │   ├── buildingblocks/      # Componentes base da arquitetura
│   │           │   ├── modules/            # Módulos do sistema
│   │           │   │   ├── authentication/  # Módulo de autenticação
│   │           │   │   ├── customer/        # Módulo de clientes
│   │           │   │   ├── product/         # Módulo de produtos
│   │           │   │   ├── checkout/        # Módulo de checkout
│   │           │   │   └── cryptography/    # Serviços de criptografia
│   │           │   └── BackendApplication.java # Ponto de entrada da aplicação
│   │           └── resources/
│   ├── docker-compose.yml         # Configuração Docker para ambiente de desenvolvimento
│   └── frontend/                  # Estrutura inicial do frontend (incompleto)
│       ├── app/                   # Lógica da aplicação
│       ├── styles/                # Folhas de estilo CSS
│       └── static/                # Recursos estáticos
├── Vagrantfile                    # Configuração do Vagrant para desenvolvimento
└── README.md                      # Documentação do projeto
```

## ⚙️ Configuração e Execução

### Pré-requisitos
- Java 23
- Docker e Docker Compose
- PostgreSQL 15+ (ou usar a versão containerizada)

### Configuração do Ambiente com Docker
```bash
# Na raiz do projeto
cd data
# Crie um arquivo .env com as configurações necessárias
# Exemplo:
# DB_USER=mussarrellos
# DB_PASSWORD=mussarrellos
# DB_NAME=mussarrellos
# PGADMIN_DEFAULT_EMAIL=admin@example.com
# PGADMIN_DEFAULT_PASSWORD=admin
docker-compose up -d
```

### Backend (Desenvolvimento)
```bash
cd data/backend
./gradlew bootRun
```

## 📝 Convenções e Padrões de Código

### Gerais
- **Clean Code**: Código legível e bem documentado
- **SOLID**: Princípios de design de software
- **DRY**: Don't Repeat Yourself
- **KISS**: Keep It Simple, Stupid

### Específicas
- **Domain-Driven Design**: Entidades, Agregados, Value Objects, Serviços de Domínio
- **Arquitetura Modular**: Utilizando Spring Modulith para modularização
- **Programação Reativa**: Utilizando WebFlux para operações não-bloqueantes
- **Padronização de commits**: Mensagens descritivas seguindo padrões convencionais

## 🔒 Segurança Planejada

- Autenticação com Spring Security
- Proteção de endpoints
- Validação de entrada em todas as operações
- Criptografia para dados sensíveis

## 👥 Status do Projeto e Contribuição

Este projeto está atualmente **descontinuado** devido a restrições de tempo do desenvolvedor original. Vários módulos do backend e o frontend completo não foram implementados.

Caso deseje continuar o desenvolvimento:

1. Clone o repositório
2. Crie um branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas alterações (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para o branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes. 