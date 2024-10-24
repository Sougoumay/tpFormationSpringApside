package fr.apside.formation.batch.config;

import fr.apside.formation.batch.DemoBatchApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@ContextConfiguration(classes = {DemoBatchApplication.class})
class BatchConfigTest {
    private final String SOURCE_FILE = "users.csv";
    private final String ERROR_FILE = "errors.csv";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @AfterEach
    public void clean() throws IOException {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void importUserJobTest() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("error_file", ERROR_FILE)
                .addString("source_file", SOURCE_FILE)
                .toJobParameters();
        jobLauncherTestUtils.launchJob(jobParameters);

    }
}
