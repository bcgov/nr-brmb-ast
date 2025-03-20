-- Role: "app_farms"
-- DROP ROLE "app_farms";

CREATE ROLE "app_farms" WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  CREATEROLE
  NOREPLICATION
  PASSWORD '${POSTGRES_ADMIN_PASSWORD}';
  
ALTER ROLE app_farms SET search_path TO farms;

ALTER USER app_farms set TIMEZONE to 'America/New_York';

COMMENT ON ROLE "app_farms" IS 'Farmer Access to Risk Management Service.';
