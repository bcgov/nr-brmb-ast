create or replace procedure farms_codes_write_pkg.create_sector_detail_code(
   in in_sector_code farms.sector_code.sector_code%type,
   in in_sector_detail_code farms.sector_detail_code.sector_detail_code%type,
   in in_description farms.sector_detail_code.description%type,
   in in_expiry_date farms.sector_detail_code.expiry_date%type,
   in in_user farms.sector_detail_code.update_user%type
)
language plpgsql
as $$
begin
    insert into farms.sector_detail_code (
        sector_detail_code,
        description,
        effective_date,
        expiry_date,
        create_user,
        create_date,
        update_user,
        update_date,
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

    insert into farms.sector_detail_xref (
        sector_detail_xref_id,
        sector_code,
        sector_detail_code,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
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
