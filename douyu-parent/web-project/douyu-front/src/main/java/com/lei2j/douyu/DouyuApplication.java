package com.lei2j.douyu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


/**
 * @author lei2j
 */
@SpringBootApplication
public class DouyuApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(DouyuApplication.class);

	private static final String FILE_PATH = "/opt/prod/front.properties";

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(DouyuApplication.class);
		springApplication.addInitializers((ctx)->{
			ConfigurableEnvironment environment = ctx.getEnvironment();
			try {
				DefaultPropertySourceFactory defaultPropertySourceFactory = new DefaultPropertySourceFactory();
				FileSystemResource fileResource = new FileSystemResource(FILE_PATH);
				if(!fileResource.exists()){
					LOGGER.warn("不存在此配置文件,location:{},将使用默认配置文件",FILE_PATH);
					return;
				}
				EncodedResource encodedResource = new EncodedResource(fileResource, Charset.forName("utf-8"));
				PropertySource<?> propertySource = defaultPropertySourceFactory.createPropertySource(null,
						encodedResource);
				environment.getPropertySources().addFirst(propertySource);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		springApplication.addListeners((listener)->
			LOGGER.info("Receive msg:{},timestamp:{}",listener.getSource(), LocalDateTime.ofInstant(Instant.ofEpochMilli(listener.getTimestamp()), ZoneId.of("+8")))
		);
		springApplication.run(args);
//		SpringApplication.run(DouyuApplication.class, args);
	}
}
