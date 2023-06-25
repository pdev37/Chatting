package org.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ReceiveThread extends Thread{
    static List<PrintWriter> list = new CopyOnWriteArrayList<PrintWriter>();

    Socket socket = null;
    BufferedReader in = null;
    PrintWriter out = null;

    public ReceiveThread (Socket socket) {
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            list.add(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        String name = "";
        int clientCount = 0;
        try {
            name = in.readLine();
            System.out.println("[" + name + " 새연결생성]");
            sendAll("[" + name + "]님이 들어오셨습니다.");
            

            while (in != null) {
                String inputMsg = in.readLine();
                if("quit".equals(inputMsg)) break;
                sendAll(name + ">>" + inputMsg);
            }
        } catch (IOException e) {
            System.out.println("[" + name + " 접속끊김]");
        } finally {
            sendAll("[" + name + "]님이 나가셨습니다");
            list.remove(out);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[" + name + " 연결종료]");
    }

    private void sendAll (String s) {
        for (PrintWriter out: list) {
            out.println(s);
            out.flush();
        }
    }
}
