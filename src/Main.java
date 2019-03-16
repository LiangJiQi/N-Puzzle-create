import java.util.*;

public class Main {
	
	private int N;				//棋盘状态的边长
	private int[] board;		//生成状态
	private int[][] hashTable;	//Zobrist hashing用到的hash表
	private int[] hash;			//每个可解状态的hash值
	private int hashCount;		//生成棋盘状态个数计数
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int i = 0;i<3;i++)
			new Main().run();
	}
	
	//运行函数
	public void run() {
		//初始化各项值
		System.out.print("输入N（8 or 15 or 24）：");
		Scanner input = new Scanner(System.in);
		N = (int)Math.sqrt(input.nextInt()+1);					//输入8得边长为3，15得4，24得5
		board = new int[N*N];
		hashTable = new int[N*N][N*N];							//hash表棋盘状态中每个格所对应的N*N种状态
		hash = new int[1000];
		hashCount = 0;
		
		//初始化hash表里的值，取32位的随机数
		boolean flag = true;
		int k=0;
		int temp = 0;
		while(k < (N*N)*(N*N)) {
			for(int i = 0;i < N*N;i++) {
				for(int j = 0; j< N*N;j++) {
					temp = (int)(Math.random()*1000000000);		//再多就溢出了
					if(hashTable[i][j] == temp)
						flag = false;
				}
			}
			if(flag == true) {
				hashTable[(int)(k/(N*N))][k%(N*N)] = temp;
				k++;
			}
		}
		
		//初始化棋盘
		for(int i = 0;i < N*N;i++ ) {
			//空格用0表示，棋盘状态中值从1开始算
			if(i == N*N-1) {
				board[i] = 0;
				break;
			}
			board[i] = i+1;
		}
		
		//初始化每个可解的哈希值为0
		for(int i = 0;i < 1000;i++) {
			hash[i] = 0;
		}
		
		
		generate();
	}
	
	//生成状态函数
	public void generate() {
		//生成棋盘状态
		int count = 0;													//逆序对的数量
		for(int i = 1;hashCount < 1000;) {
			for(int j = 0;j < N*N; j++) {
				int k = ((int)(Math.random()*10))%(N*N-j)+j;			//扑克牌洗牌策略将上一状态中每个元素位置随机打乱一遍
				//if(k == N*N-1)
					//continue;
				int temp = board[j];
				board[j] = board[k];
				board[k] = temp;
				//System.out.print(((int)(Math.random()*10))%(N*N-j)+j); 
			}
			//计算生成的状态中的逆序对个数
			for(int k = 0;k<N*N;k++) {
				//System.out.print(board[k]);
				for(int l = k+1;l <N*N;l++) {
					if(board[k] != 0 && board[l] != 0)					//0不参与计算
						if(board[k]>board[l]) 
							count++;
						else;
					//else zero = (k+1)%N+((int)(k+1)/N);
				}
			}
			//判断可解性
			if(count%2 != 1)
				zobristHash();											//可解则计算hash值
			count = 0;
		}
		
	}
	
	
	//计算hash值函数
	public void zobristHash() {
		
		//使用zobrist hashing计算状态的hash
		int temp = hashTable[0][board[0]];
		for(int i = 0;i < N*N;i++)
			temp ^= hashTable[i][board[i]];								//状态的每一个元素全部异或得该状态hash值
		
		//检查生成的可解状态是否已经存在
		if(hashCheck(temp)) {
			hash[hashCount++] = temp;
			if(hashCount == 1 || hashCount == 500 || hashCount == 1000)	//要求输出的的3个初始状态取生成的1000个中的第1，500，1000个状态进行输出
				changePrint();
		}
		
	}
	
	//检查状态是否重复函数
	public boolean hashCheck(int hashValue) {
		for(int i = 0;i<1000;i++) {
			if(hashValue == hash[i])
				return false;
		}
		return true;
	}
	
	
	//输出状态棋盘函数
	public void output(int[] boardValue) {
		int i = 0,j = 0,k = 0;											//i为隔横线的长度，j为状态中元素的序号，k为状态棋盘的宽度
		while(k < N) {
			//打印棋盘状态的隔横线
		for(i = 0;i < N;i++)
			System.out.print("+---");
		System.out.println("+");
		
		//打印状态中元素的排序
		for(i = 0;i < N;i++) {
			System.out.print("|");
			if(boardValue[j] == 0)
				System.out.print(String.format("%2c ", '#'));			//0用#代替
			else System.out.print(String.format("%2d ", boardValue[j]));
			j++;
		}
		System.out.print("|\n");
		k++;
		}
		for(i = 0;i < N;i++)
			System.out.print("+---");
		System.out.println("+");
			
		/*
		System.out.println("+---+---+---+");
		System.out.println("| "+board[0]+" | "+board[1]+" | "+board[2]+" | ");
		System.out.println("+---+---+---+");
		System.out.println("| "+board[3]+" | "+board[4]+" | "+board[5]+" | ");
		System.out.println("+---+---+---+");
		System.out.println("| "+board[6]+" | "+board[7]+" | "+board[8]+" | ");
		System.out.println("+---+---+---+");
		*/
	}
	
	
	//后续状态输出函数
	public void changePrint() {
		int[] tempArr = new int[N*N];
		int zero = -1;
		for(int i = 0;i < N*N ;i++) {
			tempArr[i] = board[i];
			if(board[i] == 0)
				zero = i;
		}
		//初始状态输出一次
		output(board);
		
		//其后续状态
		System.out.println("该状态的后续状态为：");
		//判断空格能否向右移动
		if((zero+1)%N != 0) {
			//int tempValue = tempArr[zero];
			//tempArr[zero] = tempArr[zero+1];
			//tempArr[zero+1] = tempValue;
			swap(tempArr,zero,zero+1);
			System.out.println("空格向右移动：");
			output(tempArr);
			swap(tempArr,zero,zero+1);
		}
		//判断空格能否向下移动
		if((zero+N) < N*N) {
			swap(tempArr,zero,zero+N);
			System.out.println("空格向下移动：");
			output(tempArr);
			swap(tempArr,zero,zero+N);
		}
		//判断空格能否向左移动
		if(zero%N != 0) {
			swap(tempArr,zero,zero-1);
			System.out.println("空格向左移动：");
			output(tempArr);
			swap(tempArr,zero,zero-1);
		}
		//判断空格能否向上移动
		if((zero-N+1)>0) {
			swap(tempArr,zero,zero-N);
			System.out.println("空格向上移动：");
			output(tempArr);
			swap(tempArr,zero,zero-N);
		}
		System.out.println("后续状态end..............\n");
		
	}
	
	public void swap(int[] arr,int a,int b) {
		int c = arr[a];
		arr[a] = arr[b];
		arr[b] = c;
	}
	
}
