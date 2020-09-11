package com.example.webfluxmicroservicedemo.decorator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.linecorp.armeria.client.ClientRequestContext;
import com.linecorp.armeria.client.DecoratingHttpClientFunction;
import com.linecorp.armeria.client.HttpClient;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrettyJsonPreviewClientDecorator implements DecoratingHttpClientFunction {

    private static final List<String> jsonType = ImmutableList.of("json");

    private final ObjectMapper objectMapper;

    @Override
    public HttpResponse execute(HttpClient delegate, ClientRequestContext ctx, HttpRequest req) throws Exception {
        Assert.notNull(ctx.log(), "Context Log is NULL");

        ctx.log().whenComplete().thenAccept(requestLog -> {

            MediaType responseContentType = requestLog.responseHeaders().contentType();

            if (jsonType.contains(responseContentType.subtype())) {

                String responseContentPreview = requestLog.responseContentPreview();

                try {

                    String prettyJsonPreview = objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(objectMapper.readValue(responseContentPreview, Object.class));

                    log.debug("prettyJsonPreview : {}", prettyJsonPreview);

                } catch (Exception e) {

                    log.error("MAY BE TRUNCATED...", e);

                }

            }

        });

        return delegate.execute(ctx, req);
    }
}
