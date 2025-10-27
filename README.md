# Boletim — Spring Boot (MVC) + Angular

Backend em Spring Boot (MVC, Spring Data JPA, H2) e frontend em Angular 20.

- Swagger: http://localhost:8080/swagger-ui/index.html

## Sumário
- Pré-requisitos
- Como executar
  - Backend
  - Frontend
- Seed de dados
- Estrutura do projeto (resumo)
- Principais decisões técnicas
- Comandos rápidos

---

## Pré-requisitos
- Java 17+ (ou versão compatível com pom.xml)
- Maven
- Node 22+ e npm
- Angular CLI (opcional): `npm i -g @angular/cli`

---

## Como executar

### Backend
1. Abrir terminal:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
2. O backend inicia com H2 embutido. O seed de dados é carregado automaticamente via CommandLineRunner (ver seção "Seed de dados").

### Frontend
1. Abrir outro terminal:
   ```bash
   cd frontend
   npx ng serve --open
   ```
2. O comando abre o app Angular no navegador (por padrão http://localhost:4200).

---

## Seed de dados
Os dados iniciais (alunos, turmas, disciplinas, avaliações e lançamentos) são inseridos automaticamente ao iniciar a aplicação backend via CommandLineRunner (implementação no projeto). Basta executar `mvn spring-boot:run` para ter dados no banco H2 para demonstração.

---

## Estrutura do projeto (resumo)

Backend (modular, em camadas)
```
backend/src/main/java/rheserva/escola/boletim
├── BoletimApplication.java
├── controller
│   ├── AlunoController.java
│   └── BoletimController.java
├── dto
│   ├── AlunoDTO.java
│   ├── AlunoNotasDTO.java
│   ├── AvaliacaoDTO.java
│   ├── BoletimGridDTO.java
│   ├── DisciplinaDTO.java
│   ├── LancamentoBatchRequest.java
│   ├── NotaDTO.java
│   └── TurmaDTO.java
├── model
│   ├── Aluno.java
│   ├── Avaliacao.java
│   ├── AvaliacaoLancamento.java
│   ├── Disciplina.java
│   └── Turma.java
├── repository
│   ├── AlunoRepository.java
│   ├── AvaliacaoLancamentoRepository.java
│   ├── AvaliacaoRepository.java
│   ├── DisciplinaRepository.java
│   └── TurmaRepository.java
└── util
    └── MediaUtil.java
```

Recursos/Tests
```
backend/src/main/resources
├── application.properties
└── (static, templates...)
backend/test
└── tests unitários e Testcontainers config
```

Frontend (modular, serviços e componentes)
```
frontend/src/
├── app
│   ├── app.config.ts
│   ├── app.html
│   ├── app.module.ts
│   ├── app.routes.ts
│   ├── app.scss
│   ├── app.spec.ts
│   ├── app.ts
│   ├── components
│   │   ├── grade
│   │   │   ├── grade.html
│   │   │   ├── grade.scss
│   │   │   ├── grade.spec.ts
│   │   │   └── grade.ts
│   │   └── selection
│   │       ├── selection.html
│   │       ├── selection.scss
│   │       ├── selection.spec.ts
│   │       └── selection.ts
│   ├── models
│   │   └── models.ts
│   └── services
│       ├── api.spec.ts
│       └── api.ts
├── index.html
├── main.ts
└── styles.scss
```

---

## Principais decisões técnicas

Arquitetura
- Backend em camadas: controller → (service) → repository → model, seguindo MVC para separar responsabilidades.
- Frontend modular: componentes por responsabilidade e serviços para comunicação com API (fácil divisão em módulos Angular quando escalar).

Persistência
- Spring Data JPA para repositórios e consultas.
- H2 em memória para desenvolvimento e testes.
- Seed via CommandLineRunner para inicialização automática de dados.

API e DTOs
- Uso de DTOs para desacoplar entidades de persistência da API (evita expor entidades JPA e facilita validação/versionamento).
- DTOs especializados para operações de leitura/grids e lançamentos em lote (LancamentoBatchRequest, BoletimGridDTO).

Formulários e UX
- Frontend preparado para formulários com validação (ex.: notas 0–10, campos obrigatórios), Template-driven forms.

Organização para escalabilidade
- Backend: cada domínio com seus pacotes (model, dto, repository, controller) para fácil adição de services e mappers (ex.: MapStruct).
- Frontend: services para API, components para UI, models para tipagem — pronto para dividir em módulos (GradeModule, SelectionModule).

Testes
- Testes unitários no backend.

---

## Comandos rápidos
- Rodar backend:
  ```bash
  cd backend
  mvn spring-boot:run
  ```
- Rodar frontend:
  ```bash
  cd frontend
  npx ng serve --open
  ```
- Rodar docker:
  ```bash
  docker compose up
  ```
- h2 console
  ```bash
  Path: http://localhost:8080/h2-console/
  Driver Class: org.h2.Driver
  JDBC URL: jdbc:h2:mem:testdb
  User Name: sa
  Password:
  ```
