package no.ntnu.datakomm;

public class SimpleTcpClient {
    // Remote host where the server will be running
    private static final String HOST = "localhost";
    // TCP port
    private static final int PORT = 1301;

    /**
     * Run the TCP Client.
     * @param args Command line arguments. Not used.
     */
    public static void main(String[] args) {
        SimpleTcpClient client = new SimpleTcpClient();
        client.run();
    }

    /**
     * Run the TCP Client application. The logic is already implemented, no need to change anything in this method.
     * You can experiment, of course.
     */
    public void run() {
        log("Simple TCP client started");

        if (connectToServer(HOST, PORT)) {
            log("Connection to the server established");
            if (sendRequestToServer("5+7")) {
                String response = readResponseFromServer();
                if (response != null) {
                    log("Server responded with: " + response);
                    if (sendRequestToServer("bla+bla")) {
                        response = readResponseFromServer();
                        if (response != null) {
                            log("Server responded with: " + response);
                            if (sendRequestToServer("game over") && closeConnection()) {
                                log("Game over, connection closed");
                            } else {
                                log("Failed to stop conversation");
                            }
                        } else {
                            log("Failed to receive server's response!");
                        }
                    } else {
                        log("Failed to send invalid message to server!");
                    }
                } else {
                    log("Failed to receive server's response!");
                }
            } else {
                log("Failed to send valid message to server!");
            }
        } else {
            log("Failed to connect to the server");
        }

        log("Simple TCP client finished");
    }

    /**
     * Close the TCP connection to the remote server.
     * @return True on success, false otherwise
     */
    private boolean closeConnection() {
        return false;
    }

    /**
     * Try to establish TCP connection to the server (the three-way handshake).
     * @param host The remote host to connect to. Can be domain (localhost, ntnu.no, etc), or IP address
     * @param port TCP port to use
     * @return True when connection established, false otherwise
     */
    private boolean connectToServer(String host, int port) {
        // TODO - implement this method
        // Remember to catch all possible exceptions that the Socket class can throw.
        return false;
    }

    /**
     * Send a request message to the server (newline will be added automatically)
     * @param request The request message to send. Do NOT include the newline in the message!
     * @return True when message successfully sent, false on error.
     */
    private boolean sendRequestToServer(String request) {
        // TODO - implement this method
        // Hint: What can go wrong? Several things:
        // * Connection closed by remote host (server shutdown)
        // * Internet connection lost, timeout in transmission
        // * Connection not opened.
        // * What is the request is null or empty?
        return false;
    }

    /**
     * Wait for one response from the remote server.
     * @return The response received from the server, null on error.
     */
    private String readResponseFromServer() {
        // TODO - implement this method
        // Similarly to other methods, exception can happen while trying to read the input stream of the TCP Socket
        return null;
    }

    /**
     * Log a message to the system console.
     * @param message The message to be logged (printed).
     */
    private void log(String message) {
        System.out.println(message);
    }
}
