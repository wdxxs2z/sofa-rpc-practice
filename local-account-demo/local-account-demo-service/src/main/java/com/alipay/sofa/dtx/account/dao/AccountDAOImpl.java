package com.alipay.sofa.dtx.account.dao;

import java.sql.SQLException;

import org.mybatis.spring.SqlSessionTemplate;

import com.alipay.sofa.dtx.account.domain.Account;


public class AccountDAOImpl implements AccountDAO {
	
	public SqlSessionTemplate sqlSession;  
	
	public void setSqlSession(SqlSessionTemplate sqlSession) {  
        this.sqlSession = sqlSession;  
	}
	
    /** 
     * @see com.alipay.sofa.dtx.account.dao.AccountDAO#addAccount(com.alipay.dtx.perf.tcc.domains.Account)
     */
    @Override
    public void addAccount(Account account) throws SQLException {
    	sqlSession.insert("addAccount", account);
    }

    /** 
     * @see com.alipay.sofa.dtx.account.dao.AccountDAO#updateAmount(com.alipay.dtx.perf.tcc.domains.Account)
     */
    @Override
    public int updateAmount(Account account) throws SQLException {
    	return sqlSession.update("updateAmount", account);
    }

    /** 
     * @see com.alipay.sofa.dtx.account.dao.AccountDAO#getAccount(java.lang.String)
     */
    @Override
    public Account getAccount(String accountNo) throws SQLException {
        return (Account) sqlSession.selectOne("getAccount", accountNo);
    }
    
    @Override
    public  Account getAccountForUpdate(String accountNo) throws SQLException{
    	return (Account) sqlSession.selectOne("getAccountForUpdate", accountNo);
    }

    /** 
     * @see com.alipay.sofa.dtx.account.dao.AccountDAO#updateFreezedAmount(com.alipay.dtx.perf.tcc.domains.Account)
     */
    @Override
    public int updateFreezedAmount(Account account) throws SQLException {
    	return sqlSession.update("updateFreezedAmount", account);
    }
    
    @Override
    public void deleteAllAccount() throws SQLException{
    	sqlSession.update("deleteAccount");
    }

}
