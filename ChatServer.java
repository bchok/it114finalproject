import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer{
	Socket s;
	ArrayList <ChatHandler>handlers;
	public ChatServer(){
		try{
			ServerSocket ss = new ServerSocket(8189);
			handlers = new ArrayList<ChatHandler>();
			for(;;){
				s = ss.accept();
				new ChatHandler(s, handlers).start();
			}
		}catch(IOException ioe){
			System.out.println(ioe.getMessage());
		}
	}
	public static void main(String[] args){
		ChatServer tes = new ChatServer();
	}
}
