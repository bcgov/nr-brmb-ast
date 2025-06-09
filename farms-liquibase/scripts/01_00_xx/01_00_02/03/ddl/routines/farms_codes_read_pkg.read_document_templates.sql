create or replace function farms_codes_read_pkg.read_document_templates(
    in in_template_name farms.document_template.template_name%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select t.template_name,
               t.template_content
        from farms.document_template t
        where (in_template_name is null or t.template_name = in_template_name);
    return cur;

end;
$$;
