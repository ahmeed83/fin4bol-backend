CREATE TABLE IF NOT EXISTS application_user
(
    id              UUID         NOT NULL PRIMARY KEY,
    created_at      TIMESTAMP    NOT NULL,
    updated_at      TIMESTAMP    NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    name            VARCHAR(100) NOT NULL,
    password        VARCHAR(100) NOT NULL,
    role            VARCHAR(10)  NOT NULL
        CHECK (role = 'ADMIN' OR
               role = 'EMPLOYEE' OR
               role = 'CUSTOMER'),
    is_enabled      BOOL         NOT NULL,
    provider        VARCHAR(100),
    referral_source VARCHAR(255)
);

CREATE UNIQUE INDEX email_upper_idx ON application_user (UPPER(email));

CREATE TABLE product
(
    id            UUID           NOT NULL PRIMARY KEY,
    created_at    TIMESTAMP      NOT NULL,
    updated_at    TIMESTAMP      NOT NULL,
    name          VARCHAR(200)   NOT NULL,
    ean           VARCHAR(20)    NOT NULL,
    purchase_cost NUMERIC(10, 2) NOT NULL,
    app_user_id   UUID           NOT NULL REFERENCES application_user (id),
    UNIQUE (ean, app_user_id)
);

CREATE TABLE performance_rapport
(
    id                 UUID PRIMARY KEY NOT NULL,
    created_at         TIMESTAMP        NOT NULL,
    updated_at         TIMESTAMP        NOT NULL,
    period             VARCHAR(255)     NOT NULL,
    salesperson_number VARCHAR(255)     NOT NULL,
    app_user_id        UUID REFERENCES application_user (id)
);


CREATE TABLE performance
(
    id                          UUID      NOT NULL PRIMARY KEY,
    created_at                  TIMESTAMP NOT NULL,
    updated_at                  TIMESTAMP NOT NULL,
    name                        VARCHAR(255),
    ean                         VARCHAR(20),
    purchase_cost               NUMERIC,
    average_price_per_product   NUMERIC,
    revenue                     NUMERIC,
    sales_volume                INTEGER,
    vat                         NUMERIC,
    commission                  NUMERIC,
    commission_correction       NUMERIC,
    lost_item_compensation      NUMERIC,
    net_commission              NUMERIC,
    shipping_cost               NUMERIC,
    shipping_cost_correction    NUMERIC,
    bol_com_shipping_label_cost NUMERIC,
    total_shipping_cost         NUMERIC,
    unsellable_inventory_cost   NUMERIC,
    pick_pack_cost              NUMERIC,
    pick_pack_cost_correction   NUMERIC,
    net_pick_pack_cost          NUMERIC,
    inventory_cost              NUMERIC,
    sales_price_correction      NUMERIC,
    sales_price_correction_vat  NUMERIC,
    performance_rapport_id      UUID REFERENCES performance_rapport (id)
);
