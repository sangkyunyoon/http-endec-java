package fr.bytel.hope.protocol.http.mock;

import fr.bmartel.protocol.http.utils.IByteList;

/**
 * List of bytes mock class
 * 
 * @author Bertrand Martel
 * 
 */
public class ListOfBytesMock implements IByteList {

	public String test = "";

	/**
	 * Constructor of mock object
	 * 
	 * @param data
	 *            string to be stocked as multiple byte array object
	 */
	public ListOfBytesMock(String data) {
		this.test = data;
	}

	@Override
	public int add(byte[] data) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getBytes() {
		return test.getBytes();
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] get(int index) {
		// TODO Auto-generated method stub
		return null;
	}

}