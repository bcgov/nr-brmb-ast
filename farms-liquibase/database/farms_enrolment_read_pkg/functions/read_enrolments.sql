create or replace function farms_enrolment_read_pkg.read_enrolments(
    in in_enrolment_year farms.farm_program_enrolments.enrolment_year%type,
    in in_regional_office_code farms.farm_regional_office_codes.regional_office_code%type
)
returns table(
    agristability_client_id     farms.farm_scenarios_vw.agristability_client_id%type,
    participant_pin             farms.farm_scenarios_vw.participant_pin%type,
    producer_name               farms.farm_persons.corp_name%type,
    scenario_state              varchar,
    failed_to_generate_ind      farms.farm_program_enrolments.failed_to_generate_ind%type,
    failed_reason               farms.farm_program_enrolments.failed_reason%type,
    program_enrolment_id        farms.farm_program_enrolments.program_enrolment_id%type,
    enrolment_year              farms.farm_program_enrolments.enrolment_year%type,
    enrolment_fee               farms.farm_program_enrolments.enrolment_fee%type,
    generated_date              farms.farm_program_enrolments.generated_date%type,
    generated_from_cra_ind      farms.farm_program_enrolments.generated_from_cra_ind%type,
    generated_from_enw_ind      farms.farm_program_enrolments.generated_from_enw_ind%type,
    combined_farm_percent       farms.farm_program_enrolments.combined_farm_percent%type,
    when_updated                farms.farm_program_enrolments.when_updated%type,
    enrolment_revision_count    farms.farm_program_enrolments.revision_count%type
)
language sql
as $$
WITH params AS (
  SELECT in_enrolment_year::smallint AS enrol_year
),

-- Flatten the PY/PYV/SC chain, keep only what we need
py_sc AS (
  SELECT
    py.agristability_client_id,
    py.program_year_id,
    py.year,
    pyv.program_year_version_id,
    sc.agristability_scenario_id,
    sc.scenario_state_code,
    sc.scenario_category_code,
    sc.scenario_class_code,
    sc.scenario_number
  FROM farms.farm_program_years py
  JOIN farms.farm_program_year_versions pyv ON py.program_year_id = pyv.program_year_id
  JOIN farms.farm_agristability_scenarios sc ON pyv.program_year_version_id = sc.program_year_version_id
  LEFT OUTER JOIN farms.farm_office_municipality_xref omx ON omx.municipality_code = pyv.municipality_code
  WHERE py.year <= (SELECT enrol_year - 2 FROM params)
  AND in_regional_office_code IN (omx.regional_office_code, 'ALL')
),

-- Rank scenarios per client according to your rules
ranked AS (
  SELECT
    s.*,
    row_number() OVER (
      PARTITION BY s.agristability_client_id
      ORDER BY
        -- priority 1: true -> first (use NOT so true becomes false=0)
        NOT (
          s.year BETWEEN (SELECT enrol_year - 3 FROM params) AND (SELECT enrol_year - 2 FROM params)
          AND s.scenario_state_code IN ('COMP','AMEND')
          AND s.scenario_category_code = 'FIN'
          AND s.scenario_class_code = 'USER'
        ),
        -- priority 2:
        NOT (
          s.year = (SELECT enrol_year - 2 FROM params)
          AND s.scenario_state_code = 'EN_COMP'
          AND s.scenario_category_code = 'ENW'
        ),
        -- priority 3:
        NOT (
          s.year = (SELECT enrol_year - 2 FROM params)
          AND s.scenario_state_code = 'IP'
          AND s.scenario_category_code = 'ENW'
        ),
        s.year DESC,
        -- REC first within same year:
        NOT (s.scenario_state_code IN ('REC')),
        s.scenario_number DESC
    ) AS rn
  FROM py_sc s
),

-- Choose best scenario per client
best AS (
  SELECT * FROM ranked WHERE rn = 1
),

-- Base client/person info
clients AS (
  SELECT
    ac.agristability_client_id,
    ac.participant_pin,
    COALESCE(o.corp_name, o.last_name || ', ' || o.first_name) AS producer_name
  FROM farms.farm_agristability_clients ac
  JOIN farms.farm_persons o ON o.person_id = ac.person_id
)

SELECT
  b.agristability_client_id,
  c.participant_pin,
  c.producer_name,
  CASE
    WHEN b.year = (SELECT enrol_year - 2 FROM params)
         AND b.scenario_state_code IN ('COMP','AMEND')
         AND b.scenario_category_code = 'FIN'
         AND b.scenario_class_code = 'USER'
      THEN 'COMP'
    WHEN b.year = (SELECT enrol_year - 2 FROM params)
         AND b.scenario_state_code = 'EN_COMP'
         AND b.scenario_category_code = 'ENW'
      THEN 'EN_COMP'
    WHEN b.year = (SELECT enrol_year - 2 FROM params)
         AND b.scenario_state_code = 'IP'
         AND b.scenario_category_code = 'ENW'
      THEN 'EN_IP'
    ELSE 'REC'
  END AS scenario_state,
  pe.failed_to_generate_ind,
  pe.failed_reason,
  pe.program_enrolment_id,
  pe.enrolment_year,
  pe.enrolment_fee,
  pe.generated_date,
  pe.generated_from_cra_ind,
  pe.generated_from_enw_ind,
  pe.combined_farm_percent,
  pe.when_updated,
  pe.revision_count AS enrolment_revision_count
FROM best b
JOIN clients c USING (agristability_client_id)
LEFT JOIN farms.farm_program_enrolments pe
  ON pe.agristability_client_id = b.agristability_client_id
 AND pe.enrolment_year = (SELECT enrol_year FROM params);
$$;
