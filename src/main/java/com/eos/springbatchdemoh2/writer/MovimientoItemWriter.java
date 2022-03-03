package com.eos.springbatchdemoh2.writer;

import com.eos.springbatchdemoh2.model.MovimientoBanco;
import com.eos.springbatchdemoh2.model.MovimientoItemOut;
import com.eos.springbatchdemoh2.repository.MovimientoBancoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovimientoItemWriter implements ItemWriter<MovimientoItemOut> {

  private static final Logger LOGGER = LoggerFactory.getLogger(MovimientoItemWriter.class);
  
  @Autowired
  MovimientoBancoRepository movimientoTempRepository;

  @Override
  public void write(List<? extends MovimientoItemOut> items) throws Exception {
    LOGGER.info("Escribiendo los movimientos {}", items);
    for(MovimientoItemOut item: items) {
      MovimientoBanco mt = MovimientoBanco
          .builder()
          .nroOperacion(item.getNroOperacion())
          .importe(item.getImporte())
          .fechaProceso(item.getFechaProceso())
          .build();
      movimientoTempRepository.save(mt);
    }
  }

}
