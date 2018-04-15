package com.mf.xgim.service;

import com.mf.xgim.CrawlException;
import com.mf.xgim.MultipleCrawlException;
import com.mf.xgim.model.Site;
import com.mf.xgim.model.SiteResult;
import com.mf.xgim.model.TriStateOperationResult;
import com.mf.xgim.repository.ISiteResultRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.AsyncClientHttpRequest;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author xgimenez
 * @see com.mf.xgim
 * @since 1.0.0
 */
public class CrawlService {

    private static final String HTTTP_PREFFIX = "http://www.";

    private final IMarfeelizableEvaluator evaluator;

    private final AsyncClientHttpRequestFactory httpRequestFactory;

    private final ISiteResultRepository resultRepository;

    public CrawlService(IMarfeelizableEvaluator evaluator,
                        AsyncClientHttpRequestFactory httpRequestFactory,
                        ISiteResultRepository resultRepository) {
        this.evaluator = evaluator;
        this.httpRequestFactory = httpRequestFactory;
        this.resultRepository = resultRepository;
    }

    public void crawl(List<Site> sites) throws MultipleCrawlException {
        List<CrawlException> exceptions = null;
        for (Site site : sites) {
            try {
                crawl(site);
            } catch (CrawlException e) {
                if(exceptions == null) {
                    exceptions = new ArrayList<>();
                }
                exceptions.add(e);
            }
        }
        if(exceptions != null && !exceptions.isEmpty()) {
            throw new MultipleCrawlException(exceptions);
        }
    }

    public void crawl(Site site) throws CrawlException {
        try {
            URI uri = new URI(HTTTP_PREFFIX + site.getUrl()); // I believe that the protocol should come in the url
            AsyncClientHttpRequest request = httpRequestFactory.createAsyncRequest(uri, HttpMethod.GET);
            ListenableFuture<ClientHttpResponse> listenableFuture = request.executeAsync();
            listenableFuture.addCallback(new CrawlCallback(resultRepository, evaluator, site));
        } catch (Exception e) {
            throw new CrawlException(site, e);
        }
    }

    public static class CrawlCallback implements ListenableFutureCallback<ClientHttpResponse> {

        private final SiteResult result;

        private final IMarfeelizableEvaluator evaluator;

        private final ISiteResultRepository resultRepository;

        public CrawlCallback(ISiteResultRepository resultRepository, IMarfeelizableEvaluator evaluator, Site site) {
            result = new SiteResult(site);
            result.setLastMofidified(new Date());
            this.resultRepository = resultRepository;
            this.evaluator = evaluator;
        }

        @Override
        public void onFailure(Throwable throwable) {
            result.setMarfeelizable(TriStateOperationResult.ERROR);
            result.setErrorMessage(throwable.getMessage());
            resultRepository.save(result);
        }

        @Override
        public void onSuccess(ClientHttpResponse response) {
            try {
                Document document = Jsoup.parse(response.getBody(), StandardCharsets.UTF_8.name(), result.getUrl());
                boolean value = evaluator.test(document);
                result.setMarfeelizable(value ? TriStateOperationResult.TRUE : TriStateOperationResult.FALSE);
            } catch (Exception e) {
                onFailure(e);
                return;
            }
            resultRepository.save(result);
        }
    }
}
