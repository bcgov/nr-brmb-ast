drop function if exists farms_error_pkg.codify(
    varchar
);

create or replace function farms_error_pkg.codify(
    in msg varchar
)
returns varchar
language plpgsql
as $$
begin
    if msg is null then
        return null;
    elsif msg like '%pk_pyv%' then
        return 'Program Year Version PK violation';
    elsif msg like '%pk_icc1%' then
        return 'Inventory Class Code PK violation';
    elsif msg like '%pk_mc%' then
        return 'Municipality Code PK violation';
    elsif msg like '%fk_bpu_mc%' then
        return 'The specified Municipality Code was not found for this BPU';
    elsif msg like '%fk_bpu_iic%' then
        return 'The specified Inventory Code was not found for this BPU ';
    elsif msg like '%fk_cs_ac1%' then
        return 'The specified Agristability Client was not found for this Subscription ';
    elsif msg like '%fk_cs_ssc%' then
        return 'The specified Subscription Status Code was not found for this Subscription ';
    elsif msg like '%fk_cs_ar%' then
        return 'The specified Representative was not found for this Subscription';
    elsif msg like '%fk_rs_as1%' then
        return 'The specified Reference Scenario was not found for this Scenario';
    elsif msg like '%fk_rs_as%' then
        return 'The specified Scenario was not found for this Reference Year Scenario';
    elsif msg like '%fk_sbx_bpu%' then
        return 'The specified BPU was not found for this BPU cross reference entry';
    elsif msg like '%fk_sbx_as%' then
        return 'The specified Scenario was not found for this BPU cross reference entry';
    elsif msg like '%fk_zpc_zpfi%' then
        return 'File 51 record does not have a parent record in file 2';
    elsif msg like '%pk_zpc%' then
        return 'File 51 PK violation';
    elsif msg like '%pk_rie%' then
        return 'Reported Income Expense PK violation';
    elsif msg like '%pk_bpu%' then
        return 'Benchmark Per Unit PK violation';
    elsif msg like '%pk_cs1%' then
        return 'Client Subscription PK violation';
    elsif msg like '%pk_rs%' then
        return 'Reference Scenario PK violation';
    elsif msg like '%pk_puc%' then
        return 'Productive Unit Capacity PK violation';
    elsif msg like '%pk_fac%' then
        return 'Federal Accounting Code PK violation';
    elsif msg like '%pk_zfmv%' then
        return 'FMV staging table PK violation';
    elsif msg like '%pk_sbx%' then
        return 'BPU Cross Reference PK violation';
    elsif msg like '%pk_zsi%' then
        return 'File 3 PK violation';
    elsif msg like '%pk_ssa%' then
        return 'Scenario State Audit PK violation';
    elsif msg like '%pk_by%' then
        return 'Benchmark Year PK violation';
    elsif msg like '%pk_iv%' then
        return 'Import Version PK violation';
    elsif msg like '%pk_pi%' then
        return 'Production Insurance PK violation';
    elsif msg like '%pk_zei%' then
        return 'File 99 PK violation';
    elsif msg like '%pk_zicr%' then
        return 'File 29 PK violation';
    elsif msg like '%pk_bct%' then
        return 'Benefit Calculated Total PK violation';
    elsif msg like '%pk_zpi%' then
        return 'File 1 PK violation';
    elsif msg like '%pk_iic%' then
        return 'Income Expense PK violation';
    elsif msg like '%pk_fmv%' then
        return 'Fair Market Value PK violation';
    elsif msg like '%pk_zpry%' then
        return 'File 42 PK violation';
    elsif msg like '%pk_zlpc%' then
        return 'File 23 PK violation';
    elsif msg like '%pk_wfp%' then
        return 'Whole Farm Participant PK violation';
    elsif msg like '%pk_acx%' then
        return 'Agristability Commodity Cross Reference PK violation';
    elsif msg like '%pk_ssc1%' then
        return 'Subscription Status Code PK violation';
    elsif msg like '%pk_zpir%' then
        return 'File 28 PK violation';
    elsif msg like '%pk_ri%' then
        return 'Reported Inventory PK violation';
    elsif msg like '%pk_fo%' then
        return 'Farming Operation PK violation';
    elsif msg like '%pk_zpi1%' then
        return 'File 5 PK violation';
    elsif msg like '%pk_zprsd%' then
        return 'File 40 PK violation';
    elsif msg like '%pk_zied%' then
        return 'File 4 PK violation';
    elsif msg like '%pk_zps%' then
        return 'File 21 PK violation';
    elsif msg like '%pk_zpbc%' then
        return 'File 50 PK violation';
    elsif msg like '%pk_py%' then
        return 'Program Year PK violation';
    elsif msg like '%pk_zpfi%' then
        return 'File 2 PK violation';
    elsif msg like '%uk_acx_icc_iic%' then
        return 'Agristability Commodity Cross Reference combination is not unique';
    elsif msg like '%fk_zsi_zpfi%' then
        return 'File 3 record does not have a parent record in file 2';
    elsif msg like '%fk_ssa_as%' then
        return 'The specified Scenario was not found for this Scenario State Audit entry';
    elsif msg like '%fk_ssa_ssc%' then
        return 'The specified Scenario State Code was not found for this Scenario State Audit entry';
    elsif msg like '%fk_by_bpu%' then
        return 'The specified BPU was not found for this Benchmark Year ';
    elsif msg like '%fk_iv_isc%' then
        return 'The specified Import State Code was not found for this Import Version entry';
    elsif msg like '%fk_iv_icc%' then
        return 'The specified Import Class Code was not found for this Import Version entry';
    elsif msg like '%fk_pi_fo%' then
        return 'The specified Operation was not found for this Production Insurance entry';
    elsif msg like '%fk_bct_as%' then
        return 'The specified Scenario was not found for this Calculated Benefit';
    elsif msg like '%fk_fmv_mc%' then
        return 'The specified Municipality Code was not found for this FMV entry';
    elsif msg like '%fk_fmv_cuc%' then
        return 'The specified Crop Unit Code was not found for this FMV entry';
    elsif msg like '%fk_fmv_iic%' then
        return 'The specified Inventory Code was not found for this FMV entry';
    elsif msg like '%fk_zpry_zsi%' then
        return 'File 42 record does not have a parent record in file 3';
    elsif msg like '%fk_zlpc_zsi%' then
        return 'File 23 record does not have a parent record in file 3';
    elsif msg like '%fk_wfp_pyv%' then
        return 'The specified Program Year Version was not found for this Whole Farm Participant';
    elsif msg like '%fk_acx_igc%' then
        return 'The specified Inventory Group Code was not found for this Commodity cross reference entry';
    elsif msg like '%fk_acx_icc%' then
        return 'The specified Inventory Class Code was not found for this Commodity cross reference entry';
    elsif msg like '%fk_acx_iic%' then
        return 'The specified Inventory Code was not found for this Commodity cross reference entry';
    elsif msg like '%fk_zpi_zsi%' then
        return 'File 5 record does not have a parent record in file 3';
    elsif msg like '%fk_zprsd_zsi%' then
        return 'File 40 record does not have a parent record in file 3';
    elsif msg like '%fk_zprs_zpir%' then
        return 'File 40 record does not have a parent record in file 28';
    elsif msg like '%fk_zied_zsi%' then
        return 'File 4 record does not have a parent record in file 3';
    elsif msg like '%fk_zps_zsi%' then
        return 'File 21 record does not have a parent record in file 3';
    elsif msg like '%fk_zpbc_zpfi%' then
        return 'File 50 record does not have a parent record in file 2';
    elsif msg like '%fk_py_ac1%' then
        return 'The specified Client was not found for this Program year';
    elsif msg like '%fk_zpfi_zpi%' then
        return 'File 2 record does not have a parent record in file 1';
    elsif msg like '%pk_igc%' then
        return 'Inventory Group Code PK violation';
    elsif msg like '%pk_pcc%' then
        return 'Productive Capacity Code PK violation';
    elsif msg like '%pk_fsc%' then
        return 'Federal Status Code PK violation';
    elsif msg like '%pk_cuc%' then
        return 'Crop Unit Code PK violation';
    elsif msg like '%pk_ssc%' then
        return 'Scenario State Code PK violation';
    elsif msg like '%pk_ac1%' then
        return 'Agristability Client PK violation';
    elsif msg like '%pk_p%' then
        return 'Person PK violation';
    elsif msg like '%pk_scc2%' then
        return 'Structural Change Code PK violation';
    elsif msg like '%pk_isc%' then
        return 'Import State Code PK violation';
    elsif msg like '%pk_zbpu%' then
        return 'BPU Staging file PK violation';
    elsif msg like '%pk_scc1%' then
        return 'Scenario Class Code PK violation';
    elsif msg like '%pk_zpi2%' then
        return 'File 22 PK violation';
    elsif msg like '%pk_ac%' then
        return 'Agristability Claim PK violation';
    elsif msg like '%pk_ppc%' then
        return 'Participant Profile Code PK violation';
    elsif msg like '%pk_icc%' then
        return 'Import Class Code PK violation';
    elsif msg like '%pk_bcm%' then
        return 'Benefit Calculated Margin PK violation';
    elsif msg like '%pk_plc%' then
        return 'Participant Language Code PK violation';
    elsif msg like '%pk_as%' then
        return 'Agristability Scenario PK violation';
    elsif msg like '%pk_il%' then
        return 'Import Log PK violation';
    elsif msg like '%pk_ar%' then
        return 'Agristability Representative PK violation';
    elsif msg like '%pk_fop%' then
        return 'Farming Operation Partner PK violation';
    elsif msg like '%pk_sgc%' then
        return 'Structure Group Code PK violation';
    elsif msg like '%uk_ac1_pp%' then
        return 'Agristability Client/Participant PIN is not unique';
    elsif msg like '%uk_as_pyvi_sn%' then
        return 'Program Year Version/Scenario Number is not unique';
    elsif msg like '%uk_ar_ug%' then
        return 'Agrstability Representative/GUID is not unique';
    elsif msg like '%uk_ar_u%' then
        return 'Agrstability Representative/User ID combination is not unique';
    elsif msg like '%fk_zpi_zsi1%' then
        return 'File 22 record does not have a parent record in file 3';
    elsif msg like '%fk_ac_as%' then
        return 'The specified Scenario was not found for this Claim';
    elsif msg like '%fk_bcm_fo%' then
        return 'The specified Operation was not found for this Calculated Margin';
    elsif msg like '%fk_bcm_as%' then
        return 'The specified Scenario was not found for this Calculated Margin';
    elsif msg like '%fk_as_ssc%' then
        return 'The specified Scenario State Code was not found for this Scenario';
    elsif msg like '%fk_as_pyv%' then
        return 'The specified Program Year Version was not found for this Scenario';
    elsif msg like '%fk_as_scc1%' then
        return 'The specified Scenario Class Code was not found for this Scenario';
    elsif msg like '%fk_il_iv%' then
        return 'The specified Import Version was not found for this Import Log entry';
    elsif msg like '%fk_il_pyv%' then
        return 'The specified Program Year Version was not found for this Import Log entry';
    elsif msg like '%fk_fop_fo%' then
        return 'The specified Operation was not found for this Partner';
    else
        return msg;
    end if;
end;
$$;
