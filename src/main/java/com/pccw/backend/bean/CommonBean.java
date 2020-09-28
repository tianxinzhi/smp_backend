package com.pccw.backend.bean;
/**
 * KV
 */


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
// @Builder
 @NoArgsConstructor
@AllArgsConstructor
public class CommonBean extends BaseBean implements GeneralBean {
	private Long id;
    private Long pid;
    private String name;
}