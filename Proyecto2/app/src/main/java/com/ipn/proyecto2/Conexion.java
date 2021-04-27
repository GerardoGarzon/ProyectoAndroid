package com.ipn.proyecto2;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Conexion extends Thread{
    Handler bluetoothIn = new Handler();
    final int handlerState = 0;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    boolean isOn = false;

    String rpm = "";
    String cap = "";

    public Conexion(BluetoothSocket socket) {

        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[256];
        int bytes;
        int flag = 0;
        while (true) {
            try {
                bytes = mmInStream.read(buffer);
                String readMessage = new String(buffer, 0, bytes);
                bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                System.out.println(readMessage);
                if(flag == 0){
                    if(readMessage.equals("1")){
                        flag = 1;
                    }else if(readMessage.equals("0")){
                        flag = 2;
                    }
                }else if( flag == 1){
                    cap = readMessage + " uF";
                    flag = 0;
                }else if( flag == 2){
                    flag = 0;
                    rpm = readMessage + " rpm";
                }
            } catch (IOException e) {
                break;
            }
        }
    }

    public String getRPM(){
        return rpm;
    }

    public String getCAP(){
        return cap;
    }


}