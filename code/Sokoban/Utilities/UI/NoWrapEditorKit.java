package Utilities.UI;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
 
public class NoWrapEditorKit extends HTMLEditorKit
{
      public ViewFactory getViewFactory()
      {
	 return new NoWrapViewFactory();
      }

      static class NoWrapViewFactory extends HTMLEditorKit.HTMLFactory
      {
	    public View create(Element elem)
	    {
	       Object o = elem.getAttributes().getAttribute(StyleConstants.NameAttribute);
 
	       if (o instanceof HTML.Tag)
	       {
		  HTML.Tag kind = (HTML.Tag)o;
		  if (kind == HTML.Tag.CONTENT)
		  {
		     return new NoWrapBoxView(elem);
		  }
	       }

	       return super.create(elem);
	    }
      }

      static class NoWrapBoxView extends InlineView
      {
	    public NoWrapBoxView(Element elem)
	    {
	       super(elem);
	    }

	    public int getBreakWeight(int axis, float pos, float len)
	    {
	       return BadBreakWeight;
	    }
      }
}
