package top.itdn.mall;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.TestCase;
import top.itdn.mall.common.Const;
import top.itdn.mall.common.ResponseJson;
import top.itdn.mall.controller.backend.UserManageController;

/**
 * @Author: Charles
 * @Date: 2019/11/17
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes=RunApplication.class)
public class ApiTests {
    @Autowired
    private UserManageController controller;
    /**
     * 测试SpringMVC处理流程 主要逻辑
     * @param uri
     * @param params
     * @param enumMsg
     * @throws Exception
     */
    private void testHttp(String type, String uri, MultiValueMap<String, String> params, String enumMsg, String token) throws Exception {
        //创建MockMvc对象，可以发送HTTP请求，接收响应结果
        MockMvc mock = MockMvcBuilders.standaloneSetup(controller).build();
        //使用mock对象发送POST请求
        RequestBuilder request = null;
        if("post".equals(type)) {
            if(params==null) request = MockMvcRequestBuilders.post(uri);
            else request = MockMvcRequestBuilders.post(uri).params(params);
        }
        if("get".equals(type)) {
            if(params==null) request = MockMvcRequestBuilders.get(uri);
            else request = MockMvcRequestBuilders.get(uri).params(params);
        }
        MvcResult result = mock.perform(request).andReturn();
        //获取返回的结果
        String jsonStr = result.getResponse().getContentAsString();
        //将返回jsonStr结果转成ResultJson对象
        ObjectMapper mapper = new ObjectMapper();
        ResponseJson resultJson = mapper.readValue(jsonStr, ResponseJson.class);

        System.out.println(resultJson);
        //使用断言比对结果和预期
        TestCase.assertEquals(enumMsg, resultJson.getMsg());
    }

    //测试注册
    @Test
    public void testRegister() throws Exception {
        //配置参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.set("name", "chao2");
        params.set("password", "123456");
        String msg = Const.MessageEnum.INSERT_SUCCESS.getMsg();
        //测试
        testHttp("post", "/manage/user/sign.do", params, msg, null);
    }

    //测试登录
    @Test
    public void testLogin() throws Exception {
        //配置参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.set("name", "chao2");
        params.set("password", "123456");
        String msg = Const.MessageEnum.SUCCESS.getMsg();
        //测试
        testHttp("post", "/manage/user/login.do", params, msg, null);
    }


}
