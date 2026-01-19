-- Role: "proxy_farms_rest"
-- DROP ROLE "proxy_farms_rest";

CREATE ROLE "proxy_farms_rest" WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  PASSWORD '${POSTGRES_PROXY_USER_PASSWORD}';
  
ALTER ROLE proxy_farms_rest SET search_path TO farms;

ALTER USER proxy_farms_rest set TIMEZONE to 'America/New_York';

COMMENT ON ROLE "proxy_farms_rest" IS 'Proxy account for Farmer Access to Risk Management Service.';

GRANT "app_farms_rest_proxy" TO "proxy_farms_rest";

GRANT USAGE ON SCHEMA "farms" TO "app_farms_rest_proxy";
