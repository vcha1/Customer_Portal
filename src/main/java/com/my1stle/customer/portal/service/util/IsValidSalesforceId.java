package com.my1stle.customer.portal.service.util;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Implementation is based on
 *
 * @see <a href="https://stackoverflow.com/questions/9742913/validating-a-salesforce-id">Validating a Salesforce Id</a>
 */
public class IsValidSalesforceId implements Predicate<String> {

    private static IsValidSalesforceId INSTANCE = null;

    private static final String regex = "[a-zA-Z0-9]{15}|[a-zA-Z0-9]{18}";
    private static final Pattern pattern = Pattern.compile(regex);

    private IsValidSalesforceId() {

    }

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param s the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     * @implNote tests whether the given string is a valid salesforce opportunity id
     */
    @Override
    public boolean test(String s) {

        if (StringUtils.isBlank(s)) {
            return false;
        }

        return pattern.matcher(s).matches();
    }

    public static IsValidSalesforceId getInstance() {

        if (null == INSTANCE) {
            INSTANCE = new IsValidSalesforceId();
        }

        return INSTANCE;

    }

}
