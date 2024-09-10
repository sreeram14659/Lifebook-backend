package com.lifebook.Lifebook.model.requests;

import com.lifebook.Lifebook.model.Data;
import com.lifebook.Lifebook.model.PrivacyLevel;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@lombok.Data
@SuperBuilder
@Jacksonized
public class CreateOrUpdateMemoryRequest extends GenericRequest {
    private String id;
    private String name;
    private Instant creationDate;
    private PrivacyLevel privacy;
    private boolean active;
    private List<Data> dataList;
    private List<String> tags;
    private List<String> sharedWith;

    private int priority;


}

