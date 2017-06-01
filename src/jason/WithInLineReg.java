package jason;

public class WithInLineReg {
	int[] lineNumSet;
	int[] lineS;
	int[] lineE;
	
	public WithInLineReg(int[][] lines, int pX, int pY){
		
		lineNumSet = new int[lines.length];
		lineS = new int[lines.length];
		lineE = new int[lines.length];
		
		for(int i=0; i<lines.length; i++){
			int flag = 0;
			
			int xS = lines[i][0];
			int yS = lines[i][1];
			int xE = lines[i][2];
			int yE = lines[i][3];
			
			double uX = (pX-xS)/( Math.sqrt((pX-xS)*(pX-xS)+(pY-yS)*(pY-yS)) );
			double uY = (pY-yS)/( Math.sqrt((pX-xS)*(pX-xS)+(pY-yS)*(pY-yS)) );
			
			double vX = (xE-xS)/( Math.sqrt((xE-xS)*(xE-xS)+(yE-yS)*(yE-yS)) );
			double vY = (yE-yS)/( Math.sqrt((xE-xS)*(xE-xS)+(yE-yS)*(yE-yS)) );
			
			double angle = Math.acos((uX*vX+uY*vY))*180/Math.PI;
			if(angle > 90){
				flag = 1;
				lineS[i] =1;
			}
			
			uX = (pX-xE)/( Math.sqrt((pX-xE)*(pX-xE)+(pY-yE)*(pY-yE)) );
			uY = (pY-yE)/( Math.sqrt((pX-xE)*(pX-xE)+(pY-yE)*(pY-yE)) );
			
			vX = (xS-xE)/( Math.sqrt((xS-xE)*(xS-xE)+(yS-yE)*(yS-yE)) );
			vY = (yS-yE)/( Math.sqrt((xS-xE)*(xS-xE)+(yS-yE)*(yS-yE)) );
			
			angle = Math.acos((uX*vX+uY*vY))*180/Math.PI;
			if(angle > 90){
				flag = 1;
				lineE[i] =1;
			}
			
			if(flag == 0){
				lineNumSet[i] = 1;
			}
			
		}
		
		
	}
	
	public int[] getLineNumSet(){
		return lineNumSet;
	}
	public int[] getLineS(){
		return lineS;
	}
	public int[] getLineE(){
		return lineE;
	}
}
