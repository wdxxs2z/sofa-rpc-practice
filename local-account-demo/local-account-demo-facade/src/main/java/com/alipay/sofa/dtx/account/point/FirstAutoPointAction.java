package com.alipay.sofa.dtx.account.point;

public interface FirstAutoPointAction {
	
	public boolean point_add(String txId, String accountNo, double addPoint);

}
