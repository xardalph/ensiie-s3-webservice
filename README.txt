webservice project

This project aim to create an assurance claim workflow with multiples webservices.

java in flowable :
here to compile one of the class :
flowable-java-class/flow$ mvn clean package

todo : make this with multiples class to allow one jar with all our code.



here the claim object definition :
```
type Claim {
    id : Integer!
    name : String!
    surname : String!
    phoneNumer: String!
    adress: String
    policyNumber: Integer!
    claimType: Integer!
    description: String
}
```

here the repartition of work  :

Services :

    Claim Submission Service : The customer submits a claim including personal details, policy number, claim type, claimed amount, and a short description. - FLowable (Evan)

    Identity Verification Service : The customer’s identity is verified. If verification fails, the claim is rejected and the customer is notified. - REST (Evan)

    Policy Validation Service : The insurance policy is checked for validity and coverage. If the policy does not cover the claim, the process stops - GRPC (Evan).

    Fraud Detection Service : The claim is analyzed to determine a fraud risk level (low, medium, high) - GraphQL (Aristide).

    Eligibility & Rule Evaluation Service, Business rules are applied: High-risk claims above a certain amount are automatically rejected. Other claims continue in the process - GraphQL (Aristide).

    Document Review Service : The customer may be asked to provide additional documents. If documents are missing or invalid, the claim is suspended or rejected. - Flowable (Evan)

    Expert Assessment Service : An insurance expert reviews the claim and provides a decision (approve or reject). - Flowable (Evan)

    Compensation Calculation Service : For approved claims, the payable amount is calculated according to policy rules - SOAP (Aristide).

    Payment Authorization Service : The payment request is validated internally. If authorization fails, the claim is rejected - SOAP (Aristide).

    Customer Notification Service : The customer is informed at each major step and receives the final decision. - Optionnal (à voir après)

    Claim Tracking Service : The customer can consult the current status and history of the claim at any time - FLowable (Evan)
