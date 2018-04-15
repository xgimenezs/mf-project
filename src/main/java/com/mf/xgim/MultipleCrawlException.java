package com.mf.xgim;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author xgimenez
 * @see com.mf.xgim
 * @since 1.0.0
 */
public class MultipleCrawlException extends Exception {

    private final List<CrawlException> exceptions;

    public MultipleCrawlException(List<CrawlException> exceptions) {
        super("Errors launching crawling processes: "
                + exceptions.stream().map(x -> x.getMessage()).collect(Collectors.joining(",")));
        this.exceptions = exceptions;
    }

    public List<CrawlException> getExceptions() {
        return exceptions;
    }
}
