# Docker Compose - MiniManager Products Service

Este diretório contém a configuração Docker para o microserviço de produtos.

## 📋 Estrutura

```
minimanager-products/
├── docker-compose.yml          # Orquestração dos containers
├── Dockerfile                  # Build da aplicação Spring Boot
├── init-scripts/              # Scripts de inicialização do PostgreSQL
│   ├── 01-schema.sql          # Schema do banco (tabelas, triggers)
│   └── 02-sample-data.sql     # Dados de exemplo (opcional)
└── README-DOCKER.md           # Este arquivo
```

## 🚀 Como usar

### 1️⃣ Subir os containers

```bash
# Na pasta minimanager-products
docker-compose up -d
```

**O que acontece:**
- ✅ PostgreSQL sobe primeiro e executa automaticamente os scripts em `init-scripts/` (apenas na primeira vez)
- ✅ Cria o schema completo (merchants, products, product_prices, etc)
- ✅ Insere dados de exemplo (1 comerciante + 10 produtos + 1 kit)
- ✅ Spring Boot aguarda o PostgreSQL ficar saudável (healthcheck)
- ✅ Aplicação conecta e valida o schema

### 2️⃣ Verificar logs

```bash
# Logs de todos os services
docker-compose logs -f

# Apenas PostgreSQL
docker-compose logs -f postgres-products

# Apenas aplicação
docker-compose logs -f products-service
```

### 3️⃣ Testar a aplicação

```bash
# Health check
curl http://localhost:8082/actuator/health

# Swagger UI
http://localhost:8082/swagger-ui.html

# Listar produtos
curl http://localhost:8082/api/v1/products
```

### 4️⃣ Acessar o banco diretamente

```bash
docker exec -it minimanager-postgres psql -U minimanager -d minimanager_products

# Comandos úteis no psql:
\dt                          # Listar tabelas
\d products                  # Descrever tabela products
SELECT * FROM products;      # Consultar produtos
\q                           # Sair
```

### 5️⃣ Parar os containers

```bash
# Parar mas manter os dados
docker-compose stop

# Parar e remover containers (dados persistem no volume)
docker-compose down

# Remover TUDO incluindo volumes (⚠️ apaga dados!)
docker-compose down -v
```

## 🔧 Configurações

### Banco de Dados

| Variável | Valor |
|----------|-------|
| Host | `localhost` (fora) / `postgres-products` (dentro) |
| Porta | `5432` |
| Database | `minimanager_products` |
| Usuário | `minimanager` |
| Senha | `minimanager123` |

### Aplicação

| Variável | Valor |
|----------|-------|
| Porta | `8082` |
| Profile | `docker` |
| Log Level | `DEBUG` (br.com.minimanager) |

## 📦 Volumes

```bash
# Ver volumes criados
docker volume ls | grep minimanager

# Inspecionar volume de dados
docker volume inspect minimanager-products_postgres_data

# Backup do banco (exemplo)
docker exec minimanager-postgres pg_dump -U minimanager minimanager_products > backup.sql
```

## 🔄 Rebuild da aplicação

```bash
# Rebuild forçado
docker-compose up -d --build

# Rebuild apenas do service específico
docker-compose build products-service
docker-compose up -d products-service
```

## 🐛 Troubleshooting

### PostgreSQL não inicializa

```bash
# Ver logs detalhados
docker-compose logs postgres-products

# Entrar no container
docker exec -it minimanager-postgres sh

# Verificar arquivos de init
ls -la /docker-entrypoint-initdb.d/
```

### Aplicação não conecta no banco

```bash
# Verificar se PostgreSQL está saudável
docker-compose ps

# Testar conexão manualmente
docker exec -it minimanager-products-service sh
wget -O- http://postgres-products:5432 # Deve receber dados
```

### Recriar schema do zero

```bash
# Parar tudo e remover volumes
docker-compose down -v

# Subir novamente (scripts serão executados de novo)
docker-compose up -d
```

## 🎯 Próximos passos

- [ ] Adicionar script de migração (Flyway/Liquibase)
- [ ] Configurar backup automático do PostgreSQL
- [ ] Adicionar Redis para cache
- [ ] Configurar monitoring (Prometheus/Grafana)
- [ ] Adicionar nginx como reverse proxy

## 📚 Documentação útil

- [Docker Compose](https://docs.docker.com/compose/)
- [PostgreSQL Docker](https://hub.docker.com/_/postgres)
- [Spring Boot Docker](https://spring.io/guides/topicals/spring-boot-docker/)
