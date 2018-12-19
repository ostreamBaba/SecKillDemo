package com.viscu.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SeckillApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(SeckillApplication.class, args);
	}

	//既可以以war形式跑 也可以以jar形式跑
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SeckillApplication.class);
	}
}

