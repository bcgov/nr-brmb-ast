-- Prompts for a password and sets it on FARM and PROXY_FARM_ADMIN.
-- Run on the temporary Oracle server (never Oracle PROD) as a DBA, e.g. system.
-- The password can't contain a double quote.

SET ECHO OFF
-- don't print the statements with the password substituted in
SET VERIFY OFF

ACCEPT new_password CHAR PROMPT 'New password for FARM and PROXY_FARM_ADMIN: ' HIDE

ALTER USER FARM IDENTIFIED BY "&new_password";
ALTER USER PROXY_FARM_ADMIN IDENTIFIED BY "&new_password";

UNDEFINE new_password
SET VERIFY ON
