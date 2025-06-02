CREATE ROLE "app_farms_custodian";

CREATE ROLE "app_farms_rest_proxy";

GRANT "app_farms_rest_proxy" TO "app_farms" WITH ADMIN OPTION;
