CREATE SCHEMA "farms";

CREATE EXTENSION IF NOT EXISTS tablefunc;

CREATE TABLE farms.farm_benchmark_per_units (
    benchmark_per_unit_id bigint NOT NULL,
    program_year smallint NOT NULL,
    unit_comment varchar(2000),
    expiry_date timestamp(0),
    municipality_code varchar(10) NOT NULL,
    inventory_item_code varchar(10),
    structure_group_code varchar(10),
    revision_count integer NOT NULL DEFAULT 1,
    who_created varchar(30) NOT NULL,
    when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
    who_updated varchar(30),
    when_updated timestamp(0) DEFAULT statement_timestamp()
) ;

CREATE TABLE farms.farm_benchmark_years (
    benchmark_year smallint NOT NULL,
    average_margin decimal(13,2) NOT NULL,
    average_expense decimal(13,2),
    benchmark_per_unit_id bigint NOT NULL,
    revision_count integer NOT NULL DEFAULT 1,
    who_created varchar(30) NOT NULL,
    when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
    who_updated varchar(30),
    when_updated timestamp(0) DEFAULT statement_timestamp()
) ;

CREATE TABLE farms.farm_municipality_codes (
    municipality_code varchar(10) NOT NULL,
    description varchar(256) NOT NULL,
    established_date timestamp(0) NOT NULL DEFAULT statement_timestamp(),
    expiry_date timestamp(0) NOT NULL,
    revision_count integer NOT NULL DEFAULT 1,
    who_created varchar(30) NOT NULL,
    when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
    who_updated varchar(30),
    when_updated timestamp(0) DEFAULT statement_timestamp()
) ;

CREATE TABLE farms.farm_inventory_item_codes (
    inventory_item_code varchar(10) NOT NULL,
    description varchar(256) NOT NULL,
    established_date timestamp(0) NOT NULL DEFAULT statement_timestamp(),
    expiry_date timestamp(0) NOT NULL,
    revision_count integer NOT NULL DEFAULT 1,
    who_created varchar(30) NOT NULL,
    when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
    who_updated varchar(30),
    when_updated timestamp(0) DEFAULT statement_timestamp()
) ;

CREATE TABLE farms.farm_structure_group_codes (
    structure_group_code varchar(10) NOT NULL,
    description varchar(256) NOT NULL,
    established_date timestamp(0) NOT NULL DEFAULT statement_timestamp(),
    expiry_date timestamp(0) NOT NULL,
    revision_count integer NOT NULL DEFAULT 1,
    who_created varchar(30) NOT NULL,
    when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
    who_updated varchar(30),
    when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
