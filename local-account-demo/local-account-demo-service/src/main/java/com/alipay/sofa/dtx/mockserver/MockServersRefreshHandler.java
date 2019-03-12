package com.alipay.sofa.dtx.mockserver;

import java.util.List;

import com.alipay.dtx.rpc.client.refresh.ServersRefreshHandler;
import com.alipay.dtx.rpc.conn.ConnectionHolder;

public class MockServersRefreshHandler implements ServersRefreshHandler {

	@Override
	public boolean onServerConnectionsRefresh(List<ConnectionHolder> connections) {
		return true;
	}

}
