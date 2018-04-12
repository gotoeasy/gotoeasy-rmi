# gotoeasy-rmi
GotoEasy系列的RMI封装模块，目的为简化RMI程序的开发。

# 青松的姿势
业务类
```
@Rmi()
public class Sample1HelloRmi {
    /**
     * RMI远程方法例子
     */
    @RemoteMethod()
    public String hello(String name) {
        return "Hello " + name;
    }
}
```

服务器端
```
public class Sample1ServerMain {

	public static void main(String[] args) throws Exception {
		RmiServerBuilder.get() // RMI服务创建器
				.server("127.0.0.1", 1099, "service") // rmi://127.0.0.1:1099/service
				.scan("top.gotoeasy.sample.rmi") // 扫描远程服务对象和方法
				.start(); // 启动RMI服务
	}
}
```

客户端
```
public class Sample1ClientMain {

	public static void main(String[] args) throws Exception {
		Sample1HelloRmi remote = RmiClientProxy.getProxy(Sample1HelloRmi.class, "rmi://127.0.0.1:1099/service");
		String msg = remote.hello("Sample1");
		System.out.println(msg);
	}

}
```
