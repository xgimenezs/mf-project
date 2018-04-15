package com.mf.xgim.config;

import com.mongodb.MongoClient;
import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;

/**
 *
 * @author xgimenez
 * @see com.mf.xgim.config
 * @since 1.0.0
 */
@Configuration
@EnableMongoRepositories(value = "com.mf.xgim.repository")
public class EmbeddedMongoDBConfiguration {

    private static final String MONGO_DB_URL = "localhost";
    private static final int MONGO_DB_PORT = 37700;
    private static final String MONGO_DB_NAME = "mfdb";

    @Bean
    public MongoTemplate mongoTemplate() throws IOException {
        EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
        mongo.setBindIp(MONGO_DB_URL);
        mongo.setPort(MONGO_DB_PORT);
        MongoClient mongoClient = mongo.getObject();
        return new MongoTemplate(mongoClient, MONGO_DB_NAME);
    }
}
