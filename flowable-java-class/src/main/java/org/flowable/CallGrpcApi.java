package org.flowable;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import policy.Claim;
import policy.PolicyReturn;
import policy.ValidationGrpc;

public class CallGrpcApi implements JavaDelegate {

    static {
        System.out.println("CallGrpcApi class loaded successfully!");
    }

    @Override
    public void execute(DelegateExecution execution) {
        // Create a gRPC channel to connect to the Validation service
        ManagedChannel channel = ManagedChannelBuilder.forAddress(
            "localhost",
            50051
        )
            .usePlaintext()
            .build();

        try {
            // Create a gRPC stub for the Validation service
            ValidationGrpc.ValidationBlockingStub stub =
                ValidationGrpc.newBlockingStub(channel);

            // Build the Claim message
            Claim claim = Claim.newBuilder()
                .setId(execution.getVariable("id").toString())
                .setName(execution.getVariable("name").toString())
                .setSurname(execution.getVariable("surname").toString())
                .setPhoneNumer(execution.getVariable("phoneNumber").toString())
                .setAdress(execution.getVariable("address").toString())
                .setPolicyNumber(
                    Integer.parseInt(
                        execution.getVariable("policyNumber").toString()
                    )
                )
                .setClaimType(
                    Integer.parseInt(
                        execution.getVariable("claimType").toString()
                    )
                )
                .setDescription(execution.getVariable("description").toString())
                .build();

            // Make the gRPC call and get the response
            PolicyReturn response = stub.validate(claim);

            // Set the response back into the execution context
            execution.setVariable("isValid", response.getReturn());
        } finally {
            // Shut down the channel
            channel.shutdown();
        }
    }
}
