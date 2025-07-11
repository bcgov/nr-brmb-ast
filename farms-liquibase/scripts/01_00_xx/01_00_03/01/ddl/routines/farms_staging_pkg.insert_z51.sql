create or replace procedure farms_staging_pkg.insert_z51(
   in in_contribution_key farms.z51_participant_contribution.contribution_key%type,
   in in_participant_pin farms.z51_participant_contribution.participant_pin%type,
   in in_program_year farms.z51_participant_contribution.program_year%type,
   in in_provincial_contributions farms.z51_participant_contribution.provincial_contributions%type,
   in in_federal_contributions farms.z51_participant_contribution.federal_contributions%type,
   in in_interim_contributions farms.z51_participant_contribution.interim_contributions%type,
   in in_producer_share farms.z51_participant_contribution.producer_share%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.z51_participant_contribution (
        contribution_key,
        participant_pin,
        program_year,
        provincial_contributions,
        federal_contributions,
        interim_contributions,
        producer_share,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        in_contribution_key,
        in_participant_pin,
        in_program_year,
        in_provincial_contributions,
        in_federal_contributions,
        in_interim_contributions,
        in_producer_share,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
