
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;


/**
 * @author : Charles
 * @description : Description
 * @date : 2019/12/18
 */
public class AuthenticationTest {

    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void addUser(){
        simpleAccountRealm.addAccount("chaooo", "123456", "admin","user");
    }

    /**
     * 认证测试方法
     */
    @Test
    public void testAuthentication(){
        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        // 2. 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("chaooo", "123456");

        subject.login(token);
        System.out.println("isAuthenticated:"+subject.isAuthenticated());
        //subject.logout();
        //System.out.println("isAuthenticated:"+subject.isAuthenticated());
        subject.checkRole("admin");



    }
}
