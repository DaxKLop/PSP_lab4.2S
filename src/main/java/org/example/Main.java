package org.example;


import java.io.*;
import java.net.*;

public class Main {
    private static final int BUFFER_SIZE = 1024;
    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(1234);
            System.out.println("Сервер запущен и ожидает подключения...");

            while (true) {
                byte[] receiveData = new byte[BUFFER_SIZE];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                String clientInput = new String(receivePacket.getData()).trim();
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                System.out.println("Получены данные от клиента (" + clientAddress + ":" + clientPort + "): " + clientInput);

                String result = calculateAndSave(clientInput);

                byte[] sendData = result.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String calculateAndSave(String clientInput) {

        String[] parts = clientInput.split(" ");
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        double z = Double.parseDouble(parts[2]);

        double alfa = 6 + Math.pow(2.71,x-y)/(y+(Math.tan(Math.pow(x,2))/y+Math.pow(x,7)/z))*Math.pow(1+1/Math.pow(Math.tan(z/100),7),Math.sqrt(Math.abs(y)+3));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt", true))) {
            writer.write("Параметры клиента : x= " + x + ", y= " + y + ", z= " + z + " | Результат: alfa= " + alfa);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Double.toString(alfa);
    }
}