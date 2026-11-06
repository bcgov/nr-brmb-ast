/**
 * Copyright (c) 2026,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.processor;

import static ca.bc.gov.srm.farm.chefs.forms.ChefsFarmTypeCodes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import ca.bc.gov.srm.farm.chefs.resource.optOut.OptOutSubmissionDataResource;
import ca.bc.gov.srm.farm.domain.Client;

/** Server-side checks for the supplied Opt Out form, before any CORE write. */
public final class OptOutSubmissionValidator {
  private OptOutSubmissionValidator() {
  }

  public static List<String> validate(OptOutSubmissionDataResource data, Client client) {
    List<String> errors = new ArrayList<>();
    if (data.getParticipantPin() == null || data.getParticipantPin() <= 0) errors.add("A valid participant PIN is required.");
    required(errors, "Participant Name", data.getParticipantName());
    required(errors, "Province/Territory of main farmstead", data.getProvinceTerritoryOfMainFarmstead());
    // The supplied CHEFS version offers these two program years.
    if (!Arrays.asList(2026, 2027).contains(data.getYear())) errors.add("Program Year must be 2026 or 2027.");
    if (client == null) errors.add("PIN not found in BCFARMS.");

    String type = data.getFarmType() == null ? null : data.getFarmType().getValue();
    if (INDIVIDUAL.equals(type) || STATUS_INDIAN_FARMING_ON_A_RESERVE.equals(type)) {
      matchIdentifier(errors, "SIN", data.getSinNumber(), client == null ? null : client.getSin(), 9, false);
    } else if (CORPORATION.equals(type) || CO_OPERATIVE.equals(type)) {
      matchIdentifier(errors, "Business Number", data.getBusinessTaxNumberBn(), client == null ? null : client.getBusinessNumber(), 9, true);
    } else if (TRUST.equals(type) || COMMUNAL_ORGANIZATION.equals(type)) {
      matchIdentifier(errors, "Trust Number", data.getTrustNumber(), client == null ? null : client.getTrustNumber(), 8, false);
    } else if (BAND_FARM.equals(type)) {
      required(errors, "Band Number", data.getBandNumber());
      // Client has no band-number field against which this identity can be verified.
      errors.add("Band farm identity requires staff verification before opting out in CORE.");
    } else {
      errors.add("A supported Farm Type is required.");
    }

    if (!Arrays.asList("noLongerFarming", "agriStabilityIsDifficultToUnderstand", "deathOfParticipant", "otherSpecify")
        .contains(data.getReasonForCancelling())) errors.add("A valid Reason for cancelling is required.");
    required(errors, "Signature", data.getSignature());
    required(errors, "Signature Full Name", data.getSignatureFullName());
    required(errors, "Signature Date", data.getSignatureDate());
    if ("deathOfParticipant".equals(data.getReasonForCancelling())) {
      String onBehalf = data.getOnBehalfOfParticipant();
      if (!"yes".equals(onBehalf) && !"no".equals(onBehalf)) errors.add("On behalf of participant must be Yes or No.");
      if ("yes".equals(onBehalf)) {
        required(errors, "Representative Signature", data.getSignature1());
        required(errors, "Representative First Name", data.getSignatureFirstName());
        required(errors, "Representative Last Name", data.getSignatureLastName());
        required(errors, "Representative Signature Date", data.getSignatureDate1());
        required(errors, "Relation to the client", data.getHowDoYouKnowTheParticipant());
      }
      if (!hasUploadedFile(data.getSimplefile())) errors.add("A copy of the death certificate is required.");
    }
    return errors;
  }

  private static boolean hasUploadedFile(JsonNode files) {
    if (files == null || !files.isArray()) return false;
    for (JsonNode file : files) {
      if (file != null && file.isObject() && file.size() > 0) return true;
    }
    return false;
  }

  private static void required(List<String> errors, String name, String value) {
    if (value == null || value.trim().isEmpty()) errors.add(name + " is required.");
  }

  private static void matchIdentifier(List<String> errors, String name, String submitted, String stored, int digits, boolean business) {
    String value = normalize(submitted);
    String expected = normalize(stored);
    if (digits == 8) {
      expected = expected.startsWith("T") ? expected.substring(1) : expected;
      value = value.startsWith("T") ? value.substring(1) : value;
    } else if (business) {
      // CORE/FARMS may store a BN with its RC account suffix.
      if (expected.matches("[0-9]{9}RC[0-9]{4}")) expected = expected.substring(0, 9);
      if (value.matches("[0-9]{9}RC[0-9]{4}")) value = value.substring(0, 9);
    }
    if (!value.matches("[0-9]{" + digits + "}") || !value.equals(expected)) {
      errors.add(name + " is missing, invalid, or does not match BCFARMS.");
    }
  }

  private static String normalize(String value) {
    return value == null ? "" : value.replaceAll("[\\s-]", "").toUpperCase(java.util.Locale.ROOT);
  }
}
