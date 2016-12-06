import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class EchoFrame extends Frame{

	LoginPanel lp;
	EchoPanel ep;

	public EchoFrame(){
		setLayout(new CardLayout());
		setSize(500,500);
		setTitle("Echo Client");
		lp = new LoginPanel(this);
		ep = new EchoPanel();
		add(lp, "login");
		add(ep, "chat");
		add(ep, BorderLayout.CENTER);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		});

		//ep = new EchoPanel();
		//add(ep, BorderLayout.CENTER);

		setVisible(true);
	}

	public static void main(String[] args){

		EchoFrame ef = new EchoFrame();

	}
}

class LoginPanel extends Panel implements ActionListener{
	TextField tf;
	EchoFrame efl;

	public LoginPanel(EchoFrame efl){
		this.efl = efl;
		tf = new TextField(20);
		tf.addActionListener(this);
		add(tf);
	}
	public void actionPerformed(ActionEvent ae){
		efl.ep.setUserName(tf.getText());
		CardLayout cl = (CardLayout)(efl.getLayout());
		cl.next(efl);
		tf.setText("");
	}
}

class EchoPanel extends Panel implements ActionListener, Runnable{

	TextField tf;
	TextArea ta;
	Button connect, disconnect;
	Socket s;
	BufferedReader br;
	PrintWriter pw;
	Thread t;
	String fromServer;
	Label label1,label2;
	String userName;


	public EchoPanel(){

		setLayout(new BorderLayout());
		tf = new TextField();
		tf.setEditable(false);
		tf.addActionListener(this);
		add(tf, BorderLayout.NORTH);
		ta = new TextArea();
		ta.setEditable(false);
		add(ta, BorderLayout.CENTER);
		Panel buttonPanel = new Panel();
		connect = new Button("Connect");
		connect.addActionListener(this);
		buttonPanel.add(connect);
		disconnect = new Button("Disconnect");
		disconnect.setEnabled(false);
		disconnect.addActionListener(this);
		buttonPanel.add(disconnect);
		add(buttonPanel, BorderLayout.SOUTH);
		label1 = new Label("Chat Panel: ");
		label2 = new Label("Name will go here");
		add(label1);
		add(label2);



	}

	public void setUserName(String userName){
		this.userName = userName;
		label2.setText(getUserName());
	}
	public String getUserName(){
		return userName;
	}

	public void actionPerformed(ActionEvent ae){

		if(ae.getSource() == connect){
			try{
				s = new Socket("127.0.0.1", 8189);
				br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				pw = new PrintWriter(s.getOutputStream(), true);
			}catch(UnknownHostException uhe){
				System.out.println(uhe.getMessage());
			}catch(IOException ioe){
				System.out.println(ioe.getMessage());
			}

			t = new Thread(this);
			t.start();
			tf.setEditable(true);
			connect.setEnabled(false);
			disconnect.setEnabled(true);
		}else if(ae.getSource() == disconnect){
			try{
				pw.close();
				br.close();
				s.close();
			}catch(IOException ioe){
				System.out.println(ioe.getMessage());
			}
			t = null;
			ta.append("Disconnected from server. \n");
			tf.setEditable(false);
			connect.setEnabled(true);
			disconnect.setEnabled(false);
		}else if(ae.getSource() == tf){
			String fromTf = tf.getText();
			pw.println(fromTf);
			tf.setText("");

		}else{

				//additional events
		}
	}

	public void run(){
		fromServer = "";
		try{
			while((fromServer = br.readLine()) != null){

					ta.append(fromServer + "\n");
			}
		}catch(IOException ioe){
			System.out.println(ioe.getMessage());
		}
	}
}
