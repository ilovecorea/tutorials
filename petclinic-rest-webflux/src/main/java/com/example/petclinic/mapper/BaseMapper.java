package com.example.petclinic.mapper;

/**
 * @author heedong.kang
 */
public interface BaseMapper<M, D> {

  M toModel(D dto);

  D toDto(M model);

}
