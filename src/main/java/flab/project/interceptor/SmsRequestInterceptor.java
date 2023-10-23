package flab.project.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class SmsRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.uri(template.path().replaceAll("%3A", ":"));
    }
}
