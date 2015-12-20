# String Builder QL Params

Java String Builder QL Params helps building SQL/JPQL/HQL with [java.lang.StringBuilder](http://docs.oracle.com/javase/7/docs/api/java/lang/StringBuilder.html).

## DynamicQlParams
Use [DynamicQlParams](https://github.com/kwon37xi/string-builder-ql-params/blob/master/src/main/java/kr/pe/kwonnam/dynamicql/stringbuilderqlparams/DynamicQlParams.java) for building positional parameter based SQL/JPQL/HQL.
If you don't want to depend on this project, just copy&paste the source to your project.

### Creating instances

```java
StringBuilder builder = new StringBuilder();

// instantiating for normal positional parameters(like '?')
DynamicQlParams dqp = new DynamicQlParams();

// instanticating for JQPL/HQL's indexed positional parameters(like '?1', '?2', ...)
DynamicQlParams dqp = new DynamicQlParams(true);
// or
DynamicQlParams dqp = DynamicQlParams.withPositionalIndex();
```

### API
* `DynamicQlParams.param(Object param)` : Add a parameter value.
* `DynamicQlParams.inParams(Object[] params or Iterable<?> params)` : Add parameter values for IN condition.
* `DynamicQlParams.getParameters()` : get the final parameter values as `java.util.List`
* `DynamicQlParams.getParameterArray()` : get the final parameter values as an array.
* `DynamicQlParams.bindParameters(PreparedStatement preparedStatement)` : bind parameters to a preparedStatement object.

### Example Usage
You can get the fowlloing source from [DynamicQlParamsQueryBuildTest.java](https://github.com/kwon37xi/string-builder-ql-params/blob/master/src/test/java/kr/pe/kwonnam/dynamicql/stringbuilderqlparams/DynamicQlParamsQueryBuildTest.java).

```java
StringBuilder builder = new StringBuilder();
DynamicQlParams dqp = new DynamicQlParams();

builder
    .append("SELECT ")
    .append(StringUtils.join(User.COLUMNS, ", ")).append("\n")
    .append("FROM users as u\n")
    .append("WHERE 1 = 1\n");

if (user.getUserId() != null) {
    builder.append("AND user_id = ").append(dqp.param(user.getUserId())).append("\n");
}
if (StringUtils.isNotEmpty(user.getName())) {
    builder.append("AND name = ").append(dqp.param(user.getName())).append("\n");
}
if (user.getBirthday() != null) {
    builder.append("AND birthday = ").append(dqp.param(user.getBirthday())).append("\n");
}
if (CollectionUtils.isNotEmpty(zipCodes)) {
    builder.append("AND zip_code in (").append(dqp.inParams(zipCodes)).append(")\n");
}
builder.append("LIMIT ").append(dqp.param(10));

log.info("StringBuilder with DynamicQlParams : {}", builder.toString());
log.info("Query Parameters : {}", dqp.getParameters());

PreparedStatement pstmt = connection.prepareStatement(builder.toString());
dqp.bindParameters(preparedStatement); // or you can use this parameters with Spring JdbcTemplate or Hibernate session or JPA entityManager, etc..
// execute query...
```

You will get the following query string

```sql
SELECT user_id, name, email, birthday, mobile_phone, home_phone, address, zip_code
FROM users as u
WHERE 1 = 1
AND user_id = ?
AND name = ?
AND birthday = ?
AND zip_code in (?, ?, ?)
LIMIT ?
```

and parameters

```
[2015, DynamicQlParams, Thu Dec 17 00:00:00 KST 2015, 12345, 56789, 58391, 10]
```

## DynamicNamedQlParams
Use [DynamicNamedQlParams](https://github.com/kwon37xi/string-builder-ql-params/blob/master/src/main/java/kr/pe/kwonnam/dynamicql/stringbuilderqlparams/DynamicNamedQlParams.java) for building named parameter based SQL/JPQL/HQL.
If you don't want to depend on this project, just copy&paste the source to your project.

JPQL, HQL and Spring's [NamedParameterJdbcTemplate](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/jdbc/core/namedparam/NamedParameterJdbcTemplate.html) supports named parameters.

