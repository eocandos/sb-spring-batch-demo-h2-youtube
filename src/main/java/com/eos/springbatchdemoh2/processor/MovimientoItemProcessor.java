package com.eos.springbatchdemoh2.processor;

import com.eos.springbatchdemoh2.model.MovimientoItem;
import com.eos.springbatchdemoh2.model.MovimientoItemOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;

public class MovimientoItemProcessor implements ItemProcessor<MovimientoItem, MovimientoItemOut>{

  
  private static final Logger LOGGER = LoggerFactory.getLogger(MovimientoItemProcessor.class);

  
  @Override
  public MovimientoItemOut process(MovimientoItem item) throws Exception {
    LOGGER.info("Procesando el movimiento {}", item);
    
    return MovimientoItemOut
        .builder()
        .nroOperacion(item.getNroOperacion())
        .importe(item.getImporte())
        .fechaProceso(LocalDate.now())
        .build();
  }

}
