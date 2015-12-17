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

    public String param(String paramName, Object paramValue) {
        parameters.put(paramName, paramValue);
        return ":" + paramName;
    }

    public String inParams(String paramNamePrefix, Object[] paramValues) {
        return inParams(paramNamePrefix, Arrays.asList(paramValues));
    }

    public String inParams(String paramNamePrefix, Iterable<?> paramValues) {
        StringBuilder namedParameterBuilder = new StringBuilder();
        int currentIndex = 0;
        final Iterator<?> iterator = paramValues.iterator();

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

    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }
}
