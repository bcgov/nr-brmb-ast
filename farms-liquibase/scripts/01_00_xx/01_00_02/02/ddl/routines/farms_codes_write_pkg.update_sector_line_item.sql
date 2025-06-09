create or replace procedure farms_codes_write_pkg.update_sector_line_item(
   in in_line_item farms.line_item.line_item%type,
   in in_program_year farms.sector_detail_line_item.program_year%type,
   in in_sector_detail_code farms.sector_detail_line_item.sector_detail_code%type,
   in in_user farms.line_item.update_user%type
)
language plpgsql
as $$
declare
    v_sector_detail_count numeric;
    v_sector_detail_line_item_id farms.sector_detail_line_item.sector_detail_line_item_id%type := null;
    v_current_sector_detail_code farms.sector_detail_line_item.sector_detail_code%type := null;
begin

    select count(*)
    into v_sector_detail_count
    from farms.sector_detail_line_item sdli
    where sdli.line_item = in_line_item
      and sdli.program_year = in_program_year;

    if v_sector_detail_count = 1 then
        select sdli.sector_detail_line_item_id,
               sdli.sector_detail_code
        into v_sector_detail_line_item_id,
             v_current_sector_detail_code
        from farms.sector_detail_line_item sdli
        where sdli.line_item = in_line_item
          and sdli.program_year = in_program_year;
    end if;

    if v_sector_detail_line_item_id is null and in_sector_detail_code is not null then
        insert into farms.sector_detail_line_item (
            sector_detail_line_item_id,
            program_year,
            line_item,
            sector_detail_code,
            create_user,
            create_date,
            update_user,
            update_date,
            revision_count
        ) values (
            nextval('farms.seq_sdli'),
            in_program_year,
            in_line_item,
            in_sector_detail_code,
            in_user,
            current_timestamp,
            in_user,
            current_timestamp,
            1
        );
    elsif v_sector_detail_line_item_id is not null and in_sector_detail_code is not null and v_current_sector_detail_code != in_sector_detail_code then
        update farms.sector_detail_line_item sdli
        set sdli.sector_detail_code = in_sector_detail_code,
            sdli.revision_count = revision_count + 1,
            update_user = in_user,
            update_date = current_timestamp
        where sdli.sector_detail_line_item_id = v_sector_detail_line_item_id;
    elsif v_sector_detail_line_item_id is not null and in_sector_detail_code is null then
        delete from farms.sector_detail_line_item sdli
        where sdli.sector_detail_line_item_id = v_sector_detail_line_item_id;
    end if;
end;
$$;
