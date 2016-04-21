/**
 * MPASLoader - data loader used to identify files containing MPAS data
 * 
 * @author Tony Howard
 * @version $Revision$ $Date$
 */

package anl.verdi.loaders;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.logging.log4j.LogManager;		// 2014
import org.apache.logging.log4j.Logger;			// 2014 replacing System.out.println with logger messages


//import simphony.util.messages.MessageCenter;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.dataset.conv.MPASConvention;
import anl.verdi.data.DataLoader;
import anl.verdi.data.DataReader;
import anl.verdi.data.Dataset;

/**
 * @author Tony Howard
 * @version $Revision$ $Date$
 */
public class MPASLoader implements DataLoader {
	static final Logger Logger = LogManager.getLogger(MPASLoader.class.getName());

//	private static final MessageCenter msgCenter = MessageCenter.getMessageCenter(Models3Loader.class);

	/**
	 * Returns whether or not this DataLoader can handle the data at the specified
	 * url.
	 *
	 * @param url the location of the data
	 * @return true if this DataLoader can handle loading the data, otherwise
	 *         false.
	 * @throws Exception 
	 */
	public boolean canHandle(URL url) throws Exception {
		//  try to open up the file
		NetcdfFile file = null;
		try {
			String urlString = url.toExternalForm();
			if (url.getProtocol().equals("file")) {
				urlString = new URI(urlString).getPath();
			}
			file = NetcdfFile.open(urlString);
			return MPASConvention.isMine(file) && hasDimensions(file);
		}
		finally {
			try {
				if (file != null) file.close();
			} catch (IOException e) {}
		}
	}

	private boolean hasDimensions(NetcdfFile file) throws IOException {
		List<Dimension> dims = file.getDimensions();
		boolean hasCells = false;
		boolean hasMaxEdges = false;
		for (Dimension dim : dims) {
			if (dim.getShortName().equals("nCells")) hasCells = true;
			else if (dim.getShortName().equals("maxEdges")) hasMaxEdges = true;
		}
		if (!hasCells || !hasMaxEdges)
			throw new IOException("Invalid MPAS file, nCells: " + hasCells + " maxEdges: " + hasMaxEdges);
		return true;
	}

	/**
	 * Creates a Dataset from the data at the specified URL.
	 *
	 * @param url the url of the data
	 * @return a Dataset created from the data at the specified URL.
	 * @throws IOException 
	 */
	public List<Dataset> createDatasets(URL url) throws IOException {
		NetcdfDatasetFactory factory = new NetcdfDatasetFactory();
		return factory.createMPASDatasets(url);
	}

	/**
	 * Creates a DataReader that can read a particular type of Dataset.
	 *
	 * @param set the data set
	 * @return a DataReader created for the dataset.
	 */
	@SuppressWarnings("rawtypes")
	public DataReader createReader(Dataset set) {
		return new MPASNetcdfReader((MPASDataset)set);
	}
}
