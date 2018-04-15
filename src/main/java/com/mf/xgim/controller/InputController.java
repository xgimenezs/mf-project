package com.mf.xgim.controller;

import com.mf.xgim.MultipleCrawlException;
import com.mf.xgim.model.Site;
import com.mf.xgim.service.CrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author xgimenez
 * @see com.mf.xgim
 * @since 1.0.0
 */
@RestController
@RequestMapping("/input")
public class InputController {

    @Autowired
    private CrawlService crawlService;

    @RequestMapping("/test")
    public String test() {
        return "TEST OK";
    }

    @RequestMapping(value="file",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callable<String> invoke(@RequestBody List<Site> sites) {
        try {
            crawlService.crawl(sites);
        } catch (MultipleCrawlException e) {
            return e::toString;
        }
        return () -> "OK";
    }

}
