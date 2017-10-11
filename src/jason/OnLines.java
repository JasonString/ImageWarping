package jason;

public class OnLines {
	int yesNo = 0;
	int lineNum = 0;
	double a=0;
	double b=0;
	double c=0;
	double[] newPt = {0,0};
	
	public OnLines(int[][] lines,int pX, int pY){
		
		for(int i=0; i<lines.length; i++){
			int xS = lines[i][0];
			int yS = lines[i][1];
			int xE = lines[i][2];
			int yE = lines[i][3];
			if(onLine(xS,yS,xE,yE,pX,pY)==1){
				yesNo = 1;
				lineNum = i;
				break;
				//System.out.println("in:"+pX+","+pY+","+yesNo+","+lineNum);
			}
			//System.out.println("out"+pX+","+pY+","+yesNo+","+lineNum);
		}
	}
	public int yesNo(){
		return yesNo;
	}
	public int lineNum(){
		return lineNum;
	}
	public int onLine(int xS,int yS,int xE,int yE,int pX,int pY){
		if(xE-xS == 0){
			a = (double)(pY-yS)/(yE-yS);
			b = (double)(pY-yS)/(yE-yS);
		}
		else if(yE-yS == 0){
			b = (double)(pX-xS)/(xE-xS);
			a = (double)(pX-xS)/(xE-xS);
		}
		else{
			a = (double)(pX-xS)/(xE-xS);
			b = (double)(pY-yS)/(yE-yS);
		}
		if(a<0 || a>1 || b<0 || b>1){
			yesNo =0;
		}
		else{
			c = (double)( (pX-xS)*(xE-xS)+(pY-yS)*(yE-yS) )/ ( (xE-xS)*(xE-xS)+(yE-yS)*(yE-yS) );
			newPt[0] = xS + c*(xE-xS);
			newPt[1] = yS + c*(yE-yS);
			c = Math.sqrt( (pX-newPt[0])*(pX-newPt[0])+(pY-newPt[1])*(pY-newPt[1]) );
			if(c < 15){//1.5
				yesNo = 1;
			}
			else{
				yesNo = 0;
			}
		}
		return yesNo;
	}
}
