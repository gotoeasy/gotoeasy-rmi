package top.gotoeasy.framework.rmi.server;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.gotoeasy.framework.core.util.CmnClass;
import top.gotoeasy.framework.rmi.annotation.RemoteMethod;
import top.gotoeasy.framework.rmi.annotation.Rmi;
import top.gotoeasy.framework.rmi.strategy.RemoteMethodNameStrategy;

/**
 * RMI服务创建器
 * @since 2018/03
 * @author 青松
 */
public class RmiServerBuilder {

	private static final Logger			log	= LoggerFactory.getLogger(RemoteDispatcher.class);

	private String						host;
	private int							port;
	private String						serviceName;
	private String						rmiUrl;

	private RemoteDispatcher			remote;

	private RemoteMethodNameStrategy	strategy;
	private RemoteBeanProvider			provider;

	private String[]					packages;
	private Class<? extends Annotation>	typeAnnotation;
	private Class<? extends Annotation>	methodAnnotation;

	/**
	 * 生成创建器
	 * @return 创建器
	 */
	public static RmiServerBuilder get() {
		return new RmiServerBuilder();
	}

	/**
	 * 指定RMI服务器信息
	 * @param host 地址
	 * @param port 端口
	 * @param serviceName 服务名
	 * @return 创建器
	 */
	public RmiServerBuilder server(String host, int port, String serviceName) {
		this.host = host;
		this.port = port;
		this.serviceName = serviceName;
		return this;
	}

	/**
	 * 指定要扫描的包
	 * @param packages 要扫描的包
	 * @return 创建器
	 */
	public RmiServerBuilder scan(String ... packages) {
		this.packages = packages;
		return this;
	}

	/**
	 * 自定义远程类注解
	 * @param typeAnnotation 远程类注解
	 * @return 创建器
	 */
	public RmiServerBuilder customTypeAnnotation(Class<? extends Annotation> typeAnnotation) {
		this.typeAnnotation = typeAnnotation;
		return this;
	}

	/**
	 * 自定义远程方法注解
	 * @param methodAnnotation 远程方法注解
	 * @return 创建器
	 */
	public RmiServerBuilder customMethodAnnotation(Class<? extends Annotation> methodAnnotation) {
		this.methodAnnotation = methodAnnotation;
		return this;
	}

	/**
	 * 设定远程Bean对象提供者，未设定时按默认构造器创建远程Bean对象
	 * @param provider 远程Bean对象提供者
	 * @return 创建器
	 */
	public RmiServerBuilder provider(RemoteBeanProvider provider) {
		this.provider = provider;
		return this;
	}

	/**
	 * 自定义远程方法标识名策略
	 * @param strategy 远程方法标识名策略
	 * @return 创建器
	 */
	public RmiServerBuilder strategy(RemoteMethodNameStrategy strategy) {
		this.strategy = strategy;
		return this;
	}

	/**
	 * 启动RMI服务
	 */
	public void start() {
		try {
			// 创建准备工作
			build();

			// 启动RMI服务
			LocateRegistry.createRegistry(port);
			Naming.bind(rmiUrl, remote);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		log.info("RMI服务就绪 [{}]", rmiUrl);
	}

	/**
	 * 创建准备工作
	 */
	protected void build() {
		this.rmiUrl = "rmi://" + host + ":" + port + "/" + serviceName; // RMI的URL
		if ( strategy == null ) {
			// 标识的命名策略
			strategy = new RemoteMethodNameStrategy() {
			};
		}

		// 扫描要提供服务的远程类
		List<Class<?>> list = new ArrayList<>();
		for ( String pack : packages ) {
			log.info("扫描包[{}]下的远程类", pack);
			// 默认@Rmi保持有效
			list.addAll(CmnClass.getClasses(pack, Rmi.class));
			// 自定义注解设定后有效
			if ( typeAnnotation != null ) {
				list.addAll(CmnClass.getClasses(pack, typeAnnotation));
			}
		}

		// 扫描要提供服务的远程方法
		Map<String, Method> mapMethod = new HashMap<>();
		Method[] methods = null;
		Method method = null;
		String key = null;
		for ( Class<?> claz : list ) {
			methods = claz.getDeclaredMethods();
			for ( int i = 0; i < methods.length; i++ ) {
				method = methods[i];
				if ( !Modifier.isPublic(method.getModifiers()) ) {
					// 必须是PUBLIC方法
					continue;
				}

				if ( method.isAnnotationPresent(RemoteMethod.class) || (methodAnnotation != null && method.isAnnotationPresent(methodAnnotation)) ) {
					// 默认@RemoteMethod保持有效,自定义注解设定后有效
					key = strategy.getName(method); // 标识
					mapMethod.put(key, method); // method缓存
					log.info("远程方法就绪: {}", method);
				}

			}
		}

		// 创建远程服务对象
		try {
			remote = new RemoteDispatcher();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}

		// 缓存扫描结果
		remote.setMapMethod(mapMethod); // method缓存
		remote.setRemoteBeanProvider(provider); // 远程Bean对象提供者

	}

}
