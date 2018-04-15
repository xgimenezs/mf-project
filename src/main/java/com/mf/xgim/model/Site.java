package com.mf.xgim.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @author xgimenez
 * @see com.mf.xgim
 * @since 1.0.0
 */
public class Site {

    @Id
    @Field("URL")
    private String url;

    @Field("RANK")
    private Long rank;

    public Site() {
    }

    public Site(Site site) {
        this(site.getUrl(), site.getRank());
    }

    public Site(String url, long rank) {
        this.url = url;
        this.rank = rank;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Site{" +
                "url='" + url + '\'' +
                ", rank=" + rank +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Site site = (Site) o;

        return url != null ? url.equals(site.url) : site.url == null;

    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
