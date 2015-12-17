package kr.pe.kwonnam.dynamicql.stringbuilderqlparams;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DynamicQlParamsTest {
    private Logger log = LoggerFactory.getLogger(DynamicQlParamsTest.class);

    private DynamicQlParams dynamicQlParams;
    private DynamicQlParams dynamicQlParamsWithPositionalIndex;
    private Date now;

    @Mock
    private PreparedStatement preparedStatement;

    @Before
    public void setUp() throws Exception {
        dynamicQlParams = new DynamicQlParams();
        dynamicQlParamsWithPositionalIndex = DynamicQlParams.withPositionalIndex();
        now = new Date();
    }

    @Test
    public void param() throws Exception {
        assertThat(dynamicQlParams.param("Hello"), is("?"));
        assertThat(dynamicQlParams.param("World"), is("?"));
        assertThat(dynamicQlParams.param(null), is("?"));
        assertThat(dynamicQlParams.getParameters(), hasSize(3));
        assertThat(dynamicQlParams.getParameters(), hasItems((Object) "Hello", "World", null));
        assertThat(dynamicQlParams.getParameterArray(), arrayWithSize(3));
        assertThat(dynamicQlParams.getParameterArray(), is(array(equalTo((Object) "Hello"), equalTo((Object) "World"), nullValue())));
    }

    @Test
    public void param_withPositionalIndex() throws Exception {
        assertThat(dynamicQlParamsWithPositionalIndex.param("Hello"), is("?1"));
        assertThat(dynamicQlParamsWithPositionalIndex.param("World"), is("?2"));
        assertThat(dynamicQlParamsWithPositionalIndex.param(null), is("?3"));
        assertThat(dynamicQlParamsWithPositionalIndex.getParameters(), hasSize(3));
        assertThat(dynamicQlParamsWithPositionalIndex.getParameters(), hasItems((Object) "Hello", "World", null));
        assertThat(dynamicQlParamsWithPositionalIndex.getParameterArray(), arrayWithSize(3));
        assertThat(dynamicQlParamsWithPositionalIndex.getParameterArray(), is(array(equalTo((Object) "Hello"), equalTo((Object) "World"), nullValue())));
    }

    @Test
    public void inParams() throws Exception {
        assertThat(dynamicQlParams.param(123), is("?"));
        assertThat(dynamicQlParams.inParams(Arrays.asList((Object) "Hello", "World", "Good Bye~")), is("?, ?, ?"));
        assertThat(dynamicQlParams.param(now), is("?"));
        assertThat(dynamicQlParams.inParams(new Object[]{1, 2, 3}), is("?, ?, ?"));

        assertThat(dynamicQlParams.getParameters(), hasSize(8));
        assertThat(dynamicQlParams.getParameters(), hasItems((Object) 123, "Hello", "World", "Good Bye~", now, 1, 2, 3));
    }

    @Test
    public void inParams_withPositionalIndex() throws Exception {
        assertThat(dynamicQlParamsWithPositionalIndex.param(123), is("?1"));
        assertThat(dynamicQlParamsWithPositionalIndex.inParams(Arrays.asList((Object) "Hello", "World", "Good Bye~")), is("?2, ?3, ?4"));
        assertThat(dynamicQlParamsWithPositionalIndex.param(now), is("?5"));
        assertThat(dynamicQlParamsWithPositionalIndex.inParams(new Object[]{1, 2, 3}), is("?6, ?7, ?8"));

        assertThat(dynamicQlParamsWithPositionalIndex.getParameters(), hasSize(8));
        assertThat(dynamicQlParamsWithPositionalIndex.getParameters(), hasItems((Object) 123, "Hello", "World", "Good Bye~", now, 1, 2, 3));
    }

    @Test
    public void inParams_null() throws Exception {
        try {
            dynamicQlParams.inParams((List<String>) null);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                ex.getMessage(), is("params must not be null or empty."));
        }
    }

    @Test
    public void inParams_empty() throws Exception {
        try {
            dynamicQlParams.inParams(new ArrayList<Object>());
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                ex.getMessage(), is("params must not be null or empty."));
        }
    }

    @Test
    public void inParams_array_null() throws Exception {
        try {
            dynamicQlParams.inParams((Object[]) null);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                ex.getMessage(), is("params must not be null or empty."));
        }
    }

    @Test
    public void bindParameters_prapredStatement_null() throws Exception {
        try {
            dynamicQlParams.bindParameters(null);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                ex.getMessage(), CoreMatchers.is("preparedStatement must not be null."));
        }
    }

    @Test
    public void bindParameters() throws Exception {
        dynamicQlParams.param("Hello");
        dynamicQlParams.inParams(Arrays.asList(1, 2, 3));
        dynamicQlParams.param("World");
        dynamicQlParams.param(now);

        dynamicQlParams.bindParameters(preparedStatement);

        verify(preparedStatement).setObject(1, "Hello");
        verify(preparedStatement).setObject(2, 1);
        verify(preparedStatement).setObject(3, 2);
        verify(preparedStatement).setObject(4, 3);
        verify(preparedStatement).setObject(5, "World");
        verify(preparedStatement).setObject(6, now);
    }
}
