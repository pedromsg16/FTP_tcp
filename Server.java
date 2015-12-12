/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.StringTokenizer;



/**
 *
 * @author pedro
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            // Crio um socket na porta 12345
            ServerSocket serverSocket = new ServerSocket(12345);
            // Avisa que está a correr
            System.out.println("Running....");
            // Aceita o pedido do cliente
            Socket socket = serverSocket.accept();
            // Avisa que estabeleceu contacto com o cliente
            System.out.println("Cliente conectado.");
            
            // Ler e escrever no socket
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Lista os ficheiros do diretorio
            String diretorio = "/home/pedro/Univ/IRC/Projeto2/Servidor";
            File f = new File(diretorio);
            File[] fList = f.listFiles();
            
            // Ordena o array
            Arrays.sort(fList);
            
            String tem = br.readLine();
            
            if(tem.equals("LIST")){
                int count = 0;

                for (int i = 0; i < fList.length; i++) {
                    if(fList[i].canRead() && (fList[i].toString()).endsWith(".txt")){
                        count++;
                    }
                }

                pw.println(" " + count + " ficheiros .txt encontrados, listados A-Z");

                for (int i = 0; i < fList.length; i++) {
                    if(fList[i].canRead() && (fList[i].toString()).endsWith(".txt")){
                        pw.println(" " + fList[i].getName() + " " + fList[i].length() + " Bytes");
                    }   
                }

                pw.println("~");
                pw.flush();
            }
            else if(tem.equals("QUIT")){
                System.exit(0);
            }
            else{
                StringTokenizer tk = new StringTokenizer(tem, " "); 
                
                String todo = tk.nextToken();
                
                if(todo.equals("DOWNLOAD")){
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
                        try{
     
                            File ff = new File(fList[index].getAbsolutePath());
                            FileReader fr = new FileReader(ff);
                            BufferedReader bfr = new BufferedReader(fr);

                            String s;

                            while((s = bfr.readLine()) != null){
                                pw.println(s);
                            }

                            pw.flush();

                            if(bfr.readLine() == null){
                                System.out.println("Leitura feita com sucesso.");
                            }
                            
                            pw.println("~");
                            pw.flush();
                            
                           
                        }
                        catch(IOException ioe){
                            System.out.println("\nErro na transferencia. Tente novamente!");
                        }
                
                    }
                    // Fechar as streams e o socket
                    
                }
                else if(todo.equals("UPLOAD")){
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(diretorio+"/Recebido.txt")));
                    
                    String c;
                    
                    while((c= br.readLine()) != null){
                    if(!c.equals("~"))
                        bw.write(c+"\n");
                    }

                    if(br.readLine()==null){
                        System.out.println("Download completo!");
                    }

                    bw.close();
                }
                br.close();
                socket.close();
            }
                        
            // Verifica se o ficheiro existe
            
   
        } catch (Exception e) {
            System.out.println("\nErro na conecção. Tente novamente!");
        }
    }
    
}
