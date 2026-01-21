package com.syiw.insurance.Controllers;

import com.syiw.insurance.Enums.FraudRiskLevel;
import com.syiw.insurance.Model.ClaimInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import static com.syiw.insurance.Enums.FraudRiskLevel.*;

@Controller
public class FraudController {
    @QueryMapping
    public FraudRiskLevel determinateFraudRiskLevel(@Argument ClaimInput claim){
       if (claim.description().contains("nucléaire")){
           return HIGH;
       }
       if (claim.description().contains("feu")){
           return MEDIUM;
       }
       return LOW;
    }

    @QueryMapping
    public Boolean isClaimRejected(@Argument FraudRiskLevel riskLevel, @Argument Float amount){
        return riskLevel == HIGH && amount >= 5000;
    }
}
