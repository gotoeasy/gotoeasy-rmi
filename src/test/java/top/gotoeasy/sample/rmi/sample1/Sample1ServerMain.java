package top.gotoeasy.sample.rmi.sample1;

import top.gotoeasy.framework.rmi.server.RmiServerBuilder;

/**
 * 例子1的RMI服务端
 * 
 * @since 2018/03
 * @author 青松
 */
public class Sample1ServerMain {

    /**
     * 主入口方法
     * 
     * @param args 参数
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        RmiServerBuilder.get() // RMI服务创建器
                .server("127.0.0.1", 1099, "service") // rmi://127.0.0.1:1099/service
                .scan("top.gotoeasy.sample.rmi") // 扫描远程服务对象和方法
                .start(); // 启动RMI服务
    }

}
