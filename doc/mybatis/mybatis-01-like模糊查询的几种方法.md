

在Dao层指定各个参数的别名

mapper
```java
List<RoleEntity> selectBykeyWord(@Param("id") String id,@Param("roleName") String roleName,@Param("roleType") String roleType);
```
 
dao
```sql
SELECT
*
FROM
t_role
WHERE
role_name LIKE #{roleName}
OR id LIKE #{id}
OR role_type LIKE #{roleType}
</select>
```
xml
```xml
<select id="selectBykeyWord" parameterType="string" resultType="com.why.mybatis.entity.RoleEntity">
```
 
执行出来的SQL语句：

```sql
SELECT
*
FROM
t_role
WHERE
role_name LIKE '%why%'
OR id LIKE '%why%'
OR role_type LIKE '%why%';

```
 
**CONCAT（）函数** **推荐使用**
MySQL的 CONCAT（）函数用于将多个字符串连接成一个字符串，是最重要的mysql函数之一。

CONCAT(str1,str2,...) 

mapper
```java
List<RoleEntity> selectBykeyWord(@Param("keyword") String keyword);
```
 xml
```xml
<select id="selectBykeyWord" parameterType="string" resultType="com.why.mybatis.entity.RoleEntity">
SELECT
*
FROM
t_role
WHERE
role_name LIKE CONCAT('%',#{keyword},'%')
OR
id LIKE CONCAT('%',#{keyword},'%')
OR
role_type LIKE CONCAT('%',#{keyword},'%')
</select>
```
 
Mybatis的bind

```java
List<RoleEntity> selectBykeyWord(@Param("keyword") String keyword);
```
xml
```xml

<select id="selectBykeyWord" parameterType="string" resultType="com.why.mybatis.entity.RoleEntity">
<bind name="pattern" value="'%' + keyword + '%'" />
SELECT
*
FROM
t_role
WHERE
role_name LIKE #{pattern}
OR
id LIKE #{pattern}
OR
role_type like #{pattern}
</select>
```