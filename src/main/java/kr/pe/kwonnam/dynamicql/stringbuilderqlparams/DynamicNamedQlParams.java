package kr.pe.kwonnam.dynamicql.stringbuilderqlparams;

import java.util.*;

/**
 * DynamicNamedQlParams tracks query parameters and help to bulid IN clause positional parameter string for named parameters(<code>:paramname</code> style).
 * <br>
 * Spring <a href="http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/jdbc/core/namedparam/NamedParameterJdbcTemplate.html">NamedParameterJdbcTemplate</a> and
 * JPQL, HQL support named parameters.
 */
public class DynamicNamedQlParams {
    private Map<String, Object> parameters = new HashMap<String, Object>();

    /**
     * add named query parameter and return a named parameter string.
     * @param paramName a parameter name.
     * @param param parameter value
     * @return a named parameter string
     */
    public String param(String paramName, Object param) {
        if (paramName == null || paramName.isEmpty()) {
            throw new IllegalArgumentException("paramName must not be null or empty.");
        }
        parameters.put(paramName, param);
        return ":" + paramName;
    }

    /**
     * add IN operator a named query parameter array and return named parameters' string.
     *
     * @param paramNamePrefix a parameter name prefix.
     * @param params IN operator parameter values
     * @return if you give paramNamePrefix <code>"name"</code>, it will return <code>":name0, name1, name2, ..."</code>
     */
    public String inParams(String paramNamePrefix, Object[] params) {
        return inParams(paramNamePrefix, Arrays.asList(params));
    }

    /**
     * add IN operator a named query parameter list and return named parameters' string.
     *
     * @param paramNamePrefix a parameter name prefix.
     * @param params IN operator parameter values
     * @return if you give paramNamePrefix <code>"name"</code>, it will return <code>":name0, name1, name2, ..."</code>
     */
    public String inParams(String paramNamePrefix, Iterable<?> params) {
        if (paramNamePrefix == null || paramNamePrefix.isEmpty()) {
            throw new IllegalArgumentException("paramNamePrefix must not be null or empty.");
        }

        if (params == null || !params.iterator().hasNext()) {
            throw new IllegalArgumentException("params must not be null or empty.");
        }

        StringBuilder namedParameterBuilder = new StringBuilder();
        int currentIndex = 0;
        final Iterator<?> iterator = params.iterator();

        while (iterator.hasNext()) {
            final String namedParameter = param(paramNamePrefix + currentIndex, iterator.next());
            namedParameterBuilder.append(namedParameter);

            if (iterator.hasNext()) {
                namedParameterBuilder.append(", ");
                currentIndex++;
            }
        }
        return namedParameterBuilder.toString();
    }

    /**
     * get query parameters as a map
     *
     * @return query parameter Map
     */
    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }
}
