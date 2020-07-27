package com.ddingdongsogang.batchcrawling.job;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j  // log 사용을 위한 lombok annotation
@RequiredArgsConstructor    // 생성자 DI(Dependency Injection)를 위한 lombok annotation
@Configuration
public class SimpleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;    // 생성자 DI 받음 (Required Argument)
    private final StepBuilderFactory stepBuilderFactory;  // 생성자 DI 받음 (Required Argument

    // 하나의 job 안에는 다수의 step이 존재
    // 하나의 step 안에는 tasklet 또는 reader/processor/writer 묶음이 존

    @Bean
    public Job simpleJob(){
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1())
                .build();
    }

    @Bean
    public Step simpleStep1(){
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
