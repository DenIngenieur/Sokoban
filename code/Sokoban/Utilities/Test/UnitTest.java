package Utilities.Test;

import Utilities.*;

public class UnitTest
{
      private String testName;

      // test ::  -> Pair<Boolean, String>
      private Lambda test;
      
      public UnitTest(String name, Lambda t)
      {
	 testName = name;
	 test = t;
      }
      
      public Pair<Boolean, String> runTest()
      {
	 return (Pair) test.applyArguments((Object) null);
      }

      public String toString()
      {
	 return testName;
      }
}
