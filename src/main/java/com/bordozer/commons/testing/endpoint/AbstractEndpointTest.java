package com.bordozer.commons.testing.endpoint;

import com.bordozer.commons.testing.endpoint.ResponseHttpHeader.HeaderPresence;
import com.bordozer.commons.utils.FileUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.util.MimeType;

import javax.annotation.CheckForNull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public abstract class AbstractEndpointTest {

    private static final ResultMatcher ANY_MATCH = result -> {
    };

    @Autowired
    private MockMvc mockMvc;

    protected void getTo(final EndpointTestBuilder builder) {
        requestTo(builder, get(builder.getUrl()));
    }

    protected void postTo(final EndpointTestBuilder builder) {
        requestTo(builder, post(builder.getUrl()));
    }

    @SneakyThrows
    protected void uploadFile(final EndpointTestBuilder builder, final MimeType fileContentType, final String fileName,
                              final String pathToFile) {

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart(builder.getUrl())
                .file(getMultipartFile(fileContentType.getType(), fileName, pathToFile));
        requestTo(builder, requestBuilder);
    }

    @SneakyThrows
    private void requestTo(final EndpointTestBuilder endpoint, final MockHttpServletRequestBuilder requestBuilder) {
        final EndpointTestRequest request = endpoint.whenRequest();

        final Map<String, String> httpParameters = request.getRequestHttpParameters();
        httpParameters.keySet()
                .forEach(param -> requestBuilder.param(param, httpParameters.get(param)));

        requestBuilder.contentType(request.getRequestContentType());
        requestBuilder.headers(request.getRequestHttpHeaders());

        if (request.getRequestJson() != null) {
            requestBuilder.content(request.getRequestJson());
        }

        final EndpointTestResponse expectedResponseresponse = request.getExpectedResponse();

        final ResultActions resultActions = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(getResponseHttpStatusMatcher(expectedResponseresponse.getResponseHttpStatus()))
                .andExpect(getResponseContentTypeMatcher(expectedResponseresponse.getResponseContentType()))
                .andExpect(getResponseBodyMatcher(expectedResponseresponse.getResponseBody(), expectedResponseresponse.getResponseMatchType()));


        final ResponseHttpHeaders responseHttpHeaders = expectedResponseresponse.getResponseHttpHeaders();
        responseHttpHeaders.getResponseHttpHeaders()
                .forEach(httpHeader -> assertHttpHeaderExists(resultActions, httpHeader));
    }

    @SneakyThrows
    private void assertHttpHeaderExists(final ResultActions resultActions, final ResponseHttpHeader httpHeader) {
        final String header = httpHeader.getHeader();
        final String value = httpHeader.getValue();
        final HeaderPresence headerPresence = httpHeader.getHeaderPresence();

        switch (headerPresence) {
            case EXISTS_WITH_PARTICULAR_VALUE:
                resultActions.andExpect(header().exists(header));
                resultActions.andExpect(header().string(header, equalTo(value)));
                return;
            case EXISTS_WITH_ANY_VALUE:
                resultActions.andExpect(header().exists(header));
                resultActions.andExpect(header().exists(header));
                return;
            case DOES_NOT_EXISTS:
                resultActions.andExpect(header().doesNotExist(header));
                return;
            default:
                throw new IllegalArgumentException(String.format("Header presence \"%s\" is not supported", headerPresence));
        }
    }

    private ResultMatcher getResponseHttpStatusMatcher(@CheckForNull final HttpStatus responseStatus) {
        return Optional.ofNullable(responseStatus)
                .map(httpStatus -> status().is(httpStatus.value()))
                .orElse(ANY_MATCH);
    }

    private ResultMatcher getResponseContentTypeMatcher(@CheckForNull final String contentType) {
        return Optional.ofNullable(contentType)
                .map(media -> content().contentType(media))
                .orElse(ANY_MATCH);
    }

    private ResultMatcher getResponseBodyMatcher(@CheckForNull final String responseBody, @CheckForNull final ResponseMatchType responseMatchType) {
        if (responseBody == null || responseMatchType == null) {
            return ANY_MATCH;
        }
        final ContentResultMatchers content = content();
        switch (responseMatchType) {
            case CONTAINS:
                return content.string(containsString(responseBody));
            case EQUALS_TO:
                return content.string(responseBody);
            case JSON:
                return content.json(responseBody, false);
            default:
                throw new IllegalArgumentException(String.format("Unsupported ResponseMatchType: '%s'", responseMatchType));
        }
    }

    @SneakyThrows
    private static MockMultipartFile getMultipartFile(final String fileContentType, final String fileName, final String pathToFile) {
        final String systemResource = FileUtils.readSystemResource(pathToFile);
        final InputStream contentStream = new ByteArrayInputStream(systemResource.getBytes());
        return new MockMultipartFile(fileName, fileName, fileContentType, contentStream);
    }
}
