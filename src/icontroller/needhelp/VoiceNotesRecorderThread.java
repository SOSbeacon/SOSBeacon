package icontroller.needhelp;

import java.io.ByteArrayOutputStream;

import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.RecordControl;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public final class VoiceNotesRecorderThread extends Thread {
	private Player _player;
	private RecordControl _rcontrol;
	private ByteArrayOutputStream _output;
	private byte _data[];

	public VoiceNotesRecorderThread() {
	}

	public int getSize() {
		return (_output != null ? _output.size() : 0);
	}

	public byte[] getVoiceNote() {
		return _data;
	}

	public void run() {
		try {
			// Create a Player that captures live audio.
			_player = Manager.createPlayer("capture://audio");
			_player.realize();

			// Get the RecordControl, set the record stream,
			_rcontrol = (RecordControl) _player.getControl("RecordControl");

			// Create a ByteArrayOutputStream to capture the audio stream.
			_output = new ByteArrayOutputStream();
			_rcontrol.setRecordStream(_output);
			_rcontrol.startRecord();
			_player.start();

		} catch (final Exception e) {
			// UiApplication.getUiApplication().invokeAndWait(new Runnable()
			// {
			// public void run() {
			// Dialog.inform(e.toString());
			// }
			// });
			System.out.println("--------Record sound exception "
					+ e.getMessage());
		}
	}

	public void stop() {
		try {
			// Stop recording, capture data from the OutputStream,
			// close the OutputStream and player.
			_rcontrol.commit();
			_data = _output.toByteArray();
			_output.close();
			_player.close();

		} catch (Exception e) {
			synchronized (UiApplication.getEventLock()) {
				Dialog.inform(e.toString());
			}
		}
	}
}
