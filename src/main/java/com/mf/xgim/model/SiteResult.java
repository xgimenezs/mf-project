package com.mf.xgim.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 *
 * @author xgimenez
 * @see com.mf.xgim.model
 * @since 1.0.0
 */
@Document(collection = "SiteResult")
public class SiteResult extends Site {

    @Indexed
    @Field("MARFEELIZABLE")
    private TriStateOperationResult marfeelizable;

    @Field("ERROR")
    private String errorMessage;

    @Field("LAST_MODIFIED")
    private Date lastMofidified;

    // SpringData constructor
    SiteResult() {
        // DO NOTHING
    }

    public SiteResult(Site site) {
        super(site);
    }

    public TriStateOperationResult getMarfeelizable() {
        return marfeelizable;
    }

    public void setMarfeelizable(TriStateOperationResult marfeelizable) {
        this.marfeelizable = marfeelizable;
    }

    public Date getLastMofidified() {
        return lastMofidified;
    }

    public void setLastMofidified(Date lastMofidified) {
        this.lastMofidified = lastMofidified;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
