package com.qfieldcloud.core.batch;

import com.qfieldcloud.core.domain.model.Delta;
import com.qfieldcloud.core.domain.repository.DeltaRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Instant;
import java.util.Map;

/**
 * Spring Batch: bulk land-record (delta) processing. Pages through every delta
 * still PENDING in chunks of {@value #CHUNK_SIZE} and advances it to APPLIED,
 * instead of applying each survey delta one request at a time.
 */
@Configuration
public class DeltaBulkApplyBatchConfig {

    private static final int CHUNK_SIZE = 20;

    @Bean
    public RepositoryItemReader<Delta> pendingDeltaReader(DeltaRepository deltaRepository) {
        return new RepositoryItemReaderBuilder<Delta>()
                .name("pendingDeltaReader")
                .repository(deltaRepository)
                .methodName("findByLastStatus")
                .arguments(Delta.Status.PENDING.getValue())
                .sorts(Map.of("createdAt", Sort.Direction.ASC))
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    public ItemProcessor<Delta, Delta> deltaApplyProcessor() {
        return delta -> {
            delta.setLastStatus(Delta.Status.APPLIED.getValue());
            delta.setLastApplyAttemptAt(Instant.now());
            return delta;
        };
    }

    @Bean
    public RepositoryItemWriter<Delta> deltaWriter(DeltaRepository deltaRepository) {
        return new RepositoryItemWriterBuilder<Delta>()
                .repository(deltaRepository)
                .methodName("save")
                .build();
    }

    @Bean
    public Step applyPendingDeltasStep(JobRepository jobRepository,
                                        PlatformTransactionManager transactionManager,
                                        RepositoryItemReader<Delta> pendingDeltaReader,
                                        ItemProcessor<Delta, Delta> deltaApplyProcessor,
                                        RepositoryItemWriter<Delta> deltaWriter) {
        return new StepBuilder("applyPendingDeltasStep", jobRepository)
                .<Delta, Delta>chunk(CHUNK_SIZE, transactionManager)
                .reader(pendingDeltaReader)
                .processor(deltaApplyProcessor)
                .writer(deltaWriter)
                .build();
    }

    @Bean
    public Job deltaBulkApplyJob(JobRepository jobRepository, Step applyPendingDeltasStep) {
        return new JobBuilder("deltaBulkApplyJob", jobRepository)
                .start(applyPendingDeltasStep)
                .build();
    }
}
