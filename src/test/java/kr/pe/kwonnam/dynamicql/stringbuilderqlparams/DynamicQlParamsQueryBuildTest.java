package kr.pe.kwonnam.dynamicql.stringbuilderqlparams;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

public class DynamicQlParamsQueryBuildTest extends AbstractQueryBuildTest {
    private Logger log = getLogger(DynamicQlParamsQueryBuildTest.class);

    private StringBuilder builder;
    private DynamicQlParams dqp;

    @Test
    public void build_sql() throws Exception {
        builder = new StringBuilder();
        dqp = new DynamicQlParams();

        builder
            .append("SELECT ")
            .append(StringUtils.join(User.COLUMNS, ", ")).append("\n")
            .append("FROM users as u\n")
            .append("WHERE 1 = 1\n");

        if (user.getUserId() != null) {
            builder.append(format("AND user_id = %s %n", dqp.param(user.getUserId())));
        }
        if (StringUtils.isNotEmpty(user.getName())) {
            builder.append(format("AND name = %s %n", dqp.param(user.getName())));
        }
        if (user.getBirthday() != null) {
            builder.append(format("AND birthday = %s %n", dqp.param(user.getBirthday())));
        }
        if (CollectionUtils.isNotEmpty(zipCodes)) {
            builder.append(format("AND zip_code in (%s) %n",dqp.inParams(zipCodes)));
        }

//        if (user.getUserId() != null) {
//            builder.append("AND user_id = ").append(dqp.param(user.getUserId())).append("\n");
//        }
//        if (StringUtils.isNotEmpty(user.getName())) {
//            builder.append("AND name = ").append(dqp.param(user.getName())).append("\n");
//        }
//        if (user.getBirthday() != null) {
//            builder.append("AND birthday = ").append(dqp.param(user.getBirthday())).append("\n");
//        }
//        if (CollectionUtils.isNotEmpty(zipCodes)) {
//            builder.append("AND zip_code in (").append(dqp.inParams(zipCodes)).append(")\n");
//        }
        builder.append("LIMIT ").append(dqp.param(10));

        log.info("StringBuilder with DynamicQlParams : {}", builder.toString());
        log.info("Query Parameters : {}", dqp.getParameters());
    }
}
