import java.util.*;

public class Main {
	
	private int N;				//����״̬�ı߳�
	private int[] board;		//����״̬
	private int[][] hashTable;	//Zobrist hashing�õ���hash��
	private int[] hash;			//ÿ���ɽ�״̬��hashֵ
	private int hashCount;		//��������״̬��������
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int i = 0;i<3;i++)
			new Main().run();
	}
	
	//���к���
	public void run() {
		//��ʼ������ֵ
		System.out.print("����N��8 or 15 or 24����");
		Scanner input = new Scanner(System.in);
		N = (int)Math.sqrt(input.nextInt()+1);					//����8�ñ߳�Ϊ3��15��4��24��5
		board = new int[N*N];
		hashTable = new int[N*N][N*N];							//hash������״̬��ÿ��������Ӧ��N*N��״̬
		hash = new int[1000];
		hashCount = 0;
		
		//��ʼ��hash�����ֵ��ȡ32λ�������
		boolean flag = true;
		int k=0;
		int temp = 0;
		while(k < (N*N)*(N*N)) {
			for(int i = 0;i < N*N;i++) {
				for(int j = 0; j< N*N;j++) {
					temp = (int)(Math.random()*1000000000);		//�ٶ�������
					if(hashTable[i][j] == temp)
						flag = false;
				}
			}
			if(flag == true) {
				hashTable[(int)(k/(N*N))][k%(N*N)] = temp;
				k++;
			}
		}
		
		//��ʼ������
		for(int i = 0;i < N*N;i++ ) {
			//�ո���0��ʾ������״̬��ֵ��1��ʼ��
			if(i == N*N-1) {
				board[i] = 0;
				break;
			}
			board[i] = i+1;
		}
		
		//��ʼ��ÿ���ɽ�Ĺ�ϣֵΪ0
		for(int i = 0;i < 1000;i++) {
			hash[i] = 0;
		}
		
		
		generate();
	}
	
	//����״̬����
	public void generate() {
		//��������״̬
		int count = 0;													//����Ե�����
		for(int i = 1;hashCount < 1000;) {
			for(int j = 0;j < N*N; j++) {
				int k = ((int)(Math.random()*10))%(N*N-j)+j;			//�˿���ϴ�Ʋ��Խ���һ״̬��ÿ��Ԫ��λ���������һ��
				//if(k == N*N-1)
					//continue;
				int temp = board[j];
				board[j] = board[k];
				board[k] = temp;
				//System.out.print(((int)(Math.random()*10))%(N*N-j)+j); 
			}
			//�������ɵ�״̬�е�����Ը���
			for(int k = 0;k<N*N;k++) {
				//System.out.print(board[k]);
				for(int l = k+1;l <N*N;l++) {
					if(board[k] != 0 && board[l] != 0)					//0���������
						if(board[k]>board[l]) 
							count++;
						else;
					//else zero = (k+1)%N+((int)(k+1)/N);
				}
			}
			//�жϿɽ���
			if(count%2 != 1)
				zobristHash();											//�ɽ������hashֵ
			count = 0;
		}
		
	}
	
	
	//����hashֵ����
	public void zobristHash() {
		
		//ʹ��zobrist hashing����״̬��hash
		int temp = hashTable[0][board[0]];
		for(int i = 0;i < N*N;i++)
			temp ^= hashTable[i][board[i]];								//״̬��ÿһ��Ԫ��ȫ�����ø�״̬hashֵ
		
		//������ɵĿɽ�״̬�Ƿ��Ѿ�����
		if(hashCheck(temp)) {
			hash[hashCount++] = temp;
			if(hashCount == 1 || hashCount == 500 || hashCount == 1000)	//Ҫ������ĵ�3����ʼ״̬ȡ���ɵ�1000���еĵ�1��500��1000��״̬�������
				changePrint();
		}
		
	}
	
	//���״̬�Ƿ��ظ�����
	public boolean hashCheck(int hashValue) {
		for(int i = 0;i<1000;i++) {
			if(hashValue == hash[i])
				return false;
		}
		return true;
	}
	
	
	//���״̬���̺���
	public void output(int[] boardValue) {
		int i = 0,j = 0,k = 0;											//iΪ�����ߵĳ��ȣ�jΪ״̬��Ԫ�ص���ţ�kΪ״̬���̵Ŀ��
		while(k < N) {
			//��ӡ����״̬�ĸ�����
		for(i = 0;i < N;i++)
			System.out.print("+---");
		System.out.println("+");
		
		//��ӡ״̬��Ԫ�ص�����
		for(i = 0;i < N;i++) {
			System.out.print("|");
			if(boardValue[j] == 0)
				System.out.print(String.format("%2c ", '#'));			//0��#����
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
	
	
	//����״̬�������
	public void changePrint() {
		int[] tempArr = new int[N*N];
		int zero = -1;
		for(int i = 0;i < N*N ;i++) {
			tempArr[i] = board[i];
			if(board[i] == 0)
				zero = i;
		}
		//��ʼ״̬���һ��
		output(board);
		
		//�����״̬
		System.out.println("��״̬�ĺ���״̬Ϊ��");
		//�жϿո��ܷ������ƶ�
		if((zero+1)%N != 0) {
			//int tempValue = tempArr[zero];
			//tempArr[zero] = tempArr[zero+1];
			//tempArr[zero+1] = tempValue;
			swap(tempArr,zero,zero+1);
			System.out.println("�ո������ƶ���");
			output(tempArr);
			swap(tempArr,zero,zero+1);
		}
		//�жϿո��ܷ������ƶ�
		if((zero+N) < N*N) {
			swap(tempArr,zero,zero+N);
			System.out.println("�ո������ƶ���");
			output(tempArr);
			swap(tempArr,zero,zero+N);
		}
		//�жϿո��ܷ������ƶ�
		if(zero%N != 0) {
			swap(tempArr,zero,zero-1);
			System.out.println("�ո������ƶ���");
			output(tempArr);
			swap(tempArr,zero,zero-1);
		}
		//�жϿո��ܷ������ƶ�
		if((zero-N+1)>0) {
			swap(tempArr,zero,zero-N);
			System.out.println("�ո������ƶ���");
			output(tempArr);
			swap(tempArr,zero,zero-N);
		}
		System.out.println("����״̬end..............\n");
		
	}
	
	public void swap(int[] arr,int a,int b) {
		int c = arr[a];
		arr[a] = arr[b];
		arr[b] = c;
	}
	
}
