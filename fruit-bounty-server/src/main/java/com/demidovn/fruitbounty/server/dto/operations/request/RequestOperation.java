package com.demidovn.fruitbounty.server.dto.operations.request;

import com.demidovn.fruitbounty.server.entities.Connection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestOperation {

  private OperationType type;
  private LinkedHashMap<String, String> data;
  private Connection connection;

}
