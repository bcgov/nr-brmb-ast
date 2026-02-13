/**
 * Copyright (c) 2025,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.resource.npp;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;

public class NppCommodityGrid extends ChefsResource {

    private LabelValue commodity;
    private Double acres;
    private Double squareMeters;
    private Double numberOfAnimals;

    public NppCommodityGrid() {
        super();
    }

    public NppCommodityGrid(String label, String value, Double acres, Double squareMeters, Double numberOfAnimals) {
        super();
        this.commodity = new LabelValue(label, value);
        this.acres = acres;
        this.squareMeters = squareMeters;
        this.numberOfAnimals = numberOfAnimals;
    }

    public LabelValue getCommodity() {
        return commodity;
    }

    public void setCommodity(LabelValue commodity) {
        this.commodity = commodity;
    }

    public Double getAcres() {
        return acres;
    }

    public void setAcres(Double acres) {
        this.acres = acres;
    }

    public Double getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(Double squareMeters) {
        this.squareMeters = squareMeters;
    }

    public Double getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public void setNumberOfAnimals(Double numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
    }

}