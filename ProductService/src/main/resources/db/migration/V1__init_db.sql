CREATE TABLE categories
(
    id            BINARY(16)   NOT NULL,
    category_name VARCHAR(255) NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE orders
(
    id BINARY(16) NOT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE orders_products
(
    orders_id   BINARY(16) NOT NULL,
    products_id BINARY(16) NOT NULL
);

CREATE TABLE prices
(
    id       BINARY(16)   NOT NULL,
    currency VARCHAR(255) NULL,
    price    DOUBLE       NOT NULL,
    discount DOUBLE       NOT NULL,
    CONSTRAINT pk_prices PRIMARY KEY (id)
);

CREATE TABLE products
(
    id            BINARY(16)    NOT NULL,
    title         VARCHAR(255)  NULL,
    `description` VARCHAR(1000) NULL,
    image         VARCHAR(255)  NULL,
    price_id      BINARY(16)    NULL,
    category_id   BINARY(16)    NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_PRICE FOREIGN KEY (price_id) REFERENCES prices (id);

ALTER TABLE orders_products
    ADD CONSTRAINT fk_ordpro_on_order FOREIGN KEY (orders_id) REFERENCES orders (id);

ALTER TABLE orders_products
    ADD CONSTRAINT fk_ordpro_on_product FOREIGN KEY (products_id) REFERENCES products (id);