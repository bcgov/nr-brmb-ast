package ca.bc.gov.farms.service.api.v1.validation;

public class Errors {

    public static final String PROGRAM_YEAR_NOTNULL = "error.program.year.notnull";

    public static final String UNIT_COMMENT_SIZE = "error.unit.comment.size";

    public static final String MUNICIPALITY_CODE_NOTBLANK = "error.municipality.code.notblank";
    public static final String MUNICIPALITY_CODE_SIZE = "error.municipality.code.size";

    public static final String INVENTORY_ITEM_CODE_NOTBLANK = "error.inventory.item.code.notblank";
    public static final String INVENTORY_ITEM_CODE_SIZE = "error.inventory.item.code.size";

    public static final String STRUCTURE_GROUP_CODE_NOTBLANK = "error.structure.group.code.notblank";
    public static final String STRUCTURE_GROUP_CODE_SIZE = "error.structure.group.code.size";

    public static final String INVENTORY_CODE_SIZE = "error.inventory.code.size";
    public static final String INVENTORY_DESC_SIZE = "error.inventory.desc.size";

    public static final String YEAR_MINUS_6_MARGIN_DIGITS = "error.year.minus.6.margin.digits";
    public static final String YEAR_MINUS_5_MARGIN_DIGITS = "error.year.minus.5.margin.digits";
    public static final String YEAR_MINUS_4_MARGIN_DIGITS = "error.year.minus.4.margin.digits";
    public static final String YEAR_MINUS_3_MARGIN_DIGITS = "error.year.minus.3.margin.digits";
    public static final String YEAR_MINUS_2_MARGIN_DIGITS = "error.year.minus.2.margin.digits";
    public static final String YEAR_MINUS_1_MARGIN_DIGITS = "error.year.minus.1.margin.digits";

    public static final String YEAR_MINUS_6_EXPENSE_DIGITS = "error.year.minus.6.expense.digits";
    public static final String YEAR_MINUS_5_EXPENSE_DIGITS = "error.year.minus.5.expense.digits";
    public static final String YEAR_MINUS_4_EXPENSE_DIGITS = "error.year.minus.4.expense.digits";
    public static final String YEAR_MINUS_3_EXPENSE_DIGITS = "error.year.minus.3.expense.digits";
    public static final String YEAR_MINUS_2_EXPENSE_DIGITS = "error.year.minus.2.expense.digits";
    public static final String YEAR_MINUS_1_EXPENSE_DIGITS = "error.year.minus.1.expense.digits";

    public static final String CROP_UNIT_CODE_NOTBLANK = "error.crop.unit.code.notblank";
    public static final String CROP_UNIT_CODE_SIZE = "error.crop.unit.code.size";

    public static final String PERIOD_01_PRICE_DIGITS = "error.period.01.price.digits";
    public static final String PERIOD_02_PRICE_DIGITS = "error.period.02.price.digits";
    public static final String PERIOD_03_PRICE_DIGITS = "error.period.03.price.digits";
    public static final String PERIOD_04_PRICE_DIGITS = "error.period.04.price.digits";
    public static final String PERIOD_05_PRICE_DIGITS = "error.period.05.price.digits";
    public static final String PERIOD_06_PRICE_DIGITS = "error.period.06.price.digits";
    public static final String PERIOD_07_PRICE_DIGITS = "error.period.07.price.digits";
    public static final String PERIOD_08_PRICE_DIGITS = "error.period.08.price.digits";
    public static final String PERIOD_09_PRICE_DIGITS = "error.period.09.price.digits";
    public static final String PERIOD_10_PRICE_DIGITS = "error.period.10.price.digits";
    public static final String PERIOD_11_PRICE_DIGITS = "error.period.11.price.digits";
    public static final String PERIOD_12_PRICE_DIGITS = "error.period.12.price.digits";

    public static final String PERIOD_01_VARIANCE_DIGITS = "error.period.01.variance.digits";
    public static final String PERIOD_02_VARIANCE_DIGITS = "error.period.02.variance.digits";
    public static final String PERIOD_03_VARIANCE_DIGITS = "error.period.03.variance.digits";
    public static final String PERIOD_04_VARIANCE_DIGITS = "error.period.04.variance.digits";
    public static final String PERIOD_05_VARIANCE_DIGITS = "error.period.05.variance.digits";
    public static final String PERIOD_06_VARIANCE_DIGITS = "error.period.06.variance.digits";
    public static final String PERIOD_07_VARIANCE_DIGITS = "error.period.07.variance.digits";
    public static final String PERIOD_08_VARIANCE_DIGITS = "error.period.08.variance.digits";
    public static final String PERIOD_09_VARIANCE_DIGITS = "error.period.09.variance.digits";
    public static final String PERIOD_10_VARIANCE_DIGITS = "error.period.10.variance.digits";
    public static final String PERIOD_11_VARIANCE_DIGITS = "error.period.11.variance.digits";
    public static final String PERIOD_12_VARIANCE_DIGITS = "error.period.12.variance.digits";

    public static final String ELIGIBILITY_IND_NOTBLANK = "error.eligibility.ind.notblank";
    public static final String ELIGIBILITY_IND_SIZE = "error.eligibility.ind.size";

    public static final String INSURABLE_VALUE_DIGITS = "error.insurable.value.digits";

    public static final String PREMIUM_RATE_DIGITS = "error.premium.rate.digits";

    public static final String COMMODITY_TYPE_CODE_SIZE = "error.commodity.type.code.size";

    public static final String FRUIT_VEG_TYPE_CODE_NOTBLANK = "error.fruit.veg.type.code.notblank";
    public static final String FRUIT_VEG_TYPE_CODE_SIZE = "error.fruit.veg.type.code.size";

    public static final String MULTI_STAGE_COMMDTY_CODE_SIZE = "error.multi.stage.commdty.code.size";

    public static final String MARKET_COMMODITY_IND_NOTBLANK = "error.market.commodity.ind.notblank";
    public static final String MARKET_COMMODITY_IND_SIZE = "error.market.commodity.ind.size";

    public static final String INVENTORY_GROUP_CODE_SIZE = "error.inventory.group.code.size";

    public static final String INVENTORY_CLASS_CODE_NOTBLANK = "error.inventory.class.code.notblank";
    public static final String INVENTORY_CLASS_CODE_SIZE = "error.inventory.class.code.size";

    public static final String ROLLUP_INVENTORY_ITEM_CODE_SIZE = "error.rollup.inventory.item.code.size";

    public static final String ROLLUP_STRUCTURE_GROUP_CODE_SIZE = "error.rollup.structure.group.code.size";

    public static final String PARAMETER_NAME_NOTBLANK = "error.parameter.name.notblank";
    public static final String PARAMETER_NAME_SIZE = "error.parameter.name.size";

    public static final String PARAMETER_VALUE_NOTBLANK = "error.parameter.value.notblank";
    public static final String PARAMETER_VALUE_SIZE = "error.parameter.value.size";

    public static final String SENSITIVE_DATA_IND_NOTBLANK = "error.sensitive.data.ind.notblank";
    public static final String SENSITIVE_DATA_IND_SIZE = "error.sensitive.data.ind.size";

    public static final String CONFIG_PARAM_TYPE_CODE_NOTBLANK = "error.config.param.type.code.notblank";
    public static final String CONFIG_PARAM_TYPE_CODE_SIZE = "error.config.param.type.code.size";

    public static final String LINE_ITEM_NOTNULL = "error.line.item.notnull";

    public static final String DESCRIPTION_NOTBLANK = "error.description.notblank";
    public static final String DESCRIPTION_SIZE = "error.description.size";

    public static final String PROVINCE_SIZE = "error.province.size";

    public static final String ELIGIBILITY_FOR_REF_YEARS_IND_NOTBLANK = "error.eligibility.for.ref.years.ind.notblank";
    public static final String ELIGIBILITY_FOR_REF_YEARS_IND_SIZE = "error.eligibility.for.ref.years.ind.size";

    public static final String YARDAGE_IND_NOTBLANK = "error.yardage.ind.notblank";
    public static final String YARDAGE_IND_SIZE = "error.yardage.ind.size";

    public static final String PROGRAM_PAYMENT_IND_NOTBLANK = "error.program.payment.ind.notblank";
    public static final String PROGRAM_PAYMENT_IND_SIZE = "error.program.payment.ind.size";

    public static final String CONTRACT_WORK_IND_NOTBLANK = "error.contract.work.ind.notblank";
    public static final String CONTRACT_WORK_IND_SIZE = "error.contract.work.ind.size";

    public static final String SUPPLY_MANAGED_COMMODITY_IND_NOTBLANK = "error.supply.managed.commodity.ind.notblank";
    public static final String SUPPLY_MANAGED_COMMODITY_IND_SIZE = "error.supply.managed.commodity.ind.size";

    public static final String EXCLUDE_FROM_REVENUE_CALC_IND_NOTBLANK = "error.exclude.from.revenue.calc.ind.notblank";
    public static final String EXCLUDE_FROM_REVENUE_CALC_IND_SIZE = "error.exclude.from.revenue.calc.ind.size";

    public static final String INDUSTRY_AVERAGE_EXPENSE_IND_NOTBLANK = "error.industry.average.expense.ind.notblank";
    public static final String INDUSTRY_AVERAGE_EXPENSE_IND_SIZE = "error.industry.average.expense.ind.size";

    public static final String REVENUE_VARIANCE_LIMIT_NOTNULL = "error.revenue.variance.limit.notnull";

    public static final String MIN_TOTAL_PREMIUM_AMOUNT_NOTNULL = "error.min.total.premium.amount.notnull";

    public static final String MAX_TOTAL_PREMIUM_AMOUNT_NOTNULL = "error.max.total.premium.amount.notnull";

    public static final String RISK_CHARGE_FLAT_AMOUNT_NOTNULL = "error.risk.charge.flat.amount.notnull";

    public static final String RISK_CHARGE_PCT_PREMIUM_NOTNULL = "error.risk.charge.pct.premium.notnull";

    public static final String ADJUST_CHARGE_FLAT_AMOUNT_NOTNULL = "error.adjust.charge.flat.amount.notnull";

    public static final String CONVERSION_FACTOR_NOTNULL = "error.conversion.factor.notnull";
    public static final String CONVERSION_FACTOR_DIGITS = "error.conversion.factor.digits";

    public static final String TARGET_CROP_UNIT_CODE_NOTBLANK = "error.target.crop.unit.code.notblank";
    public static final String TARGET_CROP_UNIT_CODE_SIZE = "error.target.crop.unit.code.size";
}
