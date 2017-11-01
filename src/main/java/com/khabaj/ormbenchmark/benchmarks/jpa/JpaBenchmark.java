package com.khabaj.ormbenchmark.benchmarks.jpa;

import com.khabaj.ormbenchmark.benchmarks.BaseBenchmark;
import com.khabaj.ormbenchmark.benchmarks.configuration.jpa.JpaSpringConfiguration;
import com.khabaj.ormbenchmark.benchmarks.configuration.jpa.JpaVendor;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

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

    @TearDown
    public void closeApplicationContext() {
        entityManager.close();
        applicationContext.close();
    }

    @Setup()
    public abstract void setUp();

    public void performBatchInsert(int rowsNumber) {
        entityManager.getTransaction().begin();
        for (int i = 0; i < rowsNumber; i++) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
                entityManager.getTransaction().commit();
                entityManager.getTransaction().begin();
            }
            entityManager.persist(new User("John" + i, "LastName" + i));
        }
        entityManager.getTransaction().commit();
    }

    protected List<User> getUsers() {
        List<User> users = entityManager.createQuery("select u from User as u").getResultList();
        return users;
    }
}
