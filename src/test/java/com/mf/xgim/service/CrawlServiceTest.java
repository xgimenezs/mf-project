package com.mf.xgim.service;

import com.mf.xgim.CrawlException;
import com.mf.xgim.MultipleCrawlException;
import com.mf.xgim.model.Site;
import com.mf.xgim.model.SiteResult;
import com.mf.xgim.model.TriStateOperationResult;
import com.mf.xgim.repository.ISiteResultRepository;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;
import org.springframework.http.client.AsyncClientHttpRequest;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 *
 * @author xgimenez
 * @see com.mf.xgim.service
 * @since 1.0.0
 */
public class CrawlServiceTest {


    @Test(expected = CrawlException.class)
    public void testWrongUri() throws CrawlException, InterruptedException {
        CrawlService service = new CrawlService(null, null, null);
        service.crawl(new Site("|", 0L));
    }

    @Test(expected = MultipleCrawlException.class)
    public void testWrongMultiple() throws MultipleCrawlException, InterruptedException {
        CrawlService service = new CrawlService(null, null, null);
        service.crawl(getSites("|", "#"));
    }

    @Test
    public void testOKSingleCrawl() throws Exception {
        IMarfeelizableEvaluator evaluator = mock(IMarfeelizableEvaluator.class);
        when(evaluator.test(any())).thenReturn(true);
        AsyncClientHttpRequestFactory factory = getMockAsyncClientHttpRequestFactory("ANY");
        ISiteResultRepository resultRepository = spy(ISiteResultRepository.class);
        ArgumentCaptor<SiteResult> argument = ArgumentCaptor.forClass(SiteResult.class);

        CrawlService service = new CrawlService(evaluator, factory, resultRepository);
        service.crawl(new Site("wwww.google.com", 1));

        verify(resultRepository).save(argument.capture());
        assertEquals("wwww.google.com", argument.getValue().getUrl());
        assertEquals(TriStateOperationResult.TRUE, argument.getValue().getMarfeelizable());
        assertNull(argument.getValue().getErrorMessage());
    }

    @Test
    public void testMultipleCrawl() throws Exception {
        IMarfeelizableEvaluator evaluator = mock(IMarfeelizableEvaluator.class);
        when(evaluator.test(any())).thenReturn(true);
        AsyncClientHttpRequestFactory factory = getMockAsyncClientHttpRequestFactory("ANY");
        ISiteResultRepository resultRepository = spy(ISiteResultRepository.class);
        CrawlService service = new CrawlService(evaluator, factory, resultRepository);

        List<Site> sites = getSites("any.com", "another.cat");
        service.crawl(sites);

        verify(resultRepository, times(sites.size())).save((SiteResult)any());
    }

    @Test
    public void testKOSingleCrawl() throws Exception {
        IMarfeelizableEvaluator evaluator = mock(IMarfeelizableEvaluator.class);
        when(evaluator.test(any())).thenReturn(true);
        AsyncClientHttpRequestFactory factory = getMockAsyncClientHttpRequestFactory(new RuntimeException("TestKO"));
        ISiteResultRepository resultRepository = spy(ISiteResultRepository.class);
        ArgumentCaptor<SiteResult> argument = ArgumentCaptor.forClass(SiteResult.class);

        CrawlService service = new CrawlService(evaluator, factory, resultRepository);
        service.crawl(new Site("wwww.google.com", 1));

        verify(resultRepository).save(argument.capture());
        assertEquals("wwww.google.com", argument.getValue().getUrl());
        assertEquals(TriStateOperationResult.ERROR, argument.getValue().getMarfeelizable());
        assertEquals("TestKO", argument.getValue().getErrorMessage());
    }

    /*
     *  HttpAsync mock methods. Too much code. Possibly it would be worth creating a service that abstracts us
     *  that would isolate us from this and be easier to mock.
     */

    private AsyncClientHttpRequestFactory getMockAsyncClientHttpRequestFactory(String title) {
        return getMockAsyncClientHttpRequestFactory(getOkAnswer(title));
    }

    private AsyncClientHttpRequestFactory getMockAsyncClientHttpRequestFactory(Throwable t) {
        return getMockAsyncClientHttpRequestFactory(getKOAnswer(t));
    }

    private AsyncClientHttpRequestFactory getMockAsyncClientHttpRequestFactory(ListenableFuture listenableFuture) {
        AsyncClientHttpRequestFactory factory = mock(AsyncClientHttpRequestFactory.class);
        AsyncClientHttpRequest request = mock(AsyncClientHttpRequest.class);
        try {
            when(factory.createAsyncRequest(any(), any())).thenReturn(request);
            when(request.executeAsync()).thenReturn(listenableFuture);
        } catch (IOException e) {
            // Mock doesnt't throw this exception
        }
        return factory;
    }

    private ListenableFuture<ClientHttpResponse> getKOAnswer(Throwable t) {
        ListenableFuture<ClientHttpResponse> listenableFuture = mock(ListenableFuture.class);
        ArgumentCaptor<ListenableFutureCallback> argument = ArgumentCaptor.forClass(ListenableFutureCallback.class);
        Answer answer = invocationOnMock -> {
            argument.getValue().onFailure(t);
            return null;
        };
        doAnswer(answer).when(listenableFuture).addCallback(argument.capture());
        return listenableFuture;
    }

    private ListenableFuture<ClientHttpResponse> getOkAnswer(String title) {
        ListenableFuture<ClientHttpResponse> listenableFuture = mock(ListenableFuture.class);
        ArgumentCaptor<ListenableFutureCallback> argument = ArgumentCaptor.forClass(ListenableFutureCallback.class);
        ClientHttpResponse response = mock(ClientHttpResponse.class);
        try {
            String html ="<html><head><title>" + title + "</title></head><body></body></html>";
            when(response.getBody()).thenReturn(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            // Mock doesnt't throw this exception
        }
        Answer answer = invocationOnMock -> {
            argument.getValue().onSuccess(response);
            return null;
        };
        doAnswer(answer).when(listenableFuture).addCallback(argument.capture());
        return listenableFuture;
    }

    private List<Site> getSites(String ... sites) {
        return Stream.of(sites).map(x -> new Site(x, 0L)).collect(toList());

    }

}