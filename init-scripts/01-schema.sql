-- ============================================
-- MiniManager Intelligent - Database Schema
-- PostgreSQL 14+
-- ============================================

-- Extension for UUID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Extension for trigram similarity search (text search optimization)
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- ============================================
-- TABLE: MERCHANTS
-- Objetivo: Gerenciar dados dos comerciantes/empresas
-- Funcionalidade: Armazena informações cadastrais, fiscais e de contato
-- dos comerciantes que utilizam o sistema (suporte multi-tenant)
-- ============================================
CREATE TABLE merchants (
    -- Identificador único do comerciante (gerado automaticamente)
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    
    -- Razão social da empresa
    legal_name VARCHAR(200) NOT NULL,
    
    -- Nome fantasia (nome comercial)
    trading_name VARCHAR(200),
    
    -- CNPJ (pessoa jurídica) ou CPF (pessoa física/MEI)
    tax_id VARCHAR(20) UNIQUE NOT NULL,
    
    -- Tipo de pessoa (INDIVIDUAL=Pessoa Física/MEI, BUSINESS=Pessoa Jurídica)
    person_type VARCHAR(20) DEFAULT 'BUSINESS' CHECK (person_type IN ('INDIVIDUAL', 'BUSINESS')),
    
    -- Email principal de contato
    email VARCHAR(150) NOT NULL,
    
    -- Telefone principal
    phone VARCHAR(20),
    
    -- Telefone celular
    mobile VARCHAR(20),
    
    -- Website
    website VARCHAR(200),
    
    -- Endereço completo
    address_street VARCHAR(200),
    address_number VARCHAR(20),
    address_complement VARCHAR(100),
    address_neighborhood VARCHAR(100),
    address_city VARCHAR(100),
    address_state VARCHAR(2),
    address_postal_code VARCHAR(10),
    address_country VARCHAR(3) DEFAULT 'BRA',
    
    -- Status do comerciante (ACTIVE=ativo, INACTIVE=inativo, SUSPENDED=suspenso)
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED')),
    
    -- Logo da empresa (URL)
    logo_url TEXT,
    
    -- Observações gerais
    notes TEXT,
    
    -- Auditoria
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Índice para busca por CNPJ/CPF
CREATE INDEX idx_merchants_tax_id ON merchants(tax_id);

-- Índice para filtrar por status
CREATE INDEX idx_merchants_status ON merchants(status) WHERE deleted_at IS NULL;

-- Índice para busca por nome
CREATE INDEX idx_merchants_legal_name ON merchants(legal_name);

COMMENT ON TABLE merchants IS 'Cadastro de comerciantes/empresas que utilizam o sistema';
COMMENT ON COLUMN merchants.id IS 'Identificador único UUID do comerciante';
COMMENT ON COLUMN merchants.legal_name IS 'Razão social';
COMMENT ON COLUMN merchants.trading_name IS 'Nome fantasia';
COMMENT ON COLUMN merchants.tax_id IS 'CNPJ ou CPF';
COMMENT ON COLUMN merchants.person_type IS 'Tipo de pessoa (INDIVIDUAL, BUSINESS)';
COMMENT ON COLUMN merchants.email IS 'Email de contato';
COMMENT ON COLUMN merchants.phone IS 'Telefone principal';
COMMENT ON COLUMN merchants.status IS 'Status (ACTIVE, INACTIVE, SUSPENDED)';
COMMENT ON COLUMN merchants.deleted_at IS 'Data de exclusão lógica (soft delete)';


-- ============================================
-- TABLE: PRODUCTS
-- Objetivo: Gerenciar o catálogo completo de produtos/serviços
-- Funcionalidade: Controla informações básicas, preços, estoque, 
-- medidas e identificação fiscal de cada produto
-- ============================================
CREATE TABLE products (
    -- Identificador único do produto (gerado automaticamente)
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    
    -- Código interno do produto definido pelo comerciante (único)
    code VARCHAR(50) UNIQUE NOT NULL,
    
    -- Nome do produto (obrigatório, usado em buscas)
    name VARCHAR(200) NOT NULL,
    
    -- Descrição detalhada do produto (opcional)
    description TEXT,
    
    -- Categoria do produto (FOOD, BEVERAGE, SERVICE, CRAFT, OTHER)
    category VARCHAR(50),
    
    -- Código do grupo de produtos para agrupamento (ex: BOLOS, SALGADOS)
    group_code VARCHAR(50),
    
    -- Unidade de medida (UN, KG, L, M, CAIXA, DUZIA)
    unit VARCHAR(10) DEFAULT 'UN',
    
    -- Status do produto (ACTIVE=ativo, INACTIVE=inativo, DELETED=excluído logicamente)
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'DELETED')),
    
    -- Tipo do produto (PRODUCT=produto físico, SERVICE=serviço, COMPOSITE=kit/combo)
    product_type VARCHAR(20) DEFAULT 'PRODUCT' CHECK (product_type IN ('PRODUCT', 'SERVICE', 'COMPOSITE')),
    
    -- Custo de produção ou compra (usado para cálculo de margem)
    cost_price NUMERIC(12, 4) NOT NULL DEFAULT 0,
    
    -- Preço de venda ao cliente
    sale_price NUMERIC(12, 4) NOT NULL DEFAULT 0,
    
    -- Margem de lucro em percentual (calculado automaticamente)
    profit_margin NUMERIC(5, 2) GENERATED ALWAYS AS (
        CASE 
            WHEN cost_price > 0 THEN ((sale_price - cost_price) / cost_price * 100)
            ELSE 0 
        END
    ) STORED,
    
    -- Quantidade atual em estoque
    current_stock NUMERIC(12, 3) DEFAULT 0,
    
    -- Estoque mínimo (dispara alerta de reposição)
    minimum_stock NUMERIC(12, 3) DEFAULT 0,
    
    -- Estoque máximo recomendado
    maximum_stock NUMERIC(12, 3),
    
    -- Ponto de reposição (quando atingido, aciona pedido de compra)
    reorder_point NUMERIC(12, 3),
    
    -- Peso em quilogramas (usado para cálculo de frete)
    weight_kg NUMERIC(10, 3),
    
    -- Comprimento em centímetros
    length_cm NUMERIC(10, 2),
    
    -- Largura em centímetros
    width_cm NUMERIC(10, 2),
    
    -- Altura em centímetros
    height_cm NUMERIC(10, 2),
    
    -- Nomenclatura Comum do Mercosul (classificação fiscal)
    ncm_code VARCHAR(10),
    
    -- Código Especificador da Substituição Tributária
    cest_code VARCHAR(10),
    
    -- Código de barras (EAN-13 ou GTIN)
    ean_gtin VARCHAR(14),
    
    -- URL da imagem principal do produto
    image_url TEXT,
    
    -- URL da miniatura (thumbnail) para listagens
    thumbnail_url TEXT,
    
    -- Permite venda fracionada (ex: 0.5 kg, 1.25 unidades)
    is_fractional BOOLEAN DEFAULT FALSE,
    
    -- Indica se é um produto composto (kit/combo com outros produtos)
    is_composite BOOLEAN DEFAULT FALSE,
    
    -- Requer controle de número de série individual
    requires_serial BOOLEAN DEFAULT FALSE,
    
    -- Observações gerais sobre o produto
    notes TEXT,
    
    -- ID do comerciante proprietário do produto (FK para merchants)
    merchant_id UUID NOT NULL REFERENCES merchants(id) ON DELETE RESTRICT,
    
    -- ID do usuário que criou o registro
    created_by UUID,
    
    -- ID do usuário que fez a última atualização
    updated_by UUID,
    
    -- Data e hora de criação do registro
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Data e hora da última atualização
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Data e hora de exclusão lógica (soft delete)
    deleted_at TIMESTAMP,
    
    -- Garante que preços não sejam negativos
    CONSTRAINT chk_prices CHECK (sale_price >= 0 AND cost_price >= 0),
    
    -- Garante que estoque não seja negativo
    CONSTRAINT chk_stock CHECK (current_stock >= 0)
);

-- Índice para buscar produtos por comerciante (excluindo deletados)
CREATE INDEX idx_products_merchant ON products(merchant_id) WHERE deleted_at IS NULL;

-- Índice para filtrar por status
CREATE INDEX idx_products_status ON products(status) WHERE deleted_at IS NULL;

-- Índice para filtrar por categoria
CREATE INDEX idx_products_category ON products(category);

-- Índice para busca por código
CREATE INDEX idx_products_code ON products(code);

-- Índice para busca por nome
CREATE INDEX idx_products_name ON products(name);

-- Índice para identificar produtos com estoque baixo
CREATE INDEX idx_products_low_stock ON products(current_stock) WHERE current_stock <= minimum_stock;

-- Índice para busca textual avançada por nome (requer extensão pg_trgm)
CREATE INDEX idx_products_name_trgm ON products USING gin(name gin_trgm_ops);

COMMENT ON TABLE products IS 'Catálogo de produtos/serviços com controle de estoque, preços e informações fiscais';
COMMENT ON COLUMN products.id IS 'Identificador único UUID do produto';
COMMENT ON COLUMN products.code IS 'Código interno do comerciante (SKU)';
COMMENT ON COLUMN products.name IS 'Nome do produto';
COMMENT ON COLUMN products.description IS 'Descrição detalhada';
COMMENT ON COLUMN products.category IS 'Categoria (FOOD, BEVERAGE, SERVICE, CRAFT, OTHER)';
COMMENT ON COLUMN products.group_code IS 'Grupo de produtos para organização';
COMMENT ON COLUMN products.unit IS 'Unidade de medida (UN, KG, L, etc)';
COMMENT ON COLUMN products.status IS 'Status (ACTIVE, INACTIVE, DELETED)';
COMMENT ON COLUMN products.product_type IS 'Tipo (PRODUCT, SERVICE, COMPOSITE)';
COMMENT ON COLUMN products.cost_price IS 'Custo de aquisição/produção';
COMMENT ON COLUMN products.sale_price IS 'Preço de venda';
COMMENT ON COLUMN products.profit_margin IS 'Margem de lucro em % (calculado)';
COMMENT ON COLUMN products.current_stock IS 'Quantidade atual em estoque';
COMMENT ON COLUMN products.minimum_stock IS 'Estoque mínimo (alerta)';
COMMENT ON COLUMN products.maximum_stock IS 'Estoque máximo recomendado';
COMMENT ON COLUMN products.reorder_point IS 'Ponto de reposição automática';
COMMENT ON COLUMN products.weight_kg IS 'Peso em kg';
COMMENT ON COLUMN products.ncm_code IS 'Código NCM (Nomenclatura Comum Mercosul)';
COMMENT ON COLUMN products.cest_code IS 'Código CEST (Substituição Tributária)';
COMMENT ON COLUMN products.ean_gtin IS 'Código de barras EAN/GTIN';
COMMENT ON COLUMN products.is_fractional IS 'Permite venda fracionada';
COMMENT ON COLUMN products.is_composite IS 'É um kit/combo';
COMMENT ON COLUMN products.requires_serial IS 'Requer número de série';
COMMENT ON COLUMN products.merchant_id IS 'ID do comerciante proprietário';
COMMENT ON COLUMN products.deleted_at IS 'Data de exclusão lógica (soft delete)';


-- ============================================
-- TABLE: PRODUCT_PRICES
-- Objetivo: Rastrear histórico de mudanças de preços
-- Funcionalidade: Registra todas as alterações de preços (custo, venda, promocional)
-- para auditoria, análise de tendências e compliance
-- ============================================
CREATE TABLE product_prices (
    -- Identificador único do registro de alteração
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    
    -- ID do produto (excluído em cascata se produto for removido)
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    
    -- Tipo de preço alterado (COST=custo, SALE=venda, PROMOTIONAL=promocional)
    price_type VARCHAR(20) NOT NULL CHECK (price_type IN ('COST', 'SALE', 'PROMOTIONAL')),
    
    -- Valor anterior do preço
    old_value NUMERIC(12, 4),
    
    -- Novo valor do preço
    new_value NUMERIC(12, 4) NOT NULL,
    
    -- Percentual de mudança (positivo=aumento, negativo=redução)
    change_percentage NUMERIC(5, 2),
    
    -- Motivo da alteração (ex: "Aumento de custo do fornecedor")
    reason VARCHAR(200),
    
    -- Data de início da validade do novo preço
    valid_from TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Data de fim da validade (NULL = vigente)
    valid_to TIMESTAMP,
    
    -- ID do usuário que realizou a alteração
    changed_by UUID,
    
    -- Data de criação do registro
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índice para buscar histórico de preços de um produto (mais recente primeiro)
CREATE INDEX idx_product_prices_product ON product_prices(product_id, created_at DESC);

-- Índice para filtrar por tipo de preço e período de validade
CREATE INDEX idx_product_prices_type ON product_prices(price_type, valid_from);

COMMENT ON TABLE product_prices IS 'Histórico de alterações de preços para auditoria e análise';
COMMENT ON COLUMN product_prices.id IS 'Identificador único do registro';
COMMENT ON COLUMN product_prices.product_id IS 'ID do produto';
COMMENT ON COLUMN product_prices.price_type IS 'Tipo de preço (COST, SALE, PROMOTIONAL)';
COMMENT ON COLUMN product_prices.old_value IS 'Valor anterior';
COMMENT ON COLUMN product_prices.new_value IS 'Novo valor';
COMMENT ON COLUMN product_prices.change_percentage IS 'Percentual de mudança';
COMMENT ON COLUMN product_prices.reason IS 'Justificativa da alteração';
COMMENT ON COLUMN product_prices.valid_from IS 'Data de início da validade';
COMMENT ON COLUMN product_prices.valid_to IS 'Data de fim da validade';
COMMENT ON COLUMN product_prices.changed_by IS 'ID do usuário que alterou';


-- ============================================
-- TABLE: PRODUCT_STOCK_MOVEMENTS
-- Objetivo: Rastrear todas as movimentações de estoque
-- Funcionalidade: Registra entradas/saídas de estoque para controle rigoroso,
-- rastreabilidade e análise de perdas, vendas e ajustes
-- ============================================
CREATE TABLE product_stock_movements (
    -- Identificador único da movimentação
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    
    -- ID do produto movimentado
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    
    -- Tipo de movimentação:
    -- PURCHASE=compra/entrada, SALE=venda/saída, ADJUSTMENT=ajuste manual,
    -- PRODUCTION=fabricação, LOSS=perda/quebra, RETURN=devolução,
    -- TRANSFER=transferência entre locais, INVENTORY=inventário
    movement_type VARCHAR(20) NOT NULL CHECK (movement_type IN (
        'PURCHASE',
        'SALE',
        'ADJUSTMENT',
        'PRODUCTION',
        'LOSS',
        'RETURN',
        'TRANSFER',
        'INVENTORY'
    )),
    
    -- Quantidade movimentada (positivo=entrada, negativo=saída)
    quantity NUMERIC(12, 3) NOT NULL,
    
    -- Custo unitário no momento da movimentação (para valoração do estoque)
    unit_cost NUMERIC(12, 4),
    
    -- Estoque antes da movimentação
    previous_stock NUMERIC(12, 3),
    
    -- Estoque depois da movimentação
    new_stock NUMERIC(12, 3),
    
    -- ID de referência do documento origem (venda, pedido de compra, etc)
    reference_id UUID,
    
    -- Tipo do documento de referência (SALE, PURCHASE_ORDER, etc)
    reference_type VARCHAR(50),
    
    -- Observações sobre a movimentação
    notes TEXT,
    
    -- ID do comerciante (FK para merchants)
    merchant_id UUID NOT NULL REFERENCES merchants(id) ON DELETE RESTRICT,
    
    -- ID do usuário que realizou a movimentação
    created_by UUID,
    
    -- Data e hora da movimentação
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índice para buscar movimentações de um produto (mais recente primeiro)
CREATE INDEX idx_stock_movements_product ON product_stock_movements(product_id, created_at DESC);

-- Índice para filtrar por tipo de movimentação
CREATE INDEX idx_stock_movements_type ON product_stock_movements(movement_type);

-- Índice para buscar movimentações de um comerciante
CREATE INDEX idx_stock_movements_merchant ON product_stock_movements(merchant_id, created_at DESC);

COMMENT ON TABLE product_stock_movements IS 'Histórico completo de movimentações de estoque';
COMMENT ON COLUMN product_stock_movements.id IS 'Identificador único da movimentação';
COMMENT ON COLUMN product_stock_movements.product_id IS 'ID do produto';
COMMENT ON COLUMN product_stock_movements.movement_type IS 'Tipo (PURCHASE, SALE, ADJUSTMENT, PRODUCTION, LOSS, RETURN, TRANSFER, INVENTORY)';
COMMENT ON COLUMN product_stock_movements.quantity IS 'Quantidade (positivo=entrada, negativo=saída)';
COMMENT ON COLUMN product_stock_movements.unit_cost IS 'Custo unitário no momento';
COMMENT ON COLUMN product_stock_movements.previous_stock IS 'Estoque anterior';
COMMENT ON COLUMN product_stock_movements.new_stock IS 'Estoque resultante';
COMMENT ON COLUMN product_stock_movements.reference_id IS 'ID do documento origem';
COMMENT ON COLUMN product_stock_movements.reference_type IS 'Tipo do documento (SALE, PURCHASE_ORDER, etc)';
COMMENT ON COLUMN product_stock_movements.notes IS 'Observações';
COMMENT ON COLUMN product_stock_movements.merchant_id IS 'ID do comerciante';
COMMENT ON COLUMN product_stock_movements.created_by IS 'ID do usuário';
COMMENT ON COLUMN product_stock_movements.created_at IS 'Data/hora da movimentação';


-- ============================================
-- TABLE: PRODUCT_IMAGES
-- Objetivo: Gerenciar múltiplas imagens por produto
-- Funcionalidade: Armazena URLs de imagens, miniaturas e metadados
-- para exibição em catálogos, e-commerce e aplicações mobile
-- ============================================
CREATE TABLE product_images (
    -- Identificador único da imagem
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    
    -- ID do produto (excluído em cascata se produto for removido)
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    
    -- URL completa da imagem
    image_url TEXT NOT NULL,
    
    -- URL da miniatura (thumbnail) para listagens
    thumbnail_url TEXT,
    
    -- Tipo de imagem (PRODUCT=principal, DETAIL=detalhe, PACKAGING=embalagem, QR_CODE=QR code)
    image_type VARCHAR(20) DEFAULT 'PRODUCT' CHECK (image_type IN ('PRODUCT', 'DETAIL', 'PACKAGING', 'QR_CODE')),
    
    -- Ordem de exibição (0=primeira, 1=segunda, etc)
    display_order INT DEFAULT 0,
    
    -- Indica se é a imagem principal do produto
    is_primary BOOLEAN DEFAULT FALSE,
    
    -- Largura da imagem em pixels
    width_px INT,
    
    -- Altura da imagem em pixels
    height_px INT,
    
    -- Tamanho do arquivo em bytes
    size_bytes BIGINT,
    
    -- ID do usuário que fez o upload
    uploaded_by UUID,
    
    -- Data de upload
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índice para buscar imagens de um produto ordenadas por display_order
CREATE INDEX idx_product_images_product ON product_images(product_id, display_order);

COMMENT ON TABLE product_images IS 'Galeria de imagens dos produtos';
COMMENT ON COLUMN product_images.id IS 'Identificador único da imagem';
COMMENT ON COLUMN product_images.product_id IS 'ID do produto';
COMMENT ON COLUMN product_images.image_url IS 'URL da imagem completa';
COMMENT ON COLUMN product_images.thumbnail_url IS 'URL da miniatura';
COMMENT ON COLUMN product_images.image_type IS 'Tipo (PRODUCT, DETAIL, PACKAGING, QR_CODE)';
COMMENT ON COLUMN product_images.display_order IS 'Ordem de exibição';
COMMENT ON COLUMN product_images.is_primary IS 'Indica se é a imagem principal';
COMMENT ON COLUMN product_images.width_px IS 'Largura em pixels';
COMMENT ON COLUMN product_images.height_px IS 'Altura em pixels';
COMMENT ON COLUMN product_images.size_bytes IS 'Tamanho do arquivo em bytes';
COMMENT ON COLUMN product_images.uploaded_by IS 'ID do usuário que fez upload';


-- ============================================
-- TABLE: PRODUCT_COMPOSITES
-- Objetivo: Gerenciar produtos compostos (kits/combos)
-- Funcionalidade: Define quais produtos componentes fazem parte de um kit/combo,
-- suas quantidades e ordem de exibição. Útil para controle de estoque de produtos
-- compostos e desmembramento automático em vendas
-- ============================================
CREATE TABLE product_composites (
    -- Identificador único da composição
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    
    -- ID do produto composto (kit/combo pai)
    composite_product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    
    -- ID do produto componente (item que faz parte do kit)
    component_product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    
    -- Quantidade do componente no kit (ex: 50 unidades de salgados)
    quantity NUMERIC(12, 3) NOT NULL DEFAULT 1,
    
    -- Unidade de medida do componente
    unit VARCHAR(10),
    
    -- Indica se o componente é opcional no kit
    is_optional BOOLEAN DEFAULT FALSE,
    
    -- Ordem de exibição dos componentes
    display_order INT DEFAULT 0,
    
    -- Data de criação da composição
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Garante que um componente não seja duplicado no mesmo kit
    UNIQUE(composite_product_id, component_product_id),
    
    -- Impede que um produto seja componente de si mesmo
    CHECK (composite_product_id != component_product_id)
);

-- Índice para buscar componentes de um produto composto
CREATE INDEX idx_composites_parent ON product_composites(composite_product_id);

COMMENT ON TABLE product_composites IS 'Estrutura de kits/combos - define componentes de produtos compostos';
COMMENT ON COLUMN product_composites.id IS 'Identificador único';
COMMENT ON COLUMN product_composites.composite_product_id IS 'ID do produto composto (kit/combo)';
COMMENT ON COLUMN product_composites.component_product_id IS 'ID do produto componente';
COMMENT ON COLUMN product_composites.quantity IS 'Quantidade do componente no kit';
COMMENT ON COLUMN product_composites.unit IS 'Unidade de medida';
COMMENT ON COLUMN product_composites.is_optional IS 'Se é componente opcional';
COMMENT ON COLUMN product_composites.display_order IS 'Ordem de exibição';


-- ============================================
-- TRIGGERS
-- ============================================

-- Trigger: Atualizar updated_at automaticamente
-- Atualiza o campo updated_at sempre que um registro for modificado
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_merchants_updated_at BEFORE UPDATE ON merchants
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_products_updated_at BEFORE UPDATE ON products
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();


-- Trigger: Registrar histórico de preços
-- Registra automaticamente na tabela product_prices quando custo ou preço de venda mudar
CREATE OR REPLACE FUNCTION log_price_change()
RETURNS TRIGGER AS $$
BEGIN
    -- Registra mudança no custo
    IF OLD.cost_price IS DISTINCT FROM NEW.cost_price THEN
        INSERT INTO product_prices (product_id, price_type, old_value, new_value, change_percentage, changed_by)
        VALUES (
            NEW.id, 
            'COST', 
            OLD.cost_price, 
            NEW.cost_price,
            CASE WHEN OLD.cost_price > 0 THEN ((NEW.cost_price - OLD.cost_price) / OLD.cost_price * 100) ELSE 0 END,
            NEW.updated_by
        );
    END IF;
    
    -- Registra mudança no preço de venda
    IF OLD.sale_price IS DISTINCT FROM NEW.sale_price THEN
        INSERT INTO product_prices (product_id, price_type, old_value, new_value, change_percentage, changed_by)
        VALUES (
            NEW.id, 
            'SALE', 
            OLD.sale_price, 
            NEW.sale_price,
            CASE WHEN OLD.sale_price > 0 THEN ((NEW.sale_price - OLD.sale_price) / OLD.sale_price * 100) ELSE 0 END,
            NEW.updated_by
        );
    END IF;
    
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER log_product_price_changes AFTER UPDATE ON products
    FOR EACH ROW 
    WHEN (OLD.cost_price IS DISTINCT FROM NEW.cost_price OR OLD.sale_price IS DISTINCT FROM NEW.sale_price)
    EXECUTE FUNCTION log_price_change();


-- Trigger: Registrar movimentação de estoque
-- Registra automaticamente na tabela product_stock_movements quando o estoque mudar
CREATE OR REPLACE FUNCTION log_stock_movement()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.current_stock IS DISTINCT FROM NEW.current_stock THEN
        INSERT INTO product_stock_movements (
            product_id, 
            movement_type, 
            quantity, 
            previous_stock, 
            new_stock,
            notes,
            merchant_id,
            created_by
        )
        VALUES (
            NEW.id,
            'ADJUSTMENT',
            (NEW.current_stock - OLD.current_stock),
            OLD.current_stock,
            NEW.current_stock,
            'Ajuste automático via UPDATE direto na tabela products',
            NEW.merchant_id,
            NEW.updated_by
        );
    END IF;
    
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER log_product_stock_changes AFTER UPDATE ON products
    FOR EACH ROW 
    WHEN (OLD.current_stock IS DISTINCT FROM NEW.current_stock)
    EXECUTE FUNCTION log_stock_movement();


-- ============================================
-- CATEGORIAS PADRÃO
-- ============================================
-- Categorias sugeridas para o campo products.category:
-- FOOD, BEVERAGE, SERVICE, CRAFT, RAW_MATERIAL, PACKAGING, OTHER

COMMENT ON DATABASE postgres IS 'MiniManager Intelligent - Database Schema v2.0 (Simplified)';
