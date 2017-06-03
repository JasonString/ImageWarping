package jason;

public class MeshPt {
	int xx;
	int yy;
	int isOut =0;
	
	public MeshPt(int x, int y, int meshSize, double[][] keyXs, double[][] keyYs){
		int x1 = ((x-1)/meshSize)*meshSize;
		int y1 = ((y-1)/meshSize)*meshSize;
		int x2 = x1+meshSize;
		int y2 = y1+meshSize;
		if(x1 >= keyXs.length || x2 >= keyXs.length || y1 >= keyXs[0].length || y2 >= keyXs[0].length){
			isOut = 1;
		}
		else{
			double a1 = keyXs[x1][y1];
			double b1 = keyYs[x1][y1];
			double a2 = keyXs[x2][y1];
			double b2 = keyYs[x2][y1];
			double a3 = keyXs[x1][y2];
			double b3 = keyYs[x1][y2];
			double a4 = keyXs[x2][y2];
			double b4 = keyYs[x2][y2];
			
			double m11 = (double) (a1*a3*b2*y1 - a2*a3*b1*y1 - a1*a3*b2*y2 - a1*a4*b2*y1 + a2*a3*b1*y2 + a2*a4*b1*y1 - a1*a3*b4*y1 + a1*a4*b2*y2 + a1*a4*b3*y1 - a2*a4*b1*y2 + a1*a3*b4*y2 - a1*a4*b3*y2 + a2*a3*b4*y1 - a2*a4*b3*y1 - a2*a3*b4*y2 + a2*a4*b3*y2)/(a1*b2*x1*y1 - a2*b1*x1*y1 - a1*b2*x2*y1 - a1*b3*x1*y1 + a2*b1*x2*y1 + a3*b1*x1*y1 + a1*b3*x1*y2 + a2*b3*x1*y1 - a3*b1*x1*y2 - a3*b2*x1*y1 - a1*b4*x1*y2 + a1*b4*x2*y1 + a4*b1*x1*y2 - a4*b1*x2*y1 - a2*b3*x2*y2 - a2*b4*x2*y1 + a3*b2*x2*y2 + a4*b2*x2*y1 + a2*b4*x2*y2 + a3*b4*x1*y2 - a4*b2*x2*y2 - a4*b3*x1*y2 - a3*b4*x2*y2 + a4*b3*x2*y2) ;
			double m12 = (double) -(a1*a2*b3*x1 - a2*a3*b1*x1 - a1*a2*b3*x2 - a1*a2*b4*x1 + a1*a4*b2*x1 + a2*a3*b1*x2 + a1*a2*b4*x2 - a1*a4*b2*x2 - a1*a4*b3*x1 + a3*a4*b1*x1 + a1*a4*b3*x2 + a2*a3*b4*x1 - a3*a4*b1*x2 - a3*a4*b2*x1 - a2*a3*b4*x2 + a3*a4*b2*x2)/(a1*b2*x1*y1 - a2*b1*x1*y1 - a1*b2*x2*y1 - a1*b3*x1*y1 + a2*b1*x2*y1 + a3*b1*x1*y1 + a1*b3*x1*y2 + a2*b3*x1*y1 - a3*b1*x1*y2 - a3*b2*x1*y1 - a1*b4*x1*y2 + a1*b4*x2*y1 + a4*b1*x1*y2 - a4*b1*x2*y1 - a2*b3*x2*y2 - a2*b4*x2*y1 + a3*b2*x2*y2 + a4*b2*x2*y1 + a2*b4*x2*y2 + a3*b4*x1*y2 - a4*b2*x2*y2 - a4*b3*x1*y2 - a3*b4*x2*y2 + a4*b3*x2*y2) ;
			double m13 = (double) (a1*a2*b3*x1*y2 - a1*a3*b2*x2*y1 + a1*a4*b2*x1*y1 - a2*a3*b1*x1*y2 + a2*a3*b1*x2*y1 - a2*a4*b1*x1*y1 - a1*a2*b3*x2*y2 - a1*a2*b4*x1*y2 + a1*a3*b2*x2*y2 - a1*a4*b3*x1*y1 + a2*a4*b1*x1*y2 + a3*a4*b1*x1*y1 + a1*a2*b4*x2*y2 + a1*a3*b4*x2*y1 - a1*a4*b2*x2*y2 + a2*a4*b3*x1*y1 - a3*a4*b1*x2*y1 - a3*a4*b2*x1*y1 - a1*a3*b4*x2*y2 + a1*a4*b3*x2*y2 + a2*a3*b4*x1*y2 - a2*a3*b4*x2*y1 - a2*a4*b3*x1*y2 + a3*a4*b2*x2*y1)/(a1*b2*x1*y1 - a2*b1*x1*y1 - a1*b2*x2*y1 - a1*b3*x1*y1 + a2*b1*x2*y1 + a3*b1*x1*y1 + a1*b3*x1*y2 + a2*b3*x1*y1 - a3*b1*x1*y2 - a3*b2*x1*y1 - a1*b4*x1*y2 + a1*b4*x2*y1 + a4*b1*x1*y2 - a4*b1*x2*y1 - a2*b3*x2*y2 - a2*b4*x2*y1 + a3*b2*x2*y2 + a4*b2*x2*y1 + a2*b4*x2*y2 + a3*b4*x1*y2 - a4*b2*x2*y2 - a4*b3*x1*y2 - a3*b4*x2*y2 + a4*b3*x2*y2) ;
			double m21 = (double) (a1*b2*b3*y1 - a2*b1*b3*y1 - a1*b2*b3*y2 - a1*b2*b4*y1 + a2*b1*b3*y2 + a2*b1*b4*y1 + a1*b2*b4*y2 - a2*b1*b4*y2 - a3*b1*b4*y1 + a4*b1*b3*y1 + a3*b1*b4*y2 + a3*b2*b4*y1 - a4*b1*b3*y2 - a4*b2*b3*y1 - a3*b2*b4*y2 + a4*b2*b3*y2)/(a1*b2*x1*y1 - a2*b1*x1*y1 - a1*b2*x2*y1 - a1*b3*x1*y1 + a2*b1*x2*y1 + a3*b1*x1*y1 + a1*b3*x1*y2 + a2*b3*x1*y1 - a3*b1*x1*y2 - a3*b2*x1*y1 - a1*b4*x1*y2 + a1*b4*x2*y1 + a4*b1*x1*y2 - a4*b1*x2*y1 - a2*b3*x2*y2 - a2*b4*x2*y1 + a3*b2*x2*y2 + a4*b2*x2*y1 + a2*b4*x2*y2 + a3*b4*x1*y2 - a4*b2*x2*y2 - a4*b3*x1*y2 - a3*b4*x2*y2 + a4*b3*x2*y2) ;
			double m22 = (double) -(a1*b2*b3*x1 - a3*b1*b2*x1 - a1*b2*b3*x2 - a2*b1*b4*x1 + a3*b1*b2*x2 + a4*b1*b2*x1 - a1*b3*b4*x1 + a2*b1*b4*x2 + a3*b1*b4*x1 - a4*b1*b2*x2 + a1*b3*b4*x2 + a2*b3*b4*x1 - a3*b1*b4*x2 - a4*b2*b3*x1 - a2*b3*b4*x2 + a4*b2*b3*x2)/(a1*b2*x1*y1 - a2*b1*x1*y1 - a1*b2*x2*y1 - a1*b3*x1*y1 + a2*b1*x2*y1 + a3*b1*x1*y1 + a1*b3*x1*y2 + a2*b3*x1*y1 - a3*b1*x1*y2 - a3*b2*x1*y1 - a1*b4*x1*y2 + a1*b4*x2*y1 + a4*b1*x1*y2 - a4*b1*x2*y1 - a2*b3*x2*y2 - a2*b4*x2*y1 + a3*b2*x2*y2 + a4*b2*x2*y1 + a2*b4*x2*y2 + a3*b4*x1*y2 - a4*b2*x2*y2 - a4*b3*x1*y2 - a3*b4*x2*y2 + a4*b3*x2*y2) ;
			double m23 = (double) (a1*b2*b3*x1*y2 - a1*b2*b3*x2*y1 + a1*b2*b4*x1*y1 + a2*b1*b3*x2*y1 - a2*b1*b4*x1*y1 - a3*b1*b2*x1*y2 - a1*b2*b4*x1*y2 - a1*b3*b4*x1*y1 - a2*b1*b3*x2*y2 + a3*b1*b2*x2*y2 + a3*b1*b4*x1*y1 + a4*b1*b2*x1*y2 + a1*b3*b4*x2*y1 + a2*b1*b4*x2*y2 + a2*b3*b4*x1*y1 - a3*b2*b4*x1*y1 - a4*b1*b2*x2*y2 - a4*b1*b3*x2*y1 - a2*b3*b4*x2*y1 - a3*b1*b4*x2*y2 + a3*b2*b4*x1*y2 + a4*b1*b3*x2*y2 - a4*b2*b3*x1*y2 + a4*b2*b3*x2*y1)/(a1*b2*x1*y1 - a2*b1*x1*y1 - a1*b2*x2*y1 - a1*b3*x1*y1 + a2*b1*x2*y1 + a3*b1*x1*y1 + a1*b3*x1*y2 + a2*b3*x1*y1 - a3*b1*x1*y2 - a3*b2*x1*y1 - a1*b4*x1*y2 + a1*b4*x2*y1 + a4*b1*x1*y2 - a4*b1*x2*y1 - a2*b3*x2*y2 - a2*b4*x2*y1 + a3*b2*x2*y2 + a4*b2*x2*y1 + a2*b4*x2*y2 + a3*b4*x1*y2 - a4*b2*x2*y2 - a4*b3*x1*y2 - a3*b4*x2*y2 + a4*b3*x2*y2) ;
			double m31 = (double) (a1*b3*y1 - a3*b1*y1 - a1*b3*y2 - a1*b4*y1 - a2*b3*y1 + a3*b1*y2 + a3*b2*y1 + a4*b1*y1 + a1*b4*y2 + a2*b3*y2 + a2*b4*y1 - a3*b2*y2 - a4*b1*y2 - a4*b2*y1 - a2*b4*y2 + a4*b2*y2)/(a1*b2*x1*y1 - a2*b1*x1*y1 - a1*b2*x2*y1 - a1*b3*x1*y1 + a2*b1*x2*y1 + a3*b1*x1*y1 + a1*b3*x1*y2 + a2*b3*x1*y1 - a3*b1*x1*y2 - a3*b2*x1*y1 - a1*b4*x1*y2 + a1*b4*x2*y1 + a4*b1*x1*y2 - a4*b1*x2*y1 - a2*b3*x2*y2 - a2*b4*x2*y1 + a3*b2*x2*y2 + a4*b2*x2*y1 + a2*b4*x2*y2 + a3*b4*x1*y2 - a4*b2*x2*y2 - a4*b3*x1*y2 - a3*b4*x2*y2 + a4*b3*x2*y2) ;
			double m32 = (double) -(a1*b2*x1 - a2*b1*x1 - a1*b2*x2 + a2*b1*x2 - a1*b4*x1 + a2*b3*x1 - a3*b2*x1 + a4*b1*x1 + a1*b4*x2 - a2*b3*x2 + a3*b2*x2 - a4*b1*x2 + a3*b4*x1 - a4*b3*x1 - a3*b4*x2 + a4*b3*x2)/(a1*b2*x1*y1 - a2*b1*x1*y1 - a1*b2*x2*y1 - a1*b3*x1*y1 + a2*b1*x2*y1 + a3*b1*x1*y1 + a1*b3*x1*y2 + a2*b3*x1*y1 - a3*b1*x1*y2 - a3*b2*x1*y1 - a1*b4*x1*y2 + a1*b4*x2*y1 + a4*b1*x1*y2 - a4*b1*x2*y1 - a2*b3*x2*y2 - a2*b4*x2*y1 + a3*b2*x2*y2 + a4*b2*x2*y1 + a2*b4*x2*y2 + a3*b4*x1*y2 - a4*b2*x2*y2 - a4*b3*x1*y2 - a3*b4*x2*y2 + a4*b3*x2*y2) ;
			
			xx = (int)Math.round( (double)(m11*x+m12*y+m13)/(m31*x+m32*y+1)  );
			yy = (int)Math.round( (double)(m21*x+m22*y+m23)/(m31*x+m32*y+1) );
			
		}
	}
	
}
