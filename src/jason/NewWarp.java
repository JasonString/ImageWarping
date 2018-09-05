package jason;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.nio.file.Path;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;

public class NewWarp {
	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
	
	private JFrame frame;
	private BufferedImage image;
	private BufferedImage image2;
	private Mat source;
	private Mat bigSource;
	private double product;
	private Mat dst;
	private Mat playSource;
	private String imageUri;
	private String imageName;
	private Path imagePath;
	private double totalT, timeS, timeE;;
	private int ptOrSft = 0;
	private ArrayList<int[]> points = new ArrayList<int[]>();
	private ArrayList<double[]> shifts = new ArrayList<double[]>();
	private int temp[] = new int[2]; 
	private JTextField txtNote;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewWarp window = new NewWarp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NewWarp() {
		
		//讀檔
		imageName = "dajiahao";
		imageUri = "src/jason/"+imageName+".jpg";
		source = Imgcodecs.imread(imageUri);
		
		//原照片增倍,在做取樣
		product = 1;
		bigSource = new Mat((int)(source.rows()*product),(int)(source.cols()*product),source.type());
		Imgproc.resize(source, bigSource, bigSource.size(), (source.size().width)*product, (source.size().height)*product, 1);
		
		//顯示Source先接下source
		playSource = new Mat(source.rows(),source.cols(),source.type());
		for(int x=0;x<source.cols();x++){
			for(int y=0;y<source.rows();y++){
				playSource.put(y, x, source.get(y, x));
			}
		}
		dst = new Mat(source.rows(),source.cols(),source.type());
		image = matToBufferedImage(playSource);
		image2 = matToBufferedImage(dst);
		
		//GUI處理
		initialize();
		
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		
		frame = new JFrame();
		frame.setBounds(100, 100, Math.max(150+image.getWidth()+image.getWidth(), 450), Math.max(150+image.getHeight(), 450));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { //點擊新增點
				int addpt[]={e.getX(),e.getY()};
				double addsft[] = new double[2];
				if(ptOrSft == 0){
					for(int i=0; i<2; i++){
						temp[i]= addpt[i];
					}
					points.add(addpt);
					ptOrSft =1;
					System.out.println("{"+e.getX()+","+e.getY()+"}>");
				}
				else{
					for(int i=0; i<2; i++){
						addsft[i]=addpt[i]-temp[i];
					}
					shifts.add(addsft);
					ptOrSft =0;
					System.out.println("{"+e.getX()+","+e.getY()+"}");
					System.out.println("S{"+(int)addsft[0]+","+(int)addsft[1]+"}");
				}
				
				//畫點//這裡每加上一個點，就重新畫一次
				
				for(int p=0;p<points.size();p++){
					if(ptOrSft == 1){
						Point pt1 = new Point(points.get(p)[0],points.get(p)[1]);
						Imgproc.line(playSource, pt1, pt1, new Scalar(25,55,220),5);
					}
					else{
						Point pt2 = new Point(points.get(p)[0]+shifts.get(p)[0],points.get(p)[1]+shifts.get(p)[1]);
						Imgproc.line(playSource, pt2, pt2, new Scalar(255,55,220),5);
					}	
				}
				image = matToBufferedImage(playSource);
				lblNewLabel.setIcon( new ImageIcon(image));

			}
		});
		lblNewLabel.setBounds(50, 60, image.getWidth(), image.getHeight());
		lblNewLabel.setIcon(new ImageIcon(image));
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setBounds(image.getWidth()+100, 60, image.getWidth(), image.getHeight());
		lblNewLabel_1.setIcon(new ImageIcon(image2));
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton btnRun = new JButton("Run");
		btnRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {//計算並計時
				System.out.println("開始執行");
				timeS = System.currentTimeMillis();
				process();
				timeE = System.currentTimeMillis();
				totalT = (timeE-timeS)/1000;
				System.out.println(totalT+"s");
				
				lblNewLabel.setIcon(new ImageIcon(image));
				lblNewLabel_1.setIcon(new ImageIcon(image2));
			}
		});
		btnRun.setBounds(50, 10, 90, 20);
		frame.getContentPane().add(btnRun);
		
		txtNote = new JTextField();
		txtNote.setText("note");
		txtNote.setBounds(image.getWidth()*2+100-96, 10, 96, 20);
		frame.getContentPane().add(txtNote);
		txtNote.setColumns(10);
		/*
		JButton btnResizing = new JButton("Resizing x2");
		btnResizing.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				ResizingWindow rw = new ResizingWindow(image);
				rw.NewScreen(image);
			}
		});
		btnResizing.setBounds(160, 10, 90, 20);
		frame.getContentPane().add(btnResizing);
		*/
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				MatOfInt JpgCompressionRate = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 100);
				//Imgcodecs.imwrite("src//jason//xmanWarp.jpg", dst, JpgCompressionRate);
				Imgcodecs.imwrite(imageUri.substring(0, imageUri.length()-4)+"Warp"+"["+totalT+"s]"+".jpg", dst, JpgCompressionRate);
				System.out.println("save successfully");
			}
		});
		
		JMenuItem mntmOpenImage = new JMenuItem("Open Image");
		JFileChooser imageurl = new JFileChooser();
		mntmOpenImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				imageurl.setCurrentDirectory(new java.io.File("src/jason"));
				imageurl.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(imageurl.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					 imagePath = imageurl.getSelectedFile().toPath();
					 System.out.println(imagePath.getParent().toString());
					 imageUri =imagePath.toUri().toString().substring(8);
				}
				
				process();
				lblNewLabel.setIcon(new ImageIcon(image));
				lblNewLabel_1.setIcon(new ImageIcon(image2));
			}
		});
		mnNewMenu.add(mntmOpenImage);
		mnNewMenu.add(mntmSave);
	}
	/*
	 * 主程式
	 */
	private void process(){
		Mat sourceG = new Mat(source.rows(),source.cols(),source.type());
		Imgproc.cvtColor(source, sourceG, Imgproc.COLOR_RGB2GRAY);
		
		//dst黑底
		for(int x=0;x<dst.cols();x++){
			for(int y=0;y<dst.rows();y++){
				double[] temp= {1,1,1};
				dst.put(y, x, temp);
			}
		}
				
		int rows1 = source.rows();
		int cols1 = source.cols();

		int[][] points0= {
				//chocolateS resize
				/*
				{18,5},
				{104,4},
				{193,3},
				{267,3},
				{345,3},
				{23,478},
				{90,477},
				{177,478},
				{259,477},
				{343,478},
				{19,123},
				{61,122},
				{103,121},
				{147,120},
				{188,119},
				{226,121},
				{266,120},
				{306,119},
				{348,119},
				{23,414},
				{60,414},
				{95,414},
				{138,416},
				{178,414},
				{222,413},
				{258,415},
				{304,414},
				{341,414}
				*/
		};
		for(int i=0; i<points0.length; i++){
			points.add(points0[i]);
		}
		double[][] shifts0= {
				//chocolateS
				/*
				{1,89},
				{-2,91},
				{-4,93},
				{-4,92},
				{1,93},
				{0,-30},
				{1,-28},
				{1,-29},
				{0,-29},
				{0,-31},
				{0,25},
				{1,26},
				{0,26},
				{0,29},
				{0,29},
				{1,28},
				{0,27},
				{0,27},
				{-2,30},
				{1,-29},
				{2,-27},
				{3,-29},
				{1,-30},
				{-3,-30},
				{-2,-27},
				{1,-30},
				{0,-28},
				{1,-27}
				*/
		};
		for(int i=0; i<points0.length; i++){
			shifts.add(shifts0[i]);
		}
		//讀線
		String line = "";
        String cvsSplitBy = ",";
        ArrayList<String[]> dataList = new ArrayList<String[]>(); //線string
        try(BufferedReader br = new BufferedReader(new FileReader("src//jason//"+imageName+".csv"))) {
			while ((line = br.readLine()) != null) {
                dataList.add(line.split(cvsSplitBy));
            }
		}catch (IOException e) {
			e.printStackTrace();
		}
        /*
		int[][] lines0={
				{155	,327	,492	,382},
				{366	,144	,244	,650},
				{228	,117	,514	,153}
				cactus
				{315,270,489,78}
								
		};
		*/
        //string 轉 int
        int[][] lines0 = new int[dataList.size()][4];
        for(int i=0; i<dataList.size(); i++){
			for(int j=0; j<4; j++){
				lines0[i][j]=Integer.parseInt(dataList.get(i)[j]);
			}
		}
		//int[][] 轉 arrayList
		ArrayList<int[]> lines = new ArrayList<int[]>();
		for(int i=0; i<lines0.length; i++){
				lines.add(lines0[i]);
		}

		
		int[][] shiftPoints = new int[points.size()][2];
		double[][] invShifts = new double[shifts.size()][2];
		for(int i=0; i<points.size(); i++){
			for(int j=0; j<2; j++){
				shiftPoints[i][j]=points.get(i)[j]+(int)shifts.get(i)[j];//算移動後的點
				invShifts[i][j]=-shifts.get(i)[j];//算相反shifts
			}
		}
				
		
		//計算線的位移
		int  pointsCnt = points.size();
		double diagonal = Math.sqrt(rows1*rows1+cols1*cols1);
		int linesR = lines.size();
		int linesC = 4;
		
		double[] dists = new double[points.size()];
		double[] effects = new double[points.size()];
		double[] newEffects = new double[points.size()];
		int[][] lineDists = new int[linesR][linesC];
		
		for(int i=0; i<linesR; i++){
			int xS = lines.get(i)[0]; 
			int yS = lines.get(i)[1];
			int xE = lines.get(i)[2];
			int yE = lines.get(i)[3];
			
			double[] shift ={0,0};
			//計算線段起點的影響力
			for(int k=0; k<pointsCnt; k++){
				dists[k] = Math.sqrt((xS-points.get(k)[0])*(xS-points.get(k)[0])+(yS-points.get(k)[1])*(yS-points.get(k)[1]));
				effects[k] = ((diagonal-dists[k])/diagonal)*((diagonal-dists[k])/diagonal);
				//System.out.println(i+","+effects[k]);
				if(effects[k]<0.1){
					effects[k] = 0;
				}
				newEffects[k] = effects[k];
			}
			//調整權重
			int counter = 0;
			int matchPoint = 0;
			for(int k=0; k<dists.length; k++){
				if (dists[k]==0){
					counter++;
					matchPoint = k;
				}
			}
			if (counter == 0){
				
				double sum = 0; ///權重加總
				for(int k=0; k<effects.length; k++){
					sum = sum+effects[k];
				}
	
				double power = 1;
				while(Math.abs(sum-1)>0.1){
					if(sum > 1){
						power = power + 0.1;
						for(int k=0; k<newEffects.length; k++){
							if(effects[k]==1){//避免是1進入無限回圈
								effects[k]= effects[k]-0.00001;
							}
							newEffects[k] = Math.pow(effects[k],power);
						}
						sum = 0;
						for(int k=0; k<newEffects.length; k++){
							sum = sum+newEffects[k];
						}
						//System.out.println("1,"+ sum);
					}
					else{
						power = power - 0.1;
						for(int k=0; k<newEffects.length; k++){
							if(effects[k]==1){//避免是1進入無限回圈
								effects[k]= effects[k]+0.00001;
							}
							newEffects[k] = Math.pow(effects[k],power);
						}
						sum = 0;
						for(int k=0; k<newEffects.length; k++){
							sum = sum+newEffects[k];
						}
						//System.out.println("2,"+ sum);
					}
				}
				
				for(int k=0; k<newEffects.length; k++){
					shift[0] = shift[0]+newEffects[k]*shifts.get(k)[0];
					shift[1] = shift[1]+newEffects[k]*shifts.get(k)[1];
				}
				
			}
			else{
				shift = shifts.get(matchPoint);
			}
			
			lineDists[i][0] = xS+(int)Math.round(shift[0]);
			lineDists[i][1] = yS+(int)Math.round(shift[1]);

			shift = new double[]{0,0};
			//計算線段終點的影響力
			for(int k=0; k<pointsCnt; k++){
				dists[k] = Math.sqrt((xE-points.get(k)[0])*(xE-points.get(k)[0])+(yE-points.get(k)[1])*(yE-points.get(k)[1]));
				effects[k] = ((diagonal-dists[k])/diagonal)*((diagonal-dists[k])/diagonal);

				if(effects[k]<0.1){
					effects[k] = 0;
				}
				newEffects[k] = effects[k];
			}
			counter = 0;
			matchPoint = 0;
			for(int k=0; k<dists.length; k++){
				if (dists[k]==0){
					counter++;
					matchPoint = k;
				}
			}
			if (counter == 0){
				
				double sum = 0; //權重加總
				for(int k=0; k<effects.length; k++){
					sum = sum+effects[k];
				}

				double power = 1;
				while(Math.abs(sum-1)>0.1){
					if(sum > 1){
						power = power + 0.1;
						for(int k=0; k<newEffects.length; k++){
							if(effects[k]==1){//避免是1進入無限回圈
								effects[k]= effects[k]-0.00001;
							}
							newEffects[k] = Math.pow(effects[k],power);
						}
						sum = 0;
						for(int k=0; k<newEffects.length; k++){
							sum = sum+newEffects[k];
						}
						//System.out.println("1,"+ sum);
					}
					else{
						power = power - 0.1;
						for(int k=0; k<newEffects.length; k++){
							if(effects[k]==1){//避免是1進入無限回圈
								effects[k]= effects[k]+0.00001;
							}
							newEffects[k] = Math.pow(effects[k],power);
						}
						sum = 0;
						for(int k=0; k<newEffects.length; k++){
							sum = sum+newEffects[k];
						}
						//System.out.println("2,"+ sum);
					}
				}
				
				for(int k=0; k<newEffects.length; k++){
					shift[0] = shift[0]+newEffects[k]*shifts.get(k)[0];
					shift[1] = shift[1]+newEffects[k]*shifts.get(k)[1];
				}
				
			}
			else{
				shift = shifts.get(matchPoint);
			}
			
			lineDists[i][2] = xE+(int)Math.round(shift[0]);
			lineDists[i][3] = yE+(int)Math.round(shift[1]);
			
		}
		//計算點的來源
		
		dists = new double[points.size()];
		effects = new double[points.size()];
		newEffects = new double[points.size()];
		for(int x=0; x<cols1; x++){
			for(int y=0; y<rows1; y++){
				//System.out.println(x+","+y);///////////////////////
				OnLines pts = new OnLines(lineDists,x,y);
				
				if(pts.yesNo() == 100){ //在線上///////
					int xS = lines.get(pts.lineNum())[0];
					int yS = lines.get(pts.lineNum())[1];
					int xE = lines.get(pts.lineNum())[2];
					int yE = lines.get(pts.lineNum())[3];
					
					int dxS = lineDists[pts.lineNum()][0];
					int dyS = lineDists[pts.lineNum()][1];
					int dxE = lineDists[pts.lineNum()][2];
					int dyE = lineDists[pts.lineNum()][3];
					DistLinePt linePt = new DistLinePt(dxS, dyS, dxE, dyE, x, y, xS, yS, xE, yE);
					//System.out.println(x+","+y+","+linePt.getY()+","+linePt.getX());
					dst.put(y, x, bigSource.get( (int)(Math.round( linePt.getY() )*product), (int)(Math.round( linePt.getX() )*product) ) );
				}
				
				else{
					
					WithInLineReg inLineReg = new WithInLineReg(lineDists, x, y);
					CalNewPts newPt = new CalNewPts(inLineReg.getLineNumSet(),inLineReg.getLineS(),inLineReg.getLineE(),lineDists, lines, x, y);
					
					for(int i=0; i<newPt.newPtSetX.size(); i++){
						if(newPt.newPtSetX.get(i) == newPt.newPtSetY.get(i) && (int)newPt.newPtSetX.get(i) == 0){ //移除0
							newPt.newPtSetX.remove(i);
							newPt.newPtSetY.remove(i);
							
							newPt.newSiftsX.remove(i);
							newPt.newSiftsY.remove(i);
						}
						//if(x==8 && y ==106){System.out.println(i+","+(int)newPt.newPtSetX.get(i)+","+(int)newPt.newPtSetY.get(i));}
					}
					
					//合併新舊點
					int [][] newPoints;
					double [][] newSifts;
					if(newPt.newPtSetX.isEmpty() == false){///如果有值(有新的點)
						
						newPoints= new int[newPt.newPtSetX.size()+points.size()][2];//陣列初始化
						newSifts= new double[(int)newPt.newSiftsX.size()+shifts.size()][2];
						
						//System.out.println(newPt.newPtSetX.size()+points.length+","+newPt.newSiftsX.size()+shifts.length);
						
						System.arraycopy(shiftPoints, 0, newPoints, 0, shiftPoints.length); //先複製原本的特徵點
						System.arraycopy(invShifts, 0, newSifts, 0, invShifts.length);
						for(int i=0; i<newPt.newPtSetX.size(); i++){//再放新的特徵點
							int onetwo[] = {(int)newPt.newPtSetX.get(i) , (int)newPt.newPtSetY.get(i)};
//							for(int u=0; u<onetwo.length; u++){
//								System.out.println("u:"+u+",qq:"+onetwo[u]);
//							}
							newPoints[i+shiftPoints.length] = onetwo;
							
							double onetwos[] = {(int)newPt.newSiftsX.get(i) , (int)newPt.newSiftsY.get(i)};
							newSifts[i+invShifts.length] = onetwos;
						}
					}
					else{
						newPoints = shiftPoints;
						newSifts = invShifts;
					}
					//開始計算來源點
					int [][] newPoints2;
					double [][] newSifts2;
					newPoints2 = newPoints;
					newSifts2 = newSifts;
					dists = new double[newPoints2.length];
					effects = new double[newPoints2.length];
					newEffects = new double[newPoints2.length];
					double[] shift = new double[2];
					
					for(int k=0; k<newPoints2.length; k++){//算點和各特徵點的權重
						dists[k] = Math.sqrt((x-newPoints2[k][0])*(x-newPoints2[k][0])+(y-newPoints2[k][1])*(y-newPoints2[k][1]));
						if(dists[k]<=5){
							//dists[k] = 1;
						}
						if(dists[k]==0){
							//dists[k] = 0.5;
						}
						//effects[k] = Math.pow(((diagonal-dists[k])/diagonal), 5);
						if(k> pointsCnt-1){//增加線上特徵點的權重（減少非線上特徵點的權重）
							//effects[k] = ((diagonal-dists[k])/diagonal)*((diagonal-dists[k])/diagonal);
							effects[k] = Math.pow(((diagonal-dists[k])/diagonal), 2);
						}
						else{
							//effects[k] = ((diagonal-dists[k])/diagonal)*((diagonal-dists[k])/diagonal)*((diagonal-dists[k])/diagonal);
							effects[k] = Math.pow(((diagonal-dists[k])/diagonal), 3);
						}

						
						//System.out.println(k+","+effects[k]);
						if(effects[k]<0.1){
							effects[k] = 0;
						}
						//System.out.println(newPoints2.length+","+pointsCnt);
						if(k> pointsCnt-1){//�o��p��u�W�S�x�I���[�v
							
						}
						//newEffects[k] = effects[k];////////////////////這個好像不用

					}
					/* 同如果不是特偵點一起刪
					int counter = 0;
					int matchPoint = 0;
					for(int k=0; k<dists.length; k++){
						if (dists[k]==0){
							counter++;
							matchPoint = k;
						}
					}

					if (counter == 0){ //如果不是特徵點
					*/
						double sum = 0; //權重加總
						for(int k=0; k<effects.length; k++){
							sum = sum+effects[k];
						}
						
						//System.out.println(power);////////////////////
						//改百分比權重
						/*
						for(int k=0; k<newEffects.length; k++){
							newEffects[k] = effects[k]/sum;
							//newEffects[k] = Math.pow(effects[k]+1, -0.5);							
							//System.out.println(x+" "+y+" "+newEffects[k]);
						}
						*/

						//算百分比權重
						
						double power = 1;
						//int countt= 0;
						while(Math.abs(sum-1)>0.1){
							//if(x==44 && y ==683 ){System.out.println("sum:"+sum);}//////////////////////////////
							//countt= countt+1;
							
							if(sum == 0){
								break;
							}
							else if(sum > 1){
								power = power + 0.1;
								for(int k=0; k<newEffects.length; k++){
									if(effects[k]==1){//避免是1進入無限回圈
										effects[k]= effects[k]-0.00001;
									}
									newEffects[k] = Math.pow(effects[k],power);
									//if(x==44 && y ==683 ){System.out.println("newEffects[k]:"+newEffects[k]);}///////////////////
								}
								sum = 0;
								for(int k=0; k<newEffects.length; k++){
									sum = sum+newEffects[k];
								}
									//System.out.println("1,"+ sum);
								//if(x==44 && y ==683 ){System.out.println("power:"+power);}
								//System.out.println(countt);
							}
							else{
								power = power - 0.1;
								for(int k=0; k<newEffects.length; k++){
									if(effects[k]==1){//避免是1進入無限回圈
										effects[k]= effects[k]+0.00001;
									}
									newEffects[k] = Math.pow(effects[k],power);
								}
								sum = 0;
								for(int k=0; k<newEffects.length; k++){
									sum = sum+newEffects[k];
								}
								//System.out.println("-"+countt);

									//System.out.println("2,"+ sum);
								//if(x==44 && y ==683 ){System.out.println("power:"+power);}////////////////////////////
							}
						}
						
						
						
						for(int k=0; k<newEffects.length; k++){

							shift[0] = shift[0]+newEffects[k]*newSifts2[k][0];
							shift[1] = shift[1]+newEffects[k]*newSifts2[k][1];


						}
						
					
					/*
					}
					else{
						shift = newSifts2[matchPoint];
					}
					*/
					if((int)((y+shift[1])*product)>=0 && (int)((x+shift[0])*product) >=0 && (int)((y+shift[1])*product)<rows1*product && (int)((x+shift[0])*product) <cols1*product){
						dst.put(y, x, bigSource.get( (int)((y+shift[1])*product) , (int)((x+shift[0])*product) ));
						
							//System.out.println(y+shift[1]+","+(int)((y+shift[1])*product));
						
					}
					
					
				}
				
			}
		}
		for(int p=0; p<pointsCnt; p++){
			Point pt1 = new Point(points.get(p)[0],points.get(p)[1]);
			Imgproc.line(playSource, pt1, pt1, new Scalar(25,55,220),5);
			Point pt2 = new Point(points.get(p)[0]+shifts.get(p)[0],points.get(p)[1]+shifts.get(p)[1]);
			Imgproc.line(playSource, pt2, pt2, new Scalar(255,55,220),5);
		}
		for(int p=0;p<linesR;p++){
			Point pt1= new Point(lines.get(p)[0],lines.get(p)[1]);
			Point pt2= new Point(lines.get(p)[2],lines.get(p)[3]);
			Imgproc.line(playSource, pt1, pt2, new Scalar(225,0,0),1);
		}
		/*結果圖的線條
		for(int p=0;p<linesR;p++){
			Point pt1= new Point(lineDists[p][0],lineDists[p][1]);
			Point pt2= new Point(lineDists[p][2],lineDists[p][3]);
			Imgproc.line(dst, pt1, pt2, new Scalar(225,0,0),1);
		}
		*/
		image = matToBufferedImage(playSource);
		image2 = matToBufferedImage(dst);
	}

	//copy
	public BufferedImage matToBufferedImage(Mat matrix) {
		int cols = matrix.cols();
		int rows = matrix.rows();
		int elemSize = (int) matrix.elemSize();
		byte[] data = new byte[cols * rows * elemSize];
		int type;
		matrix.get(0, 0, data);
		switch (matrix.channels()) {
		case 1:
			type = BufferedImage.TYPE_BYTE_GRAY;
			break;
		case 3:
			type = BufferedImage.TYPE_3BYTE_BGR;
			// bgr to rgb
			byte b;
			for (int i = 0; i < data.length; i = i + 3) {
				b = data[i];
				data[i] = data[i + 2];
				data[i + 2] = b;
			}
			break;
		default:
			return null;
		}
		BufferedImage image2 = new BufferedImage(cols, rows, type);
		image2.getRaster().setDataElements(0, 0, cols, rows, data);
		return image2;
	}
}
