import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

@SuppressWarnings("serial")
public class Snake extends JFrame
{
	public static ServerSocket ss;
	public static Socket s;
	public static int soc=5000;
	public static String spl[];
	public static InetAddress localhost;
	public static String ip="localhost";
	public static String  msgout="",msgin="";
	public static CardLayout c=new CardLayout();
	public static DataOutputStream dout;
	public static Snake sn;
	public static JPanel p=new JPanel(c);
	public static JPanel p1=new JPanel();
	public static JPanel p2;
	public static JLabel iplabel=new JLabel();
	public static JLabel urip=new JLabel("Your IP:");
	public static JLabel frndip=new JLabel("Friend IP:");
	public static JLabel timelabel=new JLabel("Time:(min)");
	public static JTextField iptextfield=new JTextField();
	public static JTextField time=new JTextField();
	public static JButton create=new JButton("Create");
	public static JButton join=new JButton("Join");
	public static JButton single=new JButton("single");;
	Font fo=new Font(null,Font.BOLD,58);
	public static int n=10,score=0,t=175,frndscr=0,sec,min;
	public static int mx=-10,my,fx,fy,framex=850,framey=500;
	public static boolean tch=false,gameover=false,lose=false,win=false,singl=true;
	public static ArrayList<xy> al=new ArrayList<xy>();
	public class xy
	{
		private int x,y;
		xy(int x,int y)
		{
			this.x=x;
			this.y=y;
		}
	}
	Snake()
	{
		try{
			localhost = InetAddress.getLocalHost();
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
		iplabel.setFont(new Font(null,Font.BOLD,20));
		iplabel.setBounds(500,130,300,30);
		
		urip.setFont(new Font(null,Font.BOLD,20));
		urip.setBounds(500,100,170,30);
		
		iptextfield.setFont(new Font(null,Font.BOLD,20));
		iptextfield.setBounds(500,210,170,30);
		
		frndip.setFont(new Font(null,Font.BOLD,20));
		frndip.setBounds(500,180,170,30);
		
		timelabel.setFont(new Font(null,Font.BOLD,20));
		timelabel.setBounds(500,260,170,30);
		
		time.setFont(new Font(null,Font.BOLD,20));
		time.setBounds(500,290,170,30);
		
		iptextfield.setText("127.0.0.1");
		p1.add(iplabel);
		p1.add(iptextfield);
		p1.add(urip);
		p1.add(frndip);
		p1.add(timelabel);
		p1.add(time);
		iplabel.setText((localhost.getHostAddress()).trim());
		setTitle("Snake Game");
		p1.setBackground(Color.white);
		p1.setLayout(null);
		create.setBounds(100,130,300,30);
		join.setBounds(100,180,300,30);
		single.setBounds(100,230,300,30);
		p2=new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				super.paintComponents(g);
				g.setColor(Color.DARK_GRAY);
				g.fillRect(0,0, framex-20,framey-90);//outer border
				g.setColor(Color.white);
				g.fillRect(10,10, framex-40,framey-110);//inner border
				for(int i=0;i<n;i++)
				{
					g.setColor(Color.blue);
					g.fillRect(al.get(i).x,al.get(i).y, 10, 10);//snake
				}
				g.setColor(Color.white);
				g.fillRect(al.get(0).x+2,al.get(0).y+2, 6, 6);//snake head
				if(fx==al.get(0).x && fy==al.get(0).y)
				{
					n++;
					al.add(n-1,new xy(0,0));
					if(score<20)
						score+=1;
					else if(score<40)
						score+=2;
					else if(score<80)
						score+=4;
					else if(score<160)
						score+=6;
					else if(score<320)
						score+=8;
					else if(score<640)
						score+=10;
					else if(score<1280)
						score+=12;
					else if(score<2560)
						score+=14;
					else if(score<5120)
						score+=16;
					else
						score+=18;
					tch=true;
				}
				if(gameover==true)
				{
					
					g.setFont(new Font(null,Font.BOLD,40));
					g.setColor(Color.red);
					if(singl)
						g.drawString("Game over, Score : "+score,16,framey-54);
					else
					{
						if(win==true)
						{
							g.setColor(new Color(0,200,0));
							g.drawString("You win!",16,framey-54);
							return;
						}
						else if(lose==true)
						{
							g.setColor(Color.red);
							g.drawString("You lose!",16,framey-54);
							return;
						}
					if(score<frndscr)
					{
						g.setColor(Color.red);
						g.drawString("You lose!  Your Score: "+score+"   Friend Score:  "+frndscr,16,framey-54);
					}
					else if(score>frndscr)
					{
						g.setColor(new Color(0,200,0));//green
						g.drawString("You win!  Your Score: "+score+"    Friend Score:  "+frndscr,16,framey-54);
					}
					else
						g.drawString("Draw",20,framey-54);
					}
				}
				else 
				{
					g.setFont(fo);
					g.setColor(new Color(255,140,0));//orange
					if(singl)
						g.drawString("Score : "+score,20,framey-48);
					else
						g.drawString("Score : "+score+"  Friend Score:"+frndscr,20,framey-48);
				}
				g.setColor(Color.green);
				g.fillRect(fx, fy, 10, 10);//food
			}
		};
		p1.add(create);
		p1.add(join);
		p1.add(single);
		p.add(p1,"start");
		p.add(p2,"game");
		add(p);
		c.show(p, "start");
		single.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				for(int k=0;k<n;k++)
				{
					al.add(new xy(200+(k*10),200));
				}
				c.show(p, "game");
				startgame();
			}
		});
		create.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent ce)
			{
				singl=false;
				try
				{
					ss=new ServerSocket(soc);
					s=ss.accept();
					min=Integer.parseInt(time.getText());
					sec=min*60;
					timer();
					receivemsg receive=new receivemsg();
					receive.start();
					dout=new DataOutputStream(s.getOutputStream());
					for(int k=0;k<n;k++)
					{
						al.add(new xy(200+(k*10),200));
					}
					c.show(p, "game");
					startgame();
					dout.flush();
				}catch(Exception e)
				{
					JOptionPane.showMessageDialog(null, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		join.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent ce)
			{
				singl=false;
				ip=iptextfield.getText();
				try
				{
					s=new Socket(ip,soc);
					receivemsg receive=new receivemsg();
					receive.start();
					dout=new DataOutputStream(s.getOutputStream());
					for(int k=0;k<n;k++)
					{
						al.add(new xy(200+(k*10),200));
					}
					c.show(p, "game");
					startgame();
					dout.flush();	
				}catch(Exception e1){
					JOptionPane.showMessageDialog(null, e1.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		this.setFocusable(true);
		this.requestFocusInWindow();
        this.addKeyListener(new KeyAdapter(){
        public void keyPressed(KeyEvent e)
        {
        	if(e.getKeyCode()==KeyEvent.VK_UP)
        	{
        		if(mx==0&&my==10)
        		{
        			mx=0;
    				my=10;
        		}
        		else
        		{
        			mx=0;
        			my=-10;
        		}
        	}
        	else if(e.getKeyCode()==KeyEvent.VK_DOWN)
        	{
        		if(mx==0&&my==-10)
        		{
        			mx=0;
    				my=-10;
        		}
        		else
        		{
        			mx=0;
        			my=10;
        		}
        	}
        	else if(e.getKeyCode()==KeyEvent.VK_RIGHT)
        	{
        		if(mx==-10&&my==0)
        		{
        			mx=-10;
    				my=0;
        		}
        		else
        		{
        			mx=10;
        			my=0;
        		}
        	}
        	else if(e.getKeyCode()==KeyEvent.VK_LEFT)
        	{
        		if(mx==10&&my==0)
        		{
        			mx=10;
    				my=0;
        		}
        		else
        		{
        			mx=-10;
        			my=0;
        		}
        	}
        }});
        addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent ce) 
			{
				Dimension ns=ce.getComponent().getBounds().getSize();
				framex=ns.width;
				framey=ns.height;
				while(framex%10!=0 || framey%10!=0)
				{
					framex=framex/10;
					framex=framex*10;
					framey=framey/10;
					framey=framey*10;
					iplabel.setBounds((framex*500)/850,(framey*130)/500,(framex*300)/850,(framey*30)/500);
					urip.setBounds((framex*500)/850,(framey*100)/500,(framex*170)/850,(framey*30)/500);
					iptextfield.setBounds((framex*500)/850,(framey*210)/500,(framex*170)/850,(framey*30)/500);
					frndip.setBounds((framex*500)/850,(framey*180)/500,(framex*170)/850,(framey*30)/500);
					timelabel.setBounds((framex*500)/850,(framey*260)/500,(framex*170)/850,(framey*30)/500);
					time.setBounds((framex*500)/850,(framey*290)/500,(framex*170)/850,(framey*30)/500);
					create.setBounds((framex*100)/850,(framey*130)/500,(framex*300)/850,(framey*30)/500);
					join.setBounds((framex*100)/850,(framey*180)/500,(framex*300)/850,(framey*30)/500);
					single.setBounds((framex*100)/850,(framey*230)/500,(framex*300)/850,(framey*30)/500);
				}
				while(true)
				{
					 
					fx=(int)(Math.random()*(framex-40));
					fy=(int)(Math.random()*(framey-110));
					if(fx%10==0&&fy%10==0&&fx!=0&&fy!=0)
						break;
				}
				repaint();
			}});
	}
	public static void startgame()
	{
		new SwingWorker<Void,Void>()
        {
			@Override
            protected Void doInBackground() throws Exception
            {
				while(gameover!=true)
            	{
            		for(int k=n-1;k>=0;k--)
            		{
            			if(k==0)
            			{
            				if(al.get(k).x==(framex-40)&&mx==10&&my==0)
            				{
            					al.get(k).x=10;
            				}
            				else if(al.get(k).x==10&&mx==-10&&my==0)
            				{
            					al.get(k).x=(framex-40);
            				}
            				else if(al.get(k).y==(framey-110)&&mx==0&&my==10)
            				{
            					al.get(k).y=10;
            				}
            				else if(al.get(k).y==10&&mx==0&&my==-10)
            				{
            					al.get(k).y=(framey-110);
            				}
            				else
            				{
            					al.get(k).x=al.get(k).x+mx;
            					al.get(k).y=al.get(k).y+my;
            				}
            			}
            			else
            			{
            				al.get(k).x=al.get(k-1).x;
            				al.get(k).y=al.get(k-1).y;
            			}
            		}
            		for(int k=n-1;k>=1;k--)
            		{
            			if(al.get(0).x==al.get(k).x && al.get(0).y==al.get(k).y)
            			{
            				if(s!=null)
            				{
            					dout.writeUTF("win");
            					dout.writeUTF("over");
            					lose=true;
            				}
            				gameover=true;
            				if(s!=null)
            				{
            					sn.repaint();
            					s.close();
            				}
            			}
            		}
            		sn.repaint();
            		try{Thread.sleep(t);}catch(Exception e){}
            	 }
				return null;
             }
        }.execute();
	}
	public static void timer()
	{
		new SwingWorker<Void,Void>()
		{
			protected Void doInBackground() throws Exception
			{
				try{Thread.sleep(500);}catch(Exception e){}
				while(true)
				{
					if(sec>0)
						sec-=1;
					else
					{
						gameover=true;
						sn.repaint();
						dout.writeUTF("over");
						return null;
					}
					sn.setTitle(Integer.toString(sec));
					dout.writeUTF("sec:"+sec);
					try{Thread.sleep(1000);}catch(Exception e){}
				}
			}
		}.execute();
	}
	public static class receivemsg extends Thread 
	{
		public void run()
		{
			try
			{
				DataInputStream din=new DataInputStream(s.getInputStream());
				while(!msgin.equals("over"))
				{ 			
					msgin=din.readUTF();
					spl=msgin.split(":");
					if(msgin.equals("tch"))
			        {
			        	while(true)
        			    {
        			    	fx=(int)(Math.random()*(framex-40));
        			    	fy=(int)(Math.random()*(framey-110));
        			    	if(fx%10==0&&fy%10==0&&fx!=0&&fy!=0)
        			    		break;
        			    }
			        	sn.repaint();
			        }
					else if(msgin.equals("win"))
						win=true;
					if(spl[0].equals("score"))
						frndscr=Integer.parseInt(spl[1]);
					else if(spl[0].equals("sec"))
						sn.setTitle(spl[1]);
			        tch=false;
				}
				if(msgin.equals("over"))
				{
					gameover=true;
					sn.repaint();
				}
				s.close();
			}catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	public static void foodlocator()
	{
		new SwingWorker<Void,Void>()
        {
             @Override
             protected Void doInBackground() throws Exception
             {
            	 while(true)
            	 {
            		 if(tch==true)
            		 {
            			 if(score>=20&&score<22)
            				t=t-25;
            			 else if(score>=40&&score<44)
            				t=t-25;
            			 else if(score>=80&&score<86)
            				t=t-25;
            			 else if(score>=160&&score<168)
            				t=t-20;
            			 else if(score>=320&&score<330)
            				t=t-15;
            			 else if(score>=640&&score<652)
            				t=t-15;
            			 while(true)
            			 {
            			    fx=(int)(Math.random()*(framex-40));
            			    fy=(int)(Math.random()*(framey-110));
            			    if(fx%10==0&&fy%10==0&&fx!=0&&fy!=0)
            			    	break;
            			 }
            			 if(s!=null)
            			 {
            			    dout.writeUTF("tch");
            			    dout.writeUTF("score:"+score);
            			 }
            			 sn.repaint();
            		 }
            		 tch=false;
            		 try{Thread.sleep(1);}catch(Exception e){}
            	 }
             }
        }.execute();
	}
	public static void main(String[] args)
	{
		sn=new Snake();	
		while(true)
		{
			 
			fx=(int)(Math.random()*(framex-40));
			fy=(int)(Math.random()*(framey-110));
			if(fx%10==0&&fy%10==0&&fx!=0&&fy!=0)
				break;
		}
		sn.setSize(framex,framey);
		sn.setVisible(true);
		sn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		foodlocator();
	}
}
