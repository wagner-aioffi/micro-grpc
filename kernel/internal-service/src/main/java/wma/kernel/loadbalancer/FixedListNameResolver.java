package wma.kernel.loadbalancer;

import com.google.common.base.Preconditions;
import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.ResolvedServerInfo;

import java.util.List;

public class FixedListNameResolver extends NameResolver {

    private final String serviceName;

    private List<ResolvedServerInfo> servers;

    private Listener listener;

    public FixedListNameResolver(final String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String getServiceAuthority() {
        return serviceName;
    }

    @Override
    public void start(Listener listener) {
        this.listener = Preconditions.checkNotNull(listener, "listener");
        if (servers != null) {
            //FIX-ME Is it ok to call the onUpdate here?
            listener.onUpdate(servers, Attributes.EMPTY);
        }
    }

    @Override
    public void shutdown() {
        //nothing required.
    }

    public void updateList(final List<ResolvedServerInfo> servers) {
        if (listener != null) {
            listener.onUpdate(servers, Attributes.EMPTY);
        }
        this.servers = servers;
    }
}
