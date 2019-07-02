package Assgn2;
import java.util.ArrayList;
import java.util.Random;
class Passenger{
	int Pid;
	boolean slock;
	boolean xlock;
	Passenger(int cur){
		Pid=300+(cur++);
		slock=false;
		xlock=false;
	}
	Passenger(int n,int kk){
		Pid=n;
		slock=false;
		xlock=false;
	}
}
class Flight{
	int Fid;
	int noseats;
	ArrayList<Passenger> passlist;
	boolean slock;
	boolean xlock;
	Flight(int nseats,int cur,int[] count){
		Fid=100+(cur++);
		noseats=nseats;
		Random rand=new Random();
		int nopass=rand.nextInt(nseats);
		passlist=new ArrayList<Passenger>(nopass);
		
		for(int i=0;i<nopass;i++) {
			Passenger temp=new Passenger(count[0]++);
			passlist.add(temp);
		}
		slock=false;
		xlock=false;
	}
	Flight(int Fidd,int nseats,ArrayList<Passenger> pass){
		Fid=Fidd;
		noseats=nseats;
		passlist=new ArrayList<Passenger>(pass.size());
		for(int i=0;i<pass.size();i++) {
			passlist.add(new Passenger(pass.get(i).Pid,0));
		}
		slock=false;
		xlock=false;
	}
}
class database {
	int[] count;
	ArrayList<Flight> listfl;
	boolean slock;
	boolean xlock;
	database(int nflights){
		count=new int[1];
		count[0]=0;
		listfl=new ArrayList<Flight>(nflights);
		Random rand=new Random();
		for(int i=0;i<nflights;i++) {
			int noseat=rand.nextInt(10)+2;
			Flight temp=new Flight(noseat,i,count);
			listfl.add(temp);
		}
		slock=false;
		xlock=false;
	}
	database(database Data){
		listfl=new ArrayList<Flight>(Data.listfl.size());
		for(int i=0;i<Data.listfl.size();i++) {
			listfl.add(new Flight(Data.listfl.get(i).Fid,Data.listfl.get(i).noseats,Data.listfl.get(i).passlist));
		}
		slock=false;
		xlock=false;
	}
}

class multithreading3 extends Thread{
	static database Data = null;
	static int[] notrans;
	multithreading3(database data,int[] notrans2) {
		// TODO Auto-generated constructor stub
		Data=data;
		notrans=notrans2;		
	}
	public void run() {
		long t=System.currentTimeMillis();
		long end=t+10000;
		
		while(System.currentTimeMillis()<end){
			synchronized(Data) {
			Random rand=new Random();
			int choice=rand.nextInt(5);
//			System.out.println(choice);
			int Fid=rand.nextInt(5)+100;
			int Fid2=rand.nextInt(5)+100;
			int Pid=rand.nextInt(10)+300;
			if(choice==0)
				try {
//					System.out.println("Thread "+Thread.currentThread().getId());					
					Reserve1(Fid,Pid);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else if(choice==1)
				try {
//					System.out.println("Thread "+Thread.currentThread().getId());
//					System.out.println(Fid+" "+Pid+" cancel");
					Cancel1(Fid,Pid);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else if(choice==2)
				try {
//					System.out.println("Thread "+Thread.currentThread().getId());
					MyFlights1(Pid);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else if(choice==3)
				try {
//					System.out.println("Thread "+Thread.currentThread().getId());
					TotalRes1();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else if(choice==4) {
				try {
//					System.out.println("Thread "+Thread.currentThread().getId());
					Transfer1(Fid,Fid2,Pid);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
				System.out.println("Invalid ");
		}
	}
	}
	public static void Reserve1(int Fid,int Pid) throws InterruptedException {		
//		synchronized(Data) {
			notrans[0]++;
			Thread.currentThread().sleep(100);
			System.out.println("Thread "+Thread.currentThread().getId());
			if(Data.xlock==false) {
//				System.out.println("resres");
				Data.xlock=true;
				System.out.println("Reserves "+Fid+" "+Pid+" start1");
			}
			else {
				while (true){
				    try {
				        if (Data.xlock==false){
				            Data.xlock=true;
				            System.out.println("Reserves "+Fid+" "+Pid+" start2");
				            break;
				        }
				        sleep(10);
				    } catch (InterruptedException ex){
				    }
				}
			}
			Passenger temp=new Passenger(Pid-300);
//			System.out.println("_____________________");
			int flag=0;
			for(int i=0;i<Data.listfl.size();i++) {
				if(Fid==Data.listfl.get(i).Fid) {
					if(Data.listfl.get(i).noseats>Data.listfl.get(i).passlist.size()) {
						Data.listfl.get(i).passlist.add(temp);
					}
					else System.out.println(Fid+" is full");
					flag=1;
					break;
				}
				
			}
			if(flag==0)
				System.out.println("Invalid Flight ID "+ Fid);
			Data.xlock=false;
			System.out.println("Reserves "+Fid+" "+Pid+" end");

//		}
	}
	public static void Cancel1(int Fid,int Pid) throws InterruptedException {		
//		sleep(100);
//		synchronized(Data) {

			notrans[1]++;
			Thread.currentThread().sleep(100);
			System.out.println("Thread "+Thread.currentThread().getId());
			if(Data.xlock==false) {
//				System.out.println("cancan");
				Data.xlock=true;
				System.out.println("Cancel "+Fid+" "+Pid+" start1");
			}
			else {
				while (true){
				    try {
				        if (Data.xlock==false){
				            Data.xlock=true;
				            System.out.println("Cancel "+Fid+" "+Pid+" start2");
				            break;
				        }
				        sleep(10);
				    } catch (InterruptedException ex){
				    }
				}
			}
			int flag=0;		
			for(int i=0;i<Data.listfl.size();i++) {
				if(Fid==Data.listfl.get(i).Fid) {
					for(int j=0;j<Data.listfl.get(i).passlist.size();j++) {
						if(Data.listfl.get(i).passlist.get(j).Pid==Pid) {
							Data.listfl.get(i).passlist.remove(j);
							flag=1;
							break;
						}				
					}
					
				}
				if(flag==1)
					break;
			}
			if(flag==0)
				System.out.println("Invalid Flight ID or Passenger ID");
			Data.xlock=false;
			System.out.println("Cancel "+Fid+" "+Pid+" end");

//		}
	}
	public static void MyFlights1(int Pid) throws InterruptedException {
//		sleep(100);
//		synchronized(Data) {
			notrans[2]++;

			Thread.currentThread().sleep(100);
			System.out.println("Thread "+Thread.currentThread().getId());
			if(Data.xlock==false) {
//				System.out.println("cancan");
				Data.xlock=true;
				System.out.println("MyFlights "+Pid+" start1");
			}
			else {
				while (true){
				    try {
				        if (Data.xlock==false){
				            Data.xlock=true;
				            System.out.println("MyFlights "+Pid+" start2");
				            break;
				        }
				        sleep(10);
				    } catch (InterruptedException ex){
				    }
				}
			}
			
			ArrayList<Integer> list=new ArrayList<Integer>();
			for(int i=0;i<Data.listfl.size();i++) {
				for(int j=0;j<Data.listfl.get(i).passlist.size();j++) {
					if(Data.listfl.get(i).passlist.get(j).Pid==Pid) {
						list.add(Data.listfl.get(i).Fid);
					}
				}
			}
			System.out.println(list);
			System.out.println("MyFlights "+Pid+" end");
			Data.xlock=false;
//		}
	}
	public static void TotalRes1() throws InterruptedException {
//		sleep(100);
//		synchronized(Data) {
			notrans[3]++;

			Thread.currentThread().sleep(100);
			System.out.println("Thread "+Thread.currentThread().getId());
			if(Data.xlock==false) {
//				System.out.println("cancan");
				Data.xlock=true;
				System.out.println("Total Res start1");
			}
			else {
				while (true){
				    try {
				        if (Data.xlock==false){
				            Data.xlock=true;
							System.out.println("Total Res start2");
				            break;
				        }
				        sleep(10);
				    } catch (InterruptedException ex){
				    }
				}
			}
			int total=0;
			for(int i=0;i<Data.listfl.size();i++) {
				total+=Data.listfl.get(i).passlist.size();
			}
			System.out.println("Total reservations "+total);				
			Data.xlock=false;
			System.out.println("Total Res end");
//		}
	}
	public static void Transfer1(int Fid1,int Fid2,int Pid) throws InterruptedException {
//		sleep(100);
//		synchronized(Data) {
			notrans[4]++;
			Thread.currentThread().sleep(100);
			System.out.println("Thread "+Thread.currentThread().getId());
			if(Data.xlock==false) {
//				System.out.println("cancan");
				Data.xlock=true;
				System.out.println("Transfer "+Fid1+" "+Fid2+" "+Pid+" start1");
			}
			else {
				while (true){
				    try {
				        if (Data.xlock==false){
				            Data.xlock=true;
				            System.out.println("Transfer "+Fid1+" "+Fid2+" "+Pid+" start2");
				            break;
				        }
				        sleep(10);
				    } catch (InterruptedException ex){
				    }
				}
			}
			int flag1=0;
			int flag2=0;
			int i1=0;
			int i2=0;
			for(int i=0;i<Data.listfl.size();i++) {
				if(Data.listfl.get(i).Fid==Fid1) {
					i1=i;
					flag1=1;
				}
				else if(Data.listfl.get(i).Fid==Fid2) {
					i2=i;
					flag2=1;
				}
				
			}
			if(flag1*flag2==0) {
				System.out.println("INVALID IDs in Transfer");
			}
			if(Data.listfl.get(i2).passlist.size()==Data.listfl.get(i2).noseats) {
				System.out.println("F2 is full");
			}
			else {
				int flag11=0;
				for(int j=0;j<Data.listfl.get(i1).passlist.size();j++) {
					if(Data.listfl.get(i1).passlist.get(j).Pid==Pid) {
						flag11=1;
						Data.listfl.get(i1).passlist.remove(j);
						Data.listfl.get(i2).passlist.add(new Passenger(Pid-300));
						break;
					}
				}
			}
			Data.xlock=false;
			System.out.println("Transfer "+Fid1+" "+Fid2+" "+Pid+" end");
//		}
	}
}

class multithreading1 extends Thread{
	static database Data = null;
	int Fid;
	int Fid2;
	int Pid;
	int choice;
	public multithreading1(database data, int fid,int fid2, int pid,int ch) {
		// TODO Auto-generated constructor stub
		Data=data;
		Fid=fid;
		Pid=pid;
		choice=ch;
		Fid2=fid2;
	}
	public void run() {
		if(choice==0)
			Reserve1(Fid,Pid);
		else if(choice==1)
			Cancel1(Fid,Pid);
		else if(choice==2)
			MyFlights1(Pid);
		else if(choice==3)
			TotalRes1();
		else if(choice==4) {
			Transfer1(Fid,Fid2,Pid);
		}
		else
			System.out.println("Invalid ");
	}
	public static void Reserve1(int Fid,int Pid) {		
		synchronized(Data) {
			if(Data.xlock==false) {
//				System.out.println("resres");
				Data.xlock=true;
				System.out.println("Reserves "+Fid+" "+Pid+" start1");
			}
			else {
				while (true){
				    try {
				        if (Data.xlock==false){
				            Data.xlock=true;
				            System.out.println("Reserves "+Fid+" "+Pid+" start2");
				            break;
				        }
				        sleep(10);
				    } catch (InterruptedException ex){
				    }
				}
			}
			Passenger temp=new Passenger(Pid-300);
//			System.out.println("_____________________");
			int flag=0;
			for(int i=0;i<Data.listfl.size();i++) {
				if(Fid==Data.listfl.get(i).Fid) {
					if(Data.listfl.get(i).noseats>Data.listfl.get(i).passlist.size()) {
						Data.listfl.get(i).passlist.add(temp);
					}
					else System.out.println(Fid+" is full");
					flag=1;
					break;
				}
				
			}
			if(flag==0)
				System.out.println("Invalid Flight ID "+Fid);
			Data.xlock=false;
			System.out.println("Reserves "+Fid+" "+Pid+" end");

		}
	}
	public static void Cancel1(int Fid,int Pid) {		
		synchronized(Data) {
			if(Data.xlock==false) {
//				System.out.println("cancan");
				Data.xlock=true;
				System.out.println("Cancel "+Fid+" "+Pid+" start1");
			}
			else {
				while (true){
				    try {
				        if (Data.xlock==false){
				            Data.xlock=true;
				            System.out.println("Cancel "+Fid+" "+Pid+" start2");
				            break;
				        }
				        sleep(10);
				    } catch (InterruptedException ex){
				    }
				}
			}
			int flag=0;		
			for(int i=0;i<Data.listfl.size();i++) {
				if(Fid==Data.listfl.get(i).Fid) {
					for(int j=0;j<Data.listfl.get(i).passlist.size();j++) {
						if(Data.listfl.get(i).passlist.get(j).Pid==Pid) {
							Data.listfl.get(i).passlist.remove(j);
							flag=1;
							break;
						}				
					}
					
				}
				if(flag==1)
					break;
			}
			if(flag==0)
				System.out.println("Invalid Flight ID or Passenger ID");
			Data.xlock=false;
			System.out.println("Cancel "+Fid+" "+Pid+" end");

		}
	}
	public static void MyFlights1(int Pid) {
		synchronized(Data) {
			if(Data.xlock==false) {
//				System.out.println("cancan");
				Data.xlock=true;
				System.out.println("MyFlights "+Pid+" start1");
			}
			else {
				while (true){
				    try {
				        if (Data.xlock==false){
				            Data.xlock=true;
				            System.out.println("MyFlights "+Pid+" start2");
				            break;
				        }
				        sleep(10);
				    } catch (InterruptedException ex){
				    }
				}
			}
			
			ArrayList<Integer> list=new ArrayList<Integer>();
			for(int i=0;i<Data.listfl.size();i++) {
				for(int j=0;j<Data.listfl.get(i).passlist.size();j++) {
					if(Data.listfl.get(i).passlist.get(j).Pid==Pid) {
						list.add(Data.listfl.get(i).Fid);
					}
				}
			}
			System.out.println(list);
			System.out.println("MyFlights "+Pid+" end");
			Data.xlock=false;
		}
	}
	public static void TotalRes1() {
		synchronized(Data) {

			if(Data.xlock==false) {
//				System.out.println("cancan");
				Data.xlock=true;
				System.out.println("Total Res start1");
			}
			else {
				while (true){
				    try {
				        if (Data.xlock==false){
				            Data.xlock=true;
							System.out.println("Total Res start2");
				            break;
				        }
				        sleep(10);
				    } catch (InterruptedException ex){
				    }
				}
			}
			int total=0;
			for(int i=0;i<Data.listfl.size();i++) {
				total+=Data.listfl.get(i).passlist.size();
			}
			System.out.println("Total reservations "+total);				
			Data.xlock=false;
			System.out.println("Total Res end");
		}
	}
	public static void Transfer1(int Fid1,int Fid2,int Pid) {
		synchronized(Data) {
			if(Data.xlock==false) {
//				System.out.println("cancan");
				Data.xlock=true;
				System.out.println("Transfer "+Fid1+" "+Fid2+" "+Pid+" start1");
			}
			else {
				while (true){
				    try {
				        if (Data.xlock==false){
				            Data.xlock=true;
				            System.out.println("Transfer "+Fid1+" "+Fid2+" "+Pid+" start2");
				            break;
				        }
				        sleep(10);
				    } catch (InterruptedException ex){
				    }
				}
			}
			int flag1=0;
			int flag2=0;
			int i1=0;
			int i2=0;
			for(int i=0;i<Data.listfl.size();i++) {
				if(Data.listfl.get(i).Fid==Fid1) {
					i1=i;
					flag1=1;
				}
				else if(Data.listfl.get(i).Fid==Fid2) {
					i2=i;
					flag2=1;
				}
				
			}
			if(flag1*flag2==0) {
				System.out.println("INVALID IDs in Transfer");
			}
			if(Data.listfl.get(i2).passlist.size()==Data.listfl.get(i2).noseats) {
				System.out.println("F2 is full");
			}
			else {
				int flag11=0;
				for(int j=0;j<Data.listfl.get(i1).passlist.size();j++) {
					if(Data.listfl.get(i1).passlist.get(j).Pid==Pid) {
						flag11=1;
						Data.listfl.get(i1).passlist.remove(j);
						Data.listfl.get(i2).passlist.add(new Passenger(Pid-300));
						break;
					}
				}
			}
			Data.xlock=false;
			System.out.println("Transfer "+Fid1+" "+Fid2+" "+Pid+" end");
		}
	}
}

class multithreading2 extends Thread{
	static database Data = null;
	int Fid;
	int Fid2;
	int Pid;
	int choice;
	public multithreading2(database data, int fid,int fid2, int pid,int ch) {
		// TODO Auto-generated constructor stub
		Data=data;
		Fid=fid;
		Pid=pid;
		choice=ch;
		Fid2=fid2;
	}
	public void run() {
		if(choice==0)
			Reserve2(Fid,Pid);
		else if(choice==1)
			Cancel2(Fid,Pid);
		else if(choice==2)
			MyFlights2(Pid);
		else if(choice==3)
			TotalRes2();
		else if(choice==4)
			Transfer2(Fid,Fid2,Pid);
		else
			System.out.println("Invalid ");
	}
	public static void Reserve2(int Fid,int Pid) {	
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(Data.xlock==false) {
            System.out.println("Reserve "+Fid+" "+Pid+" S-Lock aquired on DB.");
			Data.slock=true;
		}
		else {
			while (true){
			    try {
			        if (Data.xlock==false){
			            Data.slock=true;
			            System.out.println("Reserve "+Fid+" "+Pid+" S-Lock aquired on DB.");
			            break;
			        }
			        sleep(10);
			    } catch (InterruptedException ex){
			    }
			}
		}
		int flag=0;
		int Fi=0;
		for(int i=0;i<Data.listfl.size();i++) {
			if(Fid==Data.listfl.get(i).Fid) {
				flag=1;
				Fi=i;
			}
		}	
		if(flag==0) {
			System.out.println("Invalid Flight ID "+Fid);
		}
		else {
			synchronized(Data.listfl.get(Fi)) {
				if(Data.listfl.get(Fi).xlock==false && Data.listfl.get(Fi).slock==false) {
					Data.listfl.get(Fi).xlock=true;
					System.out.println("Reserve "+Fid+" "+Pid+" X-Lock aquired on F"+Fid);
				}
				else {
					while(true) {
						if(Data.listfl.get(Fi).xlock==false && Data.listfl.get(Fi).slock==false) {
							Data.listfl.get(Fi).xlock=true;
							System.out.println("Reserve "+Fid+" "+Pid+" X-Lock aquired on F"+Fid);
							break;
						}
						try {
							sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if(Data.listfl.get(Fi).passlist.size()==Data.listfl.get(Fi).noseats) {
					System.out.println(Fid + " is full.");
				}
				else {
					Data.listfl.get(Fi).passlist.add(new Passenger(Pid,0));
				}
				Data.listfl.get(Fi).xlock=false;
				System.out.println("Reserve "+Fid+" "+Pid+" X-Lock released on F"+Fid);
				Data.slock=false;
				System.out.println("Reserve "+Fid+" "+Pid+" S-Lock released on DB.");
			}
		}
	}
	public static void Cancel2(int Fid,int Pid) {
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(Data.xlock==false) {
            System.out.println("Cancel "+Fid+" "+Pid+" S-Lock aquired on DB.");
			Data.slock=true;
		}
		else {
			while (true){
			    try {
			        if (Data.xlock==false){
			            Data.slock=true;
			            System.out.println("Cancel "+Fid+" "+Pid+" S-Lock aquired on DB.");
			            break;
			        }
			        sleep(10);
			    } catch (InterruptedException ex){
			    }
			}
		}
		int flag=0;
		int Fi=0;
		for(int i=0;i<Data.listfl.size();i++) {
			if(Fid==Data.listfl.get(i).Fid) {
				flag=1;
				Fi=i;
			}
		}
		if(flag==0) {
			System.out.println("Invalid Flight ID "+Fid);
		}
		else {
			synchronized(Data.listfl.get(Fi)) {
				if(Data.listfl.get(Fi).xlock==false && Data.listfl.get(Fi).slock==false) {
					Data.listfl.get(Fi).xlock=true;
					System.out.println("Cancel "+Fid+" "+Pid+" X-Lock aquired on F"+Fid);
				}
				else {
					while(true) {
						if(Data.listfl.get(Fi).xlock==false && Data.listfl.get(Fi).slock==false) {
							Data.listfl.get(Fi).xlock=true;
							System.out.println("Cancel "+Fid+" "+Pid+" X-Lock aquired on F"+Fid);
							break;
						}
					}
				}
				int cflag2=0;
				int j=0;
				if(Data.listfl.get(Fi).passlist.size()==0) {
					System.out.println("Invalid Passenger P"+Pid);
				}
				for(;j<Data.listfl.get(Fi).passlist.size();j++) {
					if(Data.listfl.get(Fi).passlist.get(j).xlock==false) {
						System.out.println("Cancel "+Fid+" "+Pid+" S-Lock aquired on P"+Data.listfl.get(Fi).passlist.get(j).Pid);
						Data.listfl.get(Fi).passlist.get(j).slock=true;
//						System.out.println(j);
					}
					if(Data.listfl.get(Fi).passlist.get(j).Pid==Pid) {
						Data.listfl.get(Fi).passlist.remove(j);
						cflag2=1;
						break;
					}
				}
				if(cflag2==0) {
					System.out.println("Invalid Passenger P"+Pid);
				}
//				System.out.println(j);
				for(int k=j-1;k>=0;k--) { 
					Data.listfl.get(Fi).passlist.get(k).slock=false;
					System.out.println("Cancel "+Fid+" "+Pid+" S-Lock released on P"+Data.listfl.get(Fi).passlist.get(k).Pid);
				}
				Data.listfl.get(Fi).xlock=false;
				System.out.println("Cancel "+Fid+" "+Pid+" X-Lock released on F"+Fid);
				Data.slock=false;
				System.out.println("Cancel "+Fid+" "+Pid+" S-Lock released on DB.");
			}
		}
	}
	public static void MyFlights2(int Pid) {
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(Data.xlock==false) {
            System.out.println("MyFlights "+Pid+" S-Lock aquired on DB.");
			Data.slock=true;
		}
		else {
			while (true){
			    try {
			        if (Data.xlock==false){
			            Data.slock=true;
			            System.out.println("Cancel "+Pid+" S-Lock aquired on DB.");
			            break;
			        }
			        sleep(10);
			    } catch (InterruptedException ex){
			    }
			}
		}
		ArrayList<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<Data.listfl.size();i++) {
			if(Data.listfl.get(i).xlock==false) {
				Data.listfl.get(i).slock=true;
				System.out.println("MyFlights "+Pid+" S-Lock acquired on F"+Data.listfl.get(i).Fid);
			}
			else {
				while (true){
				    try {
				        if (Data.listfl.get(i).xlock==false){
				        	Data.listfl.get(i).slock=true;
							System.out.println("MyFlights "+Pid+" S-Lock acquired on F"+Data.listfl.get(i).Fid);
				            break;
				        }
				        sleep(10);
				    } catch (InterruptedException ex){
				    }
				}
			}
			for(int j=0;j<Data.listfl.get(i).passlist.size();j++) {
				if(Data.listfl.get(i).passlist.get(j).xlock==false) {
					Data.listfl.get(i).passlist.get(j).slock=true;
					System.out.println("MyFlights "+Pid+" S-Lock acquired on P"+Data.listfl.get(i).passlist.get(j).Pid);
				}
				if(Pid==Data.listfl.get(i).passlist.get(j).Pid) {
					list.add(Data.listfl.get(i).Fid);
				}
			}
		}
		System.out.println(list);
		for(int i=0;i<Data.listfl.size();i++) {
			for(int j=0;j<Data.listfl.get(i).passlist.size();j++) {
				Data.listfl.get(i).passlist.get(j).slock=false;
				System.out.println("MyFlights "+Pid+" S-Lock released on P"+Data.listfl.get(i).passlist.get(j).Pid);
			}
			Data.listfl.get(i).slock=false;
			System.out.println("MyFlights "+Pid+" S-Lock released on F"+Data.listfl.get(i).Fid);
		}
		Data.slock=false;
		System.out.println("MyFlights "+Pid+" S-Lock released on DB");

	}
	public static void TotalRes2() {
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(Data.xlock==false) {
            System.out.println("Total Res S-Lock aquired on DB.");
			Data.slock=true;
		}
		else {
			while (true){
			    try {
			        if (Data.xlock==false){
			            Data.slock=true;
			            System.out.println("Total Res S-Lock aquired on DB.");
			            break;
			        }
			        sleep(10);
			    } catch (InterruptedException ex){
			    }
			}
		}
		int tot=0;
		for(int i=0;i<Data.listfl.size();i++) {
			if(Data.listfl.get(i).xlock==false) {
				Data.listfl.get(i).slock=true;
				System.out.println("Total Res S-Lock acquired on F"+Data.listfl.get(i).Fid);
			}
			else {
				while (true){
				    try {
				        if (Data.listfl.get(i).xlock==false){
				        	Data.listfl.get(i).slock=true;
							System.out.println("Total Res S-Lock acquired on F"+Data.listfl.get(i).Fid);
				            break;
				        }
				        sleep(10);
				    } catch (InterruptedException ex){
				    }
				}
			}
			tot+=Data.listfl.get(i).passlist.size();
		}
		System.out.println("Total Res : "+tot);
		for(int i=0;i<Data.listfl.size();i++) {
			Data.listfl.get(i).slock=false;
			System.out.println("Total Res S-Lock released on F"+Data.listfl.get(i).Fid);
		}
		Data.slock=false;
		System.out.println("Total Res S-lock released on DB.");
	}
	public static void Transfer2(int Fid1,int Fid2,int Pid) {
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(Data.xlock==false) {
            System.out.println("Total Res S-Lock aquired on DB.");
			Data.slock=true;
		}
		else {
			while (true){
			    try {
			        if (Data.xlock==false){
			            Data.slock=true;
			            System.out.println("Total Res S-Lock aquired on DB.");
			            break;
			        }
			        sleep(10);
			    } catch (InterruptedException ex){
			    }
			}
		}
		
	}
}	

class multithreading4 extends Thread{
	static database Data = null;
	static int[] notrans;
	public multithreading4(database data,int[] notran) {
		// TODO Auto-generated constructor stub
		Data=data;
		notrans=notran;
	}
	public void run() {
		long t=System.currentTimeMillis();
		long end=t+10000;
		while(System.currentTimeMillis()<end){
			Random rand=new Random();
			int choice=rand.nextInt(4);
			int Fid=rand.nextInt(5)+100;
			int Fid2=rand.nextInt(5)+100;
			int Pid=rand.nextInt(10)+300;
			
			if(choice==0)
				Reserve2(Fid,Pid);
			else if(choice==1)
				Cancel2(Fid,Pid);
			else if(choice==2)
				MyFlights2(Pid);
			else if(choice==3)
				TotalRes2();
			else if(choice==4) {
				if(Fid==Fid2) {
					continue;
				}
				Transfer2(Fid,Fid2,Pid);
			}
			else
				System.out.println("Invalid ");
		}
		
	}
	public static void Reserve2(int Fid,int Pid) {	
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notrans[0]++;
		if(Data.xlock==false) {
            System.out.println("Reserve "+Fid+" "+Pid+" S-Lock aquired on DB.");
			Data.slock=true;
		}
		else {
			while (true){
			    try {
			        if (Data.xlock==false){
			            Data.slock=true;
			            System.out.println("Reserve "+Fid+" "+Pid+" S-Lock aquired on DB.");
			            break;
			        }
			        sleep(10);
			    } catch (InterruptedException ex){
			    }
			}
		}
		int flag=0;
		int Fi=0;
		for(int i=0;i<Data.listfl.size();i++) {
			if(Fid==Data.listfl.get(i).Fid) {
				flag=1;
				Fi=i;
			}
		}
		
		if(flag==0) {
			System.out.println("Invalid Flight ID "+Fid);
		}
		else {
			
				if(Data.listfl.get(Fi).xlock==false && Data.listfl.get(Fi).slock==false) {
					Data.listfl.get(Fi).xlock=true;
					System.out.println("Reserve "+Fid+" "+Pid+" X-Lock aquired on F"+Fid);
				}
				else {
					while(Data.listfl.get(Fi).xlock==true || Data.listfl.get(Fi).slock==true) {
						try {
							sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				synchronized(Data.listfl.get(Fi)) {
					Data.listfl.get(Fi).xlock=true;
					if(Data.listfl.get(Fi).passlist.size()==Data.listfl.get(Fi).noseats) {
						System.out.println(Fid + " is full.");
					}
					else {
						Data.listfl.get(Fi).passlist.add(new Passenger(Pid,0));
					}
					Data.listfl.get(Fi).xlock=false;
					System.out.println("Reserve "+Fid+" "+Pid+" X-Lock released on F"+Fid);
					Data.slock=false;
					System.out.println("Reserve "+Fid+" "+Pid+" S-Lock released on DB.");
			}
		}
	}
	public static void Cancel2(int Fid,int Pid) {
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notrans[1]++;
		if(Data.xlock==false) {
            System.out.println("Cancel "+Fid+" "+Pid+" S-Lock aquired on DB.");
			Data.slock=true;
		}
		else {
			while (true){
			    try {
			        if (Data.xlock==false){
			            Data.slock=true;
			            System.out.println("Cancel "+Fid+" "+Pid+" S-Lock aquired on DB.");
			            break;
			        }
			        sleep(10);
			    } catch (InterruptedException ex){
			    }
			}
		}
		int flag=0;
		int Fi=0;
		for(int i=0;i<Data.listfl.size();i++) {
			if(Fid==Data.listfl.get(i).Fid) {
				flag=1;
				Fi=i;
			}
		}
		if(flag==0) {
			System.out.println("Invalid Flight ID "+Fid);
		}
		else {
			
				if(Data.listfl.get(Fi).xlock==false && Data.listfl.get(Fi).slock==false) {
					Data.listfl.get(Fi).xlock=true;
					System.out.println("Cancel "+Fid+" "+Pid+" X-Lock aquired on F"+Fid);
				}
				else {
					while(Data.listfl.get(Fi).xlock==true || Data.listfl.get(Fi).slock==true) {
						try {
							sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
				synchronized(Data.listfl.get(Fi)) {
					Data.listfl.get(Fi).xlock=true;
					int cflag2=0;
					int j=0;
					if(Data.listfl.get(Fi).passlist.size()==0) {
						System.out.println("Invalid Passenger P"+Pid);
					}
					
					for(;j<Data.listfl.get(Fi).passlist.size();j++) {
						if(Data.listfl.get(Fi).passlist.get(j).xlock==false) {
							System.out.println("Cancel "+Fid+" "+Pid+" S-Lock aquired on P"+Data.listfl.get(Fi).passlist.get(j).Pid);
							Data.listfl.get(Fi).passlist.get(j).slock=true;
	//						System.out.println(j);
						}					
						if(Data.listfl.get(Fi).passlist.get(j).Pid==Pid) {
							Data.listfl.get(Fi).passlist.remove(j);
							cflag2=1;
							break;
						}
					}
					if(cflag2==0) {
						System.out.println("Invalid Passenger P"+Pid);
					}
	//				System.out.println(j);
					for(int k=j-1;k>=0;k--) { 
						Data.listfl.get(Fi).passlist.get(k).slock=false;
						System.out.println("Cancel "+Fid+" "+Pid+" S-Lock released on P"+Data.listfl.get(Fi).passlist.get(k).Pid);
					}
					
					Data.listfl.get(Fi).xlock=false;
					System.out.println("Cancel "+Fid+" "+Pid+" X-Lock released on F"+Fid);
					Data.slock=false;
					System.out.println("Cancel "+Fid+" "+Pid+" S-Lock released on DB.");
			}
		}
	}
	public static void MyFlights2(int Pid) {
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notrans[2]++;
		if(Data.xlock==false) {
            System.out.println("MyFlights "+Pid+" S-Lock aquired on DB.");
			Data.slock=true;
		}
		else {
			while (true){
			    try {
			        if (Data.xlock==false){
			            Data.slock=true;
			            System.out.println("Cancel "+Pid+" S-Lock aquired on DB.");
			            break;
			        }
			        sleep(10);
			    } catch (InterruptedException ex){
			    }
			}
		}
		ArrayList<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<Data.listfl.size();i++) {
			if(Data.listfl.get(i).xlock==false) {
				Data.listfl.get(i).slock=true;
				System.out.println("MyFlights "+Pid+" S-Lock acquired on F"+Data.listfl.get(i).Fid);
			}
			else {
				while (true){
				    try {
				        if (Data.listfl.get(i).xlock==false){
				        	Data.listfl.get(i).slock=true;
							System.out.println("MyFlights "+Pid+" S-Lock acquired on F"+Data.listfl.get(i).Fid);
				            break;
				        }
				        sleep(10);
				    } catch (InterruptedException ex){
				    }
				}
			}
			for(int j=0;j<Data.listfl.get(i).passlist.size();j++) {
				if(Data.listfl.get(i).passlist.get(j).xlock==false) {
					Data.listfl.get(i).passlist.get(j).slock=true;
					System.out.println("MyFlights "+Pid+" S-Lock acquired on P"+Data.listfl.get(i).passlist.get(j).Pid);
				}
				if(Pid==Data.listfl.get(i).passlist.get(j).Pid) {
					list.add(Data.listfl.get(i).Fid);
				}
			}
		}
		System.out.println(list);
		for(int i=0;i<Data.listfl.size();i++) {
			for(int j=0;j<Data.listfl.get(i).passlist.size();j++) {
				Data.listfl.get(i).passlist.get(j).slock=false;
				System.out.println("MyFlights "+Pid+" S-Lock released on P"+Data.listfl.get(i).passlist.get(j).Pid);
			}
			Data.listfl.get(i).slock=false;
			System.out.println("MyFlights "+Pid+" S-Lock released on F"+Data.listfl.get(i).Fid);
		}
		Data.slock=false;
		System.out.println("MyFlights "+Pid+" S-Lock released on DB");

	}
	public static void TotalRes2() {
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notrans[3]++;
		if(Data.xlock==false) {
            System.out.println("Total Res S-Lock aquired on DB.");
			Data.slock=true;
		}
		else {
			while (true){
			    try {
			        if (Data.xlock==false){
			            Data.slock=true;
			            System.out.println("Total Res S-Lock aquired on DB.");
			            break;
			        }
			        sleep(10);
			    } catch (InterruptedException ex){
			    }
			}
		}
		int tot=0;
		for(int i=0;i<Data.listfl.size();i++) {
			if(Data.listfl.get(i).xlock==false) {
				Data.listfl.get(i).slock=true;
				System.out.println("Total Res S-Lock acquired on F"+Data.listfl.get(i).Fid);
			}
			else {
				while (true){
				    try {
				        if (Data.listfl.get(i).xlock==false){
				        	Data.listfl.get(i).slock=true;
							System.out.println("Total Res S-Lock acquired on F"+Data.listfl.get(i).Fid);
				            break;
				        }
				        sleep(10);
				    } catch (InterruptedException ex){
				    }
				}
			}
			tot+=Data.listfl.get(i).passlist.size();
		}
		System.out.println("Total Res : "+tot);
		for(int i=0;i<Data.listfl.size();i++) {
			Data.listfl.get(i).slock=false;
			System.out.println("Total Res S-Lock released on F"+Data.listfl.get(i).Fid);
		}
		Data.slock=false;
		System.out.println("Total Res S-lock released on DB.");
	}
	public static void Transfer2(int Fid1,int Fid2,int Pid) {
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		notrans[4]++;
		if(Data.xlock==false) {
            System.out.println("Transfer"+Fid1+" "+Fid2+" "+Pid+" S-Lock aquired on DB.");
			Data.slock=true;
		}
		else {
			while (true){
			    try {
			        if (Data.xlock==false){
			            Data.slock=true;
			            System.out.println("Transfer"+Fid1+" "+Fid2+" "+Pid+" S-Lock aquired on DB.");
			            break;
			        }
			        sleep(10);
			    } catch (InterruptedException ex){
			    }
			}
		}
//		if(1==1)
//			return;
		int flag1=0;
		int flag2=0;
		int Fi1=0;
		int Fi2=0;
		for(int i=0;i<Data.listfl.size();i++) {
			if(Data.listfl.get(i).Fid==Fid1) {
				Fi1=i;
				flag1=1;
			}
			else if(Data.listfl.get(i).Fid==Fid2) {
				Fi2=i;
				flag2=1;
			}
		}
		if(flag1*flag2==0) {
			System.out.println("Invalid F"+Fid1+" or F"+Fid2);
		}
		else {
//			synchronized(Data.listfl.get(Fi1)) {
				if(Data.listfl.get(Fi1).xlock==false && Data.listfl.get(Fi1).slock==false) {
					Data.listfl.get(Fi1).xlock=true;
					System.out.println("Transfer"+Fid1+" "+Fid2+" "+Pid+" X-lock acquired on F"+Fid1);
//					System.out.println("Transfer\"+Fid1+\" \"+Fid2+\" \"+Pid+\" F"+Fi1);
				}
				else {
					while(Data.listfl.get(Fi1).xlock==true || Data.listfl.get(Fi1).slock==true) {
						try {
							sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
//					while(true) {
//						if(Data.listfl.get(Fi1).xlock==false && Data.listfl.get(Fi1).slock==false) {
//							Data.listfl.get(Fi1).xlock=true;
//							System.out.println("Transfer F"+Fi1);
//							break;
//						}
//					}
				}
			synchronized(Data.listfl.get(Fi1)) {
				Data.listfl.get(Fi1).xlock=true;	
				System.out.println("Transfer"+Fid1+" "+Fid2+" "+Pid+" X-lock acquired on F"+Fid1);
				int flag3=0;
				int Pi=0;
				for(int i=0;i<Data.listfl.get(Fi1).passlist.size();i++) {
					if(Pid==Data.listfl.get(Fi1).passlist.get(i).Pid) {
						flag3=1;
						Pi=i;
						break;
					}
				}
				if(flag3==0) {
					System.out.println("Invalid P"+Pid);
				}
				else {
					boolean check=false;
//					synchronized(Data.listfl.get(Fi2)) {
						if(Data.listfl.get(Fi2).xlock==false && Data.listfl.get(Fi2).slock==false) {
							Data.listfl.get(Fi2).xlock=true;
							System.out.println("Transfer"+Fid1+" "+Fid2+" "+Pid+" X-lock acquired on F"+Fid2);
						}
						else {
							while(Data.listfl.get(Fi2).xlock==true || Data.listfl.get(Fi2).slock==true) {
								try {
									sleep(10);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}	
//							while(true) {
//								if(Data.listfl.get(Fi2).xlock==false && Data.listfl.get(Fi2).slock==false) {
//									Data.listfl.get(Fi2).xlock=true;
//									System.out.println("Transfer F"+Fi2);
//									break;
//								}
//							}
						synchronized(Data.listfl.get(Fi2)) {	
							Data.listfl.get(Fi2).xlock=true;
							System.out.println("Transfer"+Fid1+" "+Fid2+" "+Pid+" X-lock acquired on F"+Fid2);
							if(Data.listfl.get(Fi2).passlist.size()==Data.listfl.get(Fi2).noseats) {
								System.out.println("F"+Fid2+" is full.");
							}
							else {
								Data.listfl.get(Fi2).passlist.add(new Passenger(Pid, 0));
								check=true;
							}
						System.out.println("Transfer"+Fid1+" "+Fid2+" "+Pid+" X-lock released on F"+Fid2);	
						Data.listfl.get(Fi2).xlock=false;
					}
					if(check==true)
						Data.listfl.get(Fi1).passlist.remove(Pi);
				}
				System.out.println("Transfer "+Fid1+" "+Fid2+" "+Pid+" X-lock released on F"+Fid1);
				Data.listfl.get(Fi1).xlock=false;
			}
		}
		System.out.println("Transfer "+Fid1+" "+Fid2+" "+Pid+" S-lock released on DB");
		Data.slock=false;
		notrans[4]++;
	}
}

public class Main extends Thread {
	static database Data=createdb();
	public static database createdb() {
		Random rand=new Random();
		int nofl=rand.nextInt(3)+3;
		database Data=new database(nofl);
		for(int i=0;i<nofl;i++) {
			System.out.println("Flight id "+Data.listfl.get(i).Fid);
			System.out.println("Capacity : "+Data.listfl.get(i).noseats);
			System.out.println("Passenger list : "+Data.listfl.get(i).passlist.size());
			for(int j=0;j<Data.listfl.get(i).passlist.size();j++) {
				System.out.println(Data.listfl.get(i).passlist.get(j).Pid);
			}
			System.out.println();
		}
		return Data;
	}
	public static void print(database Data) {
		for(int i=0;i<Data.listfl.size();i++) {
			System.out.println("Flight id "+Data.listfl.get(i).Fid);
			System.out.println("Capacity : "+Data.listfl.get(i).noseats);
			System.out.println("Passenger list : "+Data.listfl.get(i).passlist.size());
			for(int j=0;j<Data.listfl.get(i).passlist.size();j++) {
				System.out.println(Data.listfl.get(i).passlist.get(j).Pid);
			}
			System.out.println();
		}
	}
	public static void main(String[] args) throws InterruptedException {
		database DB=new database(Data);
////		Part 1
//		System.out.println("Serial Output");
////		Reserve
//		Thread res1=new multithreading1(Data,101,0,302,0);
//		res1.start();	
////		Cancel
//		Thread can1=new multithreading1(Data,102,0,304,1);
//		can1.start();
////		MyFlights
//		Thread myfls1= new multithreading1(Data,0,0,302,2);
//		myfls1.start();
////		Total Reservations
//		Thread totres1=new multithreading1(Data,0,0,0,3);
//		totres1.start();
////		Transfer
//		Thread trans1=new multithreading1(Data,100,102,300,4);
//		trans1.start();	
//		currentThread();
//		Thread.sleep(1000);
//		print(Data);
		
		int nothreads=5;
		int[] notrans=new int[5];
		notrans[0]=0;notrans[1]=0;notrans[2]=0;notrans[3]=0;notrans[4]=0;
		Thread[] threads=new Thread[nothreads];
		for(int i=0;i<nothreads;i++) {
			threads[i]=new multithreading3(Data,notrans);
		}
		for(int i=0;i<nothreads;i++) {
			threads[i].start();
		}
		currentThread();
		Thread.sleep(10000);
		for(int i=0;i<nothreads;i++) {
			threads[i].stop();
		}
		System.out.println("_____________________________");
		print(Data);
		
		System.out.println("_____________________________");

		
//		print(DB);
//		Part 2 (2PL)
//		Thread T1=new multithreading2(DB, 100, 0, 310, 0);
//		T1.start();
//		Thread T2=new multithreading2(DB, 100, 0, 300, 1);
//		T2.start();
		currentThread();
		Thread.sleep(1000);
//		System.out.println(notrans[0]);
//		print(DB);
		
//		Thread T3=new multithreading2(Data, 0, 0, 301, 2);
//		T3.start();
		
		int nothreads2=nothreads;
		int[] notrans2=new int[5];
		notrans2[0]=0;
		Thread[] threads2=new Thread[nothreads2];
		for(int i=0;i<nothreads2;i++) {
			threads2[i]=new multithreading4(DB,notrans2);
		}
		for(int i=0;i<nothreads2;i++) {
			threads2[i].start();
		}
		currentThread();
		Thread.sleep(10000);
		for(int i=0;i<nothreads2;i++) {
			threads2[i].stop();
		}
		currentThread();
		Thread.sleep(1000);
		print(DB);
		int ser=notrans[0]+notrans[2]+notrans[2]+notrans[3]+notrans[4];
		int par=notrans2[0]+notrans2[2]+notrans2[2]+notrans2[3]+notrans2[4];
		System.out.println("2PL "+par);
		System.out.println("Serial "+ser);
//		System.out.println(notrans[0]+" "+notrans[1]+" "+notrans[2]+" "+notrans[3]+" "+notrans[4]);
//		System.out.println(notrans2[0]+" "+notrans2[1]+" "+notrans2[2]+" "+notrans2[3]+" "+notrans2[3]);
	}
}
