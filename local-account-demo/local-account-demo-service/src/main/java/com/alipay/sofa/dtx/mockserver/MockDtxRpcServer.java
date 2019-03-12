package com.alipay.sofa.dtx.mockserver;

import com.alipay.dtx.rpc.server.impl.RpcServer;

public class MockDtxRpcServer{
	
	
	public void init()  {
		RpcServer dtxRpcServer = new RpcServer();
		
		MockServerMessageHandler testServerMessageHandler = new MockServerMessageHandler();
		testServerMessageHandler.setRpcServer(dtxRpcServer);
		
		dtxRpcServer.setServerMessageHandler(testServerMessageHandler);
		
		dtxRpcServer.init();
		
		System.out.println("dtx server(mock) init finish....");
	}


}
