# MiniManager Products - PostgreSQL Migration

## 📋 Visão Geral

Microserviço de gestão de produtos migrado para PostgreSQL com suporte multi-tenant.

## 🏗️ Arquitetura

### Stack Tecnológica
- **Java**: 11
- **Spring Boot**: 2.7.18
- **Database**: PostgreSQL 14
- **Build**: Gradle 8.5
- **Containerization**: Docker & Docker Compose

### Estrutura do Banco de Dados

```
merchants (multi-tenant)
  └─> products
       ├─> product_prices (histórico de preços)
       ├─> product_stock_movements (movimentações de estoque)
       ├─> product_images (múltiplas imagens)
       └─> product_composites (kits/combos)
```

## 🚀 Executando o Projeto

### Pré-requisitos
- Docker e Docker Compose instalados
- Gradle 8.5+ (opcional - incluído no wrapper)
- Java 11+

### Opção 1: Docker Compose (Recomendado)

```bash
# Navegar até o diretório do projeto
cd "d:/tools/source/java/projetoaplicado/SP205 - Postagem da Sprint 2/minimanager-products"

# Iniciar todos os serviços
docker-compose up -d

# Ver logs
docker-compose logs -f products-service

# Parar serviços
docker-compose down

# Parar e remover volumes (limpar banco)
docker-compose down -v
```

O banco será automaticamente inicializado com:
- Schema completo (tabelas, índices, triggers)
- Dados de exemplo (1 merchant + 10 produtos)

### Opção 2: Desenvolvimento Local

```bash
# 1. Iniciar apenas o PostgreSQL
docker-compose up -d postgres-products

# 2. Executar a aplicação localmente
./gradlew bootRun

# Ou
./gradlew build
java -jar build/libs/minimanager-products-0.0.1-SNAPSHOT.jar
```

## 📡 Endpoints

### Base URL
- Local: `http://localhost:8082/api/v1`
- Swagger UI: `http://localhost:8082/swagger-ui.html`
- API Docs: `http://localhost:8082/api-docs`

### Merchants
```http
GET    /merchants                      # Listar merchants
GET    /merchants/{id}                 # Buscar por ID
GET    /merchants/search?query=...     # Pesquisar
POST   /merchants                      # Criar
PUT    /merchants/{id}                 # Atualizar
DELETE /merchants/{id}                 # Soft delete
PATCH  /merchants/{id}/status?status=... # Alterar status
```

### Products
```http
GET    /merchants/{merchantId}/products                    # Listar produtos
GET    /merchants/{merchantId}/products/{id}               # Buscar por ID
GET    /merchants/{merchantId}/products/search?query=...   # Pesquisar
GET    /merchants/{merchantId}/products/categories         # Listar categorias
GET    /merchants/{merchantId}/products/category/{name}    # Filtrar por categoria
GET    /merchants/{merchantId}/products/low-stock          # Produtos em falta
POST   /merchants/{merchantId}/products                    # Criar
PUT    /merchants/{merchantId}/products/{id}               # Atualizar
DELETE /merchants/{merchantId}/products/{id}               # Soft delete
PATCH  /merchants/{merchantId}/products/{id}/stock/adjust  # Ajustar estoque
PATCH  /merchants/{merchantId}/products/{id}/stock/add     # Adicionar estoque
PATCH  /merchants/{merchantId}/products/{id}/stock/remove  # Remover estoque
```

## 🗄️ Configuração do Banco

### Dados de Conexão (Docker)
```properties
Host: localhost
Port: 5432
Database: minimanager_products
Username: minimanager_user
Password: minimanager_pass123
```

### Conexão JDBC
```
jdbc:postgresql://localhost:5432/minimanager_products
```

### Conectar via psql
```bash
docker exec -it minimanager-postgres-products psql -U minimanager_user -d minimanager_products
```

## 📊 Funcionalidades

### Gestão de Merchants (Multi-tenant)
- ✅ CRUD completo
- ✅ Pessoa física ou jurídica
- ✅ Dados cadastrais e fiscais (CPF/CNPJ)
- ✅ Endereço completo
- ✅ Status (ACTIVE, INACTIVE, SUSPENDED)
- ✅ Soft delete

### Gestão de Produtos
- ✅ CRUD completo por merchant
- ✅ Categorização
- ✅ Unidades de medida variadas (UN, KG, CX, PCT, etc)
- ✅ Preços (custo, venda, margem automática)
- ✅ Controle de estoque (atual, mínimo, máximo, ponto de reposição)
- ✅ Dimensões e peso
- ✅ Códigos fiscais (NCM, CEST, EAN)
- ✅ Produtos fracionados
- ✅ Produtos compostos (kits)
- ✅ Status (ACTIVE, INACTIVE, DELETED)
- ✅ Soft delete
- ✅ Pesquisa por nome/código/categoria
- ✅ Alerta de estoque baixo

### Histórico de Preços
- ✅ Auditoria completa de alterações
- ✅ Tipos: COST, SALE, PROMOTIONAL
- ✅ Valor anterior/novo
- ✅ Percentual de mudança
- ✅ Motivo da alteração
- ✅ Período de validade
- ✅ Usuário responsável

### Movimentações de Estoque
- ✅ Registro completo de movimentações
- ✅ Tipos: PURCHASE, SALE, ADJUSTMENT, PRODUCTION, LOSS, RETURN, TRANSFER, INVENTORY
- ✅ Quantidade e custo unitário
- ✅ Estoque anterior/novo
- ✅ Referência externa (pedido, nota, etc)
- ✅ Observações
- ✅ Auditoria (usuário, data)

### Imagens de Produtos
- ✅ Múltiplas imagens por produto
- ✅ Tipos: PRODUCT, DETAIL, PACKAGING, QR_CODE
- ✅ Thumbnail automático
- ✅ Ordem de exibição
- ✅ Marcação de imagem principal
- ✅ Metadados (dimensões, tamanho)

### Produtos Compostos (Kits)
- ✅ Montagem de kits/combos
- ✅ Componentes obrigatórios ou opcionais
- ✅ Quantidade por componente
- ✅ Ordem de exibição

## 🔧 Desenvolvimento

### Estrutura do Projeto
```
src/main/java/br/com/minimanager/products/
├── config/             # Configurações Spring
├── controller/         # REST Controllers
├── model/             # Entidades JPA
├── repository/        # Repositories
└── service/           # Regras de negócio
```

### Tecnologias Utilizadas
- **Spring Data JPA**: Persistência
- **PostgreSQL Driver**: Conexão com banco
- **Lombok**: Redução de boilerplate
- **Validation**: javax.validation.constraints
- **SpringDoc OpenAPI**: Documentação automática
- **Hibernate**: ORM

### Build
```bash
# Build do projeto
./gradlew build

# Executar testes
./gradlew test

# Limpar build
./gradlew clean
```

## 📝 Modelo de Dados

### Merchant
- ID (UUID)
- Legal Name / Trading Name
- Tax ID (CPF/CNPJ)
- Person Type (INDIVIDUAL/BUSINESS)
- Contact (Email, Phone, Mobile)
- Address (completo)
- Website
- Status
- Audit fields (created, updated, deleted)

### Product
- ID (UUID)
- Merchant ID (FK)
- Code (único por merchant)
- Name / Description
- Category / Unit
- Prices (cost, sale, profit margin)
- Stock (current, min, max, reorder point)
- Dimensions (weight, length, width, height)
- Fiscal codes (NCM, CEST, EAN)
- Flags (fractional, composite, requires serial)
- Type (PRODUCT/SERVICE/COMPOSITE)
- Status (ACTIVE/INACTIVE/DELETED)
- Audit fields

## 🛡️ Segurança e Boas Práticas

- ✅ UUID para IDs (sem sequência previsível)
- ✅ BigDecimal para valores monetários
- ✅ Soft delete preserva histórico
- ✅ Validação de dados (javax.validation)
- ✅ Triggers automáticos para auditoria
- ✅ Índices otimizados
- ✅ Foreign keys com CASCADE
- ✅ Comentários SQL detalhados
- ✅ Transações gerenciadas

## 📖 Documentação Adicional

- [README-DOCKER.md](README-DOCKER.md) - Guia completo do Docker
- [database-schema.sql](database-schema.sql) - Schema PostgreSQL completo
- [init-scripts/](init-scripts/) - Scripts de inicialização

## 🐛 Troubleshooting

### Erro de conexão com banco
```bash
# Verificar se o container está rodando
docker ps

# Reiniciar PostgreSQL
docker-compose restart postgres-products

# Ver logs do banco
docker-compose logs postgres-products
```

### Limpar e reiniciar do zero
```bash
# Parar tudo e remover volumes
docker-compose down -v

# Limpar build da aplicação
./gradlew clean

# Subir novamente
docker-compose up -d
```

### Verificar schema do banco
```bash
# Conectar no banco
docker exec -it minimanager-postgres-products psql -U minimanager_user -d minimanager_products

# Listar tabelas
\dt

# Descrever tabela
\d products

# Ver dados de exemplo
SELECT * FROM merchants;
SELECT * FROM products LIMIT 5;
```

## 📞 Contato

**Autor**: Marcelino Neto  
**Projeto**: Projeto Aplicado - FIAP  
**Sprint**: 2
