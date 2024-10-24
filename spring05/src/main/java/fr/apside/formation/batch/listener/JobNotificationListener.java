package fr.apside.formation.batch.listener;

import fr.apside.formation.batch.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    public JobNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB TERMINE! VERIFIONS LES RESULTATS !!!");
            jdbcTemplate.query("SELECT first_name, last_name FROM users",
                    (rs, row) -> new User(rs.getString(1), rs.getString(2))
            ).forEach(user -> log.info("Utilisateur <" + user + "> trouvé dans la bdd."));

            if (jdbcTemplate.query("SELECT error_message FROM error",
                    (rs, row) -> new Error(rs.getString(1))
            ).stream().findAny().isPresent()) {
                throw new RuntimeException("La table error devrait être vide.");
            }
        }
    }
}
