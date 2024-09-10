package com.lifebook.Lifebook.model.types;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.lifebook.Lifebook.model.UnifiedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
@JsonTypeName("Memory")
public class Memory extends UnifiedEntity {
}
