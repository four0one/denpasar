package com.plumeria.denpasar.util;


import java.util.HashMap;
import java.util.List;

/**
 * Created by chenwei on 2016/11/9.
 */
public class SensitiveWordUtil {

    private HashMap<String, Object> treeMap;

    private static SensitiveWordUtil instance;

    private SensitiveWordUtil(List<String> sensitiveWords) {
        init(sensitiveWords);
    }

	//初始化方法，将敏感词库构建成树形结构，以map形式保存
    public void init(List<String> sensitiveWords) {
        if (treeMap != null) {
            return;
        }

        treeMap = new HashMap<>(sensitiveWords.size());
        if (!sensitiveWords.isEmpty()) {
            for (String word : sensitiveWords) {
                HashMap<String, Object> currentMap = treeMap;
                HashMap<String, Object> tmpMap;
                for (int i = 0; i < word.length(); i++) {
                    String key = String.valueOf(word.charAt(i));
                    Object o = currentMap.get(key);
                    if (null == o) {
                        tmpMap = new HashMap<>();
                        tmpMap.put("end", false);//当前节点是否结束，true为最后节点
                        currentMap.put(key, tmpMap);
                        currentMap = tmpMap;
                    } else {
                        currentMap = (HashMap<String, Object>) o;
                    }
                    if (i == word.length() - 1) {
                        currentMap.put("end", true);
                    }
                }
            }
        }
    }

	//敏感词过滤方法，讲匹配的敏感词替换为*,传入参数为文字内容
    public String filter(String content) {
        HashMap scanMap = treeMap;
        StringBuffer tmpWord = new StringBuffer();
        int cursor = -1;
        for (int i = 0; i < content.length(); i++) {//循环文字内容
            String c = String.valueOf(content.charAt(i));//注意去除字符
            Object o = scanMap.get(c);//到map中匹配节点
            if (o != null) {
				/**
				匹配到某个字节点，当前游标移动到该位置，
				并且临时敏感词变量加入第一个字，
				扫描树由完整的原始数据变为当前字符开始的树结构
				*/
                cursor = i;
                tmpWord.append(c);
                scanMap = (HashMap) o;
                if ((boolean) scanMap.get("end")) {
					/**
						树形结构结束，证明完全匹配了当前敏感词组，替换文本中符合敏感词临时变量的文字为*
					*/
                    content = content.replaceAll(tmpWord.toString(), "***");
                    tmpWord.delete(0, tmpWord.length());
                    scanMap = treeMap;
                    cursor = -1;
                }
            } else {
				/**
					没有找到文字对应map，如果游标为-1，则表示在初始化的树种查找不到，继续下一个文字
					如果游标有值，证明前面匹配到几个字，但是后面不匹配，
					这时候需要将游标返回到-1，临时变量清除，扫描的树变为最初的树
					循环变量返回到最后失败前位置
				*/
                if (cursor != -1) {
                    i = cursor;
                    tmpWord.delete(0, tmpWord.length());
                    scanMap = treeMap;
                    cursor = -1;
                }
            }
        }
        return content;
    }

    public static SensitiveWordUtil getInstance() {
        if (null == instance) {
            synchronized (SensitiveWordUtil.class) {
                if (null == instance) {
                    //读取敏感词库做参数初始化工具类
                    instance = new SensitiveWordUtil(null);
                }
            }
        }

        return instance;
    }

    public static void destroy() {
        if (null != instance) {
            instance = null;
        }
    }

}
