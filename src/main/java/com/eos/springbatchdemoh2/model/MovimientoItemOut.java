package com.eos.springbatchdemoh2.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MovimientoItemOut {

  private String nroOperacion;
  
  private BigDecimal importe;
  
  private LocalDate fechaProceso;
}
