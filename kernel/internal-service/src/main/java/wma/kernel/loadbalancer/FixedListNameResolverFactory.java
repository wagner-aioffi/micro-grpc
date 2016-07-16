package wma.kernel.loadbalancer;

import com.google.common.base.Preconditions;
import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.ResolvedServerInfo;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FixedListNameResolverFactory extends NameResolver.Factory {

    private String SCHEME = "list";

    private static Map<String, FixedListNameResolver> resolvers =
            new HashMap<>();

    @Nullable
    @Override
    public NameResolver newNameResolver(final URI targetUri,
            final Attributes params) {
        if (SCHEME.equals(targetUri.getScheme())) {
            String targetPath = Preconditions
                    .checkNotNull(targetUri.getPath(), "targetPath");
            Preconditions.checkArgument(targetPath.startsWith("/"),
                    "the path component (%s) of the target (%s) must start with '/'",
                    targetPath, targetUri);
            String name = targetPath.substring(1);

            synchronized (FixedListNameResolver.class) {
                FixedListNameResolver resolver = resolvers.get(name);
                if (resolver == null) {
                    resolver = new FixedListNameResolver(name);
                    resolvers.put(name, resolver);
                }
                return resolver;
            }
        } else {
            return null;
        }
    }

    @Override
    public String getDefaultScheme() {
        return SCHEME;
    }

    /**
     * Configure the list of servers for a given service name.
     * @param serviceName
     * @param servers
     */
    public static void configureList(final String serviceName,
            List<ResolvedServerInfo> servers) {
        synchronized (FixedListNameResolver.class) {
            FixedListNameResolver resolver = resolvers.get(serviceName);
            if (resolver == null) {
                //create a resolver and set the list.
                resolver = new FixedListNameResolver(serviceName);
                resolvers.put(serviceName, resolver);
                resolver.updateList(servers);
            }
        }
    }
}
