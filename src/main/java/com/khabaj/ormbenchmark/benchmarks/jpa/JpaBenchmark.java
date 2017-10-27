package com.khabaj.ormbenchmark.benchmarks.jpa;

import com.khabaj.ormbenchmark.benchmarks.BaseBenchmark;
import com.khabaj.ormbenchmark.benchmarks.configuration.jpa.JpaSpringConfiguration;
import com.khabaj.ormbenchmark.benchmarks.configuration.jpa.JpaVendor;
import org.openjdk.jmh.annotations.Setup;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class JpaBenchmark extends BaseBenchmark{

    protected ConfigurableApplicationContext applicationContext;
    protected EntityManager entityManager;

    public void init(JpaVendor jpaVendor) {
        try {
            this.applicationContext = new AnnotationConfigApplicationContext(JpaSpringConfiguration.class);
            this.entityManager = applicationContext.getBean(EntityManagerFactory.class, jpaVendor).createEntityManager();
        } catch (Exception e) {
            if (applicationContext != null) {
                applicationContext.close();
            }
            System.out.println(e.getMessage());
        }
    }

    @Setup()
    public abstract void setUp();
}
