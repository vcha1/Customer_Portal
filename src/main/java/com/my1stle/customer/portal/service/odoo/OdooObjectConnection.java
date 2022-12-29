package com.my1stle.customer.portal.service.odoo;


import java.util.List;
import java.util.Map;

public interface OdooObjectConnection {
    /**
     * Create new object records
     * @param objectType The API name of the object
     * @param objects The objects to create
     * @return The list of ids for the objects that were created, in the same order as the input
     */
    List<Integer> createObjects(String objectType, List<Map<String, Object>> objects);

    /**
     * Find objects based on criteria
     * @param objectType The API name of the object
     * @param fields The list of fields that should be in the returned plain object (will always contain 'id')
     * @param criteria The list of criteria that the objects must meet. Operators are in Polish notation and are binary,
     *                 meaning that operators are represented as strings that prefix their two operands. Conditions are
     *                 {@code List}s of three things, the field name as a string, the operator as a string, then the
     *                 operand (as any object). For example, the sentence "The field 'name' must contain 'spam' or the
     *                 field 'amount' must be greater than 10" would be represented as:
     *                 <pre>
     *                 Arrays.asList(
     *                     "|"
     *                     Arrays.asList("name", "like", "spam"),
     *                     Arrays.asList("amount", ">", 10)
     *                 )
     *                 </pre>
     * @return A list of plain objects that match the provided criteria
     */
    List<Map<String, ?>> findObjects(String objectType, List<String> fields, List<Object> criteria);

    /**
     * Update an object with the given field values
     * @param objectType The API name of the object
     * @param id The id of the object to update
     * @param values the fields mapped to the values to update
     */
    void updateObject(String objectType, Integer id, Map<String, Object> values);

    /**
     * Update multiple objects with the given field values
     * @param objectType The API name of the object
     * @param ids the ids of the objects to update with the single set of provided values
     * @param values the fields mapped to the values that all of the specified objects will be updated to have
     */
    void updateObjects(String objectType, List<Integer> ids, Map<String, Object> values);
}
