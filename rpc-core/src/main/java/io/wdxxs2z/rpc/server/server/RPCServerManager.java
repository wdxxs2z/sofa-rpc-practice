package io.wdxxs2z.rpc.server.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RPCServerManager implements RPCServer {
	
	private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	 
    private static final HashMap<String, Class> serviceRegistry = new HashMap<String, Class>();
 
    private static boolean isRunning = false;
 
    private static int port;
    
    public RPCServerManager(int port) {
    	this.port = port;
    }

	public void stop() {
		isRunning = false;
		executor.shutdown();

	}

	//启动程序，序列化，反序列化，反射等
	public void start() throws IOException {
		ServerSocket socket = new ServerSocket();
		socket.bind(new InetSocketAddress(port));
		try {
			while(true) {
				executor.execute(new RpcServiceTask(socket.accept()));
			}
		} catch (Exception e) {
			socket.close();
		}

	}

	//注册中心
	@SuppressWarnings("rawtypes")
	public void register(Class serviceInterface, Class impl) {
		serviceRegistry.put(serviceInterface.getName(), impl);
	}

	public boolean isRunning() {
		return isRunning;
	}

	public int getPort() {
		return port;
	}
	
	//RPC Server端的任务，其中包含最重要的序列化和反序列化，主要通过反射获取服务提供者，最后获取执行结果
	private static class RpcServiceTask implements Runnable {
        Socket clent = null;
 
        public RpcServiceTask(Socket client) {
            this.clent = client;
        }
 
        @SuppressWarnings({ "rawtypes", "unchecked" })
		public void run() {
            ObjectInputStream input = null;
            ObjectOutputStream output = null;
            try {
                //将客户端发送的码流反序列化成对象，反射调用服务实现者，获取执行结果
                input = new ObjectInputStream(clent.getInputStream());
                String serviceName = input.readUTF();
                String methodName = input.readUTF();
                Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                Object[] arguments = (Object[]) input.readObject();
                Class serviceClass = serviceRegistry.get(serviceName);
                if (serviceClass == null) {
                    throw new ClassNotFoundException(serviceName + " not found");
                }
                Method method = serviceClass.getMethod(methodName, parameterTypes);
                Object result = method.invoke(serviceClass.newInstance(), arguments);
 
                //将执行结果反序列化，通过socket发送给客户端
                output = new ObjectOutputStream(clent.getOutputStream());
                output.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (clent != null) {
                    try {
                        clent.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } 
        }
	}
}
