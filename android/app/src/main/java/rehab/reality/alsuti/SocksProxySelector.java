package rehab.reality.alsuti;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;

/**
 * Created by reality on 3/11/16.
 */
public class SocksProxySelector extends ProxySelector {
    @Override
    public List<Proxy> select(URI uri) {
        System.out.println("got proxy select");
        return null;
    }

    @Override
    public void connectFailed(URI uri, SocketAddress address, IOException failure) {

    }
}
