package no.ntnu.datakomm.chat;

import no.ntnu.datakomm.chat.helpers.DummyResponseCounter;
import no.ntnu.datakomm.chat.helpers.DummyMsgReceiver;
import org.junit.Test;
import static org.junit.Assert.*;

public class TcpClientTest {
    // Host and port to be used for all connection in the tests
    private static final String SERVER_HOST = "datakomm.work";
    private static final int SERVER_PORT = 1310;
    
    // How many ms to sleep when waiting for server response to arrive
    private static final int THREAD_SLEEP_TIME = 2000;

    /**
     * Test if opening and closing connection works
     */
    @Test
    public void testConnection() {
        TCPClient client = new TCPClient();
        assertFalse(client.isConnectionActive());
        assertTrue(client.connect(SERVER_HOST, SERVER_PORT));
        assertTrue(client.isConnectionActive());
        client.disconnect();
        assertFalse(client.isConnectionActive());
    }

    /**
     * Try to close a connection that was never opened. There should be no exception.
     */
    @Test
    public void testDisconnectFail() {
        TCPClient client = new TCPClient();
        client.disconnect();
    }

    /**
     * Test if login works correctly
     * @throws InterruptedException 
     */
    @Test
    public void testLogin() throws InterruptedException {
        TCPClient client = new TCPClient();
        assertTrue(client.connect(SERVER_HOST, SERVER_PORT));
        // The incoming messages will be received on another thread
        client.startListenThread();
        DummyResponseCounter counter = new DummyResponseCounter();
        client.addListener(counter);
        assertEquals(0, counter.loginSuccess);
        assertEquals(0, counter.loginError);

        // Try bad username
        client.tryLogin("Bad username");
        // allow the login response to arrive
        Thread.sleep(THREAD_SLEEP_TIME);
        assertEquals(0, counter.loginSuccess);
        assertEquals(1, counter.loginError);
        counter.loginSuccess = 0;
        counter.loginError = 0;

        // Try ok username with letters only
        client.tryLogin("unittestnormal");
        // allow the login response to arrive
        Thread.sleep(THREAD_SLEEP_TIME);
        assertEquals(1, counter.loginSuccess);
        assertEquals(0, counter.loginError);
        counter.loginSuccess = 0;
        counter.loginError = 0;

        // Try ok username with big and small letters
        client.tryLogin("UnitTestCamel");
        // allow the login response to arrive
        Thread.sleep(THREAD_SLEEP_TIME);
        assertEquals(1, counter.loginSuccess);
        assertEquals(0, counter.loginError);
        counter.loginSuccess = 0;
        counter.loginError = 0;

        // Alphanumerics
        client.tryLogin("UnitTest35Alpha");
        // allow the login response to arrive
        Thread.sleep(THREAD_SLEEP_TIME);
        assertEquals(1, counter.loginSuccess);
        assertEquals(0, counter.loginError);
        counter.loginSuccess = 0;
        counter.loginError = 0;
    }

    /**
     * Test if sending public messages works
     * @throws java.lang.InterruptedException
     */
    @Test
    public void testPublicMessages() throws InterruptedException {
        // Create three clients. When one sends a message others should receive
        TCPClient c1 = new TCPClient();
        assertTrue(c1.connect(SERVER_HOST, SERVER_PORT));
        TCPClient c2 = new TCPClient();
        assertTrue(c2.connect(SERVER_HOST, SERVER_PORT));
        TCPClient c3 = new TCPClient();
        assertTrue(c3.connect(SERVER_HOST, SERVER_PORT));
        
        // Client 1 logs in with a specific username
        String C1_USERNAME = "UnitTestCC";
        c1.tryLogin(C1_USERNAME);
        
        // Clients 2 and 3 will listen for incoming messages
        c2.startListenThread();
        c3.startListenThread();
        DummyMsgReceiver rec2 = new DummyMsgReceiver();
        DummyMsgReceiver rec3 = new DummyMsgReceiver();
        c2.addListener(rec2);
        c3.addListener(rec3);
        
        final String MSG_TEXT = "[Unittest] This is a specific text message, please, don't repeat it!";
        c1.sendPublicMessage(MSG_TEXT);
        
        // allow the messages to arrive
        Thread.sleep(THREAD_SLEEP_TIME);
        
        TextMessage expectedMsg = new TextMessage(C1_USERNAME, false, MSG_TEXT);
        assertTrue(rec2.hasReceived(expectedMsg));
        assertTrue(rec3.hasReceived(expectedMsg));
    }
    // TODO - make some more tests
}
