package org.client;

import java.io.*;
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
            byte[] nameBytes = name.getBytes();
            OutputStream out = socket.getOutputStream();
            out.write(nameBytes);
            out.flush();

            while (true) {
                String outputMsg = bufferedReader.readLine();
                byte[] msgBytes = outputMsg.getBytes();
                out.write(msgBytes);
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
