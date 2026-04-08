package ca.bc.gov.farms.data.entities;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String description;
    private Integer displayOrder;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
}
