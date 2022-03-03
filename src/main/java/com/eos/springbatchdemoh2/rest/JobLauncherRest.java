/****
 * Controlador para llamar al Batch e iniciar la ejecución
 * Retorna el id del Job
 */

package com.eos.springbatchdemoh2.rest;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("jobs")
public class JobLauncherRest {

  @Autowired
  JobLauncher jobLoteMovimientosLauncher;
  
  @Autowired
  Job procesarLoteMovimientosJob;

  /***
   * Crea un jobExcecution para iniciar la ejecución de este
   * Usa la clase JobLauncer, llama al metodo run y le envía como parametros
   * un job de la clase Job entre otros
   * @return Long
   * @throws Exception
   */
  @PostMapping("/run")
  public Long runJob() throws Exception {

    JobExecution jobExecution = jobLoteMovimientosLauncher
        .run(procesarLoteMovimientosJob, new JobParametersBuilder()
            .addLong("idInicio", System.nanoTime())
            .toJobParameters());
    return jobExecution.getId();

  }
  
}
