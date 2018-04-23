package com.menyi.msgcenter.msgif;
/**
 * 
 * @preserve all
 */
public class StatusRsp extends MsgHeader {
//	public byte result;	// PWY

	/**
	 * @roseuid 509C9C63022F
	 */
	public StatusRsp() {
		this.command_Id = IfCommand.STATUS_RSP;
	}

	public void decode(byte[] b) {

		decodeHead(b);
		int pos = 12;
		result = b[pos];
		pos += 1;
	}

	public byte[] encode() {

		this.total_Length = 13;
		byte[] b = new byte[total_Length];
		System.arraycopy(encodeHead(), 0, b, 0, 12);
		int pos = 12;
		b[pos] = result;
		pos += 1;

		return b;
	}

	public String toString() {

		String msg = "NoteRsp: ";
		msg += "result = " + result + "";
		return msg;

	}
}
