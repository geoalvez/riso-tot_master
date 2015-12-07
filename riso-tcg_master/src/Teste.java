import br.edu.ufcg.copin.riso.tot.main.RisoTotMain2;


public class Teste {

	public static void main(String[] args) {
		int i = 0;

//		System.out.println(i++ + " " + RisoTotMain2.getDataPosCompareRefact("X < 01-02-2016", "1-5-2014 < X < 10-3-2015"));				
//		System.out.println(i++ + " " + RisoTotMain2.getDataPosCompareRefact("X < 01-02-2015", "1-5-2014 < X < 10-3-2015"));		
//		System.out.println(i++ + " " + RisoTotMain2.getDataPosCompareRefact("X > 01-02-2015", "1-5-2014 < X < 10-3-2015"));
//		System.out.println(i++ + " " + RisoTotMain2.getDataPosCompareRefact("X > 01-02-2014", "1-5-2014 < X < 10-3-2015"));		
//		System.out.println(i++ + " " + RisoTotMain2.getDataPosCompareRefact("X > 01-02-2016", "1-5-2014 < X < 10-3-2015"));		
//		System.out.println(i++ + " " + RisoTotMain2.getDataPosCompareRefact("X > 01-06-2014", "1-5-2014 < X < 10-3-2015"));		

//		System.out.println(++i + " " + RisoTotMain2.getDataPosCompareRefact("1-5-2014 < X < 10-3-2015", "X > 01-02-2016"));		
//		System.out.println(++i + " " + RisoTotMain2.getDataPosCompareRefact("1-5-2014 < X < 10-3-2015", "X > 01-02-2015"));
//		System.out.println(++i + " " + RisoTotMain2.getDataPosCompareRefact("1-5-2014 < X < 10-3-2015", "X < 01-02-2015"));		
//		System.out.println(++i + " " + RisoTotMain2.getDataPosCompareRefact("1-5-2014 < X < 10-3-2015", "X > 01-02-2014"));		
//		System.out.println(++i + " " + RisoTotMain2.getDataPosCompareRefact("1-5-2014 < X < 10-3-2015", "X > 01-06-2014"));		
//		System.out.println(++i + " " + RisoTotMain2.getDataPosCompareRefact("1-5-2014 < X < 10-3-2015", "X < 01-02-2016"));
		
		System.out.println(i++ + " " + RisoTotMain2.getDataPosCompare("1-5-2010 < X < 10-3-2013", "1-5-2014 < X < 10-3-2015"));
		System.out.println(i++ + " " + RisoTotMain2.getDataPosCompare("1-3-2014 < X < 10-2-2015", "1-5-2014 < X < 10-3-2015"));
		System.out.println(i++ + " " + RisoTotMain2.getDataPosCompare("1-5-2014 < X < 10-2-2015", "1-5-2014 < X < 10-3-2015"));
		System.out.println(i++ + " " + RisoTotMain2.getDataPosCompare("1-5-2018 < X < 10-2-2019", "1-5-2014 < X < 10-3-2015"));
		System.out.println(i++ + " " + RisoTotMain2.getDataPosCompare("1-5-2010 < X < 10-3-2013", "1-5-2010 < X < 10-3-2013"));		
		
		
		
		
	}
	
}
