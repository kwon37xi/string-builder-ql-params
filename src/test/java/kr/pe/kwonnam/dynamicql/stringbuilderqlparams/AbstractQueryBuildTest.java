package kr.pe.kwonnam.dynamicql.stringbuilderqlparams;

import org.junit.Before;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractQueryBuildTest {
    protected User user;
    protected List<String> zipCodes;

    @Before
    public void setUpUserAndZipCodes() throws Exception {
        user = new User();

        user.setUserId(2015L);
        user.setName("DynamicQlParams");
        user.setBirthday(new SimpleDateFormat("yyyy/MM/dd").parse("2015/12/17"));
        user.setEmail("someone@email.com");

        zipCodes = Arrays.asList("12345", "56789", "58391");
    }
}
