package top.gotoeasy.framework.rmi.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.proxy.Enhancer;
import top.gotoeasy.framework.rmi.strategy.RemoteMethodNameStrategy;

/**
 * RMI客户端代理类
 * @since 2018/03
 * @author 青松
 */
public class RmiClientProxy {

	/** 缓存客户端代理对象 */
	private static final Map<String, Object>					mapProxy	= new ConcurrentHashMap<>();
	/** 缓存策略对象 */
	private static final Map<String, RemoteMethodNameStrategy>	mapStrategy	= new ConcurrentHashMap<>();

	/**
	 * 取得远程对象代理
	 * <p>
	 * 使用默认的远程方法标识名策略（保持和服务端策略一致才能成功调用）
	 * </p>
	 * @param <T> 远程对象类
	 * @param clas 远程对象类
	 * @param rmiUrl RMI服务的URL
	 * @return 远程对象代理
	 */
	public static <T> T getProxy(Class<T> clas, String rmiUrl) {
		return getProxy(clas, rmiUrl, RemoteMethodNameStrategy.class);
	}

	/**
	 * 取得远程对象代理
	 * <p>
	 * 使用指定的远程方法标识名策略（保持和服务端策略一致才能成功调用）
	 * </p>
	 * @param <T> 远程对象类
	 * @param clas 远程对象类
	 * @param rmiUrl RMI服务的URL
	 * @param strategyClass 远程方法标识名策略（保持和服务端策略一致才能成功调用）
	 * @return 远程对象代理
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProxy(Class<T> clas, String rmiUrl, Class<? extends RemoteMethodNameStrategy> strategyClass) {
		return (T)createProxy(clas, rmiUrl, strategyClass);
	}

	/**
	 * 创建代理对象
	 * <p>
	 * 不做可否远程调用的检查（比如有无指定注解），便于服务端用非注解式发布
	 * </p>
	 * @param clas 对象类
	 * @param rmiUrl RMI服务URL
	 * @param strategyClass 策略类
	 * @return 代理对象
	 */
	private static Object createProxy(Class<?> clas, String rmiUrl, Class<? extends RemoteMethodNameStrategy> strategyClass) {

		String strategyName = strategyClass.toGenericString();
		String key = clas.toGenericString() + rmiUrl + strategyName;
		Object obj = mapProxy.get(key);
		if ( obj == null ) {

			// 策略
			RemoteMethodNameStrategy strategy = mapStrategy.get(strategyName);
			if ( strategy == null ) {
				try {
					if ( RemoteMethodNameStrategy.class.equals(strategyClass) ) {
						strategy = new RemoteMethodNameStrategy() {
						};
					} else {
						strategy = strategyClass.newInstance();
					}
					mapStrategy.put(strategyName, strategy); // 缓存策略
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			// 增强
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(clas);
			enhancer.setCallback(new ClientInterceptor(rmiUrl, strategy));
			obj = enhancer.create(); // 返回目标类的增强子类
			mapProxy.put(key, obj); // 缓存代理对象
		}

		return obj;
	}

}
