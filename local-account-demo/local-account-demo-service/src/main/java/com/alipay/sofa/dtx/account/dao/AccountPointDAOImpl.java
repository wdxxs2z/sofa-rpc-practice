package com.alipay.sofa.dtx.account.dao;

import java.sql.SQLException;

import org.mybatis.spring.SqlSessionTemplate;

import com.alipay.sofa.dtx.account.domain.AccountPoint;

public class AccountPointDAOImpl implements AccountPointDAO {
	
	public SqlSessionTemplate sqlSession;
	
	public void setSqlSession(SqlSessionTemplate sqlSession) {  
        this.sqlSession = sqlSession;  
	}

	@Override
	public void addAccountPoint(AccountPoint accountPoint) throws SQLException {
    	sqlSession.insert("addAccountPoint", accountPoint);
	}

	@Override
	public AccountPoint findAccountPoint(String txId) throws SQLException {
        return (AccountPoint) sqlSession.selectOne("getAccountPoint", txId);
	}

	@Override
	public void deleteAccountPoint(String txId) throws SQLException {
    	sqlSession.delete("deleteAccountPoint", txId);
	}
	
	
	@Override
	public void updateAccountPointStatus(AccountPoint accountPoint) throws SQLException{
		sqlSession.update("updateAccountPointStatus", accountPoint);
    }
	
	@Override
	public void  deleteAllAccountPoint() throws SQLException{
		sqlSession.update("deleteAllAccountPoint");
    }

}
