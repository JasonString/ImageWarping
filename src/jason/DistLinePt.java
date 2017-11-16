package jason;

public class DistLinePt {
	
	double a=0;
	double b=0;
	
	double qX;
	double qY;
	
	public DistLinePt(int xS, int yS, int xE, int yE, int pX, int pY, int dxS, int dyS, int dxE, int dyE){
		if(Math.abs(xE-xS) <= 3){
			a = (double)(pY-yS)/(yE-yS);
			b = (double)(pY-yS)/(yE-yS);
		}
		else if(Math.abs(yE-yS) <= 3){
			b = (double)(pX-xS)/(xE-xS);
			a = (double)(pX-xS)/(xE-xS);
		}
		else{
			a = (double)(pX-xS)/(xE-xS);
			b = (double)(pY-yS)/(yE-yS);
		}
		//System.out.println("dxS:"+dxS+" dyS:"+dyS);
		//System.out.println("dxE:"+dxE+" dyE:"+dyE);
		//System.out.println("a:"+a+" b:"+b);
		//System.out.println("(dxE-dxS):"+(dxE-dxS)+" (dyE-dyS):"+(dyE-dyS));
		qX = dxS + a*(dxE-dxS);
		qY = dyS + b*(dyE-dyS);
		//System.out.println("X:"+pX+","+"Y:"+pY+"qX:"+(int)Math.round(qX)+","+"qY:"+(int)Math.round(qY));
		/*
		if(xS ==119 && yS==61 && pX==217 && pY==60){
			System.out.println(a+","+b);
		}
		*/
	}
	
	public double getX(){
		return qX;
	}
	
	public double getY(){
		return qY;
	}
	
}
