package com.alipay.sofa.dtx.account.dao;

import java.sql.SQLException;

import org.mybatis.spring.SqlSessionTemplate;

import com.alipay.sofa.dtx.account.domain.AccountTransaction;


public class AccountTransactionDAOImpl implements AccountTransactionDAO {

	public SqlSessionTemplate sqlSession;  
	
	public void setSqlSession(SqlSessionTemplate sqlSession) {  
        this.sqlSession = sqlSession;  
	}
	
    /** 
     * @throws SQLException 
     * @see com.alipay.sofa.dtx.account.dao.AccountTransactionDAO#addTransaction(com.alipay.dtx.perf.tcc.domains.AccountTransaction)
     */
    @Override
    public void addTransaction(AccountTransaction accountTransaction) throws SQLException {
    	sqlSession.insert("addAccountTransaction", accountTransaction);
    }

    /** 
     * @see com.alipay.sofa.dtx.account.dao.AccountTransactionDAO#findTransaction(java.lang.String)
     */
    @Override
    public AccountTransaction findTransaction(String txId) throws SQLException{
        return (AccountTransaction) sqlSession.selectOne("getAccountTransaction", txId);
    }

    /** 
     * @throws SQLException 
     * @see com.alipay.sofa.dtx.account.dao.AccountTransactionDAO#deleteTransaction(java.lang.String)
     */
    @Override
    public void deleteTransaction(String txId) throws SQLException {
    	sqlSession.delete("deleteAccountTransaction", txId);
    }
    
    @Override
    public void deleteAllTransaction() throws SQLException{
    	sqlSession.delete("deleteAllTransaction");
    }


}
