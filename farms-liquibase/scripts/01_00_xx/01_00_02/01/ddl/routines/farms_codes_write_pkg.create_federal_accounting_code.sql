create or replace procedure farms_codes_write_pkg.create_federal_accounting_code(
   in in_federal_accounting_code farms.federal_accounting_code.federal_accounting_code%type,
   in in_description farms.federal_accounting_code.description%type,
   in in_effective_date farms.federal_accounting_code.effective_date%type,
   in in_expiry_date farms.federal_accounting_code.expiry_date%type,
   in in_user farms.federal_accounting_code.update_user%type
)
language plpgsql
as $$
begin
    insert into farms.federal_accounting_code (
        federal_accounting_code,
        description,
        effective_date,
        expiry_date,
        create_user,
        create_date,
        update_user,
        update_date,
        revision_count
    ) values (
        in_federal_accounting_code,
        in_description,
        in_effective_date,
        in_expiry_date,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp,
        1
    );
end;
$$;
