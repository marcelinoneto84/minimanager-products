-- ============================================
-- MiniManager Intelligent - Dados Iniciais
-- Dados de exemplo para testes e demonstração
-- ============================================

-- Inserir comerciante de exemplo
INSERT INTO merchants (id, legal_name, trading_name, tax_id, person_type, email, phone, 
                       address_city, address_state, address_country, status)
VALUES (
    '11111111-1111-1111-1111-111111111111',
    'Padaria e Confeitaria Silva LTDA',
    'Padaria Silva',
    '12.345.678/0001-90',
    'BUSINESS',
    'contato@padariasilva.com.br',
    '(11) 98765-4321',
    'São Paulo',
    'SP',
    'BRA',
    'ACTIVE'
);

-- Inserir produtos de exemplo
INSERT INTO products (id, code, name, description, category, unit, status, product_type,
                     cost_price, sale_price, current_stock, minimum_stock, merchant_id)
VALUES
-- Produtos de panificação
('22222222-2222-2222-2222-222222222221', 'PD001', 'Pão Francês', 'Pão francês tradicional crocante', 'FOOD', 'UN', 'ACTIVE', 'PRODUCT', 0.50, 0.80, 500, 100, '11111111-1111-1111-1111-111111111111'),
('22222222-2222-2222-2222-222222222222', 'PD002', 'Pão de Forma Integral', 'Pão de forma integral 500g', 'FOOD', 'UN', 'ACTIVE', 'PRODUCT', 4.50, 8.90, 50, 10, '11111111-1111-1111-1111-111111111111'),
('22222222-2222-2222-2222-222222222223', 'PD003', 'Baguete', 'Baguete francesa 250g', 'FOOD', 'UN', 'ACTIVE', 'PRODUCT', 2.00, 4.50, 80, 20, '11111111-1111-1111-1111-111111111111'),

-- Bolos e doces
('22222222-2222-2222-2222-222222222224', 'BC001', 'Bolo de Chocolate', 'Bolo de chocolate com cobertura 1kg', 'FOOD', 'UN', 'ACTIVE', 'PRODUCT', 15.00, 35.00, 15, 5, '11111111-1111-1111-1111-111111111111'),
('22222222-2222-2222-2222-222222222225', 'BC002', 'Torta de Morango', 'Torta de morango premium 1.5kg', 'FOOD', 'UN', 'ACTIVE', 'PRODUCT', 25.00, 55.00, 8, 3, '11111111-1111-1111-1111-111111111111'),
('22222222-2222-2222-2222-222222222226', 'DC001', 'Brigadeiro Gourmet', 'Brigadeiro gourmet (unidade)', 'FOOD', 'UN', 'ACTIVE', 'PRODUCT', 1.50, 3.50, 200, 50, '11111111-1111-1111-1111-111111111111'),

-- Salgados
('22222222-2222-2222-2222-222222222227', 'SG001', 'Coxinha de Frango', 'Coxinha grande de frango com catupiry', 'FOOD', 'UN', 'ACTIVE', 'PRODUCT', 2.00, 5.00, 150, 30, '11111111-1111-1111-1111-111111111111'),
('22222222-2222-2222-2222-222222222228', 'SG002', 'Empada de Palmito', 'Empada de palmito tradicional', 'FOOD', 'UN', 'ACTIVE', 'PRODUCT', 1.80, 4.50, 100, 20, '11111111-1111-1111-1111-111111111111'),
('22222222-2222-2222-2222-222222222229', 'SG003', 'Pastel de Carne', 'Pastel grande de carne moída', 'FOOD', 'UN', 'ACTIVE', 'PRODUCT', 2.50, 6.00, 120, 25, '11111111-1111-1111-1111-111111111111'),

-- Bebidas
('22222222-2222-2222-2222-222222222230', 'BB001', 'Café Expresso', 'Café expresso 50ml', 'BEVERAGE', 'UN', 'ACTIVE', 'SERVICE', 0.80, 3.00, 0, 0, '11111111-1111-1111-1111-111111111111');

-- Inserir imagens de exemplo para alguns produtos
INSERT INTO product_images (product_id, image_url, thumbnail_url, image_type, display_order, is_primary)
VALUES
('22222222-2222-2222-2222-222222222221', 'https://exemplo.com/images/pao-frances.jpg', 'https://exemplo.com/images/thumbs/pao-frances.jpg', 'PRODUCT', 0, true),
('22222222-2222-2222-2222-222222222224', 'https://exemplo.com/images/bolo-chocolate.jpg', 'https://exemplo.com/images/thumbs/bolo-chocolate.jpg', 'PRODUCT', 0, true),
('22222222-2222-2222-2222-222222222227', 'https://exemplo.com/images/coxinha.jpg', 'https://exemplo.com/images/thumbs/coxinha.jpg', 'PRODUCT', 0, true);

-- Criar um kit/combo de exemplo
INSERT INTO products (id, code, name, description, category, unit, status, product_type,
                     cost_price, sale_price, current_stock, minimum_stock, is_composite, merchant_id)
VALUES
('22222222-2222-2222-2222-222222222231', 'KIT001', 'Kit Festa 50 pessoas', 'Kit completo para festa com 50 salgados + 2L refrigerante', 'FOOD', 'UN', 'ACTIVE', 'COMPOSITE', 100.00, 180.00, 5, 2, true, '11111111-1111-1111-1111-111111111111');

-- Definir componentes do kit
INSERT INTO product_composites (composite_product_id, component_product_id, quantity, unit, display_order)
VALUES
('22222222-2222-2222-2222-222222222231', '22222222-2222-2222-2222-222222222227', 25, 'UN', 0),  -- 25 coxinhas
('22222222-2222-2222-2222-222222222231', '22222222-2222-2222-2222-222222222228', 15, 'UN', 1),  -- 15 empadas
('22222222-2222-2222-2222-222222222231', '22222222-2222-2222-2222-222222222229', 10, 'UN', 2);  -- 10 pastéis

COMMENT ON DATABASE minimanager_products IS 'MiniManager Intelligent - Database com dados iniciais';
