package com.example.ChallanTrack.config;

import com.example.ChallanTrack.entity.ViolationType;
import com.example.ChallanTrack.repository.ViolationTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeData(
            ViolationTypeRepository repository) {

        return args -> {

            if (repository.count() == 0) {

                // Speeding
                ViolationType speeding =
                        new ViolationType();

                speeding.setViolationName(
                        "Speeding"
                );

                speeding.setBaseFine(
                        new BigDecimal("1000.00")
                );

                speeding.setEscalationPercent(
                        new BigDecimal("50.00")
                );

                repository.save(speeding);

                // Signal violation
                ViolationType signal =
                        new ViolationType();

                signal.setViolationName(
                        "Signal Violation"
                );

                signal.setBaseFine(
                        new BigDecimal("1500.00")
                );

                signal.setEscalationPercent(
                        new BigDecimal("50.00")
                );

                repository.save(signal);

                // Wrong parking
                ViolationType parking =
                        new ViolationType();

                parking.setViolationName(
                        "Wrong Parking"
                );

                parking.setBaseFine(
                        new BigDecimal("500.00")
                );

                parking.setEscalationPercent(
                        new BigDecimal("25.00")
                );

                repository.save(parking);

                // No helmet
                ViolationType helmet =
                        new ViolationType();

                helmet.setViolationName(
                        "No Helmet"
                );

                helmet.setBaseFine(
                        new BigDecimal("1000.00")
                );

                helmet.setEscalationPercent(
                        new BigDecimal("50.00")
                );

                repository.save(helmet);
            }
        };
    }
}
