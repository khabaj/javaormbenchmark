package com.khabaj.ormbenchmark.benchmarks.jpa;


import com.khabaj.ormbenchmark.config.JpaSpringConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManagerFactory;

public class EclipseLinkJpaBenchmark extends JpaBenchmark {

    @Override
    public void setUp() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(JpaSpringConfiguration.class);
        this.entityManager = ((EntityManagerFactory) applicationContext.getBean("eclipseLinkEntityManagerFactory")).createEntityManager();
    }
}
