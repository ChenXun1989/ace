package wiki.chenxun.ace.mybatis.test;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Description: Created by chenxun on 2017/4/15.
 */
public class TestH2 {

    private Server server;
    private String port = "8082";
    private static String sourceURL1 = "jdbc:h2:./test";
    private String user = "sa";
    private String password = "";

    public void startServer() {
        try {
            System.out.println("正在启动h2...");
            server = Server.createTcpServer(
                    new String[]{"-tcpPort", port}).start();
        } catch (SQLException e) {
            System.out.println("启动h2出错：" + e.toString());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void stopServer() {
        if (server != null) {
            System.out.println("正在关闭h2...");
            server.stop();
            System.out.println("关闭成功.");
        }
    }

    public void testH2() {
        try {
            // 加载驱动
            Class.forName("org.h2.Driver");

            // 创建连接
            Connection conn = DriverManager.getConnection(sourceURL1, user, password);
            Statement stat = conn.createStatement();

            // 插入数据
            stat.execute("CREATE MEMORY Table If Not Exists t_person(id INT auto_increment PRIMARY KEY , name_1 VARCHAR ) ");
            stat.execute("delete from t_person");
            stat.execute("INSERT INTO t_person values (1,'abc')");
            // 查询数据
            ResultSet result = stat.executeQuery("select name_1 from t_person ");
            int i = 1;
            while (result.next()) {
                System.out.println(i++ + ":" + result.getString("name_1"));
            }

            result.close();
            stat.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args) {
        TestH2 h2 = new TestH2();
        // 开始服务
        h2.startServer();
        h2.testH2();
        // 关闭服务
        h2.stopServer();
    }



}
