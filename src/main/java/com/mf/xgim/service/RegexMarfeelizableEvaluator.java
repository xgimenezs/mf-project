package com.mf.xgim.service;

import org.jsoup.nodes.Document;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/**
 *
 * @author xgimenez
 * @see com.mf.xgim.service
 * @since 1.0.0
 */
public class RegexMarfeelizableEvaluator implements IMarfeelizableEvaluator {

    private final List<Pattern> patterns;

    public RegexMarfeelizableEvaluator(List<String> regex) {
        patterns = regex.stream().map(Pattern::compile).collect(toList());
    }

    @Override
    public boolean test(Document document) {
        if(document == null) {
            throw new IllegalArgumentException("Document must not be null.");
        }

        String title = document.title();
        return title != null && patterns.stream().anyMatch(x -> x.matcher(title).matches());

    }
}
