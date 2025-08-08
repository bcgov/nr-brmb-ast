create or replace procedure farms_codes_write_pkg.create_sector_detail_code(
   in in_sector_code farms.farm_sector_codes.sector_code%type,
   in in_sector_detail_code farms.farm_sector_detail_codes.sector_detail_code%type,
   in in_description farms.farm_sector_detail_codes.description%type,
   in in_expiry_date farms.farm_sector_detail_codes.expiry_date%type,
   in in_user farms.farm_sector_detail_codes.who_updated%type
)
language plpgsql
as $$
begin
    insert into farms.farm_sector_detail_codes (
        sector_detail_code,
        description,
        established_date,
        expiry_date,
        who_created,
        when_created,
        who_updated,
        when_updated,
        revision_count
    ) values (
        in_sector_detail_code,
        in_description,
        current_date,
        in_expiry_date,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp,
        1
    );

    insert into farms.farm_sector_detail_xref (
        sector_detail_xref_id,
        sector_code,
        sector_detail_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.seq_sdx'),
        in_sector_code,
        in_sector_detail_code,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
