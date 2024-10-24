package fr.apside.formation.batch.config;

import fr.apside.formation.batch.domain.Error;
import fr.apside.formation.batch.domain.User;
import fr.apside.formation.batch.listener.JobNotificationListener;
import fr.apside.formation.batch.processor.UserProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.WritableResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    // Configuration importUserStep

    @Bean
    @StepScope
    public FlatFileItemReader<User> userReader(@Value("#{jobParameters[source_file]}") String source) {

        return new FlatFileItemReaderBuilder<User>()
                .name("userItemReader")
                .resource(new ClassPathResource(source))
                .linesToSkip(1)
                .delimited().delimiter(";")
                .names(new String[]{"first_name", "last_name"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
                    setTargetType(User.class);
                }})
                .build();

//        return null; // TODO new FlatFileItemReaderBuilder...;
    }

    @Bean
    public JdbcBatchItemWriter<User> userWriter(DataSource dataSource) {
        // INSERT INTO users (first_name, last_name) VALUES (:firstName, :lastName)
//        return null; // TODO new JdbcBatchItemWriterBuilder...;

        return new JdbcBatchItemWriterBuilder<User>()
                .sql("INSERT INTO users (first_name, last_name) VALUES (:firstName, :lastName)")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .dataSource(dataSource).build();
    }

    @Bean
    public Step userImportStep(JobRepository jobRepository,
                               FlatFileItemReader<User> userReader,
                               UserProcessor userProcessor,
                               JdbcBatchItemWriter<User> userWriter,
                               StepExecutionListener stepSkipListener,
                               PlatformTransactionManager tm,
                               JdbcTemplate jdbcTemplate) {

        // Faire un Step faultTolerant avec un chunk de 10 par exemple et une skipPolicy custom
//        return null; // TODO
        return new StepBuilder("userImportStep", jobRepository)
                .<User, User>chunk(10, tm)
                .reader(userReader)
                .processor(userProcessor)
                .writer(userWriter)
                .faultTolerant()
                .skipPolicy((Throwable throwable, long i) -> {
                    jdbcTemplate.execute("insert into error(error_message) values ('"+throwable.getMessage()+"')");
                    return true;
                })
                .listener(stepSkipListener)
                .build();
    }

    // Configuration exportErrorStep

    @Bean
    public JdbcCursorItemReader<Error> errorReader(DataSource dataSource) {
//        return null; // TODO
        return new JdbcCursorItemReaderBuilder<Error>()
                .name("errorReader")
                .dataSource(dataSource)
                .sql("select error_message from error")
                .rowMapper(new BeanPropertyRowMapper<>(Error.class))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Error> errorWriter(@Value("#{jobParameters[error_file]}") String dest) {
        // Le fichier errors.csv aura les mêmes colonnes que les champs de la classe Error
//        return null; // TODO
        return new FlatFileItemWriterBuilder<Error>()
                .name("errorWriter")
                .delimited().delimiter(";")
                .names("errorMessage")
                .resource(new PathResource(dest))
                .build();
    }

    @Bean
    public Step exportErrorStep(JobRepository jobRepository,
                                JdbcCursorItemReader<Error> errorReader,
                                FlatFileItemWriter<Error> errorWriter, PlatformTransactionManager tm) {
        // Faire un Step tout simple avec un reader et un writer
//        return null; // TODO
        return new StepBuilder("exportErrorStep", jobRepository)
                .<Error, Error>chunk(10, tm)
                .reader(errorReader)
                .writer(errorWriter)
                .build();
    }

    // Configuration flushErrorTableStep

    @Bean
    public Step flushErrorTableStep(JobRepository jobRepository,
                                    JdbcTemplate jdbcTemplate,
                                    PlatformTransactionManager tm) {
        // Faire un Step (tasklet) qui fera un DELETE FROM error et retournera un status fini
//        return null; // TODO
        return new StepBuilder("flushErrorTableStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    jdbcTemplate.execute("delete from error");
                    return RepeatStatus.FINISHED;
                }, tm).build();
    }

    // Configuration errorFlow

    @Bean
    public Flow errorFlow(Step exportErrorStep, Step flushErrorTableStep) {
        // Faire un flow qui va exécuter les steps d'export des erreurs et de vidage de la table des erreurs

//        return null; // TODO new FlowBuilder...;
        return new FlowBuilder<Flow>("errorFlow")
                .start(exportErrorStep)
                .next(flushErrorTableStep)
                .build();
    }

    // Configuration userJob

    @Bean
    public Job userJob(JobRepository jobRepository,
                       Step userImportStep,
                       Flow errorFlow,
                       JobNotificationListener listener) {
        // Faire le job avec une logique conditionnelle sur le flow de gestion des erreurs
//        return null; // TODO
        return new JobBuilder("userJob", jobRepository)
                .start(userImportStep)
                .on("FAILED")
                .to(errorFlow)
                .end()
                .listener(listener)
                .build();

    }
}
