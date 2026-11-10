# Zelo — Zeladoria Colaborativa · API REST

Global Solution 2026 · FIAP · Turma 3SIR · Microservice and Web Engineering & IT Services

| Entregável | Link |
|---|---|
| API no Azure | `https://<fqdn-do-container-app>` *(preencher)* |
| Docker Hub | `https://hub.docker.com/r/<usuario-dockerhub>/zelo-api` *(preencher)* |
| Workflow | aba **Actions** deste repositório → *Continuous Delivery* |

## Integrantes

| Nome | RM |
|---|---|
| *(preencher)* | *(preencher)* |

---

## 📌 Objetivo da solução

O **Zelo** é uma plataforma de zeladoria colaborativa. Moradores registram problemas dos espaços públicos do bairro (buracos, iluminação apagada, lixo acumulado, calçadas quebradas), outros moradores **confirmam** as ocorrências e o sistema **calcula uma prioridade**, para que a equipe de zeladoria saiba onde agir primeiro. O status de cada ocorrência é acompanhado até a resolução, quando o morador avalia o atendimento.

Esta API é o microsserviço central da solução, desenvolvida em **Java com Spring Boot** e persistência em **Microsoft SQL Server**.

## 🧱 Arquitetura

```text
Controller  →  Service  →  Repository  →  SQL Server
   (DTO)      (regras)     (JPA)
```

| Pacote | Responsabilidade |
|---|---|
| `controller` | Endpoints REST (`api/v1/...`). Usam apenas Services e Mappers |
| `service` | Regras de negócio (prioridade, confirmações, resolução) |
| `repository` | Acesso ao SQL Server com `JpaRepository` |
| `model` | Entidades JPA e enums |
| `dto` | Request DTOs, Response DTOs e Mappers (ModelMapper) |

### Entidades e tabelas

| Entidade | Tabela | Relacionamentos |
|---|---|---|
| `Bairro` | `bairros` | — |
| `Morador` | `moradores` | pertence a um Bairro |
| `Ocorrencia` | `ocorrencias` | registrada por um Morador, em um Bairro |
| `Confirmacao` | `confirmacoes` | de um Morador para uma Ocorrência |

### Regras de negócio

- **Prioridade (0 a 100)** = gravidade da categoria (Alagamento 35, Buraco e Árvore 30, Iluminação e Calçada 25, Lixo e Sinalização 20, Outros 10) + 5 por confirmação (máx. 40) + 15 se estiver perto de escola, hospital ou ponto de ônibus + 10 se o bairro for periférico.
- Toda ocorrência começa como `REGISTRADA`. Ao mudar para `RESOLVIDA`, a data de resolução é registrada.
- A nota de avaliação (1 a 5) só é aceita quando o status é `RESOLVIDA`.
- O autor não pode confirmar a própria ocorrência, e ocorrências resolvidas não recebem confirmações.
- Morador `anonimo` aparece como "Anônimo" nas respostas (LGPD).

## 🔗 Endpoints

A documentação Swagger abre na **raiz** da aplicação (`/`).

| Recurso | POST | GET | GET /{id} | PUT /{id} | DELETE /{id} |
|---|---|---|---|---|---|
| `/api/v1/bairros` | ✔ | ✔ | ✔ | ✔ | ✔ |
| `/api/v1/moradores` | ✔ | ✔ | ✔ | ✔ | ✔ |
| `/api/v1/ocorrencias` | ✔ | ✔ | ✔ | ✔ | ✔ |
| `/api/v1/confirmacoes` | ✔ | ✔ | ✔ | ✔ | ✔ |

| Código | Quando |
|---|---|
| `201 Created` | Registro criado |
| `200 OK` | Consulta ou atualização |
| `204 No Content` | Exclusão |
| `400 Bad Request` | Dados inválidos ou regra de negócio violada |
| `404 Not Found` | Registro inexistente (inclusive ids informados no corpo) |
| `409 Conflict` | Exclusão de registro que ainda está em uso (ex.: bairro com moradores) |

---

## 📋 Pré-requisitos

- Java 17
- Maven (ou o Maven Wrapper incluído)
- SQL Server
- Docker (opcional)

## 🗄️ Configuração do SQL Server

1. Tenha um SQL Server em execução (local ou Azure SQL).
2. Crie o banco:

```sql
CREATE DATABASE zelodb;
```

As **tabelas são criadas automaticamente** pelo Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

## 🔐 Variáveis de ambiente

| Variável | Descrição | Exemplo |
|---|---|---|
| `DB_SERVER_URL` | Endereço do SQL Server | `localhost` |
| `DB_SERVER_PORT` | Porta | `1433` |
| `DB_SCHEMA` | Nome do banco | `zelodb` |
| `DB_USER` | Usuário | `sa` |
| `DB_PWD` | Senha | `sua_senha` |
| `SPRING_PROFILES_ACTIVE` | Profile (`dev` ou `prd`) | `dev` |

> Não versione credenciais. Use um arquivo `.env` local (já no `.gitignore`) a partir do `.env.example`.

## 🚀 Execução local (Spring Boot)

### Linux / macOS

```sh
export DB_SERVER_URL=localhost
export DB_SERVER_PORT=1433
export DB_SCHEMA=zelodb
export DB_USER=sa
export DB_PWD=sua_senha
export SPRING_PROFILES_ACTIVE=dev

./mvnw spring-boot:run
```

### Windows PowerShell

```powershell
$env:DB_SERVER_URL="localhost"
$env:DB_SERVER_PORT="1433"
$env:DB_SCHEMA="zelodb"
$env:DB_USER="sa"
$env:DB_PWD="sua_senha"
$env:SPRING_PROFILES_ACTIVE="dev"

.\mvnw.cmd spring-boot:run
```

A aplicação sobe em `http://localhost:8080` (abre o Swagger).

## 🐳 Execução com Docker

```sh
docker build -t zelo-api:1.0 .

docker run \
  -p 8080:8080 \
  -e DB_SERVER_URL=host.docker.internal \
  -e DB_SERVER_PORT=1433 \
  -e DB_SCHEMA=zelodb \
  -e DB_USER=sa \
  -e DB_PWD=sua_senha \
  -e SPRING_PROFILES_ACTIVE=dev \
  zelo-api:1.0
```

> `host.docker.internal` permite que o container acesse o SQL Server da máquina host.

## 📦 Docker Hub

| Item | Valor |
|---|---|
| Imagem | `<usuario-dockerhub>/zelo-api` |
| Tags | `latest`, `v<número da execução>`, `<commit curto>` |

```sh
docker pull <usuario-dockerhub>/zelo-api:latest
```

## ⚙️ Continuous Delivery (GitHub Actions)

Arquivo: `.github/workflows/continuous_delivery.yml`

A cada **push na branch `main`**, o workflow faz checkout, autentica no Docker Hub, constrói a imagem (o `Dockerfile` compila a aplicação com Maven) e publica com as tags `latest`, `v<número>` e `<commit>`.

Configuração em *Settings → Secrets and variables → Actions*:

| Tipo | Nome | Valor |
|---|---|---|
| Variable | `DOCKER_USER` | usuário do Docker Hub |
| Secret | `DOCKER_TOKEN` | token de acesso do Docker Hub (Read & Write) |

## ☁️ Azure

A API roda no **Azure Container Apps** com a imagem do Docker Hub, conectada a um **Azure SQL Database**. Passo a passo: [`DEPLOY_AZURE.md`](DEPLOY_AZURE.md).

URL: `https://<fqdn-do-container-app>` *(preencher)*

## 🧪 Como testar

Abra a URL da API: a raiz mostra o Swagger. Execute nesta ordem (use os ids retornados em cada resposta):

1. `POST /api/v1/bairros`

```json
{ "nome": "Capão Redondo", "cidade": "São Paulo", "zona": "SUL", "periferico": true, "populacaoEstimada": 275000 }
```

2. `POST /api/v1/moradores` (crie dois moradores)

```json
{ "nome": "Ana Souza", "email": "ana@email.com", "telefone": "11987654321", "anonimo": false, "idBairro": 1 }
```

3. `POST /api/v1/ocorrencias` com o primeiro morador

```json
{
  "titulo": "Buraco grande na faixa da direita",
  "descricao": "Perto do ponto de ônibus",
  "categoria": "BURACO",
  "latitude": -23.6693,
  "longitude": -46.7809,
  "proximoServicoEssencial": true,
  "idMorador": 1,
  "idBairro": 1
}
```

Resposta `201` com `"prioridade": 55` (30 + 15 + 10).

4. `POST /api/v1/confirmacoes` com o **segundo** morador → `201`, e a prioridade da ocorrência sobe para 60.

```json
{ "idOcorrencia": 1, "idMorador": 2, "comentario": "Continua lá", "problemaPersiste": true }
```

5. `PUT /api/v1/ocorrencias/1` para resolver e avaliar → `200` com `dataResolucao` preenchida.

```json
{
  "titulo": "Buraco grande na faixa da direita",
  "categoria": "BURACO",
  "latitude": -23.6693,
  "longitude": -46.7809,
  "proximoServicoEssencial": true,
  "status": "RESOLVIDA",
  "notaAvaliacao": 5
}
```

6. Casos de erro:
   - `GET /api/v1/bairros/999` → `404`
   - `POST /api/v1/bairros` com `{ "nome": "" }` → `400`
   - confirmação feita pelo próprio autor → `400`
   - `DELETE /api/v1/bairros/1` com moradores vinculados → `409`

7. **Persistência:** reinicie a aplicação e consulte de novo — os dados continuam no SQL Server.
