package voterSearch.app.SmdInfo;

public class Items {
	
	  String partno = null;
	  boolean selected = false;
	
		public Items(String _partno)
		{
			this.partno = _partno;
		}
	 
	 public Items(String _partno, boolean selected) {
		  super();
		  this.partno = _partno;
		  this.selected = selected;
		 }
	
	 public String getPartNo() 
	 {
		  return partno;
	 }
		
	 public void setPartNo(String _partno) 
	 {
		 this.partno = _partno;
	 }
	 
	 public boolean isSelected()
	 {
		  return selected;
	 }
	 
	 public void setSelected(boolean selected)
	 {
		  this.selected = selected;
	 }

	@Override
	public String toString()
	{
		 return partno;
	}
}
