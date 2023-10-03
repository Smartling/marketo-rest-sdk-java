package com.smartling.marketo.sdk.domain.campaign;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class TriggerCampaignRequest {

    private LeadId[] leads;
    private Token[] tokens;

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class LeadId {

        private int id;
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class Token {

        private String name;
        private String value;
    }
}
