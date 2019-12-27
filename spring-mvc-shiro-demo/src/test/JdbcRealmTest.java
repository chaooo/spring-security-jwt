import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @author : Charles
 * @description : Description
 * @date : 2019/12/18
 */
public class JdbcRealmTest {

    private DruidDataSource dataSoure = new DruidDataSource();
    {
        dataSoure.setUrl("jdbc:mysql://localhost:3306/test");
        dataSoure.setUsername("root");
        dataSoure.setPassword("123qwe");
    }

    @Test
    public void testAuthentication(){
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSoure);
        jdbcRealm.setPermissionsLookupEnabled(true);//默认false

        //shiro有默认sql,也可自定义sql
        String sql = "select password from test_user where username=?";
        jdbcRealm.setAuthenticationQuery(sql);
        String sql2 = "select role_name from test_user_role where username=?";
        jdbcRealm.setUserRolesQuery(sql2);

        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        // 2. 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("chaoo", "123456");

        subject.login(token);
        System.out.println("isAuthenticated:"+subject.isAuthenticated());
        //subject.logout();
        //System.out.println("isAuthenticated:"+subject.isAuthenticated());
        //subject.checkRole("admin");
        //subject.checkPermission("user:delete");
        //subject.checkPermission("user:update");




    }
}
