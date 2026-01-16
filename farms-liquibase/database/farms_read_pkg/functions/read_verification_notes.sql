create or replace function farms_read_pkg.read_verification_notes(
    in py_id numeric
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        -- If any of the notes clobs are empty, return its template
        select (case
                   when py.interim_verification_notes is null or length(py.interim_verification_notes) = 0 then it.template_content
                   else py.interim_verification_notes
               end) interim_verification_notes,
               (case
                   when py.final_verification_notes is null or length(py.final_verification_notes) = 0 then ft.template_content
                   else py.final_verification_notes
               end) final_verification_notes,
               (case
                   when py.adjustment_verification_notes is null or length(py.adjustment_verification_notes) = 0 then at.template_content
                   else py.adjustment_verification_notes
               end) adjustment_verification_notes
        from farms.farm_program_years py
        left outer join farms.farm_document_templates it on it.template_name = 'Verification Notes - Interim'
        left outer join farms.farm_document_templates ft on ft.template_name = 'Verification Notes - Final'
        left outer join farms.farm_document_templates at on at.template_name = 'Verification Notes - Adjustment'
        where py.program_year_id = py_id;
    return cur;
end;
$$;
