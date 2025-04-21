# Módulo de Usuários

Este módulo contém todas as funcionalidades relacionadas ao gerenciamento de usuários e clientes no sistema Mussarrellos.

## Arquitetura

O módulo segue uma arquitetura baseada em Domain-Driven Design (DDD) e Command Query Responsibility Segregation (CQRS):

```
API → Application (Commands/Queries) → Domain → Infrastructure
```

### Principais componentes

- **Domain**: Contém as entidades, regras de negócio e interfaces de repositório
- **Application**: Contém comandos, queries e seus respectivos handlers
- **Infrastructure**: Contém a implementação dos repositórios e serviços externos
- **API**: Expõe endpoints REST para interação com o módulo

## Padrão Mediator com IUserModule

Implementamos o padrão Mediator usando a interface `IUserModule`, que serve como um ponto de entrada centralizado para executar comandos e consultas no módulo.

### Exemplo de uso no Controller

```java
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final IUserModule userModule;

    @PostMapping
    public Mono<UUID> registerClient(@RequestBody RegisterClientRequest request) {
        return userModule.executeCommand(
            new RegisterClientCommand(request.getEmail(), request.getPassword())
        );
    }
}
```

### Criando novos comandos

1. Crie um comando implementando `ICommand<TResult>` ou `ICommandWithoutResult`:

```java
@Value
public class RegisterClientCommand implements ICommand<UUID> {
    String email;
    String password;
}
```

2. Crie um handler implementando `ICommandHandler<TCommand, TResult>` ou `ICommandWithoutResultHandler<TCommand>`:

```java
@Component
@RequiredArgsConstructor
public class RegisterClientCommandHandler 
    implements ICommandHandler<RegisterClientCommand, UUID> {
    
    private final ClientRepository clientRepository;
    
    @Override
    public Mono<UUID> handle(RegisterClientCommand command) {
        // Implementação
    }
}
```

### Criando novas consultas

1. Crie uma query implementando `IQuery<TResult>`:

```java
@Value
public class GetClientByEmailQuery implements IQuery<ClientDto> {
    String email;
}
```

2. Crie um handler implementando `IQueryHandler<TQuery, TResult>`:

```java
@Component
@RequiredArgsConstructor
public class GetClientByEmailQueryHandler 
    implements IQueryHandler<GetClientByEmailQuery, ClientDto> {
    
    private final ClientRepository clientRepository;
    
    @Override
    public Mono<ClientDto> handle(GetClientByEmailQuery query) {
        // Implementação
    }
}
```

## Boas práticas

1. **Sempre use IUserModule diretamente**: Injetar o `IUserModule` nos controllers e serviços em vez de injetar handlers individuais.
2. **Comandos e queries imutáveis**: Utilize `@Value` ou `record` para garantir a imutabilidade.
3. **Handlers focados**: Cada handler deve fazer apenas uma coisa e fazê-la bem.
4. **Separação clara**: Mantenha comandos e queries em pacotes separados dos seus handlers.
5. **Validação**: Utilize validation annotations nos DTOs de request para validação de entrada. 