# My commons utils and frameworks

The project contains common for all projects utils and frameworks

### Prerequisites


```
- Java 8 or later
```

### Installing

Add to gradle build script:

```
compile group: 'com.bordozer.commons', name: 'commons', version: '3.00'
```

On board:
===============

Annotation com.bordozer.commons.annotations.NotNullByDefault
---------------

Should be applied to whole package in file package-info.java and "mark" all variables as NotNull by default. The annotation is used by SpotBugs static code 
analysis tool and by IDE Idea.

package-info.java:
```
@NotNullByDefault
package com.bordozer.commons.measury.stopwatcher;

import com.bordozer.commons.annotations.NotNullByDefault;
```
---------------
Java8DateTimeConfiguration
---------------

Class-configuration being included to spring boot application configures data serialisation and deserialisation on a project level.
 
```
@Import(Java8DateTimeConfiguration.class)
```

Date and time formats should be configured in application properties (yaml) file

``` 
application:
  properties:
    dateFormat: MM/dd/yyyy
    dateTimeFormat: MM/dd/yyyy HH:mm:ss
```

_Note:_ 

The configuration makes all endpoint contracts very strict.
If request does not contain some DTO's property, it is not considered like *null* and application fails.
So FE has always send all properties obviously initialized as null:

``` 
{
  "ruleId": null,
  "name": null,
  "businessUnit": null
}
``` 
Also application is going to fail if request contains unknown property

---------------
com.bordozer.commons.utils package
---------------

* FileUtils: File utils for loading system resources (are useful in testing)
* JsonUtils: Utils fot working with JSON

---------------

Endpoint testing framework
---------------

Package *com.bordozer.commons.testing.endpoint* contains a simple endpoint testing framework. It by fact is a wrapper around WebMvcTest and gives a possibility 
easy and fast contract testing.

 ``` 
 @WebMvcTest(GeneralController.class)
 class GeneralControllerTest extends AbstractEndpointTest {
    private static final String GET_SCENARIOS_URL = "/general/getScenarios";
    private static final String GET_SCENARIOS_EXPECTED_RESPONSE =
                FileUtils.readSystemResource("tests/GeneralControllerTest/get-scenarios-expected-response.json");
    
    @Test
    void shouldGetScenarios() {
        // given
        when(generalService.getScenarios()).thenReturn(newArrayList("S1", "S2", "S3"));

        final EndpointTestBuilder endpointTest = testEndpoint(GET_SCENARIOS_URL)
                                                     .whenRequest()
                                                     .thenResponseSuccessWithJsonBody(GET_SCENARIOS_EXPECTED_RESPONSE);
        // when
        getTo(endpointTest);
    }                
 }
 ``` 

Supports HTTP methods _getTo()_, _postTo(_), _uploadFile()_. Allows to check HTTP response status, type, body, http headers. 

 ```
@Test
    void shouldReturnNotFoundIfInternalException() {
        // given
        doThrow(new ExcelImportException(CONTROLLER_ERROR_MESSAGE)).when(businessInputService).getUploadedBusinessInputs(any(File.class));

        final EndpointTestBuilder requestBuilder = testEndpoint(URL)
                                                           .whenRequest()
                                                           .withContentType(MediaType.MULTIPART_FORM_DATA)
                                                           .thenResponse()
                                                           .hasJsonContentType()
                                                           .hasHttpStatus(HttpStatus.NOT_FOUND)
                                                           .hasBodyJson(CONTROLLER_ERROR_EXPECTED_RESPONSE)
                                                           .end();

        // when
        uploadFile(requestBuilder, APPLICATION_OCTET_STREAM, "file", "tests/BusinessInputControllerTest/happy-path-request.xlsx");
    }
 ```
 
---------------

## Authors

* **Borys Lukianov**

