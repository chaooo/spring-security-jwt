import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import realm.MyMd5Realm;

/**
 * @author : Charles
 * @description : Description
 * @date : 2019/12/18
 */
public class MyMd5RealmTest {
    @Test
    public void testAuthentication(){

        MyMd5Realm myRealm = new MyMd5Realm();

        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(myRealm);

        // 使用加密之后的
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(1);

        myRealm.setCredentialsMatcher(matcher);

        // 2. 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("chaooo", "123456");

        subject.login(token);
        System.out.println("isAuthenticated:"+subject.isAuthenticated());

        //subject.checkRoles("admin");
        //subject.checkPermissions("user:delete");

    }
}
