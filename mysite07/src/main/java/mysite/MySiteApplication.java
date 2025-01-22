package mysite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@Import({AppConfig.class, WebConfig.class})
public class MySiteApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MySiteApplication.class, args);
	}

}