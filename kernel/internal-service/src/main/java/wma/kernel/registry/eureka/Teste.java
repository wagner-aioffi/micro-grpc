package wma.kernel.registry.eureka;

import com.netflix.eureka2.client.EurekaInterestClient;
import com.netflix.eureka2.client.EurekaRegistrationClient;
import com.netflix.eureka2.client.Eurekas;
import com.netflix.eureka2.client.resolver.ServerResolver;
import com.netflix.eureka2.interests.ChangeNotification;
import com.netflix.eureka2.registry.datacenter.BasicDataCenterInfo;
import com.netflix.eureka2.registry.instance.InstanceInfo;
import com.netflix.eureka2.registry.instance.InstanceInfo.Status;
import rx.Subscriber;
import rx.Subscription;
import rx.subjects.BehaviorSubject;

import static com.netflix.eureka2.client.resolver.ServerResolvers.fromDnsName;
import static com.netflix.eureka2.client.resolver.ServerResolvers.fromEureka;
import static com.netflix.eureka2.interests.Interests.forApplications;
import static com.netflix.eureka2.interests.Interests.forVips;

/**
 * This example demonstrates how to register an application using {@link EurekaRegistrationClient}
 * and how to access registry data using {@link EurekaInterestClient}.
 *
 * @author Tomasz Bak
 */
public class Teste {

    public static final InstanceInfo SERVICE_A = new InstanceInfo.Builder()
            .withId("id_serviceA")
            .withApp("ServiceA")
            .withAppGroup("ServiceA_1")
            .withStatus(Status.UP)
            .withDataCenterInfo(BasicDataCenterInfo.fromSystemData())
            .build();

    private final String writeServerDns;
    private final int writeRegistrationPort;
    private final int serverPort;
    private final String readServerVip;

    public Teste(String writeServerDns, int writeRegistrationPort,
            int serverPort, String readServerVip) {
        this.writeServerDns = writeServerDns;
        this.writeRegistrationPort = writeRegistrationPort;
        this.serverPort = serverPort;
        this.readServerVip = readServerVip;
    }

    public void run() throws InterruptedException {

        EurekaRegistrationClient registrationClient = Eurekas.newRegistrationClientBuilder()
                .withServerResolver(fromDnsName(writeServerDns).withPort(writeRegistrationPort))
                .build();

        ServerResolver interestClientResolver =
                fromEureka(
                        fromDnsName(writeServerDns).withPort(serverPort)
                ).forInterest(forVips(readServerVip));

        EurekaInterestClient interestClient = Eurekas.newInterestClientBuilder()
                .withServerResolver(interestClientResolver)
                .build();

        interestClient.forInterest(forApplications("WriteServer", "ReadServer", "ServiceA")).subscribe(
                new Subscriber<ChangeNotification<InstanceInfo>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("Change notification stream closed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("Error in the notification channel: " + e);
                    }

                    @Override
                    public void onNext(ChangeNotification<InstanceInfo> changeNotification) {
                        System.out.println("Received notification: " + changeNotification);
                    }
                });

        BehaviorSubject<InstanceInfo> infoSubject = BehaviorSubject.create();
        Subscription subscription = registrationClient.register(infoSubject).subscribe();

        // Register client 1
        System.out.println("Registering SERVICE_A with Eureka...");
        infoSubject.onNext(SERVICE_A);
        Thread.sleep(1000);

        // Modify client 1 status
        System.out.println("Updating service status to DOWN...");
        InstanceInfo updatedInfo = new InstanceInfo.Builder().withInstanceInfo(
                SERVICE_A).withStatus(Status.DOWN).build();
        infoSubject.onNext(updatedInfo);
        Thread.sleep(1000);

        // Unregister client 1
        System.out.println("Unregistering SERVICE_A from Eureka...");
        subscription.unsubscribe();
        Thread.sleep(1000);

        Thread.sleep(5000);

        // Terminate both clients.
        System.out.println("Shutting down clients");
        registrationClient.shutdown();
        interestClient.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Local service info: " + SERVICE_A);
        Teste simpleApp = new Teste("localhost", 12102, 1310, "eurekaReadServerVip");
        simpleApp.run();
    }
}