package org.server;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ReceiveThread extends Thread{
    static List<OutputStream> list = new CopyOnWriteArrayList<OutputStream>();

    Socket socket = null;
    InputStream in = null;

    OutputStream out = null;

    public ReceiveThread (Socket socket) {
        this.socket = socket;
        try {
            out = socket.getOutputStream();
            in = socket.getInputStream();
            list.add(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        String name = "";
        try {
            byte[] nameBytes = new byte[1024];
            in.read(nameBytes);
            String inputName = new String(nameBytes).trim();
            name = inputName;
            System.out.println("[" + name + " 새연결생성]");
            sendAll("[" + name + "]님이 들어오셨습니다.");

            while (in != null) {
                byte[] msgBytes = new byte[1024];
                in.read(msgBytes);
                String inputMsg = new String(msgBytes).trim();
                if("quit".equals(inputMsg)) break;
                sendAll(name + ">>" + inputMsg);
            }
        } catch (IOException e) {
            System.out.println("[" + name + " 접속끊김]");
        } finally {
            try {
                sendAll("[" + name + "]님이 나가셨습니다");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            list.remove(out);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[" + name + " 연결종료]");
    }

    private void sendAll (String s) throws IOException {
        for (OutputStream out: list) {
            out.write(s.getBytes());
            out.flush();
        }
    }
}
