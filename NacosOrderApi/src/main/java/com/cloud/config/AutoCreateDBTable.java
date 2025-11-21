package com.cloud.config;

import com.cloud.aop.ColumnName;
import com.cloud.aop.TableName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import javax.sql.DataSource;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@Configuration
public class AutoCreateDBTable {

    @Value("${mybatis-plus.type-aliases-package}")
    private String modelPackage;

    @Autowired
    private DataSource dataSource;

    @EventListener(ApplicationReadyEvent.class)
    public void autoCreateTable() throws Exception {
        scanAndCreateTables(modelPackage);
    }

    private void scanAndCreateTables(String packageName) throws Exception {
        // 获取包下的所有类
        List<Class<?>> entityClasses = getClasses(packageName);

        for (Class<?> clazz : entityClasses) {
            if (clazz.isAnnotationPresent(TableName.class)) {
                createTableFromClass(clazz);
            }
        }
    }

    private void createTableFromClass(Class<?> clazz) throws SQLException {
        TableName tableDefine = clazz.getAnnotation(TableName.class);
        String tableName = tableDefine.name();

        try (Connection conn = dataSource.getConnection()) {
            if (!tableExists(conn, tableName)) {
                String createSQL = generateCreateTableSQL(clazz, tableName);
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createSQL);
                    log.info("成功创建表: {}", tableName);
                }
            }
        }
    }

    private String generateCreateTableSQL(Class<?> clazz, String tableName) {
        StringBuilder sql = new StringBuilder("CREATE TABLE ");
        sql.append(tableName).append(" (");

        List<String> columns = new ArrayList<>();
        String primaryKey = null;

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ColumnName.class)) {
                ColumnName columnDefine = field.getAnnotation(ColumnName.class);
                StringBuilder columnDef = new StringBuilder();
                columnDef.append(columnDefine.value()).append(" ").append(getDataType(field.getType()));

                if (columnDefine.isPrimaryKey()) {
                    primaryKey = columnDefine.value();
                }

                columns.add(columnDef.toString());
            }
        }

        sql.append(String.join(", ", columns));

        if (primaryKey != null) {
            sql.append(", PRIMARY KEY (").append(primaryKey).append(")");
        }

        sql.append(")");
        return sql.toString();
    }

    private String getDataType(Class<?> fieldType) {
        if (fieldType == Integer.class || fieldType == int.class) {
            return "INT";
        } else if (fieldType == Long.class || fieldType == long.class) {
            return "BIGINT";
        } else if (fieldType == Double.class || fieldType == double.class) {
            return "DOUBLE";
        } else if (fieldType == Float.class || fieldType == float.class) {
            return "FLOAT";
        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
            return "BOOLEAN";
        } else if (fieldType == Date.class || fieldType == java.sql.Date.class || fieldType == Timestamp.class) {
            return "TIMESTAMP";
        } else {
            return "VARCHAR(255)";
        }
    }

    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        try (ResultSet rs = meta.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private List<Class<?>> getClasses(String packageName) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        List<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private List<Class<?>> findClasses(File directory, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    try {
                        String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                        classes.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        log.warn("无法加载类: {}", file.getName(), e);
                    }
                }
            }
        }
        return classes;
    }
}