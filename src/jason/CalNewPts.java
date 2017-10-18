package jason;

import java.util.ArrayList;
import java.util.List;

public class CalNewPts {
	
	List<Integer> newPtSetX = new ArrayList<Integer>();
	List<Integer> newPtSetY = new ArrayList<Integer>();
	
	List<Integer> newSiftsX = new ArrayList<Integer>();
	List<Integer> newSiftsY = new ArrayList<Integer>();
	
	public CalNewPts(int[] lineNumS, int[] lineSS, int[] lineES, int[][] lines, ArrayList<int[]> lineDists, int x, int y){
	
		for(int i=0; i<lineNumS.length; i++){
			int xS = lines[i][0];
			int yS = lines[i][1];
			int xE = lines[i][2];
			int yE = lines[i][3];
			
			int dxS = lineDists.get(i)[0];
			int dyS = lineDists.get(i)[1];
			int dxE = lineDists.get(i)[2];
			int dyE = lineDists.get(i)[3];
			if(lineNumS[i] == 1){
				
				//System.out.println(x+","+y+","+xS+","+xE+","+yS+","+yE);
				double scale = (double)( (x-xS)*(xE-xS)+(y-yS)*(yE-yS) ) / ( (xE-xS)*(xE-xS)+(yE-yS)*(yE-yS) ) ;
				
				//System.out.println("scale1:"+((x-xS)*(xE-xS)+(y-yS)*(yE-yS))+"scale2:"+((xE-xS)*(xE-xS)+(yE-yS)*(yE-yS))+"scale3:"+scale);
				int newPtX = (int) Math.round(xS + scale*(xE-xS)); 
				int newPtY = (int) Math.round(yS + scale*(yE-yS));
				
				//System.out.println("newX"+newPtX);
				//System.out.println("new"+newPtY);
				
				DistLinePt nLinePt = new DistLinePt(xS, yS, xE, yE, newPtX, newPtY, dxS, dyS, dxE, dyE);
				//newPtSet[i][0] = (int) Math.round(nLinePt.getX());
				//newPtSet[i][1] = (int) Math.round(nLinePt.getY());
				
				//System.out.println(nLinePt.getX());
				//System.out.println(nLinePt.getY());
				
				newPtSetX.add( newPtX );
				newPtSetY.add( newPtY );
				newSiftsX.add( (int) Math.round(nLinePt.getX())-newPtX );
				newSiftsY.add( (int) Math.round(nLinePt.getY())-newPtY );
				
				
			}
			if(lineSS[i] == 1){
				newPtSetX.add( dxS );
				newPtSetY.add( dyS );
				newSiftsX.add( dxS-xS );
				newSiftsY.add( dyS-yS );
			}
			if(lineES[i] == 1){
				newPtSetX.add( dxE );
				newPtSetY.add( dyE );
				newSiftsX.add( dxE-xE );
				newSiftsY.add( dyE-yE );
			}
		}
		
	}
}
