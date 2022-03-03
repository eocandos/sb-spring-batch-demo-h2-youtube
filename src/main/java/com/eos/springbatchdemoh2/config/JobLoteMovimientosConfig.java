package com.eos.springbatchdemoh2.config;

/****
 * Clase que contiene la logica en si con:
 * - El metodo que lanza la ejecución del job y los pasos que hagan parte del job
 * - reader
 * - processor
 * - writer
 * Retorna el id del Job
 */

import com.eos.springbatchdemoh2.listener.JobLoteMovimientosListener;
import com.eos.springbatchdemoh2.model.MovimientoItem;
import com.eos.springbatchdemoh2.model.MovimientoItemOut;
import com.eos.springbatchdemoh2.processor.MovimientoItemProcessor;
import com.eos.springbatchdemoh2.writer.MovimientoItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing
public class JobLoteMovimientosConfig {

  // Necesaria para crear los jobs
  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  // Necesaria para crear los steps
  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  // Necesaria para insertar en la BD
  @Autowired
  JobRepository jobRepository;

  // De dónde extraerá la información (Archivo plano en este caso)
  @Value("${file.input}")
  private String fileInput;

  /*** Método que llaman desde el controller en este caso (Inicia la ejecución del job)
       Crea la ejecución
   */
   @Bean
  public JobLauncher jobLoteMovimientosLauncher() throws Exception {
    SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
    jobLauncher.setJobRepository(jobRepository);
    jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
  }

  /** Es la TAREA
   * Usamos el jobBuilderFactory, le decimos que empiece con el paso stepOne y termine
   * (Tiene un solo paso)
   * @param listener
   * @param stepOne
   * @return
   */
  @Bean
  public Job procesarLoteMovimientosJob(JobLoteMovimientosListener listener, Step stepOne) {
      return jobBuilderFactory.get("procesarLoteMovimientosJob")
          .listener(listener)
          .flow(stepOne)
          .end()
          .build();
  }

  /***
   * El paso que le indicamos que ejecute en el metodo anterior
   * @return
   */
  @Bean
  public Step stepOne() {
      return stepBuilderFactory.get("stepOne")
          .<MovimientoItem, MovimientoItemOut> chunk(5) // procesa en bloques de 5
          .reader(reader())
          .processor(processor())
          .writer(writer())
          .build();
  }
  
  @Bean
  public FlatFileItemReader<MovimientoItem> reader() {
      return new FlatFileItemReaderBuilder<MovimientoItem>()
          .name("movimientoItemReader")
          .resource(new ClassPathResource(fileInput))
          .delimited()
          .names(new String[] { "nroOperacion", "importe"})
          .fieldSetMapper(new BeanWrapperFieldSetMapper<MovimientoItem>() {{
              setTargetType(MovimientoItem.class);
           }})
          .build();
  }
  
  
  @Bean
  public MovimientoItemProcessor processor() {
      return new MovimientoItemProcessor();
  }
  
  @Bean
  public MovimientoItemWriter writer() {
      return new MovimientoItemWriter();
  }
  
  
}
