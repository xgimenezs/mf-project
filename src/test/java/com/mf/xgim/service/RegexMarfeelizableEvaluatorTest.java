package com.mf.xgim.service;

import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author xgimenez
 * @see com.mf.xgim.service
 * @since 1.0.0
 */
public class RegexMarfeelizableEvaluatorTest {

    private final List<String> REGEX_LIST = Arrays.asList(new String[] {
            ".*(?i)\\bnoticias\\b.*",
            ".*(?i)\\bnews\\b.*"
    });

    private final RegexMarfeelizableEvaluator defaultEvaluator = new RegexMarfeelizableEvaluator(REGEX_LIST);

    @Test(expected = IllegalArgumentException.class)
    public void testNull() {
        defaultEvaluator.test(null);
    }

    @Test
    public void testNullOrEmptyTitle() {
        Document document = mock(Document.class);
        when(document.title()).thenReturn(null);
        assertFalse(defaultEvaluator.test(document));
        when(document.title()).thenReturn("");
        assertFalse(defaultEvaluator.test(document));
    }

    @Test
    public void testNotMatch() {
        Document document = mock(Document.class);
        when(document.title()).thenReturn("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi malesuada.");
        assertFalse(defaultEvaluator.test(document));
    }

    @Test
    public void testMatchCaseSensitive() {
        Document document = mock(Document.class);
        when(document.title()).thenReturn("news Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi malesuada.");
        assertTrue(defaultEvaluator.test(document));
        when(document.title()).thenReturn("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi malesuada.news");
        assertTrue(defaultEvaluator.test(document));
        when(document.title()).thenReturn("Lorem ipsum dolor sit amet, news consectetur adipiscing elit. Morbi malesuada.");
        assertTrue(defaultEvaluator.test(document));
    }

    @Test
    public void testMatchCaseInSensitive() {
        Document document = mock(Document.class);
        when(document.title()).thenReturn("neWs Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi malesuada.");
        assertTrue(defaultEvaluator.test(document));
        when(document.title()).thenReturn("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi malesuada.News");
        assertTrue(defaultEvaluator.test(document));
        when(document.title()).thenReturn("Lorem ipsum dolor sit amet, NEWS consectetur adipiscing elit. Morbi malesuada.");
        assertTrue(defaultEvaluator.test(document));
    }

    @Test
    public void testLaVanguardia() {
        Document document = mock(Document.class);
        when(document.title()).thenReturn("Últimas noticias, actualidad y última hora en Catalunya, España y el mundo");
        assertTrue(defaultEvaluator.test(document));
    }
    @Test
    public void testNotMatchContainedWord() {
        Document document = mock(Document.class);
        when(document.title()).thenReturn("Loremnews ipsum dolor sit amet, consectetur adipiscing elit. Morbi malesuada.");
        assertFalse(defaultEvaluator.test(document));
    }
}