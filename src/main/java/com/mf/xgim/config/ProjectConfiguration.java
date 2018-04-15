package com.mf.xgim.config;

import com.mf.xgim.repository.ISiteResultRepository;
import com.mf.xgim.service.CrawlService;
import com.mf.xgim.service.IMarfeelizableEvaluator;
import com.mf.xgim.service.RegexMarfeelizableEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

/**
 *
 * @author xgimenez
 * @see com.mf.xgim.config
 * @since 1.0.0
 */
@Configuration
@PropertySource(value = "classpath:project.properties", ignoreResourceNotFound = true)
public class ProjectConfiguration {

    @Value("#{'${marfelizable.regex:.*(?i)\\bnoticias\\b.*,.*(?i)\\bnews\\b.*}'.split(',')}")
    private List<String> marfelizableRegex;

    @Value("${max.crawl.threads:500}")
    private int maxCrawlThreads;

    @Autowired
    private ISiteResultRepository resultRepository;

    @Bean
    public AsyncClientHttpRequestFactory asyncClientHttpRequestFactory() {
        // The only one impelementation I can choose. Not other frameworks alloweds
        // I prefer Netty4ClientHttpRequestFactory or HttpComponentsAsyncClientHttpRequestFactory
        // but it's imposible without include other dependencies.
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setTaskExecutor(taskExecutor());
        return factory;
    }

    @Bean
    public CrawlService crawlService() {
        return new CrawlService(iMarfeelizableEvaluator(), asyncClientHttpRequestFactory(), resultRepository);
    }

    @Bean
    public IMarfeelizableEvaluator iMarfeelizableEvaluator() {
        return new RegexMarfeelizableEvaluator(marfelizableRegex);
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(maxCrawlThreads);
        executor.setMaxPoolSize(maxCrawlThreads);
        return executor;
    }
}
