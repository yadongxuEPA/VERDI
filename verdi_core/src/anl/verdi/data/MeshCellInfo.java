/**
 * MeshCellInfo - encapsulates individual cells that compose unstructured grids
 * @author Tony Howard
 * @version $Revision$ $Date$
 **/

package anl.verdi.data;

import com.vividsolutions.jts.geom.Geometry;

public interface MeshCellInfo {
	
	public int getId();

	public double getValue(ArrayReader renderVariable, DataFrame frame, MPASDataFrameIndex index, int timestep, int layer);
	
	public double getValue(MeshDataReader reader);
	
	public int getNumVertices();
	
	public double getLon();
	
	public double getLat();
	
	public double getLon(int index);
	
	public double getLat(int index);
	
	public double getLonRad(int index);
	
	public double getLatRad(int index);
	
	public double getMinX();
	
	public double getMinLonValue();
	
	public double getMaxLonValue();
	
	public int getMinXPosition();
	
	public int getMaxXPosition();
	
	public double getMinLatValue();
	
	public double getMaxLatValue();
	
	public int getMinYPosition();
	
	public int getMaxYPosition();
	
	public double getMaxX();
	
	public double getMinY();
	
	public double getMaxY();
	
	public String getElevation(String axisName, int currentLayer, int currentTimestep);
	
	public Geometry toGeometry();


}
