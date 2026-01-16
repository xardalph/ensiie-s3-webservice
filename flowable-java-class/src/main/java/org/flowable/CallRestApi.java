package org.flowable;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;

public class CallRestApi implements JavaDelegate {

    static {
        System.out.println("CallRestApi class loaded successfully!");
    }

    @Override
    public void execute(DelegateExecution execution) {

        HashMap<String, Object> claim = new HashMap<>();
        claim.put("surname", execution.getVariable("surname"));
        claim.put("id",61);
        claim.put("name", execution.getVariable("name"));
        claim.put("phoneNumber", execution.getVariable("phoneNumber"));
        claim.put("address", execution.getVariable("address"));
        claim.put("policyNumber", execution.getVariable("policyNumber"));
        claim.put("claimType", execution.getVariable("claimType"));
        claim.put("description", execution.getVariable("description"));

        try {
            // JSON
            String jsonBody = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(claim);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            String url = "http://identity-verification:80/claim/validate";
            String response = restTemplate.postForObject(url, entity, String.class);

            System.out.println("Response: " + response);

            execution.setVariable("restResponse", response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
