package wma.kernel.registry.eureka;

import com.netflix.eureka2.client.EurekaInterestClient;
import com.netflix.eureka2.client.EurekaRegistrationClient;
import com.netflix.eureka2.client.Eurekas;
import com.netflix.eureka2.client.resolver.ServerResolver;
import com.netflix.eureka2.client.resolver.ServerResolvers;
import com.netflix.eureka2.interests.ChangeNotification;
import com.netflix.eureka2.registry.instance.InstanceInfo;
import rx.Subscriber;
import rx.Subscription;
import rx.subjects.BehaviorSubject;

import static com.netflix.eureka2.client.resolver.ServerResolvers.fromDnsName;
import static com.netflix.eureka2.client.resolver.ServerResolvers.fromEureka;
import static com.netflix.eureka2.interests.Interests.forApplications;
import static com.netflix.eureka2.interests.Interests.forVips;

/**
 * Registry the server on a Eureka cluster
 */
public class EurekaServiceRegister {
//
//    public EurekaServiceRegister() {
//        EurekaRegistrationClient registrationClient = Eurekas.newRegistrationClientBuilder()
//                .withServerResolver(fromDnsName(writeServerDns)
//                        .withPort(writeRegistrationPort))
//                .build();
//
//        ServerResolver interestClientResolver =
//                fromEureka(
//                        fromDnsName(writeServerDns).withPort(serverPort)
//                ).forInterest(forVips(readServerVip));
//
//        EurekaInterestClient interestClient = Eurekas.newInterestClientBuilder()
//                .withServerResolver(interestClientResolver)
//                .build();
//
//        interestClient.forInterest(forApplications("WriteServer", "ReadServer", "ServiceA")).subscribe(
//                new Subscriber<ChangeNotification<InstanceInfo>>() {
//                    @Override
//                    public void onCompleted() {
//                        System.out.println("Change notification stream closed");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        System.out.println("Error in the notification channel: " + e);
//                    }
//
//                    @Override
//                    public void onNext(ChangeNotification<InstanceInfo> changeNotification) {
//                        System.out.println("Received notification: " + changeNotification);
//                    }
//                });
//
//        BehaviorSubject<InstanceInfo> infoSubject = BehaviorSubject.create();
//        Subscription subscription = registrationClient.register(infoSubject).subscribe();
//
//        // Register client 1
//        System.out.println("Registering SERVICE_A with Eureka...");
//        infoSubject.onNext(SERVICE_A);
//    }
}
