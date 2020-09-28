package com.pccw.backend.bean;

import com.pccw.backend.util.Convertor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ResultRecode {
    private List<Map> data;

    /**返回一个map的key为小写的list
     *
     * @param data
     * @return
     */
    public static List<Map> returnResult(List<Map> data){
        List<Map> resultList = new ArrayList<Map>();
        for (Map orgMap:data){
            Map resultMap = Convertor.transformLowerCase(orgMap);
            resultList.add(resultMap);
        }
        return resultList;
    }

    /**
     * 将List中map的key值命名方式格式化为驼峰
     * @param list
     * @return
     */
    public static List<Map<String, Object>> returnHumpNameForList(List<Map<String, Object>> list) {
        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> o : list) {
            newList.add(Convertor.formatHumpName(o));
        }
        return newList;
    }
}
