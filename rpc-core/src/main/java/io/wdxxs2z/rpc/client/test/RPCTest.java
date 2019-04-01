package io.wdxxs2z.rpc.client.test;

import java.net.InetSocketAddress;

import io.wdxxs2z.rpc.client.customer.RPCCustomer;
import io.wdxxs2z.rpc.server.provider.RpcService;
import io.wdxxs2z.rpc.server.provider.RpcServiceImpl;
import io.wdxxs2z.rpc.server.server.RPCServer;
import io.wdxxs2z.rpc.server.server.RPCServerManager;

public class RPCTest {

	public static void main(String[] args) {
		new Thread(new Runnable() {
			
			public void run() {
				try {
					RPCServer server = new RPCServerManager(9999);
					server.register(RpcService.class, RpcServiceImpl.class);
					server.start();
				} catch (Exception e) {
					e.printStackTrace();
				}			
			}
		}).start();
		RpcService service = RPCCustomer.getRemoteObj(RpcService.class, new InetSocketAddress("localhost", 9999));
		System.out.println(service.rpcLogic("rpc logic."));
	}
}
