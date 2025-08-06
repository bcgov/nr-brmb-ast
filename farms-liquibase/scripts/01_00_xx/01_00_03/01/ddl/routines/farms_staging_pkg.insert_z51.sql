create or replace procedure farms_staging_pkg.insert_z51(
   in in_contribution_key farms.farm_z51_participant_contribs.contribution_key%type,
   in in_participant_pin farms.farm_z51_participant_contribs.participant_pin%type,
   in in_program_year farms.farm_z51_participant_contribs.program_year%type,
   in in_provincial_contributions farms.farm_z51_participant_contribs.provincial_contributions%type,
   in in_federal_contributions farms.farm_z51_participant_contribs.federal_contributions%type,
   in in_interim_contributions farms.farm_z51_participant_contribs.interim_contributions%type,
   in in_producer_share farms.farm_z51_participant_contribs.producer_share%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.farm_z51_participant_contribs (
        contribution_key,
        participant_pin,
        program_year,
        provincial_contributions,
        federal_contributions,
        interim_contributions,
        producer_share,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
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
