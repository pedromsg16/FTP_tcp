/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author pedro
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
       
        Socket socket = new Socket("127.0.0.1",12345);

        // Ler e escrever no socket
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Scanner sr = new Scanner(socket.getInputStream());

        // Ler do terminal
        Scanner sc = new Scanner(System.in);

        String s = new String();
        String aux;
        
        aux = sc.nextLine();
        
        out.println(aux);
        out.flush();
        
        if(aux.equals("QUIT")){
            System.exit(0);
        }
        else if(aux.equals("LIST")){
            while((br.read()) != '~'){
                s = br.readLine();
                System.out.println(s);
            }
        }
        else{
            
            StringTokenizer tk = new StringTokenizer(aux, " "); 
                
            String todo = tk.nextToken();

            if(todo.equals("DOWNLOAD")){
                BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/home/pedro/Univ/IRC/Projeto2/Cliente/Recebido.txt")));

                
                while((s= br.readLine()) != null){
                    if(!s.equals("~"))
                        pw.write(s+"\n");
                }

                if(br.readLine()==null){
                    System.out.println("Download completo!");
                }

                pw.close();
            }
            
            else if(todo.equals("UPLOAD")){
                String diretorio = "/home/pedro/Univ/IRC/Projeto2/Cliente";
                File f = new File(diretorio);
                File[] fList = f.listFiles();
                
                boolean existe = false;
                int index = 0;
                String name = tk.nextToken();

                index = -1;

                for (int i = 0; i < fList.length; i++) {
                    if(fList[i].getName().equals(name)){
                        index = i;
                        existe = true;
                    }
                }
                
                if(existe){
                    File ff = new File(fList[index].getAbsolutePath());
                    FileReader fr = new FileReader(ff);
                    BufferedReader bfr = new BufferedReader(fr);

                    String st;

                    while((st = bfr.readLine()) != null){
                        out.println(st);
                    }

                    if(bfr.readLine() == null){
                        System.out.println("Upload feito com sucesso!");
                    }
                    
                    out.println("~");
                    out.flush();
                }
            }
        }
    }
    
}
