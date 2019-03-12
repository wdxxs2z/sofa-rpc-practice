package com.alipay.sofa.dtx.mockserver;

import com.alipay.dtx.rpc.message.response.Response;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;

public class MockStringSyncUserProcessor extends SyncUserProcessor {

	@Override
	public Object handleRequest(BizContext bizCtx, Object request) throws Exception {
		return new Response(true);
	}

	@Override
	public String interest() {
		return String.class.getName();
	}

}
