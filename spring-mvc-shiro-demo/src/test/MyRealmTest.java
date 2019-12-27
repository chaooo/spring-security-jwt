import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import realm.MyRealm;

/**
 * @author : Charles
 * @description : Description
 * @date : 2019/12/18
 */
public class MyRealmTest {
    @Test
    public void testAuthentication(){

        MyRealm myRealm = new MyRealm();

        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(myRealm);

        // 2. 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("chaooo", "123456");

        subject.login(token);
        System.out.println("isAuthenticated:"+subject.isAuthenticated());

        subject.checkRoles("admin");
        subject.checkPermissions("user:delete");

    }
}
