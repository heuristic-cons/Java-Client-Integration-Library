/**
 * -----------------------------------------------------------------------------
 * File=PackIdentifierVerification.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Extension methods for pack identifiers.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.extensions;

import com.reply.solidsoft.nbs.integration.model.DataEntryMode;
import com.reply.solidsoft.nbs.integration.model.PackIdentifier;
import com.reply.solidsoft.nbs.integration.model.ProductCodeScheme;
import com.reply.solidsoft.nbs.integration.model.responses.LocalValidationResponse;
import com.reply.solidsoft.nbs.integration.properties.Resources;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Extension methods for pack identifiers.
 */
public final class PackIdentifierVerification {

    /**
     * Regular expression for GS1 product codes. P If GS1, the product code must
     * contain only digits.
     */
    private static final Pattern PRODUCT_CODE_GS1 = Pattern.compile("^\\d*$");

    /**
     * Regular expression for IFA product codes. If IFA, the product code must
     * be between 5 and 22 characters in length and must contain only upper case
     * letters or digits.
     */
    private static final Pattern PRODUCT_CODE_IFA = Pattern.compile("^[A-Z0-9]{5,22}$");

    /**
     * Regular expression for IFA product codes (PPNs). If IFA, the product code
     * must obey the German PPN rules. It must be 12 digits in length.
     */
    private static final Pattern PRODUCT_CODE_IFA_PPN = Pattern.compile("^\\d{12}$");

    /**
     * Regular expression for GS1 Character Set 82 fields between 1 and 20
     * characters in length. This is used for GS1 serial numbers and batch
     * identifiers. If GS1, the serial number must be between 1 and 20
     * characters in length and contain only characters specified by the GS1
     * character set 82.
     */
    private static final Pattern GS1_CHARACTER_SET_8201_20 = Pattern.compile("^[a-zA-Z0-9!\"%&'()*+,-./:;<=>?_]{1,20}$");

    /**
     * Regular expression for IFA serial numbers. If IFA, the serial number must
     * be between 1 and 20 characters in length and contain only upper case
     * letters and digits.
     */
    private static final Pattern SERIAL_NUMBER_IFA = Pattern.compile("^[A-Z0-9]{1,20}$");

    /**
     * Regular expression for IFA batch identifiers. If IFA, the batch
     * identifier must be between 1 and 20 characters in length and contain only
     * upper case letters, digits, hyphens and underscores.
     */
    private static final Pattern BATCH_ID_IFA = Pattern.compile("^[A-Z0-9-_]{1,20}$");

    /**
     * Regular expression for expiry dates (GS1 and IFA). Expiry dates must be
     * six digits in length and comply with YYMMDD format. The DD component may
     * be '00' to signify expiry months.
     */
    private static final Pattern EXPIRY_DATE = Pattern.compile("^((\\d{2}((0[13578]|1[02])(0[1-9]|[12]\\d|3[01])|(0[13456789]|1[012])(0[0-9]|[12]\\d|30)|02(0[0-9]|1\\d|2[0-8])))|([02468][048]|[13579][26])0229)$");

    /**
     * Performs the validations of a pack identifier.
     *
     * @param packIdentifier The extended pack identifier.
     * @param dataEntryMode The data entry mode.
     * @return A pack identifier validation response record.
     */
    public static LocalValidationResponse validate(PackIdentifier packIdentifier, DataEntryMode dataEntryMode) {
        /* EFPIA, EAEPC and MFE have published a joint recommendation for identifiers which
		 * recomends the use of GS1 and GTIN-14, or German IFA and PPNs.  The DR is aligned 
		 * with this, but these formats and standards are not formal requirements in law.
		 * However, the recommendations are enforced in the European Hub and therefore are
		 * effectively requirements for the entire EMVS.  The rules in this method take this
		 * into account.  Where appropriate, stakeholder rules are separated from the rules
		 * */

        LocalValidationResponse okayResponse = new LocalValidationResponse();

        // Rule 1: The product schema must be GS1 or IFA.
        if (packIdentifier.getProductCodeScheme().equalsIgnoreCase(ProductCodeScheme.UNKNOWN.toString())) {
            // Invalid
            LocalValidationResponse localValidationResponse = new LocalValidationResponse();
            localValidationResponse.setOperationCode(61020002);
            localValidationResponse.setWarning(Resources.getAPI_Validation_61020002());
            return localValidationResponse;
        }

        // Rule 2: The product code must not be null or empty.
        if (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(packIdentifier.getProductCode())) {
            // Invalid
            LocalValidationResponse localValidationResponse = new LocalValidationResponse();
            localValidationResponse.setOperationCode(61020001);
            localValidationResponse.setWarning(Resources.getAPI_Validation_61020001());
            return localValidationResponse;
        }

        // Rule 3: If GS1, the product code must be 14 characters (GTIN-14) in length  
        // and contain only digits.  If the length is less than 14 characters, the 
        // library will provide left padding of '0's as a courtesy. Any unnecessary 
        // whitespace will be trimmed.
        if (packIdentifier.getProductCodeScheme().equalsIgnoreCase(ProductCodeScheme.GS1.toString())) {
            packIdentifier.setProductCode(packIdentifier.getProductCode().trim());

            Matcher productCodeGs1Matcher = PRODUCT_CODE_GS1.matcher(packIdentifier.getProductCode());
            if (!productCodeGs1Matcher.matches()) {
                // Invalid
                LocalValidationResponse localValidationResponse = new LocalValidationResponse();
                localValidationResponse.setOperationCode(61020008);
                localValidationResponse.setWarning(Resources.getAPI_Validation_61020008());
                return localValidationResponse;
            }

            if (packIdentifier.getProductCode().length() < 14) {
                packIdentifier.setProductCode(String.format("%014d", Long.parseLong(packIdentifier.getProductCode())));
            }
        }

        // Rule 4: If GS1, the last digit of the product code is a checksum and must be set
        // correctly in accordance with GS1 rules.
        if (packIdentifier.getProductCodeScheme().equalsIgnoreCase(ProductCodeScheme.GS1.toString())) {
            Integer[] digitList = (Arrays.stream(packIdentifier.getProductCode().split("\\B"))
                    .map(s -> Integer.valueOf(s))
                    .collect(Collectors.toList())).toArray(new Integer[0]);

            if (IntStream.range(0, digitList.length)
                    .map(i -> digitList[i] * (i % 2 == 0 ? 3 : 1))
                    .sum() % 10 != 0) {
                // Invalid
                LocalValidationResponse localValidationResponse = new LocalValidationResponse();
                localValidationResponse.setOperationCode(61020008);
                localValidationResponse.setWarning(Resources.getAPI_Validation_61020008());
                return localValidationResponse;
            }
        }

        // Rule 5: If IFA, the product code must be between 7 and 22 characters in length and 
        // contain alphanumeric content in accordance with ANSI MH10.8.2.  Only capital
        // letters and digits are allowed.  If lowercase characters have been provided, these 
        // will be capitalised as a courtesy.  Any unnecessary whitespace will be trimmed.
        if (packIdentifier.getProductCodeScheme().equalsIgnoreCase(ProductCodeScheme.IFA.toString())) {
            packIdentifier.setProductCode(packIdentifier.getProductCode().trim().toUpperCase());

            /* NB. This code is based on the format specifiers provided by the ANSI MH10.8.2
			 * standard.  The standard is ambiguous regarding the first two characters of the 
			 * product code which it states are digits.  However this conflicts with the 
			 * format specifier.  Germany uses 12-digit PPNs.  However, this country-specific
			 * format is not assumed here.
			 * */
            Matcher productCodeIfaMatcher = PRODUCT_CODE_IFA.matcher(packIdentifier.getProductCode());
            if (!productCodeIfaMatcher.matches()) {
                // Invalid
                LocalValidationResponse localValidationResponse = new LocalValidationResponse();
                localValidationResponse.setOperationCode(61020008);
                localValidationResponse.setWarning(Resources.getAPI_Validation_61020008());
                return localValidationResponse;
            }
        }

        // Rule 6: If IFA, the European Hub only allows German PPNs.  These are 12 digits in length.  
        // This rule is broken out into a separate rule to rule 5 in case this changes in the future.
        if (packIdentifier.getProductCodeScheme().equalsIgnoreCase(ProductCodeScheme.IFA.toString())) {
            Matcher productCodeIfaPpnMatcher = PRODUCT_CODE_IFA_PPN.matcher(packIdentifier.getProductCode());
            if (!productCodeIfaPpnMatcher.matches()) {
                // Invalid
                LocalValidationResponse localValidationResponse = new LocalValidationResponse();
                localValidationResponse.setOperationCode(61020008);
                localValidationResponse.setWarning(Resources.getAPI_Validation_61020008());
                return localValidationResponse;
            }
        }

        // Rule 7: The serial number must not be null or empty
        if (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(packIdentifier.getSerialNumber())) {
            // Invalid
            LocalValidationResponse localValidationResponse = new LocalValidationResponse();
            localValidationResponse.setOperationCode(61020003);
            localValidationResponse.setWarning(Resources.getAPI_Validation_61020003());
            return localValidationResponse;
        }

        // Rule 8: If GS1, the serial number must be between 1 and 20 characters in length.
        // The alphanumeric characters are in accordance with character set 82 (see the 
        // GS1 specification). Any unneccessary whitespace will be trimmed.
        if (packIdentifier.getProductCodeScheme().equalsIgnoreCase(ProductCodeScheme.GS1.toString())) {
            packIdentifier.setSerialNumber(packIdentifier.getSerialNumber().trim());

            Matcher gs1CharacterSet820120Matcher = GS1_CHARACTER_SET_8201_20.matcher(packIdentifier.getSerialNumber());
            if (!gs1CharacterSet820120Matcher.matches()) {
                // Invalid
                LocalValidationResponse localValidationResponse = new LocalValidationResponse();
                localValidationResponse.setOperationCode(61020011);
                localValidationResponse.setWarning(Resources.getAPI_Validation_61020011());
                return localValidationResponse;
            }
        }

        // Rule 9: If IFA, the serial number must be between 1 and 20 characters in length and 
        // contain alphanumeric content in accordance with ANSI MH10.8.2.  Only capital
        // letters and digits are allowed.  If lowercase characters have been provided, these 
        // will be capitalised as a courtesy.  Any unneccessary whitespace will be trimmed.
        if (packIdentifier.getProductCodeScheme().equalsIgnoreCase(ProductCodeScheme.IFA.toString())) {
            packIdentifier.setSerialNumber(packIdentifier.getSerialNumber().trim().toUpperCase());

            Matcher serialNumberIfaMatcher = SERIAL_NUMBER_IFA.matcher(packIdentifier.getSerialNumber());
            if (!serialNumberIfaMatcher.matches()) {
                // Invalid
                LocalValidationResponse localValidationResponse = new LocalValidationResponse();
                localValidationResponse.setOperationCode(61020011);
                localValidationResponse.setWarning(Resources.getAPI_Validation_61020011());
                return localValidationResponse;
            }
        }

        // Rule 10: For non-manual data entry, the batch identifier must not be null or empty
        if (dataEntryMode == DataEntryMode.NON_MANUAL && com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(packIdentifier.getBatchId())) {
            // Invalid
            LocalValidationResponse localValidationResponse = new LocalValidationResponse();
            localValidationResponse.setOperationCode(61020000);
            localValidationResponse.setWarning(Resources.getAPI_Validation_61020000());
            return localValidationResponse;
        }

        // Rule 11: For manual data entry , the batch identifier is optional, but if provided
        // must not be composed of whitespace
        if (dataEntryMode == DataEntryMode.MANUAL) {
            if (!com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrEmpty(packIdentifier.getBatchId())) {
                if (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(packIdentifier.getBatchId())) {
                    LocalValidationResponse localValidationResponse = new LocalValidationResponse();
                    localValidationResponse.setOperationCode(61020006);
                    localValidationResponse.setWarning(Resources.getAPI_Validation_61020006());
                    return localValidationResponse;
                }
            }
        }

        // Rule 12: If GS1, the batch identifier must be between 1 and 20 characters in length.
        // The alphanumeric characters are in accordance with character set 82 (see the 
        // GS1 specification). Any unneccessary whitespace will be trimmed.
        if (packIdentifier.getProductCodeScheme().equalsIgnoreCase(ProductCodeScheme.GS1.toString())) {
            packIdentifier.setBatchId(packIdentifier.getBatchId() == null ? null : packIdentifier.getBatchId().trim());

            Matcher gs1CharacterSet820120Matcher = GS1_CHARACTER_SET_8201_20.matcher(packIdentifier.getBatchId());
            if (!com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(packIdentifier.getBatchId()) && !gs1CharacterSet820120Matcher.matches()) {
                // Invalid
                LocalValidationResponse localValidationResponse = new LocalValidationResponse();
                localValidationResponse.setOperationCode(61020006);
                localValidationResponse.setWarning(Resources.getAPI_Validation_61020006());
                return localValidationResponse;
            }
        }

        // Rule 13: If IFA, the batch identifier must be between 1 and 20 characters in length and 
        // contain alphanumeric content in accordance with ANSI MH10.8.2.  Only capital
        // letters and digits are allowed, together with hyphens and underscores.  If lowercase 
        // characters have been provided, these will be capitalised as a courtesy.  Any 
        // unneccessary whitespace will be trimmed.
        if (packIdentifier.getProductCodeScheme().equalsIgnoreCase(ProductCodeScheme.IFA.toString())) {
            packIdentifier.setBatchId(packIdentifier.getBatchId() == null ? null : packIdentifier.getBatchId().trim().toUpperCase());

            Matcher batchIdIfaMatcher = BATCH_ID_IFA.matcher(packIdentifier.getBatchId());
            if (!com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(packIdentifier.getBatchId()) && !batchIdIfaMatcher.matches()) {
                // Invalid
                LocalValidationResponse localValidationResponse = new LocalValidationResponse();
                localValidationResponse.setOperationCode(61020006);
                localValidationResponse.setWarning(Resources.getAPI_Validation_61020006());
                return localValidationResponse;
            }
        }

        // Rule 14: For non-manual data entry, the expiry date must not be null or empty
        if (dataEntryMode == DataEntryMode.NON_MANUAL && com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(packIdentifier.getExpiryDate())) {
            // Invalid
            LocalValidationResponse localValidationResponse = new LocalValidationResponse();
            localValidationResponse.setOperationCode(61020004);
            localValidationResponse.setWarning(Resources.getAPI_Validation_61020004());
            return localValidationResponse;
        }

        // Rule 15: For manual data entry , the expiry date is optional, but if provided
        // must not be composed of whitespace
        if (dataEntryMode == DataEntryMode.MANUAL) {
            if (!com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrEmpty(packIdentifier.getExpiryDate())) {
                if (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(packIdentifier.getExpiryDate())) {
                    LocalValidationResponse localValidationResponse = new LocalValidationResponse();
                    localValidationResponse.setOperationCode(61020007);
                    localValidationResponse.setWarning(Resources.getAPI_Validation_61020007());
                    return localValidationResponse;
                }
            }
        }

        // Rule 16: If GS1, the expiry date must be 6 digits in length and comply with
        // YYMMDD format.  The DD component can be zeros for expiry dates that represent
        // months. Any unneccessary whitespace will be trimmed.
        if (packIdentifier.getProductCodeScheme().equalsIgnoreCase(ProductCodeScheme.GS1.toString())) {
            packIdentifier.setExpiryDate(packIdentifier.getExpiryDate() == null ? null : packIdentifier.getExpiryDate().trim());

            Matcher expiryDateMatcher = EXPIRY_DATE.matcher(packIdentifier.getExpiryDate());
            if (!com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(packIdentifier.getExpiryDate()) && !expiryDateMatcher.matches()) {
                // Invalid
                LocalValidationResponse localValidationResponse = new LocalValidationResponse();
                localValidationResponse.setOperationCode(61020007);
                localValidationResponse.setWarning(Resources.getAPI_Validation_61020007());
                return localValidationResponse;
            }
        }

        // Rule 16: If IFA, the expiry date must be 6 digits in length and comply with
        // YYMMDD format.  The DD component can be zeros for expiry dates that represent
        // months. Any unneccessary whitespace will be trimmed.
        if (packIdentifier.getProductCodeScheme().equalsIgnoreCase(ProductCodeScheme.IFA.toString())) {
            packIdentifier.setExpiryDate(packIdentifier.getExpiryDate() == null ? null : packIdentifier.getExpiryDate().trim().toUpperCase());

            Matcher expiryDateMatcher = EXPIRY_DATE.matcher(packIdentifier.getExpiryDate());
            if (!com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(packIdentifier.getExpiryDate()) && !expiryDateMatcher.matches()) {
                // Invalid
                LocalValidationResponse localValidationResponse = new LocalValidationResponse();
                localValidationResponse.setOperationCode(61020007);
                localValidationResponse.setWarning(Resources.getAPI_Validation_61020007());
                return localValidationResponse;
            }
        }

        return okayResponse;
    }
}
