package mysite.config.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:mysite/config/app/jdbc.properties")
public class DBConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	public DataSource dataSource() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));
				
		return dataSource;
	}
	
	// 스프링부트에서는 TransactionManager 는 무조건 enable 상태이기에, 따로 설정할 필요 x 
	/*
	@Bean
	public TransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource); 
	}
	*/
}