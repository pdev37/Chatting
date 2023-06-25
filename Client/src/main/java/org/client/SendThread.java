package org.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

class SendThread extends Thread {
    Socket socket = null;
    String name;

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public SendThread(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            // 최초1회는 client의 name을 서버에 전송
            PrintStream out = new PrintStream(socket.getOutputStream());
            out.println(name);
            out.flush();

            while (true) {
                String outputMsg = bufferedReader.readLine();
                out.println(outputMsg);
                out.flush();
                if("quit".equals(outputMsg)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
