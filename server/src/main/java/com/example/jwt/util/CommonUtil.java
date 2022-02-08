package com.example.jwt.util;

import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 通用代码工具类
 *
 * @author : Charles
 * @date : 2022/1/6
 */
public class CommonUtil {
    /**
     * 列表拼接成字符串
     * @param list List<Integer>
     * @param join 分隔符，默认','
     * @return String
     */
    public static String idListToString(List<Integer> list, String join){
        String split = ",";
        if (StringUtils.hasLength(join)){
            split = join;
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (StringUtils.hasLength(list.get(i).toString())){
                if (i == 0) {
                    str.append(list.get(i).toString());
                } else {
                    str.append(split).append(list.get(i).toString());
                }
            }
        }
        return str.toString();
    }
}
