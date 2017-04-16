package wiki.chenxun.ace.mybatis.test;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;

/**
 * @Description: Created by chenxun on 2017/4/15.
 */
public class TestMybatis {

    private static Server dbServer;

    public static void main(String[] args) throws Exception {

        TestH2 h2 = new TestH2();
        // 开始服务
        h2.startServer();
        h2.testH2();

        Class.forName("org.h2.Driver");
        JdbcDataSource JdbcDataSource=new JdbcDataSource();
        JdbcDataSource.setUrl("jdbc:h2:./test");
        JdbcDataSource.setUser("sa");
        JdbcDataSource.setPassword("");


        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("Production", transactionFactory, JdbcDataSource);

        Configuration configuration = new Configuration(environment);
        configuration.addMapper(PersonMapper.class);
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);


        SqlSession sqlSession = sqlSessionFactory.openSession();

        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Person person=new Person();
        person.setId(1);
        Person person1= personMapper.queryByPrimaryKey(person);
        System.out.println(person1);

        h2.stopServer();


    }




}
