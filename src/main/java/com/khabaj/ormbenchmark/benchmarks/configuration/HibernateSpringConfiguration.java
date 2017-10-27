package com.khabaj.ormbenchmark.benchmarks.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DataSourceConfiguration.class)
public class HibernateSpringConfiguration {


}
