package wma.kernel.loadbalancer;

import io.grpc.NameResolver;

public class EurekaNameResolver extends NameResolver {

    @Override
    public String getServiceAuthority() {
        return null;
    }

    @Override
    public void start(Listener listener) {

    }

    @Override
    public void shutdown() {

    }
}
