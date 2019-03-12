package com.alipay.sofa.dtx.mockserver;

import java.util.Map;

import com.alipay.dtx.rpc.handler.MessageHandler;
import com.alipay.dtx.rpc.message.Message;
import com.alipay.dtx.rpc.message.response.Response;

public class MockMessageHandler implements MessageHandler {

	@Override
	public Object handleMessage(Map<String, String> networkContext, Message message) {
		return new Response(true);
	}

}
