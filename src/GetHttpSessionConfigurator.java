import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * Created by wenc on 2017/4/28.
 */
public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest req, HandshakeResponse resp)
    {
        HttpSession httpSession = (HttpSession)req.getHttpSession();
        config.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}
