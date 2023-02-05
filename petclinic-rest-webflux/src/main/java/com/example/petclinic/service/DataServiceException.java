package com.example.petclinic.service;

import org.springframework.dao.DataAccessException;

/**
 * @author heedong.kang
 */
public class DataServiceException extends DataAccessException {

  public DataServiceException(String msg) {
    super(msg);
  }

  public DataServiceException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
