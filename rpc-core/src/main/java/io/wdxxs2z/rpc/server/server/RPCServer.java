package io.wdxxs2z.rpc.server.server;

import java.io.IOException;

public interface RPCServer {
	
	public void stop();
	 
    public void start() throws IOException;
 
    @SuppressWarnings("rawtypes")
	public void register(Class serviceInterface, Class impl);
 
    public boolean isRunning();
 
    public int getPort();
}
