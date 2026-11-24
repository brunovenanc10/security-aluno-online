# Aluno Online API

## Requisitos

- Java 21
- Maven Wrapper (incluído)
- MySQL disponível em `localhost:3306`

## Configuração local

Crie o banco `aluno_online_2026` no MySQL e um usuário local `aluno_app` com permissões nesse banco. Defina `DB_PASSWORD` com a senha desse usuário e `JWT_SECRET` com pelo menos 32 bytes aleatórios no mesmo PowerShell usado para iniciar a API:

```powershell
$env:DB_PASSWORD = 'sua-senha-local-do-mysql'
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$bytes = New-Object byte[] 48
$rng.GetBytes($bytes)
$rng.Dispose()
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
.\mvnw.cmd spring-boot:run
```

Não salve senhas, tokens ou o valor de `JWT_SECRET` no repositório. A aplicação falha ao iniciar se a chave JWT não estiver configurada ou tiver menos de 32 bytes.

## Login e autorização

`POST /usuarios` é público e cadastra contas com senha BCrypt. `POST /login` autentica com `login` e `senha` e retorna `{ "token": "..." }`. O JWT dura duas horas. Para chamar rotas autenticadas, envie `Authorization: Bearer <token>`.

Login de exemplo:

```http
POST /login
Content-Type: application/json

{"login":"seu-login","senha":"sua-senha"}
```

Cadastro de exemplo:

```http
POST /usuarios
Content-Type: application/json

{"login":"seu-login","senha":"uma-senha-forte"}
```

As rotas de Swagger (`/v3/api-docs/**`, `/swagger-ui.html`, `/swagger-ui/**`) também são públicas. As demais rotas exigem autenticação.

## Vínculo e acesso a dados de aluno

Cada conta pode ter um único cadastro de aluno. Depois de fazer login, envie os dados do aluno para `POST /alunos`; a API associa o registro à conta do token e ignora qualquer tentativa de informar outro proprietário. Listagem, consulta, edição e exclusão de alunos verificam a conta autenticada.

Matrículas novas são vinculadas ao aluno da conta. Consulta de histórico, trancamento e alteração de notas verificam se a matrícula pertence a esse aluno. Dados antigos de aluno precisam ser associados às contas corretas no banco antes de ficarem acessíveis.

Disciplinas e professores continuam compartilhados. O sistema ainda não diferencia contas de aluno, professor ou administrador; assim, não há autorização por perfil nessas operações.

## Testes

Execute os testes unitários que não precisam do MySQL com:

```powershell
.\mvnw.cmd "-Dtest=TokenServiceTest,AlunoServiceTest,MatriculaAlunoServiceTest" test
```

O teste de contexto completo precisa de um MySQL disponível e das variáveis `DB_PASSWORD` e `JWT_SECRET` configuradas.
