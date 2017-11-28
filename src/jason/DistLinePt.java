package jason;

public class DistLinePt {
	
	double a=0;
	double b=0;
	
	double qX;
	double qY;
	
	public DistLinePt(int xS, int yS, int xE, int yE, int pX, int pY, int dxS, int dyS, int dxE, int dyE){
		if(xE-xS <= yE-yS){
			a = (double)(pY-yS)/(yE-yS);
			b = (double)(pY-yS)/(yE-yS);
		}
		else{
			b = (double)(pX-xS)/(xE-xS);
			a = (double)(pX-xS)/(xE-xS);
		}
		//System.out.println("dxS:"+dxS+" dyS:"+dyS);
		//System.out.println("dxE:"+dxE+" dyE:"+dyE);
		//System.out.println("a:"+a+" b:"+b);
		//System.out.println("(dxE-dxS):"+(dxE-dxS)+" (dyE-dyS):"+(dyE-dyS));
		qX = dxS + a*(dxE-dxS);
		qY = dyS + b*(dyE-dyS);
		//System.out.println("X:"+pX+","+"Y:"+pY+"qX:"+(int)Math.round(qX)+","+"qY:"+(int)Math.round(qY));
		
	}
	
	public double getX(){
		return qX;
	}
	
	public double getY(){
		return qY;
	}
	
}
