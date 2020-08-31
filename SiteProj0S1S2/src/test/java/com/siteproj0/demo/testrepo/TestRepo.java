package com.siteproj0.demo.testrepo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@DataJpaTest
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
})
@Sql("data.sql")
class TestRepo {

}
