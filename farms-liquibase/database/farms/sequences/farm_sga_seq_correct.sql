-- farm_sga_seq was never used to populate farm_structure_group_attributs (the 02_34_00 code-table
-- script used a different sequence), so it can hand out keys that already exist.
-- Advance it past the table's highest key. Safe to re-run; never moves the sequence backwards.
DO $$
DECLARE
    v_max  bigint;
    v_next bigint;
BEGIN
    SELECT max(structure_group_attrib_id) INTO v_max FROM farms.farm_structure_group_attributs;
    SELECT coalesce(last_value + increment_by, start_value) INTO v_next
      FROM pg_sequences
     WHERE schemaname = 'farms' AND sequencename = 'farm_sga_seq';
    IF v_max IS NOT NULL AND v_max >= v_next THEN
        PERFORM setval('farms.farm_sga_seq', v_max, true);
    END IF;
END
$$;
