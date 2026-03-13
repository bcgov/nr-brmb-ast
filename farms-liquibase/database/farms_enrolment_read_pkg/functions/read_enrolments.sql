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
  SELECT
      in_enrolment_year::smallint AS enrol_year,
      (in_enrolment_year - 2)::smallint AS y_m2,
      (in_enrolment_year - 3)::smallint AS y_m3
),

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
  FROM farms.farm_program_years           py
  JOIN farms.farm_program_year_versions   pyv ON py.program_year_id = pyv.program_year_id
  JOIN farms.farm_agristability_scenarios sc  ON pyv.program_year_version_id = sc.program_year_version_id
  CROSS JOIN params p
  WHERE
      py.year <= p.y_m2
      AND (
           in_regional_office_code = 'ALL'
           OR EXISTS (
                SELECT 1
                FROM farms.farm_office_municipality_xref omx
                WHERE omx.municipality_code    = pyv.municipality_code
                  AND omx.regional_office_code = in_regional_office_code
           )
      )
),

ranked AS (
  SELECT
      s.*,
      CASE
        WHEN s.year BETWEEN p.y_m3 AND p.y_m2
         AND s.scenario_state_code IN ('COMP','AMEND')
         AND s.scenario_category_code = 'FIN'
         AND s.scenario_class_code    = 'USER'
          THEN 1
        WHEN s.year = p.y_m2
         AND s.scenario_state_code = 'EN_COMP'
         AND s.scenario_category_code = 'ENW'
          THEN 2
        WHEN s.year = p.y_m2
         AND s.scenario_state_code = 'IP'
         AND s.scenario_category_code = 'ENW'
          THEN 3
        ELSE 4
      END AS pri,
      CASE WHEN s.scenario_state_code = 'REC' THEN 0 ELSE 1 END AS rec_first
  FROM py_sc s
  CROSS JOIN params p
),

best AS (
  SELECT DISTINCT ON (agristability_client_id)
      agristability_client_id,
      program_year_id,
      year,
      program_year_version_id,
      agristability_scenario_id,
      scenario_state_code,
      scenario_category_code,
      scenario_class_code,
      scenario_number
  FROM ranked
  ORDER BY agristability_client_id, pri, year DESC, rec_first, scenario_number DESC
),

y2_has_latest_base_with_unassigned_rie AS (
  SELECT DISTINCT lb.agristability_client_id
  FROM (
    SELECT DISTINCT ON (py.program_year_id)
           py.program_year_id,
           py.agristability_client_id,
           sc.program_year_version_id,
           sc.scenario_number
    FROM farms.farm_program_years           py
    JOIN farms.farm_program_year_versions   pyv ON pyv.program_year_id = py.program_year_id
    JOIN farms.farm_agristability_scenarios sc  ON sc.program_year_version_id = pyv.program_year_version_id
    CROSS JOIN params p
    WHERE py.year = p.y_m2
      AND sc.scenario_class_code IN ('CRA', 'CHEF', 'LOCAL', 'GEN')
    ORDER BY py.program_year_id, sc.scenario_number DESC
  ) AS lb
  JOIN farms.farm_farming_operations fo
    ON fo.program_year_version_id = lb.program_year_version_id
  JOIN farms.farm_reported_income_expenses rie
    ON rie.farming_operation_id = fo.farming_operation_id
   AND rie.agristability_scenario_id IS NULL
),

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
    WHEN b.year = p.y_m2
     AND b.scenario_state_code IN ('COMP','AMEND')
     AND b.scenario_category_code = 'FIN'
     AND b.scenario_class_code    = 'USER'
      THEN 'COMP'
    WHEN b.year = p.y_m3
     AND b.scenario_state_code IN ('COMP','AMEND')
     AND b.scenario_category_code = 'FIN'
     AND b.scenario_class_code    = 'USER'
     AND b.agristability_client_id IN (SELECT agristability_client_id FROM y2_has_latest_base_with_unassigned_rie)
      THEN 'COMP'
    WHEN b.year = p.y_m2
     AND b.scenario_state_code = 'EN_COMP'
     AND b.scenario_category_code = 'ENW'
      THEN 'EN_COMP'
    WHEN b.year = p.y_m2
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
JOIN clients c
  ON c.agristability_client_id = b.agristability_client_id
CROSS JOIN params p
LEFT JOIN farms.farm_program_enrolments pe
  ON pe.agristability_client_id = b.agristability_client_id
 AND pe.enrolment_year = p.enrol_year;
$$;
