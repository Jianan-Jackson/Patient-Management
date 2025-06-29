package com.pm.patientservice.grpc;


import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillingServiceGrpcClient {
    private final BillingServiceGrpc.BillingServiceBlockingStub stub;

    public BillingServiceGrpcClient(
            @Value("${BILLING_SERVICE_ADDRESS:localhost}") String serverAddress,
            @Value("${BILLING_SERVICE_GRPC_PORT:9090}") int serverPort
    ) {
        log.info("Connecting to Billing Service GRPC service at {}:{}", serverAddress, serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext()
                .build();
        stub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount(String patientId, String name, String email){
        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(name)
                .setEmail(email)
                .build();
        BillingResponse response = stub.createBillingAccount(request);
        log.info("Received response from billing service via GRPC: {}", response);
        return response;
    }
}
