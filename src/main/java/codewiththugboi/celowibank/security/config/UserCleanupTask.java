package codewiththugboi.celowibank.security.config;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserCleanupTask {
    private final JdbcTemplate jdbcTemplate;

    @Scheduled(cron = "0 0 0 * * *") // Executes at midnight every day
    public void deleteNonConfirmedUsers() {
        String sql = "DELETE FROM users WHERE confirmed = false";
        jdbcTemplate.update(sql);
    }
}
