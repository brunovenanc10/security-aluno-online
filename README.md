 <div align="center">

# 🎓 ALUNO ONLINE API

**Uma API REST para gerenciamento acadêmico, com autenticação JWT e acesso protegido aos dados de cada aluno.**

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT-black?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

</div>

---

## 📌 Sobre o projeto

O **Aluno Online** é uma API desenvolvida com Java e Spring Boot para gerenciar informações de um ambiente acadêmico.

O projeto permite realizar operações de cadastro e consulta de alunos, professores, disciplinas e matrículas, além de implementar autenticação e autorização utilizando Spring Security e JWT.

Seu principal objetivo é garantir que cada usuário autenticado tenha acesso somente aos dados acadêmicos vinculados à sua própria conta.

Mais do que desenvolver operações CRUD, o projeto explora conceitos importantes de segurança, persistência de dados, arquitetura em camadas e desenvolvimento de APIs REST.

## ✨ Funcionalidades

- 🔐 **Autenticação JWT:** login com credenciais e geração de tokens.
- 🔑 **Segurança com BCrypt:** armazenamento de senhas com criptografia.
- 👨‍🎓 **Gerenciamento de alunos:** cadastro, listagem, consulta, atualização e exclusão.
- 📚 **Gerenciamento de disciplinas:** cadastro e consulta de disciplinas.
- 👨‍🏫 **Gerenciamento de professores:** operações de cadastro, consulta, atualização e exclusão.
- 📝 **Controle de matrículas:** criação e gerenciamento de matrículas acadêmicas.
- 📊 **Histórico acadêmico:** consulta do histórico e acompanhamento de notas.
- 🔒 **Proteção de dados:** cada conta possui acesso restrito ao aluno e às matrículas vinculadas a ela.
- 📖 **Swagger / OpenAPI:** documentação interativa dos endpoints.

## 🧰 Tecnologias utilizadas

| Tecnologia | Utilização |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot 4.0.3 | Estrutura da aplicação |
| Spring Web MVC | Construção dos endpoints REST |
| Spring Security | Segurança e autenticação |
| Spring Data JPA | Persistência de dados |
| Hibernate | Mapeamento objeto-relacional |
| MySQL | Banco de dados |
| JWT | Autenticação baseada em tokens |
| BCrypt | Criptografia de senhas |
| Maven | Gerenciamento de dependências |
| Lombok | Redução de código boilerplate |
| Springdoc OpenAPI | Documentação da API |

## 🏗️ Arquitetura do projeto

O projeto utiliza uma arquitetura em camadas, separando as responsabilidades para facilitar a manutenção e evolução do código.

```text
Spring_security_AlunoOnline/
│
├── Aluno_Online/
│   └── api/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/br/com/alunoonline/api/
│       │   │   │   ├── controller/
│       │   │   │   ├── dtos/
│       │   │   │   ├── infra/
│       │   │   │   │   └── security/
│       │   │   │   ├── model/
│       │   │   │   ├── repository/
│       │   │   │   └── service/
│       │   │   │
│       │   │   └── resources/
│       │   │       ├── application.properties
│       │   │       └── application-prod.properties
│       │   │
│       │   └── test/
│       │
│       ├── mvnw
│       ├── mvnw.cmd
│       ├── pom.xml
│       └── README.md
│
├── LICENSE
└── README.md
```

**Responsabilidades das camadas:**

- `controller`: recebe e processa as requisições HTTP.
- `dtos`: organiza os dados de entrada e saída.
- `infra/security`: configura o Spring Security, filtros e autenticação JWT.
- `model`: contém as entidades do sistema.
- `repository`: realiza a comunicação com o banco de dados.
- `service`: concentra as regras de negócio.

## 🚀 Como executar o projeto

### 1. Pré-requisitos

Antes de iniciar, certifique-se de possuir:

- Java 21.
- MySQL.
- Git.

O Maven Wrapper já está incluído no projeto.

### 2. Clone o repositório

```bash
git clone https://github.com/Harlen539/Spring_security_AlunoOnline-.git
```

Entre na pasta da API:

```bash
cd Spring_security_AlunoOnline-/Aluno_Online/api
```

### 3. Configure o banco de dados

No MySQL, execute:

```sql
CREATE DATABASE aluno_online_2026;

CREATE USER 'aluno_app'@'localhost'
IDENTIFIED BY 'sua_senha_local';

GRANT ALL PRIVILEGES ON aluno_online_2026.*
TO 'aluno_app'@'localhost';

FLUSH PRIVILEGES;
```

### 4. Configure as variáveis de ambiente

A aplicação utiliza duas variáveis obrigatórias:

- `DB_PASSWORD`: senha do usuário do MySQL.
- `JWT_SECRET`: chave secreta para assinatura dos tokens JWT, com pelo menos 32 bytes.

**Windows (PowerShell):**

```powershell
$env:DB_PASSWORD = "sua_senha_local"

$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$bytes = New-Object byte[] 48
$rng.GetBytes($bytes)
$rng.Dispose()

$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
```

**Linux / macOS:**

```bash
export DB_PASSWORD="sua_senha_local"
export JWT_SECRET="$(openssl rand -base64 48)"
```

⚠️ Nunca compartilhe ou versione suas credenciais e chaves secretas.

### 5. Execute a aplicação

**Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**

```bash
./mvnw spring-boot:run
```

Após a inicialização, a API estará disponível em:

```text
http://localhost:8080
```

## 📖 Documentação Swagger

A documentação interativa pode ser acessada pelo navegador:

**Swagger UI:**

http://localhost:8080/swagger-ui/index.html

**OpenAPI JSON:**

http://localhost:8080/v3/api-docs

Por meio do Swagger, é possível visualizar os endpoints e realizar requisições diretamente pela interface.

## 🔐 Segurança e autenticação

A aplicação utiliza Spring Security com autenticação stateless baseada em JWT.

O fluxo de autenticação funciona da seguinte forma:

1. O usuário realiza seu cadastro.
2. Envia suas credenciais para o endpoint de login.
3. A API valida as informações.
4. Um token JWT é gerado.
5. O cliente utiliza esse token para acessar as rotas protegidas.

**Exemplo de cabeçalho HTTP:**

```http
Authorization: Bearer SEU_TOKEN_JWT
```

**Características de segurança:**

- Senhas armazenadas utilizando BCrypt.
- Tokens JWT com validade de 2 horas.
- Validação dos tokens por filtro de segurança.
- Rotas protegidas contra acessos não autenticados.
- Associação de cada conta a um único cadastro de aluno.
- Verificação de propriedade nos acessos a alunos e matrículas.

O cadastro de usuários, o login e a documentação Swagger são públicos. As demais rotas exigem autenticação.

**Observação:** atualmente, disciplinas e professores são recursos compartilhados. O sistema ainda não possui diferenciação de permissões por perfil, como aluno, professor e administrador.

## 📡 Principais endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | `/usuarios` | Cadastro de usuário |
| POST | `/login` | Autenticação e geração de JWT |
| POST | `/alunos` | Cadastro de aluno |
| GET | `/alunos` | Listagem de alunos da conta |
| GET | `/alunos/{id}` | Consulta de aluno |
| PUT | `/alunos/{id}` | Atualização de aluno |
| DELETE | `/alunos/{id}` | Exclusão de aluno |
| POST | `/matriculas` | Criação de matrícula |
| PATCH | `/matriculas/trancar/{id}` | Trancamento de matrícula |
| PATCH | `/matriculas/atualizar-notas/{id}` | Atualização de notas |
| GET | `/matriculas/historico/{alunoId}` | Consulta de histórico |
| POST | `/disciplinas` | Cadastro de disciplina |
| GET | `/disciplinas` | Listagem de disciplinas |
| GET | `/disciplinas/{id}` | Consulta de disciplina |
| POST | `/professore` | Cadastro de professor |
| GET | `/professore` | Listagem de professores |
| GET | `/professore/{id}` | Consulta de professor |
| PUT | `/professore/{id}` | Atualização de professor |
| DELETE | `/professore/{id}` | Exclusão de professor |

Todas as rotas acima, exceto cadastro e login, exigem autenticação JWT.

## 🧪 Testes

O projeto possui testes automatizados para serviços e segurança.

Para executar os principais testes unitários:

**Windows:**

```powershell
.\mvnw.cmd "-Dtest=TokenServiceTest,AlunoServiceTest,MatriculaAlunoServiceTest" test
```

**Linux / macOS:**

```bash
./mvnw "-Dtest=TokenServiceTest,AlunoServiceTest,MatriculaAlunoServiceTest" test
```

O teste de contexto completo requer uma conexão MySQL e as variáveis de ambiente configuradas.

## 🛣️ Melhorias futuras

- Implementação de níveis de acesso por perfil de usuário.
- Diferenciação das permissões de alunos, professores e administradores.
- Expansão dos testes automatizados.
- Melhorias na validação e tratamento de exceções.
- Ampliação da documentação dos contratos da API.

## 👨‍💻 Autor

Desenvolvido por **Harlen539**.

[GitHub — Harlen539](https://github.com/Harlen539)

---

<div align="center">

**ALUNO ONLINE API**

*Java • Spring Boot • Spring Security • JWT • MySQL*

</div>
