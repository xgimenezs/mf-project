package com.mf.xgim;

import com.mf.xgim.model.Site;

/**
 *
 * @author xgimenez
 * @see com.mf.xgim
 * @since 1.0.0
 */
public class CrawlException extends Exception {

    private final Site site;

    public CrawlException(Site site, Throwable t) {
        super("Error starting to crawl following site: " + site.toString(), t);
        this.site = site;
    }

    public Site getSite() {
        return site;
    }
}
