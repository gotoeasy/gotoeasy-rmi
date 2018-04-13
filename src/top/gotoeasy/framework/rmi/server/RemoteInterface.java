package top.gotoeasy.framework.rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 远程服务接口
 * @since 2018/03
 * @author 青松
 */
public interface RemoteInterface extends Remote {

	/**
	 * 执行远程调用
	 * @param name 标识名
	 * @param args 远程调用参数
	 * @return 执行结果
	 * @throws RemoteException 异常
	 */
	public Object execute(String name, Object ... args) throws RemoteException;

}
