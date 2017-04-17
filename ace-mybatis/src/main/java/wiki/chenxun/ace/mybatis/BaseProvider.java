package wiki.chenxun.ace.mybatis;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: Created by chenxun on 2017/4/15.
 */
public class BaseProvider {

    /**
     * SQL_MAP
     */
    private static  Map<String, String> sqlMap = new HashMap();
    /**
     * 列解析信息
     */
    private static  Map<Class, Map<String, String>> beanColumnMap = new HashMap();
    /**
     * 主键列解析信息
     */
    private static  Map<Class, Map<String, String>> beanPrimaryKeyMap = new HashMap();

    /**
     * queryByPrimaryKey
     *
     * @param obj 实体bean
     * @return sql
     */
    public String queryByPrimaryKey(Object obj) {
        Table table = getTable(obj);
        String key = "queryByPrimaryKey_" + table.getClass();
        String sql = sqlMap.get(key);
        if (sql == null) {
            StringBuilder stringBuilder = new StringBuilder("select ");
            StringBuilder columnStr = new StringBuilder();
            Map<String, String> columnMap = columnMap(obj.getClass());
            for (Map.Entry<String, String> entry : columnMap.entrySet()) {
                columnStr.append(entry.getKey());
                columnStr.append(" as ");
                columnStr.append(entry.getValue());
                columnStr.append(" ,");
            }
            columnStr.deleteCharAt(columnStr.lastIndexOf(","));
            stringBuilder.append(columnStr.toString());
            stringBuilder.append(" from ");
            stringBuilder.append(table.name());
            stringBuilder.append(buildWhereByPrimaryKey(obj.getClass()));
            sql = stringBuilder.toString();
            sqlMap.put(key, sql);

        }
        return sql;
    }

    /**
     * insert
     *
     * @param obj 实体bean
     * @return sql
     */
    public String insert(Object obj) {
        Table table = getTable(obj);
        String key = "insert_" + table.getClass();
        String sql = sqlMap.get(key);
        if (sql == null) {
            StringBuilder stringBuilder = new StringBuilder("insert into ");
            stringBuilder.append(table.name());
            stringBuilder.append("(");
            StringBuilder values = new StringBuilder("(");
            Map<String, String> columnMap = columnMap(obj.getClass());
            for (Map.Entry<String, String> entry : columnMap.entrySet()) {
                stringBuilder.append(entry.getKey());
                stringBuilder.append(",");
                values.append("#{");
                values.append(entry.getValue());
                values.append("},");
            }
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
            values.deleteCharAt(values.lastIndexOf(","));
            stringBuilder.append(" ) values ");
            values.append(" )");
            stringBuilder.append(values.toString());
            sqlMap.put(key, sql);

        }
        return sql;
    }

    /**
     * update
     *
     * @param obj 实体bean
     * @return sql
     */
    public String update(Object obj) {
        Table table = getTable(obj);
        String key = "update_" + table.getClass();
        String sql = sqlMap.get(key);
        if (sql == null) {
            StringBuilder stringBuilder = new StringBuilder(" update ");
            stringBuilder.append(table.name());
            stringBuilder.append(" set ");
            Map<String, String> columnMap = columnMap(obj.getClass());
            StringBuilder columnStr = new StringBuilder();
            for (Map.Entry<String, String> entry : columnMap.entrySet()) {
                columnStr.append(entry.getKey());
                columnStr.append(" = #{");
                columnStr.append(entry.getValue());
                columnStr.append(" } ,");

            }
            columnStr.deleteCharAt(columnStr.lastIndexOf(","));
            stringBuilder.append(columnStr.toString());
            stringBuilder.append(buildWhereByPrimaryKey(obj.getClass()));
            sql = stringBuilder.toString();
            sqlMap.put(key, sql);
        }

        return sql;


    }


    /**
     * delete
     *
     * @param obj 实体bean
     * @return sql
     */
    public String delete(Object obj) {
        Table table = getTable(obj);
        String key = "delete_" + table.getClass();
        String sql = sqlMap.get(key);
        if (sql == null) {
            StringBuilder stringBuilder = new StringBuilder("delete from  ");
            stringBuilder.append(table.name());
            stringBuilder.append(buildWhereByPrimaryKey(obj.getClass()));
            sql = stringBuilder.toString();
            sqlMap.put(key, sql);

        }
        return sql;
    }

    /**
     * 基于主键的where语句
     *
     * @param cls class
     * @return sql
     */
    private String buildWhereByPrimaryKey(Class cls) {
        StringBuilder stringBuilder = new StringBuilder(" where 1=1 ");
        Map<String, String> primaryKeyMap = primaryKeyMap(cls);
        for (Map.Entry<String, String> entry : primaryKeyMap.entrySet()) {
            stringBuilder.append(" and ");
            stringBuilder.append(entry.getKey());
            stringBuilder.append(" = ");
            stringBuilder.append("#{");
            stringBuilder.append(entry.getValue());
            stringBuilder.append("}");
        }
        return stringBuilder.toString();
    }

    /**
     * 获取table名称
     *
     * @param obj 实体bean
     * @return table
     */
    private Table getTable(Object obj) {
        Table table = obj.getClass().getAnnotation(Table.class);
        if (table == null || "".equals(table.name().trim())) {
            throw new RuntimeException("the class  must  has @table.name() ");
        }
        return table;
    }


    /**
     * 字段与列名映射
     *
     * @param cls class
     * @return map
     */
    private Map<String, String> columnMap(Class cls) {
        Map<String, String> columnMap = beanColumnMap.get(cls);
        if (columnMap == null) {
            parseColumns(cls);
            columnMap = beanColumnMap.get(cls);
        }

        return columnMap;
    }

    /**
     * 主键映射
     *
     * @param cls class
     * @return map
     */
    private Map<String, String> primaryKeyMap(Class cls) {
        Map<String, String> primaryKeyMap = beanPrimaryKeyMap.get(cls);
        if (primaryKeyMap == null) {
            parseColumns(cls);
            primaryKeyMap = beanPrimaryKeyMap.get(cls);
        }

        return primaryKeyMap;
    }

    /**
     * 解析列名
     *
     * @param cls class
     */
    private void parseColumns(Class cls) {
        Field[] fields = cls.getDeclaredFields();
        Map<String, String> columnMap = new HashMap<>();
        Map<String, String> primaryKeyMap = new HashMap<>();
        for (Field field : fields) {
            String key = field.getName();
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                if (!"".equals(column.name().trim())) {
                    key = column.name().trim();
                }
            }
            columnMap.put(key, field.getName());
            if (field.isAnnotationPresent(Id.class)) {
                primaryKeyMap.put(key, field.getName());
            }

        }
        beanColumnMap.put(cls, columnMap);
        beanPrimaryKeyMap.put(cls, primaryKeyMap);

    }


}
