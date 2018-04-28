package top.gotoeasy.sample.rmi.sample1;

import top.gotoeasy.framework.rmi.client.RmiClientProxy;

/**
 * 例子1的RMI客户端
 * 
 * @since 2018/03
 * @author 青松
 */
public class Sample1ClientMain {

    /**
     * 主入口方法
     * 
     * @param args 参数
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Sample1HelloRmi remote = RmiClientProxy.getProxy(Sample1HelloRmi.class, "rmi://127.0.0.1:1099/service");
        String msg = remote.hello("Sample1");
        System.out.println(msg);
    }

}
