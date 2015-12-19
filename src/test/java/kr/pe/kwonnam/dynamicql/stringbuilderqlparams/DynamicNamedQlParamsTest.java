package kr.pe.kwonnam.dynamicql.stringbuilderqlparams;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class DynamicNamedQlParamsTest {
    private Logger log = LoggerFactory.getLogger(DynamicNamedQlParamsTest.class);

    private DynamicNamedQlParams dynamicNamedQlParams;
    private Date now;

    @Before
    public void setUp() throws Exception {
        dynamicNamedQlParams = new DynamicNamedQlParams();
        now = new Date();
    }

    @Test
    public void param() throws Exception {
        assertThat(dynamicNamedQlParams.param("username", "DynamicNamedQlParams"), is(":username"));
        assertThat(dynamicNamedQlParams.param("greeting", "Hello World"), is(":greeting"));
        assertThat(dynamicNamedQlParams.param("type", null), is(":type"));
        assertThat(dynamicNamedQlParams.getParameters().entrySet(), hasSize(3));
        assertThat(dynamicNamedQlParams.getParameters(), hasEntry("username", (Object) "DynamicNamedQlParams"));
        assertThat(dynamicNamedQlParams.getParameters(), hasEntry("greeting", (Object) "Hello World"));
        assertThat(dynamicNamedQlParams.getParameters(), hasEntry("type", null));
    }

    @Test
    public void params_paramName_null() throws Exception {
        try {
            dynamicNamedQlParams.param(null, "Hello");
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                ex.getMessage(), is("paramName must not be null or empty."));
        }
    }

    @Test
    public void params_paramName_empty() throws Exception {
        try {
            dynamicNamedQlParams.param("", "Hello");
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                ex.getMessage(), is("paramName must not be null or empty."));
        }
    }

    @Test
    public void inParams() throws Exception {
        assertThat(dynamicNamedQlParams.param("price", 123), is(":price"));
        assertThat(dynamicNamedQlParams.inParams("type", new Object[]{"CAR", "MOTORCYCLE", "BICYCLE"}), is(":type0, :type1, :type2"));
        assertThat(dynamicNamedQlParams.param("createdDateTime", now), is(":createdDateTime"));
        assertThat(dynamicNamedQlParams.inParams("greetings", Arrays.asList("Good Morning", "Good Afternoon")), is(":greetings0, :greetings1"));

        assertThat(dynamicNamedQlParams.getParameters().entrySet(), hasSize(7));
        assertThat(dynamicNamedQlParams.getParameters(), hasEntry("price", (Object) 123));
        assertThat(dynamicNamedQlParams.getParameters(), hasEntry("type0", (Object) "CAR"));
        assertThat(dynamicNamedQlParams.getParameters(), hasEntry("type1", (Object) "MOTORCYCLE"));
        assertThat(dynamicNamedQlParams.getParameters(), hasEntry("type2", (Object) "BICYCLE"));
        assertThat(dynamicNamedQlParams.getParameters(), hasEntry("createdDateTime", (Object) now));
        assertThat(dynamicNamedQlParams.getParameters(), hasEntry("greetings0", (Object) "Good Morning"));
        assertThat(dynamicNamedQlParams.getParameters(), hasEntry("greetings1", (Object) "Good Afternoon"));
    }

    @Test
    public void inParams_null() throws Exception {
        try {
            dynamicNamedQlParams.inParams("prefix", (Iterable<Object>) null);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                ex.getMessage(), is("params must not be null or empty."));
        }
    }

    @Test
    public void inParams_empty() throws Exception {
        try {
            dynamicNamedQlParams.inParams("prefix", new ArrayList<Object>());
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                ex.getMessage(), is("params must not be null or empty."));
        }
    }

    @Test
    public void inParams_paramPrefix_null() throws Exception {
        try {
            dynamicNamedQlParams.inParams(null, new Object[]{1, 2, 3});
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                ex.getMessage(), CoreMatchers.is("paramNamePrefix must not be null or empty."));
        }
    }

    @Test
    public void inParams_paramPrefix_empty() throws Exception {
        try {
            dynamicNamedQlParams.inParams("", new Object[]{1, 2, 3});
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                ex.getMessage(), CoreMatchers.is("paramNamePrefix must not be null or empty."));
        }
    }
}
