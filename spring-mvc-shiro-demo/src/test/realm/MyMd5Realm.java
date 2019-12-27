package realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author : Charles
 * @description : Description
 * @date : 2019/12/18
 */
public class MyMd5Realm  extends AuthorizingRealm {


    Map userMap = new HashMap<String, String>(16);
    {
        userMap.put("chaooo","f51703256a38e6bab3d9410a070c32ea");//123456
        super.setName("test");
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //授权

        //1.获得用户名
        String userName = (String) principalCollection.getPrimaryPrincipal();
        // 从数据库或缓存取角色数据
        Set<String> roles = getRolesByUserName(userName);
        // 同上
        Set<String> persissions = getPersissionsByUserName(userName);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(persissions);
        simpleAuthorizationInfo.setRoles(roles);

        return simpleAuthorizationInfo;
    }
    private Set<String> getRolesByUserName(String userName){
        Set<String> sets = new HashSet<String>();
        sets.add("admin");
        sets.add("user");
        return sets;
    }
    private Set<String> getPersissionsByUserName(String userName){
        Set<String> sets = new HashSet<String>();
        sets.add("user:delete");
        sets.add("user:update");
        return sets;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //认证

        //1.从主体传过来的认证信息中，获得用户名
        String userName = (String) authenticationToken.getPrincipal();
        //2.通过用户名到数据库中获取凭证
        String password = getPasswordByUserName(userName);
        if(null==password){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo("chaooo", password, "test");
        return authenticationInfo;
    }

    private String getPasswordByUserName(String userName){
        return (String) userMap.get(userName);
    }

    public static void main(String[] args){
        Md5Hash md5Hash = new Md5Hash("admin");
        System.out.println("21232f297a57a5a743894a0e4a801fc3".length());
        System.out.println(md5Hash);
        //123456: f51703256a38e6bab3d9410a070c32ea
        // admin: 21232f297a57a5a743894a0e4a801fc3
    }
}
