package Utilities.Test;

import Utilities.*;

import java.util.*;

public class Tester
{
      private static List<UnitTest> unitTests = new ArrayList();

      public static void addUnitTest(UnitTest unitTest)
      {
	 unitTests.add(unitTest);
      }

      public static void runTests()
      {
	 System.out.println("RUNNING UNIT TESTS");
	 System.out.println("==================");

	 for(UnitTest test : unitTests)
	 {
	    System.out.print("Running test: " + test + " ==> ");
	    Pair<Boolean, String> result = test.runTest();

	    boolean success = result.element1;

	    if(success)
	       System.out.println("PASS");
	    else
	    {
	       System.out.println("FAIL");	    

	       String report = result.element2;
	       report = report.replace("\n", "\n\t");

	       System.out.println("Report:\n\t" + report);

	       System.exit(1);
	    }
	 }
      }
}
