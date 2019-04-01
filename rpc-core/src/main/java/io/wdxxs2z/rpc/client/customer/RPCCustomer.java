package io.wdxxs2z.rpc.client.customer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RPCCustomer<T> {
	
	@SuppressWarnings("unchecked")
	public static<T> T getRemoteObj(final Class<?> serviceInstance, final InetSocketAddress addr) {
		return (T) Proxy.newProxyInstance(serviceInstance.getClassLoader(), new Class<?>[]{serviceInstance}, 
				new InvocationHandler() {
					//代理远程服务提供者
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						Socket socket = null;
						ObjectOutputStream output = null;
						ObjectInputStream input = null;
						try {
							//创建socket客户端
							socket = new Socket();
							socket.connect(addr);
							
							//远程服务调用所需的接口，方法，参数等
							output = new ObjectOutputStream(socket.getOutputStream());
							output.writeUTF(serviceInstance.getName());
							output.writeUTF(method.getName());
							output.writeObject(method.getParameterTypes());
							output.writeObject(args);
							
							//BIO的方式等待回应
							input = new ObjectInputStream(socket.getInputStream());
							return input.readObject();
						} finally {
							if (socket != null) socket.close();
							if (output != null) output.close();
							if (input != null) input.close();
						}
					}
				});
	}
	
}
