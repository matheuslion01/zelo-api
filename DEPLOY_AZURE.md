# Provisionamento Azure SQL + Azure Container Apps

Mesmo roteiro usado em aula, adaptado para o Zelo. Troque `[rm]` pelo RM de um integrante e `<usuario-dockerhub>` pelo usuário do Docker Hub.

## 1. Variáveis

### PowerShell

```powershell
$env:RESOURCE_GROUP="rg-[rm]-zelo-api"
$env:SQL_SERVER="[rm]-sql-zelo-api"
$env:APP_CONTAINER="[rm]-zelo-api"
$env:CONTAINERAPPS_ENVIRONMENT="[rm]-zelo-api"

$env:DB_SERVER_URL="[rm]-sql-zelo-api.database.windows.net"
$env:DB_SERVER_PORT="1433"
$env:DB_SCHEMA="zelodb"
$env:DB_USER="sqladmin"
$env:DB_PWD="<senha>"

$env:SPRING_PROFILES_ACTIVE="prd"
$env:DOCKER_IMAGE="<usuario-dockerhub>/zelo-api"
```

### Bash

```bash
export RESOURCE_GROUP=rg-[rm]-zelo-api
export SQL_SERVER=[rm]-sql-zelo-api
export APP_CONTAINER=[rm]-zelo-api
export CONTAINERAPPS_ENVIRONMENT=[rm]-zelo-api

export DB_SERVER_URL=[rm]-sql-zelo-api.database.windows.net
export DB_SERVER_PORT=1433
export DB_SCHEMA=zelodb
export DB_USER=sqladmin
export DB_PWD="<senha>"

export SPRING_PROFILES_ACTIVE=prd
export DOCKER_IMAGE=<usuario-dockerhub>/zelo-api
```

## 2. Criar Resource Group

```bash
az group create \
  --name $RESOURCE_GROUP \
  --location brazilsouth
```

## 3. Criar SQL Server

```bash
az sql server create \
  --name $SQL_SERVER \
  --resource-group $RESOURCE_GROUP \
  --location brazilsouth \
  --admin-user $DB_USER \
  --admin-password $DB_PWD
```

## 4. Criar SQL Database

```bash
az sql db create \
  --name $DB_SCHEMA \
  --resource-group $RESOURCE_GROUP \
  --server $SQL_SERVER \
  --edition GeneralPurpose \
  --family Gen5 \
  --capacity 2 \
  --compute-model Serverless \
  --use-free-limit \
  --free-limit-exhaustion-behavior AutoPause
```

## 5. Criar regra de Firewall

```bash
az sql server firewall-rule create \
  --resource-group $RESOURCE_GROUP \
  --server $SQL_SERVER \
  --name AllowAll \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 255.255.255.255
```

> Esta regra permite conexões de qualquer endereço IPv4. Use apenas para desenvolvimento/testes.

## 6. Criar Container Apps Environment

```bash
az containerapp env create \
  --name "$CONTAINERAPPS_ENVIRONMENT" \
  --resource-group "$RESOURCE_GROUP" \
  --location brazilsouth
```

## 7. Criar Container App

```bash
az containerapp create \
  --name "$APP_CONTAINER" \
  --resource-group "$RESOURCE_GROUP" \
  --environment "$CONTAINERAPPS_ENVIRONMENT" \
  --image "$DOCKER_IMAGE" \
  --ingress external \
  --target-port 8080 \
  --min-replicas 1 \
  --max-replicas 1 \
  --secrets "db-pwd=$DB_PWD" \
  --env-vars \
    DB_SERVER_URL="$DB_SERVER_URL" \
    DB_SERVER_PORT="$DB_SERVER_PORT" \
    DB_SCHEMA="$DB_SCHEMA" \
    DB_USER="$DB_USER" \
    DB_PWD="secretref:db-pwd" \
    SPRING_PROFILES_ACTIVE="$SPRING_PROFILES_ACTIVE"
```

## 8. Obter URL da aplicação

```bash
az containerapp show \
  --name "$APP_CONTAINER" \
  --resource-group "$RESOURCE_GROUP" \
  --query properties.configuration.ingress.fqdn \
  --output tsv
```

A URL retornada é a que vai no README e no TXT de entrega (`https://<fqdn>`).

## 9. Verificar status da aplicação

```bash
az containerapp show \
  --name "$APP_CONTAINER" \
  --resource-group "$RESOURCE_GROUP" \
  --query "properties.runningStatus" \
  --output tsv
```

## 10. Forçar redeploy (após nova imagem no Docker Hub)

Bash:

```bash
az containerapp update \
  --name "$APP_CONTAINER" \
  --resource-group "$RESOURCE_GROUP" \
  --image "$DOCKER_IMAGE"
```

PowerShell:

```powershell
az containerapp update `
  --name "$env:APP_CONTAINER" `
  --resource-group "$env:RESOURCE_GROUP" `
  --image "$env:DOCKER_IMAGE" `
  --revision-suffix "$(Get-Date -Format 'yyyyMMddHHmmss')"
```

## 11. Consultar logs

```bash
az containerapp logs show \
  --name "$APP_CONTAINER" \
  --resource-group "$RESOURCE_GROUP" \
  --type console \
  --tail 50
```

## 12. Estrutura dos recursos

```text
Azure
│
└── Resource Group: rg-[rm]-zelo-api
    │
    ├── SQL Server
    │   └── [rm]-sql-zelo-api
    │       └── Database: zelodb
    │
    └── Container Apps Environment
        └── [rm]-zelo-api
            └── Container App
                └── [rm]-zelo-api
                    └── Image: <usuario-dockerhub>/zelo-api
```
