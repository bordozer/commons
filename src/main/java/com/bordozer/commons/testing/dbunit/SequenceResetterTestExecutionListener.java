package com.bordozer.commons.testing.dbunit;

import java.lang.reflect.Method;
import java.util.Set;

import javax.annotation.CheckForNull;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.Assert;

public class SequenceResetterTestExecutionListener extends AbstractTestExecutionListener {

    private static final String ALTER_SEQUENCE_QUERY = "ALTER SEQUENCE %s RESTART WITH %d";

    @CheckForNull
    private JdbcTemplate jdbcTemplate;

    @Override
    public void prepareTestInstance(final TestContext testContext) {
        jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
        Assert.notNull(jdbcTemplate, "no JdbcTemplate found");
    }

    @Override
    public void beforeTestExecution(final TestContext testContext) {
        final Method testMethod = testContext.getTestMethod();

        final Set<ResetSequence> resetSequenceAnnotations = AnnotationUtils.getDeclaredRepeatableAnnotations(testMethod, ResetSequence.class, ResetSequences.class);
        resetSequenceAnnotations.forEach(resetSequenceAnnotation -> resetSequence(resetSequenceAnnotation.name(), resetSequenceAnnotation.startWith()));
    }

    private void resetSequence(final String sequenceName, final int startWith) {
        Assert.notNull(jdbcTemplate, "TestInstance has to initialize jdbcTemplate");
        jdbcTemplate.execute(String.format(ALTER_SEQUENCE_QUERY, sequenceName, startWith));
    }
}
