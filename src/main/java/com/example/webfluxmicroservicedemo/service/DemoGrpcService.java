package com.example.webfluxmicroservicedemo.service;

import com.example.webfluxmicroservicedemo.proto.DemoGrpcProto;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DemoGrpcService{
    private final DemoService demoService;

    public void hello(DemoGrpcProto.DemoGrpcRequest request, StreamObserver<DemoGrpcProto.DemoGrpcResponse> responseObserver){
        String message = request.getMessage();
        log.debug("MESSAGE FROM GRPC REQUEST : {}", message);
        demoService.getHello()
                .map(s -> DemoGrpcProto.DemoGrpcResponse.newBuilder().setMessage(s).build())
                .subscribe(
                        demoGrpcResponse -> responseObserver.onNext(demoGrpcResponse),
                        cause -> responseObserver.onError(cause),
                        () -> responseObserver.onCompleted()
                );
    }
}
