create or replace function farms_codes_write_pkg.update_document_template(
    in in_template_name farms.farm_document_templates.template_name%type,
    in in_user farms.farm_document_templates.who_updated%type
)
returns refcursor
language plpgsql
as $$
begin

    update farms.farm_document_templates
    set template_content = '',
        when_updated = current_timestamp,
        who_updated = in_user,
        revision_count = revision_count + 1
    where template_name = in_template_name;

    return null;

end;
$$;
