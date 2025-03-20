-- Database: farms${ENV}

-- DROP DATABASE "farms${ENV}";

CREATE DATABASE "farms${ENV}"
    WITH 
    OWNER = postgres
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE "farms${ENV}"
    IS 'farms${ENV} database containing schemas used by Farmer Access to Risk Management Service.';

GRANT TEMPORARY, CONNECT ON DATABASE "farms${ENV}" TO PUBLIC;

GRANT ALL ON DATABASE "farms${ENV}" TO postgres;