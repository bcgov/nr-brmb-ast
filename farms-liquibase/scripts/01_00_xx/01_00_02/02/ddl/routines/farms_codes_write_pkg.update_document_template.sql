create or replace function farms_codes_write_pkg.update_document_template(
    in in_template_name farms.document_template.template_name%type,
    in in_user farms.document_template.update_user%type
)
returns refcursor
language plpgsql
as $$
begin

    update farms.document_template t
    set t.template_content = '',
        t.update_date = current_timestamp,
        t.update_user = in_user,
        t.revision_count = t.revision_count + 1
    where t.template_name = in_template_name;

    return null;

end;
$$;
