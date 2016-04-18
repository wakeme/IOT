import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;


public class PCD extends Thread {
	
	private static int 		N = 4;			// N = 2^n, N > number of picc devices 
	
	private JLabel 		m_jl_current;
	private List<PICC> 	m_piccDevices;
	private List<PICC>	m_piccInSlot;

	// for security
	private SecurityReader	m_sreader;
	
	public PCD(List<PICC> list, JLabel jl_cur_slot, SecurityReader sr) {
		this.m_piccDevices 	= list;
		this.m_jl_current 	= jl_cur_slot;
		this.m_piccInSlot 	= new ArrayList<PICC>();
		this.m_sreader 		= sr;
	}
	
	
	public void run() {
		try {
			System.out.println("PCD: POWER ON FIELD");
			System.out.println("");
			
			for (PICC picc : m_piccDevices) {
				picc.setStatus(PICC.State.IDLE);
			}
			
			while (!this.isAllReady()) {
				System.out.println("===========PCD: NEW LOOP===========");
				
				sleep(2000);
				this.sendREQ();
				
				sleep(1000);
				System.out.println("---------------------");
				for (int i = 1; i <= N; i++) {
					sleep(1000);
					System.out.println("------ SLOT " + i + " -------");
					this.setCurrentSlot(i);
					this.sendSlotMarker(i);
					this.processPiccInSlot();
				}
				System.out.println("---------------------");
				System.out.println("===================================");
				System.out.println("");
			}
			
			System.out.println("PCD: POWER OFF FIELD");
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isAllReady() {
		for (PICC picc : m_piccDevices) {
			if (!picc.getState().equals(PICC.State.READY)) {
				return false;
			}
		}
		return true;
	}
	
	private void sendREQ() {
		System.out.println("PCD: SEND REQUEST COMMAND");
		for (PICC picc : m_piccDevices) {
			if (!picc.getState().equals(PICC.State.READY)) {
				if (this.m_sreader.validate(picc.getSReceiver())) {
					picc.receiveREQ(N);
				}
			}
		}
	}
	
	private void setCurrentSlot(int t) {
		this.m_jl_current.setText(Integer.toString(t));
	}
	
	private void sendSlotMarker(int n) {
		System.out.println("PCD: SEND SLOT-MARKER COMMAND");
		for (PICC picc : m_piccDevices) {
			if (!picc.getState().equals(PICC.State.READY)) {
				if (this.m_sreader.validate(picc.getSReceiver())) {
					if (picc.receiveSlotMarker(n)) {
						this.m_piccInSlot.add(picc);
					}
				}
			}
		}
	}
	
	private void processPiccInSlot() throws InterruptedException {
		if (m_piccInSlot.isEmpty()) {
			// DO NOTHING
			System.out.println("PCD: NO PICC IN SLOT");
		} else if (m_piccInSlot.size() == 1) {
			// NO COLLISION
			System.out.println("PCD: PICC " + m_piccInSlot.get(0).getName() + " IS ATS AVAILABLE");
			m_piccInSlot.get(0).setStatus(PICC.State.ACTIVE);
			sleep(500);
			m_piccInSlot.get(0).setStatus(PICC.State.READY);
		} else {
			// COLLISION OCCURED
			System.out.print("PCD: PICC ");
			for (PICC picc : m_piccInSlot) {
				System.out.print(picc.getName() + ", ");
				picc.setStatus(PICC.State.COLLISION);
				picc.resetR();
			}
			System.out.println("COLLISON OCCURED");
		}
		m_piccInSlot.clear();
	}
}


class SecurityReader {
	
	private Map<Long, Long> 	table_id_key;
	private Long 				r1;
	
	public SecurityReader(HashMap<Long, Long> id_key) {
		table_id_key = id_key;
	}
	
	public boolean validate(SecurityReceiver sr) {
		r1 = Utils.nonce();
		sr.encrypt(r1);
		return this.decrypt(sr);
	}

	private boolean decrypt(SecurityReceiver sr) {
		Long r2 = sr.getR2();
		for (Long k : table_id_key.keySet()) {
			Long id = k;
			Long key = table_id_key.get(k);
			Long left = getLeftHalf(id, key, r2);
			if (left.equals(sr.getLeftID2())) {
				Long right = getRightHalf(id, key, r2);
				if (sr.matchRightHalf(right)) {
					return true;
				}
			}
		}
		return false;
	}

	private Long getLeftHalf(Long id, Long key, Long r2) {
		int g = Long.toBinaryString(r1 ^ r2 ^ key).hashCode();
		Long id2 = Long.rotateLeft(id, g % 64);
		Long left = Utils.getLeft(id2 ^ g);
		return left;
	}
	private Long getRightHalf(Long id, Long key, Long r2) {
		int g = Long.toBinaryString(r1 ^ r2 ^ key).hashCode();
		Long id2 = Long.rotateLeft(id, g % 64);
		Long right = Utils.getRight(id2 ^ g);
		return right;
	}
}
