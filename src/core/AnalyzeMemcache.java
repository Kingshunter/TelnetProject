package core;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import javax.net.SocketFactory;

public class AnalyzeMemcache {

    private static final String END_FLAG_END = "END";

    private static final String END_FLAG_END2 = "END\r\n";

    private static final String END_FLAG_EMPTY = "\r\n";

    private static final String END_FLAG_OK = "OK";

    private static final String END_FLAG_ERROR = "ERROR";

    private static final int soTimeout = 0;

    private SocketFactory socketFactory;

    private Socket socket;

    private BufferedReader in;

    private PrintWriter out;

    private Charset charset = Charset.defaultCharset();

    private static final int connectTimeout = 0;

    private static AnalyzeMemcache analyzeMemcache;
    
    private static Set<String> productionIpSet;
    
    static{
        if(productionIpSet == null){
            productionIpSet = new HashSet<String>();
        }
        productionIpSet.add("10.13.132.47");
    }

    private AnalyzeMemcache() {
        // singleton
        socketFactory = SocketFactory.getDefault();
    }

    public synchronized static AnalyzeMemcache getInstance() {
        if (analyzeMemcache == null) {
            analyzeMemcache = new AnalyzeMemcache();
        }
        return analyzeMemcache;
    }

    public void connect(String host, int port) throws IOException {
        connect(InetAddress.getByName(host), port);
    }

    private void connect(InetAddress inetAddress, int port) throws IOException {
        socket = socketFactory.createSocket();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, port);
        socket.setKeepAlive(true);
        socket.setReceiveBufferSize(Integer.MAX_VALUE);
        socket.connect(inetSocketAddress, connectTimeout);
        socket.setSoTimeout(soTimeout);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
        out = new PrintWriter(socket.getOutputStream());
    }

    public String execute(String command) {
        write(command);
        String result = read();
        System.out.println("[telnet] 打印本次执行的telnet结果:" + result);
        return result;
    }

    private String read() {
        String temp = null;
        StringBuilder sb = new StringBuilder();
        try {
            while ((temp = in.readLine()) != null) {
                sb.append(temp).append("\n");
                if (sb.lastIndexOf(END_FLAG_END) > -1 || sb.lastIndexOf(END_FLAG_END2) > -1
                        || sb.lastIndexOf(END_FLAG_EMPTY) > -1 || sb.lastIndexOf(END_FLAG_OK) > -1
                        || sb.lastIndexOf(END_FLAG_ERROR) > -1) {
                    sb.deleteCharAt(sb.length() - 1);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void write(String command) {
        try {
            out.println(command);
            out.flush();
            System.out.println("[telnet] 打印本次执行的telnet命令:" + command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disConnect() {
        closeQuietly(socket);
        closeQuietly(in);
        closeQuietly(out);
        socket = null;
        in = null;
        out = null;
    }

    private void closeQuietly(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    private void closeQuietly(Closeable close) {
        if (close != null) {
            try {
                close.close();
            } catch (IOException e) {
            }
        }
    }

}
