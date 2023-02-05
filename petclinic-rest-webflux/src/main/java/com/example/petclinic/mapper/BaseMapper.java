package com.example.petclinic.mapper;

/**
 * @author heedong.kang
 */
public interface BaseMapper<M, D> {

  M toModel(D data);

  D toData(M model);

}
