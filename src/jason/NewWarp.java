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
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import javax.swing.JButton;

public class NewWarp {
	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
	
	private JFrame frame;
	private BufferedImage image;
	private BufferedImage image2;
	private Mat dst;
	private String imageUri;
	private JTextField txtMeshSize;
	private int meshSize;
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
		long timeS, timeE, totalT;
	
		timeS = System.currentTimeMillis();
		imageUri = "src//jason//xman.jpg";
		meshSize = 5;
		process();
		timeE = System.currentTimeMillis();
		
		initialize();
		
		totalT = timeE-timeS;
		System.out.println(totalT);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 150+image.getWidth()+image2.getWidth(), 150+image2.getHeight());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(50, 60, image.getWidth(), image.getHeight());
		//ImageIcon icon = new ImageIcon(image);
		//icon.getImage().flush();
		lblNewLabel.setIcon( new ImageIcon(image));
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setBounds(image.getWidth()+100, 60, image2.getWidth(), image2.getHeight());
		lblNewLabel_1.setIcon(new ImageIcon(image2));
		frame.getContentPane().add(lblNewLabel_1);
		
		txtMeshSize = new JTextField();
		txtMeshSize.setBounds(50, 20, 50, 20);
		frame.getContentPane().add(txtMeshSize);
		txtMeshSize.setColumns(10);
		//run
		JButton btnRun = new JButton("Run");
		btnRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				meshSize = Integer.valueOf(txtMeshSize.getText());
				process();
				lblNewLabel.setIcon( new ImageIcon(image));
				lblNewLabel_1.setIcon(new ImageIcon(image2));
			}
		});
		btnRun.setBounds(120, 20, 60, 20);
		frame.getContentPane().add(btnRun);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("\u6A94\u6848");
		menuBar.add(menu);
		
		JMenuItem mntmSave = new JMenuItem("save");
		mntmSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				MatOfInt JpgCompressionRate = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 100);
				Imgcodecs.imwrite(imageUri.substring(0, imageUri.length()-4)+"Warp.jpg"+"["+meshSize+"]", dst, JpgCompressionRate);
				System.out.println("Save Successfully");
			}
		});
		//讀檔
		JMenuItem mntmOpenImage = new JMenuItem("open image");
		JFileChooser imageFileC = new JFileChooser();
		
		mntmOpenImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//imageFileC.setFileFilter();篩選
				imageFileC.setCurrentDirectory(new java.io.File("src/jason"));
				if(imageFileC.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				 	Path imagePath = imageFileC.getSelectedFile().toPath();
				 	imageUri = imagePath.toUri().toString().substring(8);
				}
				
				process();
				lblNewLabel.setIcon( new ImageIcon(image));
				lblNewLabel_1.setIcon(new ImageIcon(image2));
			}
		});
		menu.add(mntmOpenImage);
		menu.add(mntmSave);
	}
	private void process(){
		Mat source = Imgcodecs.imread(imageUri);
		dst = new Mat(source.rows(),source.cols(),source.type());
		Mat sourceG = new Mat(source.rows(),source.cols(),source.type());
		Imgproc.cvtColor(source, sourceG, Imgproc.COLOR_RGB2GRAY);
		
		//dst變黑
		for(int x=0;x<dst.cols();x++){
			for(int y=0;y<dst.rows();y++){
				double[] temp= {1,1,1};
				dst.put(y, x, temp);
			}
		}
				
		int rows1 = sourceG.rows();
		int cols1 = sourceG.cols();

		int[][] points= {
				{208,88},
				{333,97},
				{491,274},
				{84,256}
		};
		double[][] shifts= {
				{-30,-10},
				{50,3},
				{2,35},
				{-35,40}
		};
		int[][] lines={
				{68,145,213,245},
				{54,168,203,256},
				{39,194,197,269},
				{329,275,489,177},
				{338,291,507,208},
				{344,307,515,241},
				
		};
		
		int[][] shiftPoints = new int[points.length][2];
		double[][] invShifts = new double[shifts.length][2];
		for(int i=0; i<points.length; i++){
			for(int j=0; j<points[0].length; j++){
				shiftPoints[i][j]=points[i][j]+(int)shifts[i][j];//算移動後的點
				invShifts[i][j]=-shifts[i][j];//算相反shifts
			}
		}
				
		
		//計算線的位移
		int  pointsCnt = points.length;
		double diagonal = Math.sqrt(rows1*rows1+cols1*cols1);
		int linesR = lines.length;
		int linesC = lines[0].length;
		
		double[] dists = new double[points.length];
		double[] effects = new double[points.length];
		double[] newEffects = new double[points.length];
		int[][] lineDists = new int[linesR][linesC];
		
		for(int i=0; i<linesR; i++){
			int xS = lines[i][0];
			int yS = lines[i][1];
			int xE = lines[i][2];
			int yE = lines[i][3];
			
			double[] shift ={0,0};
			//計算點的影響力S
			for(int k=0; k<pointsCnt; k++){
				dists[k] = Math.sqrt((xS-points[k][0])*(xS-points[k][0])+(yS-points[k][1])*(yS-points[k][1]));
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
				
				double sum = 0; //權重加總
				for(int k=0; k<effects.length; k++){
					sum = sum+effects[k];
				}
				double power = 1;
				while(Math.abs(sum-1)>0.1){
					if(sum > 1){
						power = power + 0.1;
						for(int k=0; k<newEffects.length; k++){
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
					shift[0] = shift[0]+newEffects[k]*shifts[k][0];
					shift[1] = shift[1]+newEffects[k]*shifts[k][1];
				}
				
			}
			else{
				shift = shifts[matchPoint];
			}
			
			lineDists[i][0] = xS+(int)Math.round(shift[0]);
			lineDists[i][1] = yS+(int)Math.round(shift[1]);

			shift = new double[]{0,0};
			//計算點的影響力E
			for(int k=0; k<pointsCnt; k++){
				dists[k] = Math.sqrt((xE-points[k][0])*(xE-points[k][0])+(yE-points[k][1])*(yE-points[k][1]));
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
					shift[0] = shift[0]+newEffects[k]*shifts[k][0];
					shift[1] = shift[1]+newEffects[k]*shifts[k][1];
				}
				
			}
			else{
				shift = shifts[matchPoint];
			}
			
			lineDists[i][2] = xE+(int)Math.round(shift[0]);
			lineDists[i][3] = yE+(int)Math.round(shift[1]);
			
		}
		//計算網格點的來源
		
		dists = new double[points.length];
		effects = new double[points.length];
		newEffects = new double[points.length];
		int[][] flag = new int[cols1][rows1];
		int[][] keyXs = new int[cols1][rows1];/////////////////之後可以改在同一個陣列
		int[][] keyYs = new int[cols1][rows1];
		double[][] keyXs2 = new double[cols1][rows1];
		double[][] keyYs2 = new double[cols1][rows1];
		for(int x=0; x<cols1; x=x+meshSize){
			for(int y=0; y<rows1; y=y+meshSize){
				flag[x][y] =1;
				//System.out.println(x+","+y);
				OnLines pts = new OnLines(lineDists,x,y);
				
				if(pts.yesNo() == 1){ //在線上
					int xS = lines[pts.lineNum()][0];
					int yS = lines[pts.lineNum()][1];
					int xE = lines[pts.lineNum()][2];
					int yE = lines[pts.lineNum()][3];
					
					int dxS = lineDists[pts.lineNum()][0];
					int dyS = lineDists[pts.lineNum()][1];
					int dxE = lineDists[pts.lineNum()][2];
					int dyE = lineDists[pts.lineNum()][3];
					DistLinePt linePt = new DistLinePt(dxS, dyS, dxE, dyE, x, y, xS, yS, xE, yE);
					dst.put(y, x, source.get( (int)Math.round(linePt.getY()), (int)Math.round(linePt.getX()) ) );
					keyXs[x][y]=(int)Math.round(linePt.getX());
					keyYs[x][y]=(int)Math.round(linePt.getY());
					keyXs2[x][y] = linePt.getX();
					keyYs2[x][y] = linePt.getY();
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
					}
					//合併新舊點
					int [][] newPoints;
					double [][] newSifts;
					if(newPt.newPtSetX.isEmpty() == false){//如果有值(有新的點)
						
						newPoints= new int[newPt.newPtSetX.size()+points.length][2];//陣列初始化
						newSifts= new double[(int)newPt.newSiftsX.size()+shifts.length][2];
						
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
					//開始計算mesh的來源點
					int [][] newPoints2;
					double [][] newSifts2;
					newPoints2 = newPoints;
					newSifts2 = newSifts;
					dists = new double[newPoints2.length];
					effects = new double[newPoints2.length];
					newEffects = new double[newPoints2.length];
					double[] shift = new double[2];
					for(int k=0; k<newPoints2.length; k++){
						dists[k] = Math.sqrt((x-newPoints2[k][0])*(x-newPoints2[k][0])+(y-newPoints2[k][1])*(y-newPoints2[k][1]));
						effects[k] = ((diagonal-dists[k])/diagonal)*((diagonal-dists[k])/diagonal);
						//System.out.println(k+","+effects[k]);
						if(effects[k]<0.1){
							effects[k] = 0;
						}
						newEffects[k] = effects[k];
					}
//
					int counter = 0;
					int matchPoint = 0;
					for(int k=0; k<dists.length; k++){
						if (dists[k]==0){
							counter++;
							matchPoint = k;
						}
					}
					
					if (counter == 0){ //如果不是特徵點
//
						double sum = 0; //權重加總
						for(int k=0; k<effects.length; k++){
							sum = sum+effects[k];
						}
						if(x==585 && y==1){
							System.out.println("0,"+ sum);
						}
						double power = 1;
						while(Math.abs(sum-1)>0.1){
							if(sum == 0){
								break;
							}
							else if(sum > 1){
								power = power + 0.1;
								for(int k=0; k<newEffects.length; k++){
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
							shift[0] = shift[0]+newEffects[k]*newSifts2[k][0];
							shift[1] = shift[1]+newEffects[k]*newSifts2[k][1];
						}
						
					}
					else{
						shift = newSifts2[matchPoint];
					}
					keyXs[x][y]=x+(int)Math.round(shift[0]);
					keyYs[x][y]=y+(int)Math.round(shift[1]);  //////////////////4捨5入 會比較好?
					keyXs2[x][y] = x+shift[0];
					keyYs2[x][y] = y+shift[1];
					//System.out.println(x+","+y+","+keyXs[x][y]+","+keyYs[x][y]);
					if(keyYs[x][y]>=0 && keyXs[x][y] >=0 && keyXs[x][y]<cols1 && keyYs[x][y] <rows1){
						dst.put(y, x, source.get( keyYs[x][y] , keyXs[x][y] ) );
						//System.out.println(x+","+y+","+keyXs[x][y]+","+keyYs[x][y]);
					}
					
				}
				
			}
		}
		
		//處理未填滿的點
		//for(int m=1; m<meshSize; m++ ){
			for(int x=0; x<cols1; x++){
				for(int y=0; y<rows1; y++){
					if(flag[x][y] !=1){
						MeshPt center = new MeshPt(x, y, meshSize, keyXs2, keyYs2);
						
						if(center.isOut==1){
							continue;
						}
						int xx = center.xx;
						int yy = center.yy;
												
					/*	if(x > 30 && x <36 && y>70 && y<76){
							System.out.println(x+","+y+","+a1+","+b1+","+a2+","+b2+","+a3+","+b3+","+a4+","+b4);
						}*/
						if(xx>0 && yy >0 && xx<cols1 && yy<rows1){
							dst.put(y, x, source.get( yy , xx ) );
						}
					}
				}
			}
		//}
		for(int p=0;p<pointsCnt;p++){
			for(int x=0;x<source.cols();x++){
				for(int y=0;y<source.rows();y++){
					if(x > points[p][0]-3 && x < points[p][0]+3 && y> points[p][1]-3 && y<points[p][1]+3){
						double[] temp= {200,200,200};
						source.put(y, x, temp);
					}
				}
			}
		}
		image = matToBufferedImage(source);
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
