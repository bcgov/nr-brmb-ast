create or replace procedure farms_aarm_pkg.update_prices()
language plpgsql
as $$
declare

    c_p1_updates cursor for
      select  x.zaarm_margin_id,
              y.aarm_reference_p1_price new_price
      from    farms.farm_zaarm_margins x,
              (   select  q.participant_pin,
                          q.program_year,
                          q.inventory_type_code,
                          q.inventory_code,
                          q.production_unit,
                          q.aarm_reference_p1_price,
                          q.aarm_reference_p2_price
                  from    farms.farm_zaarm_margins q
                  where   q.aarm_reference_p1_price > 0
                  and     q.aarm_reference_p2_price > 0
                  and     not exists (    select  1
                                          from    farms.farm_zaarm_margins r
                                          where   r.participant_pin = q.participant_pin
                                          and     r.program_year = q.program_year
                                          and     r.inventory_type_code = q.inventory_type_code
                                          and     r.inventory_code = q.inventory_code
                                          and     r.production_unit = q.production_unit
                                          and     r.aarm_reference_p1_price > 0
                                          and     r.aarm_reference_p2_price > 0
                                          and     r.aarm_reference_p1_price <> q.aarm_reference_p1_price    )    ) y
      where (coalesce(x.aarm_reference_p1_price::text, '') = '' or x.aarm_reference_p1_price = 0)
      and     y.participant_pin = x.participant_pin
      and     y.program_year = x.program_year
      and     y.inventory_type_code = x.inventory_type_code
      and     y.inventory_code = x.inventory_code
      and     y.production_unit = x.production_unit
      and     y.aarm_reference_p2_price = x.aarm_reference_p2_price;


   c_p2_updates cursor for
      select  x.zaarm_margin_id,
              y.aarm_reference_p2_price new_price
      from    farms.farm_zaarm_margins x,
              (   select  q.participant_pin,
                          q.program_year,
                          q.inventory_type_code,
                          q.inventory_code,
                          q.production_unit,
                          q.aarm_reference_p1_price,
                          q.aarm_reference_p2_price
                  from    farms.farm_zaarm_margins q
                  where   q.aarm_reference_p1_price > 0
                  and     q.aarm_reference_p2_price > 0
                  and     not exists (    select  1
                                          from    farms.farm_zaarm_margins r
                                          where   r.participant_pin = q.participant_pin
                                          and     r.program_year = q.program_year
                                          and     r.inventory_type_code = q.inventory_type_code
                                          and     r.inventory_code = q.inventory_code
                                          and     r.production_unit = q.production_unit
                                          and     r.aarm_reference_p1_price > 0
                                          and     r.aarm_reference_p2_price > 0
                                          and     r.aarm_reference_p2_price <> q.aarm_reference_p2_price    )    ) y
      where (coalesce(x.aarm_reference_p2_price::text, '') = '' or x.aarm_reference_p2_price = 0)
      and     y.participant_pin = x.participant_pin
      and     y.program_year = x.program_year
      and     y.inventory_type_code = x.inventory_type_code
      and     y.inventory_code = x.inventory_code
      and     y.production_unit = x.production_unit
      and     y.aarm_reference_p1_price = x.aarm_reference_p1_price;


begin
    for v_update in c_p1_updates
    loop
      update farms.farm_zaarm_margins
      set aarm_reference_p1_price = v_update.new_price
      where zaarm_margin_id = v_update.zaarm_margin_id;
    end loop;

    for v_update in c_p2_updates
    loop
      update farms.farm_zaarm_margins
      set aarm_reference_p2_price = v_update.new_price
      where zaarm_margin_id = v_update.zaarm_margin_id;
    end loop;
end;
$$;
