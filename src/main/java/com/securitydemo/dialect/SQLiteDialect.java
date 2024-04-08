package com.securitydemo.dialect;

import org.springframework.data.relational.core.dialect.AbstractDialect;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.LimitClause;
import org.springframework.data.relational.core.dialect.LockClause;
import org.springframework.data.relational.core.sql.LockOptions;
import org.springframework.data.relational.core.sql.render.SelectRenderContext;

public class SQLiteDialect extends AbstractDialect {

	public static final SQLiteDialect INSTANCE = new SQLiteDialect();
	
	public static Dialect getDialect() {
		return INSTANCE;
	}
	
	private static final LockClause LOCK_CLAUSE = new LockClause() {
		
		@Override
		public String getLock(LockOptions lockOptions) {
			return "WITH LOCK";
		}
		
		@Override
		public Position getClausePosition() {
			return Position.AFTER_ORDER_BY;
		}
	};
	
	@Override
	public SelectRenderContext getSelectContext() {
		return super.getSelectContext();
	}

	@Override
	public LimitClause limit() {
		
		return new LimitClause() {

			@Override
			public String getLimit(long limit) {
				return "LIMIT " + limit;
			}

			@Override
			public String getOffset(long offset) {
				return "OFFSET " + offset;
			}

			@Override
			public String getLimitOffset(long limit, long offset) {
				return "LIMIT " + limit + " OFFSET " + offset;
			}

			@Override
			public Position getClausePosition() {
				return Position.AFTER_ORDER_BY;
			}
			
		};
		
	}

	@Override
	public LockClause lock() {
		return LOCK_CLAUSE;
	}
	
}