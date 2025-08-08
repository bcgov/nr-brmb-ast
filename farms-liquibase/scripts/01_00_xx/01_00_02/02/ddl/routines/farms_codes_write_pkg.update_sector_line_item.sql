create or replace procedure farms_codes_write_pkg.update_sector_line_item(
   in in_line_item farms.farm_line_items.line_item%type,
   in in_program_year farms.farm_sector_detail_line_items.program_year%type,
   in in_sector_detail_code farms.farm_sector_detail_line_items.sector_detail_code%type,
   in in_user farms.farm_line_items.who_updated%type
)
language plpgsql
as $$
declare
    v_sector_detail_count numeric;
    v_sector_detail_line_item_id farms.farm_sector_detail_line_items.sector_detail_line_item_id%type := null;
    v_current_sector_detail_code farms.farm_sector_detail_line_items.sector_detail_code%type := null;
begin

    select count(*)
    into v_sector_detail_count
    from farms.farm_sector_detail_line_items sdli
    where sdli.line_item = in_line_item
      and sdli.program_year = in_program_year;

    if v_sector_detail_count = 1 then
        select sdli.sector_detail_line_item_id,
               sdli.sector_detail_code
        into v_sector_detail_line_item_id,
             v_current_sector_detail_code
        from farms.farm_sector_detail_line_items sdli
        where sdli.line_item = in_line_item
          and sdli.program_year = in_program_year;
    end if;

    if v_sector_detail_line_item_id is null and in_sector_detail_code is not null then
        insert into farms.farm_sector_detail_line_items (
            sector_detail_line_item_id,
            program_year,
            line_item,
            sector_detail_code,
            who_created,
            when_created,
            who_updated,
            when_updated,
            revision_count
        ) values (
            nextval('farms.farm_sdli_seq'),
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
        update farms.farm_sector_detail_line_items sdli
        set sdli.sector_detail_code = in_sector_detail_code,
            sdli.revision_count = revision_count + 1,
            who_updated = in_user,
            when_updated = current_timestamp
        where sdli.sector_detail_line_item_id = v_sector_detail_line_item_id;
    elsif v_sector_detail_line_item_id is not null and in_sector_detail_code is null then
        delete from farms.farm_sector_detail_line_items sdli
        where sdli.sector_detail_line_item_id = v_sector_detail_line_item_id;
    end if;
end;
$$;
