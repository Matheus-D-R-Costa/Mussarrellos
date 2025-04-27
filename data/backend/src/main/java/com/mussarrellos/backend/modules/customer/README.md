# Módulo de Clientes (Customer)

Este módulo gerencia todas as funcionalidades relacionadas aos clientes no sistema Mussarrellos, incluindo cadastro, autenticação e gerenciamento de dados.

## Arquitetura

O módulo segue uma arquitetura hexagonal baseada em Domain-Driven Design (DDD) e Command Query Responsibility Segregation (CQRS):

```
API → Application (Commands/Queries) → Domain → Infrastructure
```

### Estrutura de diretórios

```
customer/
├── api/                 # Controllers e DTOs de requisição
│   └── requests/        # Classes de request para os endpoints
├── application/         # Camada de aplicação
│   ├── commands/        # Comandos e seus handlers
│   ├── contracts/       # Interfaces do módulo
│   ├── dtos/            # DTOs para transferência de dados
│   └── queries/         # Queries e seus handlers
├── domain/              # Camada de domínio
│   ├── entities/        # Entidades de domínio
│   ├── events/          # Eventos de domínio
│   ├── repository/      # Interfaces de repositório
│   ├── rules/           # Regras de negócio
│   └── specifications/  # Especificações de domínio
└── infra/               # Camada de infraestrutura
    └── persistance/     # Implementação de repositórios
```

## Padrão Mediator

O módulo utiliza o padrão Mediator para encapsular a comunicação entre componentes, facilitando o desacoplamento. A interface `ICustomerModule` estende a interface base `IModule` para fornecer a funcionalidade de mediação para este módulo específico.

### Implementação do Mediator

```java
public class CustomerModule extends BaseModule<ICustomerModule> implements ICustomerModule {
    public CustomerModule(ApplicationContext applicationContext) {
        super(applicationContext, ICustomerModule.class);
    }
}
```

### Exemplo de uso no Controller

```java
@RestController
@RequestMapping("/mussarellos/clients")
@RequiredArgsConstructor
public class CustomerController {

    private final Mediator mediator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UUID> registerClient(@RequestBody RegisterCustomerRequest request) {
        return mediator.send(
            new RegisterCustomerCommand(request.email(), request.password())
        );
    }
}
```

## Comandos e Queries

### Comandos

Os comandos representam intenções de modificação do estado do sistema:

```java
public record RegisterCustomerCommand(String email, String password) implements ICommand<UUID> { }

public record ChangeCustomerEmailCommand(UUID id, String newEmail) implements ICommand<Void> { }

public record ChangeCustomerPasswordCommand(UUID id, String currentPassword, 
        String newPassword) implements ICommand<Void> { }
```

### Queries

As queries representam solicitações de informação do sistema:

```java
public record GetCustomerByIdQuery(UUID id) implements IQuery<CustomerDto> { }

public record GetCustomerByEmailQuery(String email) implements IQuery<CustomerDto> { }
```

## Funcionalidades implementadas

O módulo de clientes oferece as seguintes funcionalidades:

1. **Registro de clientes**: Criação de novos clientes com email e senha
2. **Consulta de clientes**: Busca de clientes por ID ou email
3. **Alteração de dados**: Atualização de email e senha do cliente

## Boas práticas

1. **Use o Mediator**: Sempre utilize o mediator para enviar comandos e queries, evitando chamar handlers diretamente
2. **Imutabilidade**: Utilize `record` para garantir a imutabilidade de comandos e queries
3. **Separação de responsabilidades**: Mantenha os handlers focados em uma única tarefa
4. **Validações**: Implemente validações nos DTOs de entrada usando anotações de validação
5. **Eventos de domínio**: Utilize eventos de domínio para comunicação entre módulos 