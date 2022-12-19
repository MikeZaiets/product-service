DROP SCHEMA IF EXISTS product_service_schema CASCADE;

CREATE SCHEMA IF NOT EXISTS product_service_schema;

SET SCHEMA 'product_service_schema';

CREATE TABLE IF NOT EXISTS products
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100)  NOT NULL,
    description VARCHAR(1000) NOT NULL
);
