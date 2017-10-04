package com.khabaj.ormbenchmark.benchmarks.jpa;

import com.khabaj.ormbenchmark.benchmarks.config.JpaSpringConfiguration;
import com.khabaj.ormbenchmark.benchmarks.config.JpaVendor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManagerFactory;

public class HibernateJpaBenchmark extends JpaBenchmark {

    @Override
    public void setUp() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(JpaSpringConfiguration.class);
        this.entityManager = applicationContext.getBean(EntityManagerFactory.class, JpaVendor.HIBERNATE).createEntityManager();
    }
}
