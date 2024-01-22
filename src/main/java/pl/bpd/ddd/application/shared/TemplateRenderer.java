package pl.bpd.ddd.application.shared;

import java.util.Map;

public interface TemplateRenderer {
    String render(String templateName, Map<String, Object> variables);
}
