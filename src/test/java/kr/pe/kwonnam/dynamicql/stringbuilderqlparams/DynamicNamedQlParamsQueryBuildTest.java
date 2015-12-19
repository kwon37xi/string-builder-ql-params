package kr.pe.kwonnam.dynamicql.stringbuilderqlparams;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class DynamicNamedQlParamsQueryBuildTest extends AbstractQueryBuildTest {
    private Logger log = getLogger(DynamicNamedQlParamsQueryBuildTest.class);

    private StringBuilder builder;
    private DynamicNamedQlParams dnqp;
    @Test
    public void build_sql() throws Exception {
        builder = new StringBuilder();
        dnqp = new DynamicNamedQlParams();

        builder
            .append("SELECT ")
            .append(StringUtils.join(User.COLUMNS, ", ")).append("\n")
            .append("FROM users as u\n")
            .append("WHERE 1 = 1\n");

        if (user.getUserId() != null) {
            builder.append("AND user_id = ").append(dnqp.param("userId", user.getUserId())).append("\n");
        }
        if (StringUtils.isNotEmpty(user.getName())) {
            builder.append("AND name = ").append(dnqp.param("name", user.getName())).append("\n");
        }
        if (user.getBirthday() != null) {
            builder.append("AND birthday = ").append(dnqp.param("birthday", user.getBirthday())).append("\n");
        }
        if (CollectionUtils.isNotEmpty(zipCodes)) {
            builder.append("AND zip_code in (").append(dnqp.inParams("zipCode", zipCodes)).append(")\n");
        }
        builder.append("LIMIT ").append(dnqp.param("limit", 10));

        log.info("StringBuilder with DynamicNameQlParams : {}", builder.toString());
        log.info("Query Parameters : {}", dnqp.getParameters());
    }
}
