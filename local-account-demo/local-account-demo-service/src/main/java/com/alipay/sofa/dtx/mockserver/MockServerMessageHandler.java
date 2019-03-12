package com.alipay.sofa.dtx.mockserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alipay.dtx.common.log.LoggerUtil;
import com.alipay.dtx.common.log.LoggerUtil.Log;
import com.alipay.dtx.common.util.UUIDGenUtil;
import com.alipay.dtx.rpc.conn.ConnectionHolder;
import com.alipay.dtx.rpc.handler.ServerMessageHandler;
import com.alipay.dtx.rpc.message.Message;
import com.alipay.dtx.rpc.message.request.ActionRequest;
import com.alipay.dtx.rpc.message.request.ActivityRequest;
import com.alipay.dtx.rpc.message.request.ClientConnectRequest;
import com.alipay.dtx.rpc.message.request.CommitRequest;
import com.alipay.dtx.rpc.message.request.ResourceRegisterMessage;
import com.alipay.dtx.rpc.message.request.RollbackRequest;
import com.alipay.dtx.rpc.message.response.ActivityCreateResponse;
import com.alipay.dtx.rpc.message.response.Response;
import com.alipay.dtx.rpc.message.server.request.BranchCommitRequest;
import com.alipay.dtx.rpc.message.server.request.BranchRolbackRequest;
import com.alipay.dtx.rpc.server.impl.RpcServer;
import com.alipay.dtx.tools.collections.ArrayHashMap;
import com.alipay.remoting.Connection;

public class MockServerMessageHandler implements ServerMessageHandler {
	
	protected static Log logger = LoggerUtil.DTX_RPC_SERVER;
	
	private RpcServer rpcServer;
	
	private Map<String, ActivityRequest> map1 = new HashMap<String, ActivityRequest>();
	
//	private ArrayHashMap<String, ActionRequest> map2= new ArrayHashMap<String, ActionRequest>();
	
	private Map<String, ArrayHashMap<String, ActionRequest>> map3= new HashMap<String, ArrayHashMap<String, ActionRequest>>();

	 private ConcurrentHashMap<String, Connection>  connections = new ConcurrentHashMap<String, Connection>();
	
	@Override
	public Response handleMessage(String remoteIp, final Message request)  {
//		return new Response(true);
		if(request == null){
			return new Response(false);
		}
		logger.info("receive:"+request);
		
		final String ip = remoteIp;
		
		if(request instanceof ClientConnectRequest){
			return new Response(true);
		}else if(request instanceof  ResourceRegisterMessage) {
			return new Response(true);
		}else if(request instanceof ActivityRequest){
			String txId = UUIDGenUtil.genActionId();
			ActivityCreateResponse re = new ActivityCreateResponse();
			re.setTxId(txId);
			map1.put(txId, (ActivityRequest) request);
			return re;
		} else if(request instanceof ActionRequest ){
			String txId = ((ActionRequest)request).getTxId();
			ArrayHashMap<String, ActionRequest> tem = map3.get(txId);
			if(tem == null){
				tem = new ArrayHashMap<String, ActionRequest>();
			}
			tem.put(ip, (ActionRequest)request);
			map3.put(txId, tem);
			return new Response(true);
		}else if(request instanceof CommitRequest) {
			String txId = ((CommitRequest)request).getTxId();
			ArrayHashMap<String, ActionRequest> list223 = map3.get(txId);
			if(list223 != null) {
				for(final String ip234: list223.getKeySet()) {
					List<ActionRequest> list = list223.get(ip234);
					for(ActionRequest a : list){
						final BranchCommitRequest re = new BranchCommitRequest();
						re.setActionId(a.getActionId());
						re.setTxId(a.getTxId());
						re.setInstanceId(a.getInstanceId());
						re.setResourceId(a.getResourceId());
						re.setResourceType(a.getResourceType());
						re.setActionContext(a.getContext());
						re.setActivityContext(map1.get(txId).getContext());
						
						new Thread(new Runnable(){

							@Override
							public void run() {
								
								int i = 0;
								while(i++ < 5){
									try {
										Response ret = (Response) rpcServer.getServer().invokeSync(connections.get(ip234), re, 10000);
										if(ret.isSuccess()){
											break;
										}
										System.out.println("server2client:" + re + " @@@ " + ret);

									} catch (Exception e) {
										System.out.println("aa:" + e);
									}
									try {
										Thread.sleep(3000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
							
						}).start();
					}
				}
			}
			return new Response(true);
		}else if(request instanceof RollbackRequest){
			String txId = ((RollbackRequest)request).getTxId();
			ArrayHashMap<String, ActionRequest> list223 = map3.get(txId);
			if(list223 != null){
				for(final String ip234: list223.getKeySet()){
					List<ActionRequest> list = list223.get(ip234);
					
					for(ActionRequest a : list){
						final BranchRolbackRequest re = new BranchRolbackRequest();
						re.setActionId(a.getActionId());
						re.setTxId(a.getTxId());
						re.setInstanceId(a.getInstanceId());
						re.setResourceId(a.getResourceId());
						re.setResourceType(a.getResourceType());
						re.setActionContext(a.getContext());
						re.setActivityContext(map1.get(txId).getContext());
						
						new Thread(new Runnable(){
		
							@Override
							public void run() {
								
								int i = 0;
								while(i ++ < 5){
									try {
										Response ret = (Response) rpcServer.getServer().invokeSync(connections.get(ip234), re, 10000);
										if(ret.isSuccess()){
											break;
										}
//										logger.info("server2client:" + re + " @@@ " + ret);
									} catch (Exception e) {
										System.out.println("aa:" + e);
									}
									try {
										Thread.sleep(3000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
						}).start();
					}
				}
			}
			
			return new Response(true);
		}
		return new Response(true);
//		
	}

	@Override
	public boolean onConnected(String remoteAddr, ConnectionHolder conn) {
		connections.put(remoteAddr, (Connection) conn.getConnection());
		return true;
	}

	@Override
	public boolean onDisConnected(String remoteAddr, ConnectionHolder conn) {
		// TODO Auto-generated method stub
		return true;
	}

	public void setRpcServer(RpcServer rpcServer) {
		this.rpcServer = rpcServer;
	}
	
}
