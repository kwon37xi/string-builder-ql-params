package kr.pe.kwonnam.dynamicql.stringbuilderqlparams;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * DynamicQlParams tracks query parameters and help to bulid IN clause positional parameter string.
 * <br>
 * JPQL/HQL support positional parameters with index format(<code>?1, ?2, ....</code>)
 */
public class DynamicQlParams {
    private boolean withPositionalIndex = false;
    private List<Object> parameters = new ArrayList<Object>();

    public DynamicQlParams() {
        this(false);
    }

    public DynamicQlParams(boolean withPositionalIndex) {
        this.withPositionalIndex = withPositionalIndex;
    }

    /**
     * add query parameter and return positional parameter string.
     *
     * @param param query parameter
     * @return if withPosionalIndex is true, return <code>?[positionIndexNum]</code> or just <code>?</code>
     */
    public String param(Object param) {
        parameters.add(param);
        return withPositionalIndex ? "?" + parameters.size() : "?";
    }

    /**
     * add IN operator query parameters and return positional parameter string.
     *
     * @param params query parameters array for IN operator
     * @return positional parameters seperated by commoa(,). If withPosionalIndex is true, return like <code>?1, ?2, ?3, ...</code> or just <code>?, ?, ?, ...</code>
     */
    public String inParams(Object[] params) {
        if (params == null) {
            throw new IllegalArgumentException("params must not be null or empty.");
        }
        return inParams(Arrays.asList(params));
    }

    /**
     * add IN operator query parameters and return positional parameter string.
     *
     * @param params query parameter Iterator(Collection) for IN operator
     * @return positional parameters seperated by commoa(,). If withPosionalIndex is true, return like <code>?1, ?2, ?3, ...</code> or just <code>?, ?, ?, ...</code>
     */
    public String inParams(Iterable<?> params) {
        if (params == null || !params.iterator().hasNext()) {
            throw new IllegalArgumentException("params must not be null or empty.");
        }

        StringBuilder positionalParameterBuilder = new StringBuilder();
        final Iterator<?> iterator = params.iterator();
        while (iterator.hasNext()) {
            final String positionalParameter = param(iterator.next());
            positionalParameterBuilder.append(positionalParameter);

            if (iterator.hasNext()) {
                positionalParameterBuilder.append(", ");
            }
        }
        return positionalParameterBuilder.toString();
    }

    /**
     * get query parameters as a list.
     *
     * @return query parameter List
     */
    public List<Object> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    /**
     * get query parameters as an array
     *
     * @return query parameter array
     */
    public Object[] getParameterArray() {
        return parameters.toArray(new Object[parameters.size()]);
    }

    /**
     * bind query parameters to PreparedStatement
     *
     * @param preparedStatement prepared statement
     * @throws SQLException sql exception
     */
    public void bindParameters(PreparedStatement preparedStatement) throws SQLException {
        if (preparedStatement == null) {
            throw new IllegalArgumentException("preparedStatement must not be null.");
        }

        for (int i = 0; i < parameters.size(); i++) {
            preparedStatement.setObject(i + 1, parameters.get(i));
        }
    }

    /**
     * create an instance with withPositionalIndex = true.
     *
     * @return DynamicQlParams withPositionalIndex = true
     */
    public static DynamicQlParams withPositionalIndex() {
        return new DynamicQlParams(true);
    }
}
