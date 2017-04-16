package wiki.chenxun.ace.examples.simple.test;

import wiki.chenxun.ace.core.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Created by chenxun on 2017/4/9.
 */
public class Test {

    public static void main(String[] args) {
        String packageName = "wiki.chenxun.ace.examples.simple";
        List<String> argList = new ArrayList<>();
        argList.add("aceServicePackage:" + packageName);
        Main.main(argList.toArray(new String[argList.size()]));
    }
}
