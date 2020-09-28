package com.pccw.backend.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelAndValue implements GeneralBean {
    private Object Value;
    private Object Label;
    private Object Other;
    private Object Flag;
}
