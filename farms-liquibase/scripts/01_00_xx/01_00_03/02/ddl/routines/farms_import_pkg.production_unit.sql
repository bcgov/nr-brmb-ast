create or replace function farms_import_pkg.production_unit(
    in in_version_id numeric,
    in in_user varchar
)
returns numeric
language plpgsql
as $$
declare
    pu_cursor cursor for
        select z.production_unit,
               z.production_unit_description new_description,
               cu.description old_description
        from farms.farm_z28_prod_insurance_refs z
        left outer join farms.farm_crop_unit_codes cu on z.production_unit::varchar = cu.crop_unit_code
        where cu.crop_unit_code is null
        or z.production_unit_description <> cu.description;
    pu_val record;

    opened numeric := 0;
    closed numeric := 0;

    exp_date date := null;
    errors numeric := 0;

    v_production_unit farms.farm_z28_prod_insurance_refs.production_unit%type := null;
begin
    -- type casts will not occur because they have been completed

    for pu_val in pu_cursor
    loop
        v_production_unit := pu_val.production_unit;
        if opened < 1 then
            call farms_import_pkg.append_imp1(in_version_id, '<PRODUCTION_UNITS>');
            opened := 1;
        end if;

        -- old pu codes are not removed though this process.
        begin
            if pu_val.old_description is null then
                call farms_import_pkg.append_imp1(in_version_id,
                    '<PRODUCTION_UNIT crop_unit_code="' ||
                    farms_import_pkg.scrub(pu_val.production_unit::varchar) || '" action="add">' ||
                    '<ATTR name="description" new="' ||
                    farms_import_pkg.scrub(pu_val.new_description) ||
                    '"/></PRODUCTION_UNIT>');

                exp_date := to_date('12/31/9999', 'MM/DD/YYYY');

                insert into farms.farm_crop_unit_codes(
                    crop_unit_code,
                    description,
                    established_date,
                    expiry_date,
                    revision_count,
                    who_created,
                    when_created,
                    who_updated,
                    when_updated
                ) values (
                    pu_val.production_unit::varchar,
                    pu_val.new_description,
                    current_date,
                    exp_date,
                    1,
                    in_user,
                    current_timestamp,
                    in_user,
                    current_timestamp
                );
            else
                call farms_import_pkg.append_imp1(in_version_id,
                    '<PRODUCTION_UNIT crop_unit_code="' ||
                    farms_import_pkg.scrub(pu_val.production_unit::varchar) || '" action="update">' ||
                    '<ATTR name="description" old="' ||
                    farms_import_pkg.scrub(pu_val.old_description) || '" new="' ||
                    farms_import_pkg.scrub(pu_val.new_description) ||
                    '"/></PRODUCTION_UNIT>');

                update farms.farm_crop_unit_codes
                set description = pu_val.new_description,
                    revision_count = revision_count + 1,
                    who_updated = in_user,
                    when_updated = current_timestamp
                where crop_unit_code = pu_val.production_unit;
            end if;
        exception
            when others then
                errors := errors + 1;

                call farms_import_pkg.append_imp1(in_version_id,
                    '<PRODUCTION_UNIT crop_unit_code="' ||
                    farms_import_pkg.scrub(pu_val.production_unit::varchar) ||
                    '" action="error"><ERROR>' ||
                    farms_import_pkg.scrub(farms_error_pkg.codify_production_unit(sqlerrm,
                                                                                  v_production_unit::varchar)) ||
                    '</ERROR></PRODUCTION_UNIT>');
        end;
    end loop;

    if opened > 0 then
        call farms_import_pkg.append_imp1(in_version_id, '</PRODUCTION_UNITS>');
        closed := 1;
    end if;

    return errors;
exception
    when others then
        if opened > 0 and closed < 1 then
            call farms_import_pkg.append_imp1(in_version_id, '</PRODUCTION_UNITS>');
        end if;

        call farms_import_pkg.append_imp1(in_version_id, -- generic error
            '<ERROR>' || farms_import_pkg.scrub(farms_error_pkg.codify(sqlerrm)) ||
            '</ERROR>');

        return errors + 1;
end;
$$;
