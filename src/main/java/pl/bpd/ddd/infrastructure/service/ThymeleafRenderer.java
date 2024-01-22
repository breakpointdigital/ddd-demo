package pl.bpd.ddd.infrastructure.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import pl.bpd.ddd.application.shared.TemplateRenderer;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ThymeleafRenderer implements TemplateRenderer {
    private final SpringTemplateEngine templateEngine;

    @Override
    public String render(String templateName, Map<String, Object> variables) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(variables);
        return templateEngine.process(templateName, thymeleafContext);
    }
}
