package com.mf.xgim.service;

/**
 * (c) Agile-Content 2018
 *
 * @author xgimenez
 * @see com.mf.xgim.service
 * @since 1.0.0
 */

import com.mf.xgim.config.EmbeddedMongoDBConfiguration;
import com.mf.xgim.config.ProjectConfiguration;
import com.mf.xgim.model.Site;
import com.mf.xgim.model.SiteResult;
import com.mf.xgim.model.TriStateOperationResult;
import com.mf.xgim.repository.ISiteResultRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProjectConfiguration.class, CrawlService.class, EmbeddedMongoDBConfiguration.class})
public class CrawlServiceITTest {

    @Autowired
    private CrawlService service;

    @Autowired
    private ISiteResultRepository resultRepository;

    @Test
    public void testFalse() throws Exception {
        test("once.es", TriStateOperationResult.FALSE);
    }

    @Test
    public void testTrue() throws Exception {
        test("lavanguardia.com", TriStateOperationResult.TRUE);
    }

    @Test
    public void testError() throws Exception {
        test("noexisteestapagina.es", TriStateOperationResult.ERROR);
    }

    private void test(String url, TriStateOperationResult expected) throws Exception {
        service.crawl(new Site(url, 0L));
        Thread.sleep(5000L);
        SiteResult siteResult;

        int count = 0;
        while((siteResult = resultRepository.findOne(url)) == null) {
            Thread.sleep(1000);
            count ++;
            if(count > 10) {
                throw new RuntimeException("The test was not OK");
            }
        }

        assertNotNull(siteResult);
        assertEquals(expected, siteResult.getMarfeelizable());
    }
}