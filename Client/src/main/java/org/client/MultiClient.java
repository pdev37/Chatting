package org.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MultiClient {

    public void start() {
        Socket socket = null;
        BufferedReader inputName;
        InputStream in = null;

        try {
            socket = new Socket("localhost", 8000);
            System.out.println("[서버와 연결되었습니다]");

            System.out.println("id 입력");
            inputName = new BufferedReader(new InputStreamReader(System.in));
            String name = inputName.readLine();
            if (name.equals("")) {
                String name2;
                while (true) {
                    System.out.println("id 재입력");
                    BufferedReader inputName2 = new BufferedReader(new InputStreamReader(System.in));
                    name2 = inputName2.readLine();
                    if (!name2.equals("")) {
                        break;
                    }
                }
                name = name2;
            }

            System.out.println("id: " + name);
            Thread sendThread = new SendThread(socket, name);
            sendThread.start();

            in = socket.getInputStream();
            byte[] msgBytes = new byte[1024];
            in.read(msgBytes);
            String inputMsg = new String(msgBytes).trim();
            while (inputMsg != null) {
                if (("[" + name + "]님이 나가셨습니다").equals(inputMsg)) break;
                System.out.println("From:" + inputMsg);
            }
        } catch (IOException e) {
            System.out.println("[서버 접속끊김]");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[서버 연결종료]");
    }
}

