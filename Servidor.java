import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Servidor extends Thread 
{
	private static ArrayList<BufferedWriter>clientes;           
	private static ServerSocket server; 
	private static Socket con;
	private InputStream inputStream;  
	private InputStreamReader streamReader;  
	private BufferedReader bufferedreader;
	

	public Servidor(Socket con)
	{
	   	this.con = con;
	   	try
	   	{
	   		//Metodos para leitura e escrita do cliente
	        inputStream  = con.getInputStream();
	        streamReader = new InputStreamReader(inputStream);
	        bufferedreader = new BufferedReader(streamReader);
	   	}
	   	catch (IOException e) 
	   	{
	        e.printStackTrace();
	   	}                          
	}

	public void run()
	{                       
	  	try
	  	{                                  
		    //A primeira palavra lida na execução é o nome de usuário passado na interface inicial
		    String msg = bufferedreader.readLine();
			sendToAll(msg+" entrou no chat!\n");
		    
		    while(msg != null)
		    {
				msg = bufferedreader.readLine();
				sendToAll(msg+"\n");
		    }			                                      
		}
		catch (Exception e)
		{
	    	e.printStackTrace();
	   	}                       
	}

	public static void sendToAll(String msg) throws  IOException 
	{
		//Manda a mensagem para cada thread por meio do buffered writer pessoal de cada uma
		for(BufferedWriter bw : clientes)
		{
		    bw.write(msg+"\r\n");
		    bw.flush(); 
		}          
	}

	/***
	 * Método main
	 * @param args
	 */
	public static void main(String []args) 
	{    
	  	try{
		    server = new ServerSocket(12345);
		    clientes = new ArrayList<BufferedWriter>();

	     	while(true){
				//Realiza as conexões
	       		System.out.println("Aguardando conexão...");
		       	Socket con = server.accept();
		       	System.out.println("Cliente conectado...");
		     	Thread t = new Servidor(con);
	        	t.start();
	        	
	        	//Cria um buffered writer para a nova thread criada
	        	OutputStream outputStream =  con.getOutputStream();
		    	Writer writer = new OutputStreamWriter(outputStream);
		    	BufferedWriter escritor = new BufferedWriter(writer); 
		    	//Guarda o buffered pessoal da thread para ser utilizado mais tarde
		    	clientes.add(escritor);
	    	}
	  	}catch (Exception e) {
	    
	    	e.printStackTrace();
	  	}                       
	}// Fim do método main                     
}//Fim da classe