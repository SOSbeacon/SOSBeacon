package view.custom;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;

public class GridManager extends Manager {

	private int numColumns;
	public GridManager(int numColumns,long style){
		super(style);
		this.numColumns=numColumns;
	// TODO Auto-generated constructor stub
	}

	protected void sublayout(int width, int height) {
		// TODO Auto-generated method stub
		int[] columnWidths=new int[numColumns];
		int length=0;
		if(getFieldCount()%numColumns==0)
			length=getFieldCount()/numColumns;
		else 
			length=getFieldCount()/numColumns+1;
		int[] rowHeights=new int[getFieldCount()/numColumns];
		int availableWidth=width;
		int availableHeight=height;
		for(int column=0;column<numColumns;column++){
			for(int fieldIndex=column;fieldIndex<getFieldCount();fieldIndex+=numColumns){
				Field field = getField(fieldIndex);
				layoutChild(field, availableWidth, availableHeight);
				if(field.getWidth()>columnWidths[column]){
					columnWidths[column]=field.getWidth();
				}
			}
			availableWidth-=columnWidths[column];
		}
		
		int maxHeight=0;
		for(int fieldIndex=0;fieldIndex<getFieldCount();fieldIndex++){
			Field field=getField(fieldIndex);
			if(field.getHeight()+field.getMarginBottom()+field.getMarginTop()>maxHeight){
				maxHeight=field.getHeight()+field.getMarginBottom()+field.getMarginTop();
				rowHeights[fieldIndex/numColumns]=maxHeight;
			}
			if(fieldIndex%numColumns==numColumns-1){
				maxHeight=0;
			}
		}
		
		int currentRow=0;
		int currentRowHeight=0;
		int rowYOffset=0;
		for(int fieldIndex=0;fieldIndex<getFieldCount();fieldIndex++){
			Field field=getField(fieldIndex);
			int fieldOffset=0;
			if((field.getStyle()&Field.FIELD_RIGHT)==Field.FIELD_RIGHT){
				fieldOffset=columnWidths[fieldIndex%numColumns]-field.getWidth();
			}
			int y=rowHeights[fieldIndex/numColumns];
			if(fieldIndex%numColumns==0){
				setPositionChild(field, 0+fieldOffset, rowYOffset+(y-field.getHeight())/2);
			}else{
				setPositionChild(field, columnWidths[(fieldIndex%numColumns)-1]+fieldOffset, rowYOffset+(y-field.getHeight())/2);
			}
			if(field.getHeight()>currentRowHeight){
				currentRowHeight=field.getHeight()+field.getMarginBottom()+field.getMarginTop();
			}
			if(fieldIndex%numColumns==numColumns-1){
				currentRow++;
				rowYOffset+=currentRowHeight;
				currentRowHeight=0;
			}
		}
		int totalWidth=0;
		for(int i=0;i<numColumns;i++){
			totalWidth+=columnWidths[i];
		}
		setExtent(totalWidth, rowYOffset+currentRowHeight);
	}

}
