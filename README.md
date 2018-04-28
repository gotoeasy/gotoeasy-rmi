[![Codacy Badge](https://api.codacy.com/project/badge/Grade/55f4ec73d57943fda3e7731cdbdd9ea7)](https://www.codacy.com/app/gotoeasy/gotoeasy-rmi?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=gotoeasy/gotoeasy-rmi&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/top.gotoeasy/gotoeasy-rmi/badge.svg)](https://maven-badges.herokuapp.com/maven-central/top.gotoeasy/gotoeasy-rmi)

# `gotoeasy-rmi`
GotoEasy系列的RMI封装模块，目的为简化RMI程序的开发。



Maven使用
```xml
<dependency>
    <groupId>top.gotoeasy</groupId>
    <artifactId>gotoeasy-rmi</artifactId>
    <version>x.y.z</version>
</dependency>
```
Gradle使用
```gradle
compile group: 'top.gotoeasy', name: 'gotoeasy-rmi', version: 'x.y.z'
```


- depend on `gotoeasy-aop` http://github.com/gotoeasy/gotoeasy-aop/

## 青松的姿势
- 业务类，POJO方式专注实现业务，标注上注解就可以提供RMI功能
```java
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

- 服务器端，关心部署配置，配置完成就启动吧
```java
public class Sample1ServerMain {

	public static void main(String[] args) throws Exception {
		RmiServerBuilder.get() // RMI服务创建器
				.server("127.0.0.1", 1099, "service") // rmi://127.0.0.1:1099/service
				.scan("top.gotoeasy.sample.rmi") // 扫描远程服务对象和方法
				.start(); // 启动RMI服务
	}
}
```

- 客户端，关心服务来源和业务类，直接拿来像本地对象一样去用就行了
```java
public class Sample1ClientMain {

	public static void main(String[] args) throws Exception {
		Sample1HelloRmi remote = RmiClientProxy.getProxy(Sample1HelloRmi.class, "rmi://127.0.0.1:1099/service");
		String msg = remote.hello("Sample1");
		System.out.println(msg); // 结果：Hello Sample1
	}

}
```

## GotoEasy系列
- `gotoeasy-core` http://github.com/gotoeasy/gotoeasy-core/
- `gotoeasy-aop` http://github.com/gotoeasy/gotoeasy-aop/
- `gotoeasy-rmi` http://github.com/gotoeasy/gotoeasy-rmi/
- TODO
- TODO
- TODO

## LICENSE

    Copyright (c) 2018 ZhangMing (www.gotoeasy.top)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
