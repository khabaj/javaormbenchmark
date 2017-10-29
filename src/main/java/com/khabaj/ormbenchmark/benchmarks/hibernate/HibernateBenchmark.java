package com.khabaj.ormbenchmark.benchmarks.hibernate;

import com.khabaj.ormbenchmark.benchmarks.BaseBenchmark;
import com.khabaj.ormbenchmark.benchmarks.configuration.HibernateSpringConfiguration;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class HibernateBenchmark extends BaseBenchmark {

    protected ConfigurableApplicationContext applicationContext;
    protected Session session;

    @Setup
    public void setUp() {
        try {
            this.applicationContext = new AnnotationConfigApplicationContext(HibernateSpringConfiguration.class);
            this.session = applicationContext.getBean(SessionFactory.class).openSession();
        } catch (Exception e) {
            if (applicationContext != null) {
                applicationContext.close();
            }
            System.out.println(e.getMessage());
        }
    }

    @TearDown
    public void closeApplicationContext() {
        session.close();
        applicationContext.close();
    }



    protected void performBatchInsert(int rowsNumber, int batchSize) {
        for (int i = 0; i<rowsNumber; i++) {

            if ( i > 0 && i % batchSize == 0 ) {
                session.flush();
                session.clear();

                session.getTransaction().commit();
                session.getTransaction().begin();
            }
            session.persist(new User("John" + i, "LastName" + i));
        }
    }
}
