# portal
  ## version:V1.0
  ## 实现功能
    1、对单条数据的查询、插入、更新、删除。
	2、批量操作数据：批量插入，根据条件删除，根据条件更新指定的列名-字段值。
	3、高级查询：可设置查询列，查询条件，排序，分页
 **attention**
 1.因本人使用的数据库为Mysql，Oracle没做过测试。
 2.java 实体类字段名为驼峰风格，数据库字段名为下划线风格。
 3.每张表都必须要有一个主键字段，字段类型为Integer，若要改为String或Object需要修改源码。
 4.必须在实体类中注明主键，表名(可选)，使用JPA注解。
 5.对于枚举类型的处理，以枚举ordinal()值，存取到数据库中。
