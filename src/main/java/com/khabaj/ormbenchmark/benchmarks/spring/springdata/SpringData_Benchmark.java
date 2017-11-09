package com.khabaj.ormbenchmark.benchmarks.spring.springdata;

import com.khabaj.ormbenchmark.benchmarks.BaseBenchmark;
import com.khabaj.ormbenchmark.benchmarks.configuration.SpringDataConfiguration;
import com.khabaj.ormbenchmark.benchmarks.entities.Phone;
import com.khabaj.ormbenchmark.benchmarks.entities.User;
import com.khabaj.ormbenchmark.benchmarks.spring.springdata.repositories.UserRepository;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class SpringData_Benchmark extends BaseBenchmark {
    protected ConfigurableApplicationContext applicationContext;
    protected UserRepository userRepository;

    @Setup
    public void setUp() {
        try {
            this.applicationContext = new AnnotationConfigApplicationContext(SpringDataConfiguration.class);
            this.userRepository = applicationContext.getBean(UserRepository.class);

        } catch (Exception e) {
            if (applicationContext != null) {
                applicationContext.close();
            }
            System.out.println(e.getMessage());
        }
    }

    @TearDown
    public void clear() {
        applicationContext.close();
    }

    protected void batchInsertUsers(int rowsToInsert) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= rowsToInsert; i++) {
            if (i % BATCH_SIZE == 0) {
                userRepository.save(users);
                users.clear();
            }
            users.add(new User("FirstName" + i, "LastName" + i));
        }
        userRepository.save(users);
    }

    protected void batchInsertUsersWithPhones(int rowsToInsert) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= rowsToInsert; i++) {
            if (i % BATCH_SIZE == 0) {
                userRepository.save(users);
                users.clear();
            }
            User user = new User("FirstName" + i, "LastName" + i);
            assignPhones(user, String.valueOf(i));
            users.add(user);
        }
        userRepository.save(users);
    }

    private void assignPhones(User user, String beginPhoneString) {
        Set<Phone> phones = new HashSet<>();

        for (int i=1; i <= 3; i++) {
            Phone phone = new Phone();
            phone.generatePhoneNumber(beginPhoneString, String.valueOf(i));
            phone.setUser(user);
            phones.add(phone);
        }
        user.setPhones(phones);
    }
}
