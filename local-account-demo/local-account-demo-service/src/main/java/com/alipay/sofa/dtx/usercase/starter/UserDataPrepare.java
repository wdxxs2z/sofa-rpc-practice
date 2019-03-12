package com.alipay.sofa.dtx.usercase.starter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;

import com.alipay.dtx.parser.meta.DtxMetaDataParserImpl;
import com.alipay.dtx.parser.meta.domain.ColumnMetaData;
import com.alipay.dtx.parser.meta.domain.ColumnType;
import com.alipay.dtx.parser.meta.domain.IndexMetaData;
import com.alipay.dtx.parser.meta.domain.IndexType;
import com.alipay.dtx.parser.meta.domain.KeyMetaData;
import com.alipay.dtx.parser.meta.domain.TableMetaData;
import com.alipay.sofa.dtx.account.dao.AccountDAO;
import com.alipay.sofa.dtx.account.dao.AccountPointDAO;
import com.alipay.sofa.dtx.account.dao.AccountTransactionDAO;
import com.alipay.sofa.dtx.account.domain.Account;

public class UserDataPrepare implements InitializingBean {
	
	private DataSource fistDataSource;
	
	private DataSource secondDataSource;
	
	 //账户dao
    private AccountDAO            firstAccountDAO;
    
    private AccountDAO            secondAccountDAO;
    
    private AccountTransactionDAO firstAccountTransactionDAO;
    
    private AccountTransactionDAO secondAccountTransactionDAO;

    private AccountPointDAO firstAccountPointDAO;
    
    private AccountPointDAO secondAccountPointDAO;
    
    String userDir = System.getProperty("user.home");
    
	@Override
	public void afterPropertiesSet() throws Exception {
		{
			prepareDBTables(fistDataSource);
			prepreMetaDataForH2("first_dtx_ds");
			firstAccountDAO.deleteAllAccount();
			firstAccountTransactionDAO.deleteAllTransaction();
			firstAccountPointDAO.deleteAllAccountPoint();
			int num =  10;
			int account = 100;
			for(int i = 0; i < num; i ++){
				String accountNo = "A" + alignRight(i+"", 2, "0");
				Account account_ = new Account();
				account_.setAccountNo(accountNo);
				account_.setFreezedAmount(0);
				account_.setAmount(account);
				firstAccountDAO.addAccount(account_);
			}
		}
		{
			prepareDBTables(secondDataSource);
			prepreMetaDataForH2("second_dtx_ds");
			secondAccountDAO.deleteAllAccount();
			secondAccountTransactionDAO.deleteAllTransaction();
			secondAccountPointDAO.deleteAllAccountPoint();
			int num =  10;
			int account = 100;
			for(int i = 0; i < num; i ++){
				String accountNo = "B" + alignRight(i+"", 2, "0");
				Account account_ = new Account();
				account_.setAccountNo(accountNo);
				account_.setFreezedAmount(0);
				account_.setAmount(account);
				secondAccountDAO.addAccount(account_);
			}
		}
	}
	
	/**
	 * H2 数据源不支持获取元数据，手动添加元数据
	 * @param dbId
	 */
	private void prepreMetaDataForH2(String dbId ) {
		{
			//account 表元数据
			TableMetaData tableMetaData = new TableMetaData();
			List<ColumnMetaData> columns = new ArrayList<ColumnMetaData>();
			ColumnMetaData c = new ColumnMetaData();
			c.setColumnName("account_no");
			c.setColumnType(ColumnType.VARCHAR);
			columns.add(c);
			ColumnMetaData c1 = new ColumnMetaData();
			c1.setColumnName("amount");
			c1.setColumnType(ColumnType.DOUBLE);
			columns.add(c1);
			ColumnMetaData c2 = new ColumnMetaData();
			c2.setColumnName("freezed_amount");
			c2.setColumnType(ColumnType.DOUBLE);
			columns.add(c2);
			tableMetaData.setColumns(columns);
			
			List<IndexMetaData> indexes =  new ArrayList<IndexMetaData>();
			IndexMetaData i = new IndexMetaData();
			i.setIndexName("PRIMARY");
			i.setIndexType(IndexType.PRIMARY);
			List<ColumnMetaData> columns2 = new ArrayList<ColumnMetaData>();
			columns2.add(c);
			i.setColumns(columns2);
			indexes.add(i);
			tableMetaData.setIndexes(indexes);
			
			KeyMetaData primaryKey = new KeyMetaData ();
			primaryKey.setColumns(columns2);
			primaryKey.setIndexType(IndexType.PRIMARY);
			tableMetaData.setPrimaryKey(primaryKey);
			
			DtxMetaDataParserImpl.getInstance().addMetaData(dbId, "account", tableMetaData);
			
		}
		{
			//account_point 表元数据
			TableMetaData tableMetaData = new TableMetaData();
			List<ColumnMetaData> columns = new ArrayList<ColumnMetaData>();
			ColumnMetaData c = new ColumnMetaData();
			c.setColumnName("account_no");
			c.setColumnType(ColumnType.VARCHAR);
			columns.add(c);
			ColumnMetaData c1 = new ColumnMetaData();
			c1.setColumnName("tx_id");
			c1.setColumnType(ColumnType.VARCHAR);
			columns.add(c1);
			ColumnMetaData c2 = new ColumnMetaData();
			c2.setColumnName("point");
			c2.setColumnType(ColumnType.DOUBLE);
			columns.add(c2);
			ColumnMetaData c3 = new ColumnMetaData();
			c3.setColumnName("status");
			c3.setColumnType(ColumnType.INTEGER);
			columns.add(c3);
			tableMetaData.setColumns(columns);
			
			List<IndexMetaData> indexes =  new ArrayList<IndexMetaData>();
			IndexMetaData i = new IndexMetaData();
			i.setIndexName("PRIMARY");
			i.setIndexType(IndexType.PRIMARY);
			List<ColumnMetaData> columns2 = new ArrayList<ColumnMetaData>();
			columns2.add(c1);
			i.setColumns(columns2);
			indexes.add(i);
			tableMetaData.setIndexes(indexes);
			
			KeyMetaData primaryKey = new KeyMetaData ();
			primaryKey.setColumns(columns2);
			primaryKey.setIndexType(IndexType.PRIMARY);
			tableMetaData.setPrimaryKey(primaryKey);
			
			DtxMetaDataParserImpl.getInstance().addMetaData(dbId, "account_point", tableMetaData);
		}
		{
			//account_transaction 表元数据
			TableMetaData tableMetaData = new TableMetaData();
			List<ColumnMetaData> columns = new ArrayList<ColumnMetaData>();
			ColumnMetaData c = new ColumnMetaData();
			c.setColumnName("account_no");
			c.setColumnType(ColumnType.VARCHAR);
			columns.add(c);
			ColumnMetaData c1 = new ColumnMetaData();
			c1.setColumnName("tx_id");
			c1.setColumnType(ColumnType.VARCHAR);
			columns.add(c1);
			ColumnMetaData c2 = new ColumnMetaData();
			c2.setColumnName("amount");
			c2.setColumnType(ColumnType.DOUBLE);
			columns.add(c2);
			ColumnMetaData c3 = new ColumnMetaData();
			c3.setColumnName("type");
			c3.setColumnType(ColumnType.VARCHAR);
			columns.add(c3);
			tableMetaData.setColumns(columns);
			
			List<IndexMetaData> indexes =  new ArrayList<IndexMetaData>();
			IndexMetaData i = new IndexMetaData();
			i.setIndexName("PRIMARY");
			i.setIndexType(IndexType.PRIMARY);
			List<ColumnMetaData> columns2 = new ArrayList<ColumnMetaData>();
			columns2.add(c1);
			i.setColumns(columns2);
			indexes.add(i);
			tableMetaData.setIndexes(indexes);
			
			KeyMetaData primaryKey = new KeyMetaData ();
			primaryKey.setColumns(columns2);
			primaryKey.setIndexType(IndexType.PRIMARY);
			tableMetaData.setPrimaryKey(primaryKey);
			
			DtxMetaDataParserImpl.getInstance().addMetaData(dbId, "account_transaction", tableMetaData);
		}
	}
	
	private void prepareDBTables(DataSource theDataSource) throws SQLException {
		Connection conn = theDataSource.getConnection();
		
		try{
			Statement st = conn.createStatement();
			try {
                st.execute("drop table account");
            } catch (Exception e) {
            }
			st.execute("create table account( account_no varchar(64) not null , amount DOUBLE , freezed_amount DOUBLE , primary key (account_no))");
			try {
                st.execute("drop table account_point");
            } catch (Exception e) {
            }
			st.execute("create table account_point( tx_id varchar(128) not null,    account_no varchar(256) not null ,    point DOUBLE ,    status int,    primary key (tx_id))");
			try {
                st.execute("drop table account_transaction");
            } catch (Exception e) {
            }
			st.execute("create table account_transaction(tx_id varchar(128) not null,    account_no varchar(256) not null,    amount DOUBLE,    type varchar(256) not null,    primary key (tx_id))");
			try {
                st.execute("drop table dtx_branch_info");
            } catch (Exception e) {
            }
			st.execute("create table dtx_branch_info (action_id varchar(128) NOT NULL ,   tx_id varchar(128)  NOT NULL  ,  status varchar(4)  ,  log_info blob DEFAULT NULL ,  biz_type varchar(32) DEFAULT NULL ,  instance_id varchar(32) NOT NULL ,  context varchar(2000) DEFAULT NULL , feature varchar(2000) DEFAULT NULL ,gmt_create TIMESTAMP(6) NOT NULL ,gmt_modified TIMESTAMP(6) NOT NULL , PRIMARY KEY (action_id)) ");
			try {
                st.execute("drop table dtx_row_lock");
            } catch (Exception e) {
            }
			st.execute("create table dtx_row_lock (action_id varchar(128) NOT NULL ,  tx_id varchar(128) NOT NULL , table_name varchar(64) DEFAULT NULL ,   row_key varchar(250) NOT NULL ,  instance_id varchar(32) NOT NULL ,  context varchar(2000) DEFAULT NULL , feature varchar(2000) DEFAULT NULL ,  gmt_create TIMESTAMP(6) NOT NULL ,   gmt_modified TIMESTAMP(6) NOT NULL, PRIMARY KEY (row_key)) ");
			try {
                st.execute("drop table dtx_tcc_action");
            } catch (Exception e) {
            }
			st.execute("create table dtx_tcc_action ( action_id varchar(96) NOT NULL , action_name varchar(64) DEFAULT NULL , tx_id varchar(128) NOT NULL , action_group varchar(32) DEFAULT NULL , status varchar(10) DEFAULT NULL , param_data varchar(4000) DEFAULT NULL ,  gmt_create TIMESTAMP(6) NOT NULL , gmt_modified TIMESTAMP(6) NOT NULL ,  sharding_key varchar(128) DEFAULT NULL,  PRIMARY KEY (action_id) ,  UNIQUE KEY  idx_tx_id (tx_id, action_name))");
		
		}finally{
			conn.close();
		}
		
		
	}


	public static String readFileByLine(String filename) {
		File file=new File(filename);
		BufferedReader reader=null;
		try {
			reader=new BufferedReader(new FileReader(file));
			String temp=null;
			temp=reader.readLine();
			reader.close();
			return temp;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	public void setFirstAccountDAO(AccountDAO firstAccountDAO) {
		this.firstAccountDAO = firstAccountDAO;
	}

	public void setSecondAccountDAO(AccountDAO secondAccountDAO) {
		this.secondAccountDAO = secondAccountDAO;
	}
	
	public void setFirstAccountTransactionDAO( AccountTransactionDAO firstAccountTransactionDAO) {
		this.firstAccountTransactionDAO = firstAccountTransactionDAO;
	}

	public void setSecondAccountTransactionDAO( AccountTransactionDAO secondAccountTransactionDAO) {
		this.secondAccountTransactionDAO = secondAccountTransactionDAO;
	}

	public void setFirstAccountPointDAO(AccountPointDAO firstAccountPointDAO) {
		this.firstAccountPointDAO = firstAccountPointDAO;
	}

	public void setSecondAccountPointDAO(AccountPointDAO secondAccountPointDAO) {
		this.secondAccountPointDAO = secondAccountPointDAO;
	}
	
	public void setFistDataSource(DataSource fistDataSource) {
		this.fistDataSource = fistDataSource;
	}

	public void setSecondDataSource(DataSource secondDataSource) {
		this.secondDataSource = secondDataSource;
	}

	public void setUserDir(String userDir) {
		this.userDir = userDir;
	}


	public static String alignRight(String str, int size, String padStr) {
	        if (str == null) {
	            return null;
	        }

	        if ((padStr == null) || (padStr.length() == 0)) {
	            padStr = " ";
	        }

	        int padLen = padStr.length();
	        int strLen = str.length();
	        int pads = size - strLen;

	        if (pads <= 0) {
	            return str;
	        }

	        if (pads == padLen) {
	            return padStr.concat(str);
	        } else if (pads < padLen) {
	            return padStr.substring(0, pads).concat(str);
	        } else {
	            char[] padding = new char[pads];
	            char[] padChars = padStr.toCharArray();

	            for (int i = 0; i < pads; i++) {
	                padding[i] = padChars[i % padLen];
	            }

	            return new String(padding).concat(str);
	        }
	    }
	
}
