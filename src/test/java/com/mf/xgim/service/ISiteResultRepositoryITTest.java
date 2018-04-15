package com.mf.xgim.service;

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

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author xgimenez
 * @see com.mf.xgim.service
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProjectConfiguration.class, CrawlService.class, EmbeddedMongoDBConfiguration.class})
public class ISiteResultRepositoryITTest {

    @Autowired
    private ISiteResultRepository resultRepository;

    @Test
    public void testSaveAndUpdate() {
        SiteResult result = new SiteResult(new Site("sport.es", 0L));
        result.setMarfeelizable(TriStateOperationResult.TRUE);
        result = resultRepository.save(result);


        SiteResult result2 = resultRepository.findOne("sport.es");
        assertEquals(TriStateOperationResult.TRUE, result2.getMarfeelizable());
        assertFalse(result == result2);
        assertTrue(result.equals(result2));

        result.setMarfeelizable(TriStateOperationResult.FALSE);
        resultRepository.save(result);
        SiteResult result3 = resultRepository.findOne("sport.es");
        assertEquals(TriStateOperationResult.FALSE, result3.getMarfeelizable());

    }


}