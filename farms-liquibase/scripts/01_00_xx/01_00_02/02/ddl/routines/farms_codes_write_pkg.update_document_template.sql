create or replace function farms_codes_write_pkg.update_document_template(
    in in_template_name farms.farm_document_templates.template_name%type,
    in in_user farms.farm_document_templates.who_updated%type
)
returns refcursor
language plpgsql
as $$
begin

    update farms.farm_document_templates t
    set t.template_content = '',
        t.when_updated = current_timestamp,
        t.who_updated = in_user,
        t.revision_count = t.revision_count + 1
    where t.template_name = in_template_name;

    return null;

end;
$$;
